package io.github.cooperlyt.mis;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ConditionalOnWebApplication
public class LoadBalancedAutoConfigure {

  private final ObjectMapper objectMapper;

  public LoadBalancedAutoConfigure(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }


  @Bean
  @LoadBalanced
  public WebClient.Builder builder() {
    final ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
        .codecs(configurer -> configurer.defaultCodecs()
            .jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper)))
        .build();

    return WebClient.builder().exchangeStrategies(exchangeStrategies);
  }
}
