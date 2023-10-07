package io.github.cooperlyt.mis.work.impl.repositories;


import io.github.cooperlyt.mis.work.impl.model.WorkTaskModel;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkTaskRepository extends ReactiveCrudRepository<WorkTaskModel,String> {
}
