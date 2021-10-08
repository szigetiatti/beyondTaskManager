package com.example.demo.domain.task;

import com.example.demo.domain.shared.ProcessingStatus;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * @author szigeti.attila96@gmail.com
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Slf4j
@Table(name = "BEYOND_TASK",
    indexes = {
        @Index(name = "BEYOND_TASK_U1", columnList = "TASK_ID", unique = true),
        @Index(name = "BEYOND_TASK_N1", columnList = "USER_ID")
    })
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "TASK_ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "TASK_NAME", nullable = false, length = 240)
  private String name;

  @Column(name = "DESCRIPTION", length = 240)
  private String description;

  @Column(name = "DATE_TIME", nullable = false)
  private LocalDateTime scheduledDate;

  @Column(name = "USER_ID", nullable = false)
  private Long userId;

  @Enumerated(EnumType.STRING)
  @Column(name = "PROCESSING_STATUS", nullable = false, length = 240)
  private ProcessingStatus processingStatus;

  private Task(TaskBuilder taskBuilder) {
    this.name = taskBuilder.name;
    this.description = taskBuilder.description;
    this.scheduledDate = taskBuilder.scheduledDate;
    this.userId = taskBuilder.userId;
    this.processingStatus = taskBuilder.processingStatus;
  }

  public boolean merge(Task otherTask) {
    this.name = StringUtils.defaultIfBlank(otherTask.getName(), this.name);
    this.description = StringUtils.defaultIfBlank(otherTask.description, this.description);
    this.scheduledDate = otherTask.scheduledDate;
    return true;
  }

  public void setTaskToDone() {
    this.processingStatus = ProcessingStatus.DONE;
  }

  public static class TaskBuilder {

    public Task build() {
      Validate.notBlank(this.name, "username must not be blank");
      Validate.notNull(this.scheduledDate, "Scheduled date must not be null");

      this.processingStatus = ProcessingStatus.PENDING;
      return new Task(this);
    }
  }

  @Override
  public String toString() {
    return "Task: " + "[" + this.id + " " + this.name + " " + this.scheduledDate + " " + this.userId + "]";
  }
}
