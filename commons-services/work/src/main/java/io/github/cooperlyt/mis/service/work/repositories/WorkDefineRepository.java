package io.github.cooperlyt.mis.service.work.repositories;

import io.github.cooperlyt.mis.service.work.model.WorkDefineModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface WorkDefineRepository extends ReactiveCrudRepository<WorkDefineModel,String> {


  @NotNull
  @Cacheable("define")
  @Override
  Mono<WorkDefineModel> findById(@NotNull String id);

  Flux<WorkDefineModel> findAllByType(String type);
}
