package com.example.demo.application.task;

import com.example.demo.domain.shared.ProcessingStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Task entity.
 *
 * @author szigeti.attila96@gmail.com
 */
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class TaskDto {

  @JsonProperty("id")
  private Long id;

  @NotEmpty(message = "name cannot be empty")
  @JsonProperty("name")
  private String taskName;

  @JsonProperty("description")
  private String description;

  @NotNull(message = "date_time cannot be empty")
  @JsonProperty("date_time")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime dateTime;

  @JsonProperty("status")
  private ProcessingStatus taskStatus;
}