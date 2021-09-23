package ru.yauroff.awsdeployment.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yauroff.awsdeployment.service.S3AWSService;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class S3AWSServiceImpl implements S3AWSService {

    private final String DELIMITER = "/";

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.s3.dirName}")
    private String projectDirName;


    @Override
    public String putZipObject(String zipFileLocation, String zipFileName) throws IOException {
        // Read file from disk
        File zipFile = new File(zipFileLocation);
        String s3PathWithoutBucket = projectDirName + DELIMITER + zipFileName + ".zip";
        // Write to AWS S3
        amazonS3.putObject(
                bucketName,
                s3PathWithoutBucket,
                zipFile
        );
        // Delete file
        zipFile.delete();
        return bucketName + DELIMITER + s3PathWithoutBucket;
    }

    @Override
    public void deleteZipObject(String zipFileName) {
        amazonS3.deleteObject(bucketName, projectDirName + DELIMITER + zipFileName + ".zip");
    }
}
