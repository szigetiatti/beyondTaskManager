package com.example.demo.application.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import com.example.demo.domain.shared.ProcessingStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Unit test class for TaskController class.
 *
 * @author szigeti.attila96@gmail.com
 */

@RunWith(MockitoJUnitRunner.class)
public class TaskControllerTest {

  private static final Long USER_ID = 1L;
  private static final Long TASK_ID = 2L;

  @Mock
  private TaskService taskService;

  @InjectMocks
  private TaskController taskController;

  @Test
  public void testGetTaskSuccessfully() {
    when(taskService.getTaskInfo(Mockito.any(), Mockito.any())).thenReturn(Optional.of(createTaskDto("getTask")));
    assertEquals(HttpStatus.OK, taskController.getTaskInfo(USER_ID, TASK_ID).getStatusCode());
  }

  @Test
  public void testGetTaskNotFound() {
    assertEquals(HttpStatus.NOT_FOUND, taskController.getTaskInfo(USER_ID, TASK_ID).getStatusCode());
  }

  @Test
  public void testListUserTasksSuccessfully() {
    when(taskService.listAllTasksForUser(USER_ID)).thenReturn(new ArrayList<>() {{
      add(createTaskDto("firstTask"));
      add(createTaskDto("secondTask"));
    }});

    ResponseEntity<List<TaskDto>> resultList = taskController.listAllTasksForUser(USER_ID);
    assertEquals(HttpStatus.OK, resultList.getStatusCode());
    assertNotNull(resultList.getBody());
    assertEquals(2, resultList.getBody().size());
  }

  @Test
  public void testListUserTasksNotFound() {
    when(taskService.listAllTasksForUser(USER_ID)).thenReturn(new ArrayList<>());
    assertEquals(HttpStatus.NOT_FOUND, taskController.listAllTasksForUser(USER_ID).getStatusCode());
  }

  @Test
  public void testCreateTaskSuccessfully() {
    TaskDto taskDto = createTaskDto("createTask");
    when(taskService.createTask(taskDto, USER_ID)).thenReturn(Optional.of(taskDto));
    assertEquals(HttpStatus.OK, taskController.create(USER_ID, taskDto).getStatusCode());
  }

  @Test
  public void testCreateTaskNotFound() {
    TaskDto taskDto = createTaskDto("createTask");
    when(taskService.createTask(taskDto, USER_ID)).thenReturn(Optional.empty());
    assertEquals(HttpStatus.NOT_FOUND, taskController.create(USER_ID, taskDto).getStatusCode());
  }

  @Test
  public void testUpdateTaskSuccessfully() {
    TaskDto taskDto = createTaskDto("updateTask");
    when(taskService.updateTask(USER_ID, TASK_ID, taskDto)).thenReturn(Optional.of(taskDto));
    assertEquals(HttpStatus.OK, taskController.update(USER_ID, taskDto, TASK_ID).getStatusCode());
  }

  @Test
  public void testUpdateTaskNotFound() {
    TaskDto taskDto = createTaskDto("updateTask");
    when(taskService.updateTask(USER_ID, TASK_ID, taskDto)).thenReturn(Optional.empty());
    assertEquals(HttpStatus.NOT_FOUND, taskController.update(USER_ID, taskDto, TASK_ID).getStatusCode());
  }

  @Test
  public void testDeleteTaskSuccessfully() {
    TaskDto taskDto = createTaskDto("deleteTask");
    assertEquals(HttpStatus.NO_CONTENT, taskController.deleteTask(USER_ID, TASK_ID).getStatusCode());
  }

  @Test
  public void testDeleteTaskNotFound() {
    TaskDto taskDto = createTaskDto("deleteTask");
    assertEquals(HttpStatus.NO_CONTENT, taskController.deleteTask(USER_ID, TASK_ID).getStatusCode());
  }

  private TaskDto createTaskDto(String taskName) {
    return TaskDto.builder()
        .taskName(taskName)
        .description("description")
        .dateTime(LocalDateTime.now())
        .taskStatus(ProcessingStatus.PENDING)
        .build();
  }
}
