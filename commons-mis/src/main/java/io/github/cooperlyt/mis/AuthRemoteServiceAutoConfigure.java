package io.github.cooperlyt.mis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ClientCredentialsReactiveOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(ReactiveOAuth2AuthorizedClientService.class)
public class AuthRemoteServiceAutoConfigure {

  @Bean
  @Lazy
  @ConditionalOnMissingBean
  @ConditionalOnClass(ReactiveOAuth2AuthorizedClientService.class)
  public AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager reactiveClientManager(
      ReactiveClientRegistrationRepository clientRegistrationRepository,
      ReactiveOAuth2AuthorizedClientService authorizedClientService) {
    AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager clientManager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
        clientRegistrationRepository, authorizedClientService);
    clientManager.setAuthorizedClientProvider(new ClientCredentialsReactiveOAuth2AuthorizedClientProvider());
    return clientManager;
  }

  @Bean
  @Lazy
  @ConditionalOnMissingBean
  @ConditionalOnClass(ReactiveOAuth2AuthorizedClientService.class)
  public WebClient webClient(WebClient.Builder builder, ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
    ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2Client =
        new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
    return builder.filter(oauth2Client).build();
  }
}
