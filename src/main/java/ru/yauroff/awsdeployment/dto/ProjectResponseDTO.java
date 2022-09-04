package ru.yauroff.awsdeployment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yauroff.awsdeployment.model.Project;
import ru.yauroff.awsdeployment.model.ProjectStatus;

@Data
@AllArgsConstructor
public class ProjectResponseDTO {

    private Long id;
    private String name;
    private String description;
    private ProjectStatus status;
    private String url;

    public static ProjectResponseDTO fromProject(Project project) {
        ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO(project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStatus(),
                project.getUrl());
        return projectResponseDTO;
    }
}
