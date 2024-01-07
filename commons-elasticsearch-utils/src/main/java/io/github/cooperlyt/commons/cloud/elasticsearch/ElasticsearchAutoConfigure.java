package io.github.cooperlyt.commons.cloud.elasticsearch;

import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientOptions;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@ConditionalOnClass(ElasticsearchAsyncClient.class)
public class ElasticsearchAutoConfigure {

  @Bean
  @ConditionalOnMissingBean
  @ConfigurationProperties(prefix = "elasticsearch")
  ElasticsearchProperties elasticsearchProperties() {
    return new ElasticsearchProperties();
  }

  @Bean
  @ConditionalOnMissingBean
  RestClientTransport restClientTransport(RestClient restClient, ObjectProvider<RestClientOptions> restClientOptions) {
    return new RestClientTransport(restClient, new JacksonJsonpMapper(), restClientOptions.getIfAvailable());
  }

  @Bean
  @ConditionalOnMissingBean
  public ElasticsearchAsyncClient elasticsearchAsyncClient(ElasticsearchProperties properties) {
    RestClient restClient = RestClient.builder(getESHttpHosts(properties.hosts, properties.scheme)).setRequestConfigCallback(requestConfigBuilder -> {
      //设置连接超时时间
      requestConfigBuilder.setConnectTimeout(properties.connectionTimeout);
      requestConfigBuilder.setSocketTimeout(properties.socketTimeout);
      requestConfigBuilder.setConnectionRequestTimeout(properties.requestTimeout);
      return requestConfigBuilder;
    }).setFailureListener(new RestClient.FailureListener() {
      //某节点失败,这里可以做一些告警
      @Override
      public void onFailure(Node node) {
        log.error(node.toString());
      }
    }).setHttpClientConfigCallback(httpClientBuilder -> {
      httpClientBuilder.disableAuthCaching();
      //设置账密
      // tcp 连接超时 java.net.SocketException: Connection reset
      // 疑似 为 docker overlay TCP 15分钟断开， 而TCP连接池中的连接还在，导致连接失效  https://stackoverflow.com/questions/62148388/java-elasticsearch-highlevelrestclient-throw-java-net-socketexception-connecti
      httpClientBuilder.setConnectionTimeToLive(5, TimeUnit.MINUTES);
      httpClientBuilder.setKeepAliveStrategy((response, context) -> Duration.ofMinutes(5).toMillis());
      return getHttpAsyncClientBuilder(httpClientBuilder, properties.username, properties.password);
    }).build();
    ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
    return new ElasticsearchAsyncClient(transport);
  }


  /**
   * ElasticSearch 连接地址
   * 多个逗号分隔
   * 示例：127.0.0.1:9201,127.0.0.1:9202,127.0.0.1:9203
   */
  private HttpHost[] getESHttpHosts(String elasticsearchHosts, String scheme) {
    String[] hosts = elasticsearchHosts.split(",");
    HttpHost[] httpHosts = new HttpHost[hosts.length];
    for (int i = 0; i < httpHosts.length; i++) {
      String host = hosts[i];
      host = host.replaceAll("http://", "").replaceAll("https://", "");
      Assert.isTrue(host.contains(":"), String.format("your host %s format error , Please refer to [ 127.0.0.1:9200 ] ", host));
      httpHosts[i] = new HttpHost(host.split(":")[0], Integer.parseInt(host.split(":")[1]), StringUtils.hasText(scheme) ? scheme : "http");
    }
    return httpHosts;
  }

  private HttpAsyncClientBuilder getHttpAsyncClientBuilder(HttpAsyncClientBuilder httpClientBuilder,String username, String password) {
    if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
      return httpClientBuilder;
    }
    //账密设置
    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    //es账号密码（一般使用,用户elastic）
    credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
    return httpClientBuilder;
  }

}
