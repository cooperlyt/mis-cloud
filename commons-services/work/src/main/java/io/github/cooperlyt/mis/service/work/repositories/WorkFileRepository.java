package io.github.cooperlyt.mis.service.work.repositories;

import io.github.cooperlyt.mis.service.work.model.WorkFileModel;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface WorkFileRepository extends ReactiveCrudRepository<WorkFileModel,String> {

//  @Modifying
//  @Query("update work_file LEFT JOIN attachment on attachment.id = work_file.attach_id set task_id = :taskId where task_id is null and attachment.work_id = :workId")
//  Mono<Void> updateFileWorkItem(String taskId,long workId);
//
//  Flux<WorkFileModel> findAllByAttachId(long attachId);
//
//  Mono<Void> deleteByFid(String fid);
//
//  Mono<Void> deleteAllByAttachId(long attachId);
}
