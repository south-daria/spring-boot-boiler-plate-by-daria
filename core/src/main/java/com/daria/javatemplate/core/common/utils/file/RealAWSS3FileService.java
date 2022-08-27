package com.daria.javatemplate.core.common.utils.file;

import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.daria.javatemplate.core.common.exception.ApplicationErrorException;
import com.daria.javatemplate.core.common.exception.ApplicationErrorType;
import com.daria.javatemplate.core.common.model.entity.FileResponseEntity;
import com.daria.javatemplate.core.common.utils.AWSS3UrlUtil;
import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile({"local"})
public class RealAWSS3FileService implements AWSS3FileService {

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Value("${aws.s3.image.url.prefix}")
    private String urlPrefix;

    private final AmazonS3 s3;

    public FileResponseEntity uploadForInternal(MultipartFile file, String pathSuffix) {
        try {
            String newFileName = makeNewFilename(file.getOriginalFilename(), pathSuffix);
            uploadToS3(file, newFileName, false);

            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                return new FileResponseEntity(newFileName, file.getOriginalFilename(), AWSS3UrlUtil.convertToS3Url(newFileName), 0, 0);
            } else {
                return new FileResponseEntity(newFileName, file.getOriginalFilename(), AWSS3UrlUtil.convertToS3Url(newFileName), image.getHeight(), image.getWidth());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationErrorException(ApplicationErrorType.FILE_UPLOAD_ERROR);
        }
    }

    public FileResponseEntity uploadForCustomer(MultipartFile file, String pathSuffix) {
        try {
            String newFileName = makeNewFilename(file.getOriginalFilename(), pathSuffix);
            uploadToS3(file, newFileName, true);

            return new FileResponseEntity(newFileName, file.getOriginalFilename(), AWSS3UrlUtil.convertToS3Url(newFileName), 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationErrorException(ApplicationErrorType.FILE_UPLOAD_ERROR);
        }
    }

    public FileResponseEntity uploadForPickup(MultipartFile file, String pathSuffix, long orderSheetNumber) {
        try {
            String newFileName = makeNewFilenameForPickup(file.getOriginalFilename(), pathSuffix, orderSheetNumber);
            uploadToS3(file, newFileName, false);

            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                return new FileResponseEntity(newFileName, file.getOriginalFilename(), AWSS3UrlUtil.convertToS3Url(newFileName), 0, 0);
            } else {
                return new FileResponseEntity(newFileName, file.getOriginalFilename(), AWSS3UrlUtil.convertToS3Url(newFileName), image.getHeight(), image.getWidth());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationErrorException(ApplicationErrorType.FILE_UPLOAD_ERROR);
        }
    }

    private String uploadToS3(MultipartFile file, String fileName, boolean isPrivate) throws Exception {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        byte[] resultByte = DigestUtils.md5(file.getBytes());
        String streamMD5 = new String(Base64.encodeBase64(resultByte));
        metadata.setContentMD5(streamMD5);

        s3.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata)
            .withCannedAcl(isPrivate ? CannedAccessControlList.Private : CannedAccessControlList.PublicRead));

        return fileName;
    }

    /**
     * 파일 업로드
     * @exception SdkClientException, AmazonServiceException, RuntimeException
     */
    public PutObjectResult uploadToS3(String bucketName, File file) {
        return s3.putObject(new PutObjectRequest(bucketName, file.getName(), file)
            .withCannedAcl(CannedAccessControlList.BucketOwnerFullControl));
    }

    public void deleteFile(String filename) {
        if (filename == null) {
            throw new ApplicationErrorException(ApplicationErrorType.CANNOT_BE_DELETED);
        }

        if (filename.startsWith("http")) {
            filename = filename.replace(urlPrefix + "/", "");
        }

        s3.deleteObject(bucketName, filename);
    }

    public String makeNewFilename(String filename, String pathSuffix) {
        HashCode hash = Hashing.murmur3_128()
            .newHasher()
            .putLong(System.currentTimeMillis())
            .putString(filename, Charsets.UTF_8)
            .hash();

        String fileExt = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

        return pathSuffix + "/" + hash.toString() + "." + fileExt;
    }

    private String makeNewFilenameForPickup(String filename, String pathSuffix, long orderSheetNumber) {
        String fileExt = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

        return String.format("%s/%d_%d.%s", pathSuffix, orderSheetNumber, System.currentTimeMillis(), fileExt);
    }

    public String generatePreSignedURL(final String s3ObjectKey) {
        return generatePreSignedURL(s3ObjectKey, 6);
    }

    @Override
    public String generatePreSignedURL(String s3ObjectKey, int expirationHour) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
            new GeneratePresignedUrlRequest(bucketName, s3ObjectKey)
                .withMethod(HttpMethod.GET)
                .withExpiration(DateTime.now().plusHours(expirationHour).toDate());
        URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);

        try {
            GetObjectMetadataRequest metadataRequest = new GetObjectMetadataRequest(bucketName, s3ObjectKey);
            ObjectMetadata objectMetadata = s3.getObjectMetadata(metadataRequest);

            log.debug("{}", objectMetadata);

        } catch (Exception e) {
            // log.error(e.getMessage(), e);
        }

        return url.toString();
    }

    @Override
    public InputStream getFile(String fileName, String pathSuffix) {
        return this.getFile(bucketName, fileName, pathSuffix);
    }

    @Override
    public InputStream getFile(String bucketName, String fileName, String pathSuffix) {
        final String s3ObjectName = fileName + Objects.toString(pathSuffix, "");
        try {
            S3Object object = s3.getObject(new GetObjectRequest(bucketName, s3ObjectName));
            return object.getObjectContent();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationErrorException(ApplicationErrorType.FILE_DOWNLOAD_ERROR);
        }
    }
}
