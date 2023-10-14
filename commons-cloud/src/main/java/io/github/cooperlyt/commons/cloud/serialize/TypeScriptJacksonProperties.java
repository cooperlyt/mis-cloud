package io.github.cooperlyt.commons.cloud.serialize;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix = "mis.jackson")
public class TypeScriptJacksonProperties {

  private ZonedTimeProperties zonedDate;


  private LongProperties longType;

  @Getter
  @Setter
  static class ZonedTimeProperties {

    private Boolean enable;

    private String localTimeZone;

  }

  @Getter
  @Setter
  static class LongProperties {
    private Boolean toString;
  }
}
