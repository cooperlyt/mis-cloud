package io.github.cooperlyt.mis.work.impl.repositories;

import io.github.cooperlyt.mis.work.impl.model.WorkApplicantModel;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkApplicantRepository extends ReactiveCrudRepository<WorkApplicantModel,Long> {
}
