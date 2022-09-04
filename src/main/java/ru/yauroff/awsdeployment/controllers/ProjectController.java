package ru.yauroff.awsdeployment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yauroff.awsdeployment.dto.ProjectResponseDTO;
import ru.yauroff.awsdeployment.model.Project;
import ru.yauroff.awsdeployment.service.ProjectService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        List<Project> projectList = projectService.getAll();
        if (projectList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<ProjectResponseDTO> projectResponseDTOList = projectList.stream()
                                                                     .map(project -> ProjectResponseDTO.fromProject(project))
                                                                     .collect(Collectors.toList());
        return new ResponseEntity<>(projectResponseDTOList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getProject(@PathVariable("id") Long projectId) {
        if (projectId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Project project = projectService.getById(projectId);
        if (project == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ProjectResponseDTO projectResponseDTO = ProjectResponseDTO.fromProject(project);
        return new ResponseEntity<>(projectResponseDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> getCountProjects() {
        Long count = this.projectService.getCount();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
