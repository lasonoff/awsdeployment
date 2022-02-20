package ru.yauroff.awsdeployment.service.impl;

import com.amazonaws.services.codebuild.AWSCodeBuild;
import com.amazonaws.services.codebuild.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yauroff.awsdeployment.service.CodeBuildService;

/**
 * Класс для взаимодействия с AWS CodeBuild сервисом.
 *
 * @author Yauroff
 */
@Service
@Slf4j
public class CodeBuildServiceImpl implements CodeBuildService {
    @Value("${aws.codebuild.region}")
    private String regionName;
    @Value("${aws.account.id}")
    private String accountId;

    @Autowired
    private AWSCodeBuild awsCodeBuild;

    @Override
    public String buildDockerImage(String s3path, String projectName) {
        String tagImage = accountId + ".dkr.ecr." + regionName + ".amazonaws.com/" + projectName + ":latest";
        CreateProjectRequest createProjectRequest = new CreateProjectRequest()
                .withName(projectName)
                .withDescription("Project for deploy with name " + projectName)
                .withSource(new ProjectSource()
                        .withType(SourceType.S3)
                        .withLocation(s3path)
                        .withBuildspec(createBuildSpec(projectName, tagImage)))
                .withEnvironment(new ProjectEnvironment()
                        .withType("LINUX_CONTAINER")
                        .withImage("aws/codebuild/amazonlinux2-x86_64-standard:3.0")
                        .withPrivilegedMode(true)
                        .withComputeType(ComputeType.BUILD_GENERAL1_SMALL))
                .withServiceRole("arn:aws:iam::" + accountId + ":role/service-role/codebuild-demo1-service-role")
                .withEncryptionKey("arn:aws:kms:" + regionName + ":" + accountId + ":alias/aws/s3")
                .withTimeoutInMinutes(60)
                .withArtifacts(new ProjectArtifacts()
                        .withType(ArtifactsType.NO_ARTIFACTS));
        log.info("CreateProjectRequest : " + createProjectRequest);
        CreateProjectResult createProjectResult = awsCodeBuild.createProject(createProjectRequest);
        StartBuildResult startBuildResult = awsCodeBuild.startBuild(new StartBuildRequest()
                .withProjectName(createProjectResult.getProject()
                                                    .getName()));
        startBuildResult.getBuild()
                        .getBuildStatus();
        return tagImage;
    }

    private String createBuildSpec(String projectName, String tagImage) {
        log.info("Tag docker image " + tagImage);
        String buildSpec = "version: 0.2\n" +
                "\n" +
                "phases:\n" +
                "  install:\n" +
                "    runtime-versions:\n" +
                "      docker: 19\n" +
                "\n" +
                "  pre_build:\n" +
                "    commands:\n" +
                "      - echo Logging in to Amazon ECR...\n" +
                "      - aws ecr get-login-password --region " + regionName +
                " | docker login --username AWS --password-stdin " + accountId + ".dkr.ecr." + regionName + ".amazonaws.com\n" +
                "  build:\n" +
                "    commands:\n" +
                "      - echo Build started on `date`\n" +
                "      - echo Building jar...\n" +
                "      - bash ./gradlew clean build\n" +
                "      - echo Building the Docker image...          \n" +
                "      - docker build -t " + projectName + " .\n" +
                "      - docker tag " + projectName + ":latest " + tagImage + " \n" +
                "  post_build:\n" +
                "    commands:\n" +
                "      - echo Build completed on `date`\n" +
                "      - echo Pushing the Docker image...\n" +
                "      - docker push " + tagImage;
        log.info(buildSpec);
        return buildSpec;
    }
}
