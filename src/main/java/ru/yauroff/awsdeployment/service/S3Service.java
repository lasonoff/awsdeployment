package ru.yauroff.awsdeployment.service;

import java.io.IOException;

/**
 * Класс для взаимодействия с AWS S3 сервисом.
 *
 * @author Yauroff
 */
public interface S3Service {
    String putZipObject(String zipFileLocation, String zipFileName) throws IOException;

    void deleteZipObject(String zipFileName);
}
