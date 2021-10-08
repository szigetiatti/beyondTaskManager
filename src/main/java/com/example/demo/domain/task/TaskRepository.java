package com.example.demo.domain.task;

import com.example.demo.domain.shared.ProcessingStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Task entity.
 *
 * @author szigeti.attila96@gmail.com
 */
@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {

  List<Task> findAllByUserId(Long userId);

  List<Task> findAllByProcessingStatus(ProcessingStatus processingStatus);
}
