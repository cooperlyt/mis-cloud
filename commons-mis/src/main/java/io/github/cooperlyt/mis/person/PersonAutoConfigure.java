package io.github.cooperlyt.mis.person;

import io.github.cooperlyt.mis.person.impl.PersonRemoteServiceImpl;
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
public class PersonAutoConfigure {

  @Bean
  @Lazy
  @ConditionalOnMissingBean
  @ConditionalOnProperty("mis.internal.person.server")
  public PersonRemoteService peopleRemoteService(ExchangeStrategies exchangeStrategies) {
    return new PersonRemoteServiceImpl(WebClient.builder().exchangeStrategies(exchangeStrategies).build());
  }
}
