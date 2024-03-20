package io.github.cooperlyt.mis.person;

import io.github.cooperlyt.mis.dictionary.DictionaryRemoteService;
import io.github.cooperlyt.mis.dictionary.impl.DictionaryRemoteServiceImpl;
import io.github.cooperlyt.mis.person.impl.PeopleRemoteServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty("mis.internal.person.serverName")
public class PersonAutoConfigure {

  @Bean
  @Lazy
  @ConditionalOnMissingBean
  @ConditionalOnProperty("mis.internal.person.serverName")
  public PeopleRemoteService dictionaryRemoteService(ExchangeStrategies exchangeStrategies) {
    return new PeopleRemoteServiceImpl(WebClient.builder().exchangeStrategies(exchangeStrategies).build());
  }
}
