package io.github.cooperlyt.mis.service.work.repositories;

import io.github.cooperlyt.mis.service.work.model.WorkAttachmentModel;
import io.github.cooperlyt.mis.service.work.model.WorkFileModel;
import io.github.cooperlyt.mis.work.data.WorkAttachment;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface WorkAttachmentRepository extends ReactiveCrudRepository<WorkAttachmentModel,Long> {

  Flux<WorkAttachmentModel> findAllByWorkId(long workId);

  @Query("SELECT a.* , (SELECT count(*) from attachment_file f where f.id = a.id) as file_count  FROM attachment a WHERE work_id = :workId")
  Flux<WorkAttachment> findWorkAttachment(long workId);


  @Modifying
  @Query("INSERT INTO attachment_file(id,fid) VALUES (:id,:fid)")
  Mono<Void> addAttachmentFile(long id, String fid);

  @Modifying
  @Query("DELETE FROM attachment_file WHERE id = :id AND fid = :fid")
  Mono<Void> removeAttachmentFile(long id, String fid);

  @Query("SELECT f.* FROM attachment_file a left join work_file f on f.fid = a.fid WHERE a.id = :attachId")
  Flux<WorkFileModel> listAttachmentFile(long attachId);

  @Query("SELECT fid FROM attachment_file WHERE id = :attachId")
  Flux<String> listAttachmentFileId(long attachId);

  @Modifying
  @Query("INSERT INTO attachment_file(id,fid) SELECT :newAttachId,fid FROM attachment_file WHERE id = :attachId")
  Mono<Void> cloneAttachmentFile(@Param("attachId") long attachId,@Param("newAttachId") long newAttachId);

  @Modifying
  @Query("DELETE FROM attachment_file WHERE id = :attachId")
  Mono<Void> clearAttachmentFile(long attachId);
}
