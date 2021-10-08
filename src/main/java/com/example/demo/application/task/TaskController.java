package com.example.demo.application.task;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handling REST requests for Task entity.
 * Incoming object is {@link TaskDto}
 *
 * @author szigeti.attila96@gmail.com
 */
@RestController
@RequestMapping("/user")
public class TaskController {

  private final TaskService taskService;

  @Autowired
  TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @PostMapping("/{user_id}/task")
  public ResponseEntity<TaskDto> create(@PathVariable("user_id") Long userId, @Valid @RequestBody TaskDto task) {
    return ResponseEntity.of(taskService.createTask(task, userId));
  }

  @PutMapping("/{user_id}/task/{task_id}")
  public ResponseEntity<TaskDto> update(@PathVariable("user_id") Long userId, @Valid @RequestBody TaskDto task,
      @PathVariable("task_id") Long taskId) {
    return ResponseEntity.of(taskService.updateTask(userId, taskId, task));
  }

  @DeleteMapping("/{user_id}/task/{task_id}")
  public ResponseEntity<Object> deleteTask(@PathVariable("user_id") Long userId, @PathVariable("task_id") Long taskId) {
    taskService.deleteTask(userId, taskId);

    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{user_id}/task")
  public ResponseEntity<List<TaskDto>> listAllTasksForUser(@PathVariable("user_id") Long id) {
    List<TaskDto> dtoList = taskService.listAllTasksForUser(id);
    if (!dtoList.isEmpty()) {
      return ResponseEntity.ok(dtoList);
    }
    return ResponseEntity.notFound().build();
  }

  @GetMapping("/{user_id}/task/{task_id}")
  public ResponseEntity<TaskDto> getTaskInfo(@PathVariable("user_id") Long userId,
      @PathVariable("task_id") Long taskId) {
    return ResponseEntity.of(taskService.getTaskInfo(taskId, userId));
  }
}
