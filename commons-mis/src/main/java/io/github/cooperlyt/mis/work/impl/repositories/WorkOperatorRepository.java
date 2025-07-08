package io.github.cooperlyt.mis.work.impl.repositories;


import io.github.cooperlyt.mis.work.data.WorkAction;
import io.github.cooperlyt.mis.work.data.WorkActionBasic;
import io.github.cooperlyt.mis.work.data.WorkActionType;
import io.github.cooperlyt.mis.work.data.WorkOperatorBasic;
import io.github.cooperlyt.mis.work.impl.model.WorkOperatorModel;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;


@Repository
public interface WorkOperatorRepository extends ReactiveCrudRepository<WorkOperatorModel,String> {

  @Query("SELECT o.*,t.message,t.task_name,t.pass FROM work_operator o " +
      "left join work_task t on t.task_id = o.task_id WHERE o.work_id = :workId")
  Flux<WorkAction.Sample> workActions(long workId);


  @Query("SELECT o.*,t.message,t.task_name,t.pass FROM work_task t " +
      "left join work_operator o on t.task_id = o.task_id WHERE o.work_id = :workId AND t.pass = false")
  Flux<WorkAction.Sample> workRejectActions(long workId);


  @Query("SELECT wo.type, wo.work_time, wo.user_id, wo.user_name, wo.org_name, wo.task_id, " +
      "wt.message, wt.pass, wt.task_name " +
      "FROM work_operator wo " +
      "LEFT JOIN work_task wt on wt.task_id = wo.task_id")
  Flux<WorkActionBasic.Sample> findWorkOperatorBasics(long workId);

  Mono<WorkOperatorModel> findFirstByWorkIdAndTypeIn(long workId, Collection<WorkActionType> types);
}
