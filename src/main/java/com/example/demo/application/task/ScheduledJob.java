package com.example.demo.application.task;

import com.example.demo.domain.shared.ProcessingStatus;
import com.example.demo.domain.task.Task;
import com.example.demo.domain.task.TaskRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled job to check all tasks in the Database and update the required ones.
 *
 *
 * @author szigeti.attila96@gmail.com
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ScheduledJob {

  private final TaskRepository taskRepository;

  /**
   * Update all the PENDING Tasks to DONE which date expired.
   *
   */
  @Scheduled(fixedDelayString = "${schedulerjob.thread.frequency}",
      initialDelayString = "${schedulerjob.thread.frequency}")
  public List<Long> checkPendingTasks() {
    log.info("Scheduled job for processing PENDING Tasks has just started");
    List<Task> pendingList = taskRepository.findAllByProcessingStatus(ProcessingStatus.PENDING);
    if (!pendingList.isEmpty()) {
      List<Long> pendingIds = new ArrayList<>();
      log.info("Reprocessing {} Pending Tasks", pendingList.size());
      pendingList.stream()
          .filter(this::isTaskCompleted)
          .forEach(task -> {
            updateTaskToDone(task);
            pendingIds.add(task.getId());
          });
      return pendingIds;
    }
    return Collections.emptyList();
  }

  /**
   *
   * @param task Task
   * @return true if Task's processing status should be updated to DONE, else return false.
   */
  private boolean isTaskCompleted(Task task) {
    return !task.getScheduledDate().isAfter(LocalDateTime.now());
  }

  /**
   * Updates task
   *
   * @param task Task
   */
  private void updateTaskToDone(Task task) {
    task.setTaskToDone();
    System.out.println(task);
    taskRepository.save(task);
  }

}
