package ru.yauroff.awsdeployment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yauroff.awsdeployment.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
