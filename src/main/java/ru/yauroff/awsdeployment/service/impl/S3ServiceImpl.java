package ru.yauroff.awsdeployment.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yauroff.awsdeployment.service.S3Service;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
/**
 * Класс для взаимодействия с AWS S3 сервисом.
 *
 * @author Yauroff
 */
public class S3ServiceImpl implements S3Service {

    private final String DELIMITER = "/";

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.s3.dirName}")
    private String projectDirName;

    @Autowired
    private AmazonS3 amazonS3;

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
