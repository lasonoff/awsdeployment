package ru.yauroff.awsdeployment.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ru.yauroff.awsdeployment.service.S3AWSService;

import java.io.File;
import java.io.IOException;

public class S3AWSServiceImpl implements S3AWSService {

    private final String DELIMITER = "/";

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${s3.bucket}")
    private String bucketName;

    @Value("${s3.dirName}")
    private String projectDirName;


    @Override
    public void putZipObject(String zipFileLocation, String zipFileName) throws IOException {
        // Read file from disk
        File zipFile = new File(zipFileLocation);
        // Write to AWS S3
        amazonS3.putObject(
                bucketName,
                projectDirName + DELIMITER + zipFileName + ".zip",
                zipFile
        );
        // Delete file
        zipFile.delete();
    }

    @Override
    public void deleteZipObject(String zipFileName) {
        amazonS3.deleteObject(bucketName, projectDirName + DELIMITER + zipFileName + ".zip");
    }
}
