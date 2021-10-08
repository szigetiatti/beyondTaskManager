package com.example.demo.application.task;

import java.util.List;
import java.util.Optional;

/**
 * Defines basic functions for Task entity.
 *
 * @author szigeti.attila96@gmail.com
 */
public interface TaskService {

  /**
   * Create a Task from Dto for the user matching userId,
   * gives back the Dto representation for it.
   * When userId not matches, returns Optional.empty().
   *
   * @param task   TaskDto
   * @param userId Long
   * @return TaskDto wrapped into Optional.
   */
  Optional<TaskDto> createTask(TaskDto task, Long userId);

  /**
   * Updates Task matching with userId and taskId,
   * gives back the updated Task Dto representation.
   *
   * @param userId Long
   * @param id     Long - taskId
   * @param task   TaksDto
   * @return updated TaskDto wrapped into Optional.
   */
  Optional<TaskDto> updateTask(Long userId, Long id, TaskDto task);

  /**
   * @param userId Long
   * @return all TaskDtos for a user in a List.
   */
  List<TaskDto> listAllTasksForUser(Long userId);

  /**
   * @param taskId Long
   * @param userId Long
   * @return TaskDto wrapped into Optional with matching userId and taskId.
   */
  Optional<TaskDto> getTaskInfo(Long taskId, Long userId);

  /**
   * Deletes a Task
   *
   * @param userId Long
   * @param id     Long
   */
  void deleteTask(Long userId, Long id);

}
