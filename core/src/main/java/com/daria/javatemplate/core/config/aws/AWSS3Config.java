package com.daria.javatemplate.core.config.aws;

import com.amazonaws.auth.*;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class AWSS3Config {

    @Value("${aws.s3.client.region:}")
    private String clientRegion;

    @Value("${aws.s3.key.access:}")
    private String accessKey;

    @Value("${aws.s3.key.secret:}")
    private String secretKey;

    private final Environment environment;


    @Bean
    public AmazonS3 s3() {
        if (Arrays.asList(environment.getActiveProfiles()).contains("local")) {
            AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

            return AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .build();
        } else {
            return AmazonS3ClientBuilder.standard()
                    .withCredentials(new EC2ContainerCredentialsProviderWrapper())
                    .build();
        }
    }
}
