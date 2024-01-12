package io.github.cooperlyt.mis.work.impl.repositories;


import io.github.cooperlyt.mis.work.data.WorkAction;
import io.github.cooperlyt.mis.work.data.WorkTask;
import io.github.cooperlyt.mis.work.impl.model.WorkActionModel;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;


@Repository
public interface WorkOperatorRepository extends ReactiveCrudRepository<WorkActionModel,String> {

  @Query("SELECT o.*,t.message,t.task_name,t.pass FROM work_operator o " +
      "left join work_task t on t.task_id = o.task_id WHERE o.work_id = :workId")
  Flux<WorkTask> workActions(long workId);


  @Query("SELECT o.*,t.message,t.task_name,t.pass FROM work_task t " +
      "left join work_operator o on t.task_id = o.task_id WHERE o.work_id = :workId AND t.pass = false")
  Flux<WorkTask> workRejectActions(long workId);


  Mono<WorkActionModel> findFirstByWorkIdAndTypeIn(long workId, Collection<WorkAction.ActionType> types);
}
