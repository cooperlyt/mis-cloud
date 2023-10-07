package io.github.cooperlyt.commons.cloud.elasticsearch;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ElasticsearchProperties {

  public String hosts;

  public String username;

  public String password;

  public String scheme = "http";

  public int connectionTimeout;

  public int socketTimeout;

  public int requestTimeout;
}
