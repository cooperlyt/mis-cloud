package io.github.cooperlyt.commons.cloud.resteasy;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix = "resteasy.client")
public class ResteasyClientProperties {

  private Integer responseBufferSize;

  private ConnectionProperties connection;

  private Boolean disableTrustManager;


  private ResteasyClientBuilder.HostnameVerificationPolicy hostnameVerificationPolicy;

  @Getter
  @Setter
  static class ConnectionProperties {

    private DurationProperties ttl;

    private Integer maxPooledPerRoute;

    private DurationProperties checkoutTimeout;

    private Integer poolSize;

  }

  @Getter
  @Setter
  static class DurationProperties{

    private Long duration;

    private TimeUnit unit;
  }
}
