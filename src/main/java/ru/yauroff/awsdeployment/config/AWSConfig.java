package ru.yauroff.awsdeployment.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.codebuild.AWSCodeBuild;
import com.amazonaws.services.codebuild.AWSCodeBuildClientBuilder;
import com.amazonaws.services.ecr.AmazonECR;
import com.amazonaws.services.ecr.AmazonECRClientBuilder;
import com.amazonaws.services.eks.AmazonEKS;
import com.amazonaws.services.eks.AmazonEKSClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AWSConfig {
    @Value("${aws.accesskey}")
    private String accessKey;
    @Value("${aws.secretkey}")
    private String secretKey;
    @Value("${aws.s3.region}")
    private String s3RegionName;
    @Value("${aws.codebuild.region}")
    private String codebuildRegionName;
    @Value("${aws.ecr.region}")
    private String ecrRegionName;
    @Value("${aws.eks.region}")
    private String eksRegionName;

    @Bean
    public AmazonS3 s3Client() {
        log.info("S3RegionName = " + s3RegionName);
        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(
                        accessKey,
                        secretKey
                )))
                .withRegion(Regions.fromName(s3RegionName))
                .build();
        return s3client;
    }

    @Bean
    public AWSCodeBuild codebuildClient() {
        log.info("CodebuildRegionName = " + codebuildRegionName);
        AWSCodeBuild codeBuild = AWSCodeBuildClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(
                        accessKey,
                        secretKey
                )))
                .withRegion(Regions.fromName(codebuildRegionName))
                .build();

        return codeBuild;
    }

    @Bean
    public AmazonECR ecrClient() {
        log.info("EcrRegionName = " + ecrRegionName);
        AmazonECR ecrClient = AmazonECRClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(
                        accessKey,
                        secretKey
                )))
                .withRegion(Regions.fromName(ecrRegionName))
                .build();
        return ecrClient;
    }

    @Bean
    public AmazonEKS eksClient() {
        log.info("EksRegionName = " + eksRegionName);
        AmazonEKS eksClient = AmazonEKSClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(
                        accessKey,
                        secretKey
                )))
                .withRegion(Regions.fromName(eksRegionName))
                .build();
        return eksClient;
    }

}
