package ru.yauroff.awsdeployment.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yauroff.awsdeployment.dto.ProjectResponseDTO;
import ru.yauroff.awsdeployment.model.Project;
import ru.yauroff.awsdeployment.model.ProjectStatus;
import ru.yauroff.awsdeployment.service.ProjectService;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectControllerV1 {

    @Autowired
    ProjectService projectService;

    @PostMapping
    public @ResponseBody
    ResponseEntity<ProjectResponseDTO> uploadFile(@RequestParam("name") String name,
                                                  @RequestParam("description") String description,
                                                  @RequestParam("project") MultipartFile multipartFile,
                                                  Authentication authentication) {
        if (multipartFile.isEmpty() || name == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // TODO: add User Service
        //User user = userService.getByLogin(authentication.getName());

        Project projectEntity = new Project();
        projectEntity.setDescription(description);
        projectEntity.setName(name);
        projectEntity.setStatus(ProjectStatus.LOADED_ZIP);

        try {
            projectEntity = this.projectService.uploadProject(multipartFile, projectEntity, null);
        } catch (IOException e) {
            new ResponseEntity<>("Error upload file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ProjectResponseDTO fileResponseDTO = ProjectResponseDTO.fromProject(projectEntity);
        return new ResponseEntity<>(fileResponseDTO, HttpStatus.OK);
    }
}
