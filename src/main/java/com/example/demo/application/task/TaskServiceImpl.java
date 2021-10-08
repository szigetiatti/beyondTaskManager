package com.example.demo.application.task;

import com.example.demo.domain.shared.ProcessingStatus;
import com.example.demo.domain.task.Task;
import com.example.demo.domain.task.TaskRepository;
import com.example.demo.domain.user.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation class of {@link TaskService}
 *
 * @author szigeti.attila96@gmail.com
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TaskServiceImpl implements TaskService {

  private final TaskRepository taskRepository;
  private final UserRepository userRepository;

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<TaskDto> createTask(TaskDto taskDto, Long userId) {

    if (isUserNotExists(userId)) {
      log.info("User does not exists with id: {}", userId);
      return Optional.empty();
    }
    Task task = deriveTaskFromDto(taskDto, userId);
    return Optional.of(createTaskDto(taskRepository.save(task)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<TaskDto> updateTask(Long userId, Long id, TaskDto task) {
    if (isUserNotExists(userId)) {
      log.info("User does not exists with id: {}", userId);
      return Optional.empty();
    }
    Optional<Task> taskToUpdate = taskRepository.findById(id)
        .filter(task1 -> ProcessingStatus.PENDING.equals(task1.getProcessingStatus()));
    if (taskToUpdate.isPresent()) {
      Task updateTask = deriveTaskFromDto(task, taskToUpdate.get().getUserId());
      if (updateTask == null) {
        log.error("UserDto is null");
        return Optional.empty();
      }
      if (taskToUpdate.get().merge(updateTask)) {
        taskRepository.save(taskToUpdate.get());
      }
      return Optional.of(createTaskDto(taskToUpdate.get()));
    }
    log.info("Task to update does not exists with id: {} and userId : {} ", id, userId);
    return Optional.empty();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<TaskDto> listAllTasksForUser(Long userId) {
    if (isUserNotExists(userId)) {
      log.info("User does not exists with id: {}", userId);
      return Collections.emptyList();
    }
    List<Task> taskList = taskRepository.findAllByUserId(userId);
    return taskList.stream().map(task -> createTaskDto(task)).collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<TaskDto> getTaskInfo(Long taskId, Long userId) {
    if (isUserNotExists(userId)) {
      log.info("User does not exists with id: {}", userId);
      return Optional.empty();
    }
    return taskRepository.findById(taskId).map(this::createTaskDto);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteTask(Long userId, Long taskId) {
    if (isUserNotExists(userId)) {
      log.info("User does not exists with id: {}", userId);
    } else {
      Optional<Task> taskToDelete = taskRepository.findById(taskId);
      taskToDelete.ifPresent(taskRepository::delete);
      log.info("Task deleted with taskId : {}", taskId);
    }
  }

  /**
   *
   * @param taskDto TaskDto
   * @param userId Long
   * @return Task derived from dto.
   */
  private Task deriveTaskFromDto(TaskDto taskDto, Long userId) {
    return Task.builder()
        .name(taskDto.getTaskName())
        .description(taskDto.getDescription())
        .scheduledDate(taskDto.getDateTime())
        .userId(userId)
        .processingStatus(ProcessingStatus.PENDING)
        .build();
  }

  /**
   *
   * @param task Task
   * @return TaskDto created from Task.
   */
  private TaskDto createTaskDto(Task task) {
    return TaskDto.builder()
        .id(task.getId())
        .taskName(task.getName())
        .description(task.getDescription())
        .dateTime(task.getScheduledDate())
        .taskStatus(task.getProcessingStatus())
        .build();
  }

  /**
   * @param userId Long
   * @return true when user exists, else false.
   */
  private boolean isUserNotExists(Long userId) {
    if (userRepository.findById(userId).isEmpty()) {
      log.info("User with Id: {} doesn't exist!", userId);
      return true;
    }
    return false;
  }
}
