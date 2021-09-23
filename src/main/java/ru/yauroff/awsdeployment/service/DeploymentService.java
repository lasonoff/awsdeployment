package ru.yauroff.awsdeployment.service;

import ru.yauroff.awsdeployment.model.Project;

public interface DeploymentService {

    void deploy(Project project);
}
