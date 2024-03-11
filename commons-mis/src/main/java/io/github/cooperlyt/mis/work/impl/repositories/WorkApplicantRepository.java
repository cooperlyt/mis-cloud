package io.github.cooperlyt.mis.work.impl.repositories;

import io.github.cooperlyt.mis.work.impl.model.WorkApplicantModel;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface WorkApplicantRepository extends ReactiveCrudRepository<WorkApplicantModel,Long> {

  @Modifying
  @Query("INSERT INTO work_applicant(id_type,id_number,name,tel,work_id) " +
      "SELECT id_type,id_number,name,tel, :workId " +
      "FROM work_applicant " +
      "WHERE work_id = :sourceWorkId")
  Mono<Void> cloneApplicant(long workId, long sourceWorkId);

}
