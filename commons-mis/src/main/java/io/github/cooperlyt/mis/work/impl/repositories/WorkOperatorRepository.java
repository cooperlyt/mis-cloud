package io.github.cooperlyt.mis.work.impl.repositories;


import io.github.cooperlyt.mis.work.data.WorkAction;
import io.github.cooperlyt.mis.work.impl.model.WorkOperatorModel;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;


@Repository
public interface WorkOperatorRepository extends ReactiveCrudRepository<WorkOperatorModel,String> {

  @Query("SELECT o.*,t.message,t.task_name,t.pass FROM work_operator o " +
      "left join work_task t on t.task_id = o.task_id WHERE o.work_id = :workId")
  Flux<WorkAction> workActions(long workId);
}
