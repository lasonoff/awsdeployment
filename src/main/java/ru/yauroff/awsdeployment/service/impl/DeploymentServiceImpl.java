package ru.yauroff.awsdeployment.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yauroff.awsdeployment.model.Project;
import ru.yauroff.awsdeployment.repository.ProjectRepository;
import ru.yauroff.awsdeployment.service.*;

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

    @Autowired
    private K8sService k8sService;

    @Autowired
    private ProjectRepository projectRepository;

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
        String tagImage = codeBuildAWSService.buildDockerImage(s3Path, project.getName());
        Project deployedProject = k8sService.createServiceWithDeployment(project, tagImage);
        projectRepository.save(deployedProject);
        Project projectWithURL = k8sService.updateProjectUrl(deployedProject);
        projectRepository.save(projectWithURL);
    }
}
