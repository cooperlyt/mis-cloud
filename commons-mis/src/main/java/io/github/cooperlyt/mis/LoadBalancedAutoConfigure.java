package io.github.cooperlyt.mis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ConditionalOnWebApplication
public class LoadBalancedAutoConfigure {

  @Bean
  @LoadBalanced
  public WebClient.Builder builder() {
    return WebClient.builder();
  }
}
