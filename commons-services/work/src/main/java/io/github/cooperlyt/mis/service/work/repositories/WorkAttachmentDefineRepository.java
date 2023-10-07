package io.github.cooperlyt.mis.service.work.repositories;

import io.github.cooperlyt.mis.service.work.model.WorkAttachmentDefineModel;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface WorkAttachmentDefineRepository extends ReactiveCrudRepository<WorkAttachmentDefineModel,Long> {

  Flux<WorkAttachmentDefineModel> findAllByDefineId(String defineId);

}
