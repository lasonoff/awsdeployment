package ru.yauroff.awsdeployment.service;

import ru.yauroff.awsdeployment.model.Project;

public interface K8sService {
    Project createServiceWithDeployment(Project project, String repositoryURI);

    Project updateProjectUrl(Project project);
}
