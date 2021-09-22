package ru.yauroff.awsdeployment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.yauroff.awsdeployment.model.Project;
import ru.yauroff.awsdeployment.model.User;
import ru.yauroff.awsdeployment.repository.ProjectRepository;
import ru.yauroff.awsdeployment.service.ProjectService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;


    @Override
    public List<Project> getAll() {
        return null;
    }

    @Override
    public Project getById(Long id) {
        return null;
    }

    @Override
    public long getCount() {
        return 0;
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
        return projEntity;
    }

    @Override
    public void deleteProject(Project projectEntity, User user) throws IOException {

    }
}
