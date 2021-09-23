package ru.yauroff.awsdeployment.service;

public interface CodeBuildAWSService {

    void buildDockerImage(String s3path, String projectName);
}
