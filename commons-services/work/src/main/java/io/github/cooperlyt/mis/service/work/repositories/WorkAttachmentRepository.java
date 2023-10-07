package io.github.cooperlyt.mis.service.work.repositories;

import io.github.cooperlyt.mis.service.work.model.WorkAttachmentModel;
import io.github.cooperlyt.mis.work.data.WorkAttachment;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface WorkAttachmentRepository extends ReactiveCrudRepository<WorkAttachmentModel,Long> {

  Flux<WorkAttachmentModel> findAllByWorkId(long workId);

  @Query("SELECT a.* , (SELECT count(*) from work_file f where f.attach_id = a.id) as file_count  FROM attachment a WHERE work_id = :workId")
  Flux<WorkAttachment> findWorkAttachment(long workId);
}
