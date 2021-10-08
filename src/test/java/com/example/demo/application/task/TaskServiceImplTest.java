package com.example.demo.application.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.domain.shared.ProcessingStatus;
import com.example.demo.domain.task.Task;
import com.example.demo.domain.task.TaskRepository;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Test class for TaskServiceImpl.
 *
 * @author szigeti.attila96@gmail.com
 */
@RunWith(MockitoJUnitRunner.class)
public class TaskServiceImplTest {

  private static final Long USER_ID = 1L;
  private static final Long TASK_ID = 2L;
  private static final String USER_EXISTS = "userExists";

  @Mock
  private TaskRepository taskRepository;
  @Mock
  private UserRepository userRepository;
  @InjectMocks
  private TaskServiceImpl taskService;

  private LocalDateTime actualDateTime;

  @Before
  public void setup() {
    actualDateTime = LocalDateTime.now();
  }

  @Test
  public void testCreateTaskNonExistingUser() {
    assertTrue(taskService.createTask(createTaskDto("testUserNotExist"), TASK_ID).isEmpty());
    assertTrue(taskService.createTask(createTaskDto("testUserNotExist"), null).isEmpty());
    assertTrue(taskService.createTask(null, null).isEmpty());
  }

  @Test(expected = NullPointerException.class)
  public void testCreateTaskTaskDtoIsNull() {
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(createUser("taskDtoIsNull")));
    taskService.createTask(createTaskDto("nonExistingUser"), USER_ID);
  }

  @Test
  public void testCreateTaskSuccessfully() {
    TaskDto taskDto = createTaskDto("createTask");
    Task task = createTask("createTask");

    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(createUser(USER_EXISTS)));
    when(taskRepository.save(any(Task.class))).thenAnswer((i -> i.getArguments()[0]));

    Optional<TaskDto> result = taskService.createTask(taskDto, USER_ID);
    assertTrue(result.isPresent());
    assertEquals(task.getScheduledDate(), result.get().getDateTime());
    assertEquals(task.getDescription(), result.get().getDescription());
    assertEquals(task.getName(), result.get().getTaskName());
    assertEquals(task.getProcessingStatus(), result.get().getTaskStatus());
  }

  @Test
  public void testDeleteTaskNonExistingUser() {
    Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(createUser("asd")));
    taskService.deleteTask(USER_ID, TASK_ID);
    verify(taskRepository, Mockito.times(1)).findById(Mockito.any());
  }

  @Test
  public void testDeleteTaskSuccessfully() {
    Task task = createTask("deleteTask");
    TaskDto taskDto = createTaskDto("deleteTask");
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(createUser(USER_EXISTS)));
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));

    taskService.deleteTask(USER_ID, TASK_ID);
    verify(taskRepository, Mockito.times(1)).delete(Mockito.any(Task.class));
  }

  @Test
  public void testUpdateTaskNonExistingUser() {
    TaskDto taskDto = createTaskDto("nonExistingUser");
    assertTrue(taskService.updateTask(USER_ID, TASK_ID, taskDto).isEmpty());
  }

  @Test
  public void testUpdateTaskCannotBeFound() {
    TaskDto taskDto = createTaskDto("taskNotFound");
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(createUser(USER_EXISTS)));
    assertTrue(taskService.updateTask(USER_ID, TASK_ID, taskDto).isEmpty());
  }

  @Test
  public void testUpdateTaskFilterDone() {
    Task task = createTask("filterDone");
    ReflectionTestUtils.setField(task, "processingStatus", ProcessingStatus.DONE);
    TaskDto taskDto = createTaskDto("filterDone");

    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(createUser(USER_EXISTS)));
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));

    assertTrue(taskService.updateTask(USER_ID, TASK_ID, taskDto).isEmpty());
  }

  @Test(expected = NullPointerException.class)
  public void testUpdateTaskDtoIsNull() {
    Task task = createTask("dtoIsNull");

    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(createUser(USER_EXISTS)));
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));

   taskService.updateTask(USER_ID, TASK_ID, null);
  }

  @Test
  public void testUpdateTaskSuccessfully_WithDateTime() {
    Task task = createTask("updateTask");
    Task incomingTask = createTask("incomingTask");
    ReflectionTestUtils.setField(task, "scheduledDate", LocalDateTime.now());
    ReflectionTestUtils.setField(task, "description", "incomingDesc");
    TaskDto taskDto = createTaskDto("updateTask");

    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(createUser(USER_EXISTS)));
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));

    Optional<TaskDto> result = taskService.updateTask(USER_ID, TASK_ID, taskDto);
    assertTrue(result.isPresent());
    assertEquals(task.getScheduledDate(), incomingTask.getScheduledDate());
    assertEquals(task.getDescription(), incomingTask.getDescription());
  }

  @Test
  public void testGetTaskNonExistingUser() {
    assertTrue(taskService.getTaskInfo(USER_ID, TASK_ID).isEmpty());
  }

  @Test
  public void testGetTaskCannotBeFound() {
    when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(createUser(USER_EXISTS)));
    assertTrue(taskService.getTaskInfo(USER_ID, TASK_ID).isEmpty());
  }

  @Test
  public void testGetTaskSuccessfully() {
    Task task = createTask("getTask");
    TaskDto taskDto = createTaskDto("getTask");

    when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(createUser(USER_EXISTS)));
    when(taskRepository.findById(Mockito.any())).thenReturn(Optional.of(task));

    Optional<TaskDto> result = taskService.getTaskInfo(USER_ID, TASK_ID);
    assertTrue(result.isPresent());
    assertEquals(taskDto.getDateTime(), result.get().getDateTime());
    assertEquals(taskDto.getDescription(), result.get().getDescription());
    assertEquals(taskDto.getTaskName(), result.get().getTaskName());
  }

  @Test
  public void testListUserTasksCannotBeFound() {
    assertTrue(taskService.listAllTasksForUser(USER_ID).isEmpty());
  }

  @Test
  public void testListUserTasksSuccessfully() {
    TaskDto taskDto = createTaskDto("listUserTask");
    List<Task> taskList = new ArrayList<>() {{
      add(createTask("listUserTask1"));
      add(createTask("listUserTask2"));
    }};
    when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(createUser("listUser")));
    when(taskRepository.findAllByUserId(USER_ID)).thenReturn(taskList);
    List<TaskDto> result = taskService.listAllTasksForUser(USER_ID);
    assertFalse(result.isEmpty());
    assertEquals(taskList.size(), result.size());
  }

  private TaskDto createTaskDto(String taskName) {
    return TaskDto.builder()
        .taskName(taskName)
        .description("description")
        .dateTime(actualDateTime)
        .taskStatus(ProcessingStatus.PENDING)
        .build();
  }

  private Task createTask(String taskName) {
    return Task.builder()
        .name(taskName)
        .description("description")
        .scheduledDate(actualDateTime)
        .userId(USER_ID)
        .processingStatus(ProcessingStatus.PENDING)
        .build();
  }

  private User createUser(String userName) {
    return User.builder()
        .userName(userName)
        .firstName("firstName")
        .lastName("lastName")
        .build();
  }
}
