package org.taskmanagerapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.taskmanagerapi.models.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
