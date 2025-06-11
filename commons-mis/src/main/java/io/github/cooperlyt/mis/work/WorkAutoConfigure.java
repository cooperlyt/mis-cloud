package io.github.cooperlyt.mis.work;


import io.github.cooperlyt.mis.work.create.WorkAspect;
import io.github.cooperlyt.mis.work.impl.WorkRemoteServiceImpl;
import io.github.cooperlyt.mis.work.impl.WorkServiceImpl;
import io.github.cooperlyt.mis.work.impl.repositories.WorkApplicantRepository;
import io.github.cooperlyt.mis.work.impl.repositories.WorkOperatorRepository;
import io.github.cooperlyt.mis.work.impl.repositories.WorkRepository;
import io.github.cooperlyt.mis.work.impl.repositories.WorkTaskRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty("mis.internal.work.serverName")
public class WorkAutoConfigure {

  @Bean
  @Lazy
  @ConditionalOnMissingBean
  public WorkService workService(

      WorkOperatorRepository workOperatorRepository,
      WorkTaskRepository workTaskRepository,
      WorkApplicantRepository workApplicantRepository,
      WorkRepository workRepository,
      @Value("${mis.localization.organization:}") String organization) {

    return new WorkServiceImpl(workOperatorRepository,workTaskRepository,workApplicantRepository,workRepository, organization);
//    return new WorkServiceImpl(webClientBuilder.build(), serverName,
//        workOperatorRepository,workTaskRepository,workApplicantRepository,workRepository, organization);
  }

  @Bean
  @Lazy
  @ConditionalOnMissingBean
  public WorkRemoteService workRemoteService(
      WebClient.Builder webClientBuilder,
      @Value("${mis.internal.work.serverName}") String serverName
  ) {
    return new WorkRemoteServiceImpl(webClientBuilder.build(), serverName);
  }


  @Bean
  @Lazy
  @ConditionalOnMissingBean
  public WorkAspect workPostAspect(WorkService workService, WorkRemoteService workRemoteService) {
      return new WorkAspect(workService, workRemoteService);
  }

}
