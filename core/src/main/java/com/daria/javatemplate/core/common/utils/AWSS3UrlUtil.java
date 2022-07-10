package com.daria.javatemplate.core.common.utils;

import com.daria.javatemplate.core.common.model.dto.ImageDto;
import com.daria.javatemplate.core.common.utils.file.AWSS3FileService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AWSS3UrlUtil {
    private static String urlPrefix;
    private static AWSS3FileService staticAWSS3FileService;
    private final com.daria.javatemplate.core.common.utils.file.AWSS3FileService AWSS3FileService;

    @PostConstruct
    public void init() {
        AWSS3UrlUtil.staticAWSS3FileService = AWSS3FileService;
    }

    @Value("${aws.s3.image.url.prefix}")
    public void setUrlPrefix(String prefix) {
        urlPrefix = prefix;
    }

    public static List<String> convertToS3Urls(List<String> imagePaths) {
        if (CollectionUtils.isEmpty(imagePaths)) {
            return Lists.newArrayList();
        }
        return imagePaths.stream()
            .map(AWSS3UrlUtil::convertToS3Url)
            .collect(Collectors.toList());
    }

    public static String removeUrlPrefix(String imagePath) {
        return imagePath != null && imagePath.contains(urlPrefix) ?
            imagePath.replace(urlPrefix + "/", "") :
            imagePath;
    }

    public static String convertToS3Url(String path) {
        if (StringUtils.isEmpty(path) || path.startsWith("http")) {
            return path;
        }

        if (path.startsWith("public")) {
            return getFullUrl(path);
        }

        return staticAWSS3FileService.generatePreSignedURL(path);
    }

    public static String convertToS3Url(String path, int expirationHour) {
        if (StringUtils.isEmpty(path) || path.startsWith("http")) {
            return path;
        }

        if (path.startsWith("public")) {
            return getFullUrl(path);
        }

        return staticAWSS3FileService.generatePreSignedURL(path, expirationHour);
    }

    public static String convertPreSignedURL(String path) {
        if (StringUtils.isEmpty(path) || path.startsWith("http")) {
            return path;
        }

        return staticAWSS3FileService.generatePreSignedURL(path);
    }


    private static String getFullUrl(String path) {
        return urlPrefix + "/" + path;
    }


    public static List<String> convertToS3Paths(List<String> urls) {
        return urls.stream()
            .map(AWSS3UrlUtil::convertToS3Path)
            .collect(Collectors.toList());
    }

    public static List<ImageDto> convertToImagePathsWithMetaData(List<ImageDto> images, String directImageUrl) {
        if (CollectionUtils.isEmpty(images) && directImageUrl == null) {
            return Lists.newArrayList();
        }

        if (CollectionUtils.isEmpty(images) && directImageUrl != null) {
            ImageDto image = new ImageDto();
            image.setUrl(directImageUrl);
            images = Lists.newArrayList(image);
        }

        return images.stream()
            .map(image -> {
                image.setUrl(AWSS3UrlUtil.convertToS3Path(image.getUrl()));
                if (image.getWidth() == 0 || image.getHeight() == 0) {
                    try {
                        URL url = new URL(getFullUrl(image.getUrl()));
                        BufferedImage i = ImageIO.read(url);
                        image.setWidth(i.getWidth());
                        image.setHeight(i.getHeight());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                return image;
            }).collect(Collectors.toList());
    }

    public static List<ImageDto> convertToFullImageUrl(List<ImageDto> images) {
        return images.stream()
            .map(image -> {
                image.setUrl(AWSS3UrlUtil.convertToS3Url(image.getUrl()));
                return image;
            }).collect(Collectors.toList());
    }

    public static String convertToS3Path(String url) {
        return url.replace(urlPrefix + "/", "");
    }
}
