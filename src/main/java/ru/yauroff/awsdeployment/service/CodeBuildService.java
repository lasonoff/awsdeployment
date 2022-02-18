package ru.yauroff.awsdeployment.service;


/**
 * Интерфейс для взаимодействия с AWS CodeBuild сервисом.
 *
 * @author Yauroff
 */
public interface CodeBuildService {

    void buildDockerImage(String s3path, String projectName);
}
