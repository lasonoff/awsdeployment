package ru.yauroff.awsdeployment.model;

public enum ProjectStatus {
    LOADED_ZIP,
    VALIDATED,
    VALIDATE_ERROR,
    BUILT_JAR,
    BUILD_JAR_ERROR,
    BUILT_IMAGE,
    BUILD_IMAGE_ERROR,
    PUSHED_IMAGE,
    PUSH_IMAGE_ERROR,
    DEPLOYED,
    DEPLOY_ERROR
}
