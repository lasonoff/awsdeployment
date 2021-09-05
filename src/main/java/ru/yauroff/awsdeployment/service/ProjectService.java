package ru.yauroff.awsdeployment.service;

import org.springframework.web.multipart.MultipartFile;
import ru.yauroff.awsdeployment.model.Project;
import ru.yauroff.awsdeployment.model.User;

import java.io.IOException;
import java.util.List;

public interface ProjectService {
    List<Project> getAll();

    Project getById(Long id);

    long getCount();

    Project uploadProject(MultipartFile multipartFile, Project projectEntity, User user) throws IOException;

    void deleteProject(Project projectEntity, User user) throws IOException;
}
