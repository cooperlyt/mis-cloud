package io.github.cooperlyt.mis.work.impl.repositories;


import io.github.cooperlyt.mis.work.impl.model.WorkModel;
import io.github.cooperlyt.mis.work.message.WorkStatus;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface WorkRepository extends ReactiveCrudRepository<WorkModel,Long> {

  @Modifying
  @Query("UPDATE work SET status = :status WHERE work_id = :workId")
  Mono<Void> updateWorkStatus(@Param("workId") long workId, @Param("status") WorkStatus status);

}
