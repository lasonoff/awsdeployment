package ru.yauroff.awsdeployment.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.yauroff.awsdeployment.model.Project;
import ru.yauroff.awsdeployment.model.User;
import ru.yauroff.awsdeployment.repository.ProjectRepository;
import ru.yauroff.awsdeployment.service.DeploymentService;
import ru.yauroff.awsdeployment.service.ProjectService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    private final DeploymentService deploymentService;

    public ProjectServiceImpl(ProjectRepository projectRepository, DeploymentService deploymentService) {
        this.projectRepository = projectRepository;
        this.deploymentService = deploymentService;
    }

    @Override
    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    @Override
    public Project getById(Long id) {
        return projectRepository.getById(id);
    }

    @Override
    public long getCount() {
        return projectRepository.count();
    }

    @Override
    public Project uploadProject(MultipartFile multipartFile, Project projectEntity, User user) throws IOException {
        // Create archive with project ID
        File tempFile = File.createTempFile("_" + projectEntity.getName(), ".zip");
        projectEntity.setPath(tempFile.getPath());
        // Create object project
        Project projEntity = projectRepository.save(projectEntity);
        // Write data to temp file
        byte[] bytes = multipartFile.getBytes();
        BufferedOutputStream stream =
                new BufferedOutputStream(new FileOutputStream(tempFile));
        stream.write(bytes);
        stream.close();
        // TODO: start deploymentService
        Thread th = new Thread(() -> {
            deploymentService.deploy(projEntity);
        });
        th.start();
        return projEntity;
    }

    @Override
    public void deleteProject(Project projectEntity, User user) throws IOException {

    }
}
