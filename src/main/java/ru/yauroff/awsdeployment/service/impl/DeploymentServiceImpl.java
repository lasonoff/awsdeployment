package ru.yauroff.awsdeployment.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yauroff.awsdeployment.model.Project;
import ru.yauroff.awsdeployment.service.CodeBuildService;
import ru.yauroff.awsdeployment.service.DeploymentService;
import ru.yauroff.awsdeployment.service.ECRService;
import ru.yauroff.awsdeployment.service.S3Service;

import java.io.IOException;

@Service
@Slf4j
public class DeploymentServiceImpl implements DeploymentService {
    @Autowired
    private S3Service s3AWSService;
    @Autowired
    private CodeBuildService codeBuildAWSService;
    @Autowired
    private ECRService ecrService;

    @Override
    public void deploy(Project project) {
        String s3Path = null;
        try {
            s3Path = s3AWSService.putZipObject(project.getPath(), project.getName());
        } catch (IOException e) {
            log.error(e.getMessage());
            return;
        }
        ecrService.createPrivateRepository(project.getName());
        codeBuildAWSService.buildDockerImage(s3Path, project.getName());
    }
}
