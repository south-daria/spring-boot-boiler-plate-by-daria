package com.daria.javatemplate.core.common.utils.file;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.daria.javatemplate.core.common.model.entity.FileResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

public interface AWSS3FileService {
    FileResponseEntity uploadForInternal(MultipartFile file, String pathSuffix);

    FileResponseEntity uploadForCustomer(MultipartFile file, String pathSuffix);

    FileResponseEntity uploadForPickup(MultipartFile file, String pathSuffix, long orderSheetNumber);

    void deleteFile(String filename);

    String makeNewFilename(String filename, String pathSuffix);

    String generatePreSignedURL(final String s3ObjectKey);

    String generatePreSignedURL(final String s3ObjectKey, int expirationHour);

    InputStream getFile(String pathSuffix, String fileName);

    InputStream getFile(String bucketName, String fileName, String pathSuffix);

    PutObjectResult uploadToS3(String bucketName, File file);
}
