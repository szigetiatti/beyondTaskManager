package com.example.demo.application.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.example.demo.domain.shared.ProcessingStatus;
import com.example.demo.domain.task.Task;
import com.example.demo.domain.task.TaskRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test class for ScheduledJob class.
 *
 * @author szigeti.attila96@gmail.com
 */
@RunWith(MockitoJUnitRunner.class)
public class ScheduledJobTest {

  @Mock
  private TaskRepository taskRepository;
  @InjectMocks
  private ScheduledJob scheduledJob;

  @Test
  public void testNoPendingTaskFound() {
    assertTrue(scheduledJob.checkPendingTasks().isEmpty());
  }

  @Test
  public void testSuccessfulExecution() {
    List<Task> taskList = new ArrayList<>() {{
      add(createTask());
      add(createTask());
      add(createTask());
    }};

    when(taskRepository.findAllByProcessingStatus(Mockito.any()))
        .thenReturn(taskList);

    List<Long> resultList = scheduledJob.checkPendingTasks();
    assertFalse(resultList.isEmpty());
    assertEquals(taskList.size(), resultList.size());
  }

  private Task createTask() {
    return Task.builder()
        .name("taskName")
        .description("description")
        .scheduledDate(LocalDateTime.now().minusMinutes(3))
        .userId(2L)
        .processingStatus(ProcessingStatus.PENDING)
        .build();
  }
}
