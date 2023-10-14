package io.github.cooperlyt.mis.work;

import io.github.cooperlyt.mis.work.create.WorkCreateAspect;
import io.github.cooperlyt.mis.work.create.WorkOperatorPersistableHandler;
import io.github.cooperlyt.mis.work.impl.WorkRemoteServiceImpl;
import io.github.cooperlyt.mis.work.impl.repositories.WorkOperatorRepository;
import io.github.cooperlyt.mis.work.impl.repositories.WorkRepository;
import io.github.cooperlyt.mis.work.impl.repositories.WorkTaskRepository;
import io.github.cooperlyt.mis.work.impl.WorkPersistableService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ConditionalOnWebApplication
public class WorkCreateAutoConfigure {

  @Bean
  @Lazy
  @ConditionalOnMissingBean
  @ConditionalOnProperty("mis.internal.work.serverName")
  public WorkRemoteService workRemoteService(WebClient.Builder builder, StreamBridge streamBridge){
    return new WorkRemoteServiceImpl(builder.build(),streamBridge);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean(WorkRemoteService.class)
  @ConditionalOnProperty(prefix = "mis.internal.work.operator",name = "create")
  public WorkCreateAspect workCreateAspect(WorkRemoteService workRemoteService,
                                           WorkOperatorPersistableHandler workPrepareCreateHandler){
    return new WorkCreateAspect(workRemoteService,workPrepareCreateHandler);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnProperty(prefix = "mis.internal.work.operator",name = "persistable")
  public WorkPersistableService workOperatorService(WorkOperatorRepository workOperatorRepository,
                                                    WorkTaskRepository workTaskRepository,
                                                    WorkRepository workRepository){
    return new WorkPersistableService(workOperatorRepository,workTaskRepository,workRepository);
  }

}
