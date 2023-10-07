package io.github.cooperlyt.mis.dictionary;

import io.github.cooperlyt.mis.dictionary.DictionaryRemoteService;
import io.github.cooperlyt.mis.dictionary.fill.DictionaryFillAspect;
import io.github.cooperlyt.mis.dictionary.impl.DictionaryRemoteServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ConditionalOnWebApplication
public class DictionaryAutoConfigure {

  @Bean
  @Lazy
  @ConditionalOnMissingBean
  @ConditionalOnProperty("internal.dictionary.serverName")
  public DictionaryRemoteService dictionaryRemoteService(WebClient.Builder builder){
    return new DictionaryRemoteServiceImpl(builder.build());
  }
  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean(DictionaryRemoteService.class)
  public DictionaryFillAspect dictionaryFill(DictionaryRemoteService dictionaryRemoteService){
    return new DictionaryFillAspect(dictionaryRemoteService);
  }

}
