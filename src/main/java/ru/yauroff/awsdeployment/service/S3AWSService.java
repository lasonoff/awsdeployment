package ru.yauroff.awsdeployment.service;

import java.io.IOException;

public interface S3AWSService {
    String putZipObject(String zipFileLocation, String zipFileName) throws IOException;

    void deleteZipObject(String zipFileName);
}
