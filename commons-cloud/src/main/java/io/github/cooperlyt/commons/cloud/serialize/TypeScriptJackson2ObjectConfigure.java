package io.github.cooperlyt.commons.cloud.serialize;


import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 *
 * LocalDateTime 类型很坑， 没有时区信息，不能转成 yyyy-MM-dd'T'HH:mm:ss.SSSXXX  在使用时需要根据时区转换

 *  LocalDate 更坑，因为没有时间， 连转换都不可能。时区信息直接丢失

 * yyyy-MM-dd'T'HH:mm:ss.SSS'Z'  Z means UTC

 * yyyy-MM-dd'T'HH:mm:ss.SSSXXX XXX means UTC+8 for china
 *
 */
@Configuration
public class TypeScriptJackson2ObjectConfigure {


  @Value("${mis.jackson.date.local-time-zone}")
  private String timeZone;

  @Bean
  @ConditionalOnProperty(prefix = "mis.jackson.date", name = "enable")
  public Jackson2ObjectMapperBuilderCustomizer jackson2LocalDateTimeMapperBuilder() {

    return builder -> {


      ZoneId zoneId = StringUtils.hasText(timeZone) ? ZoneId.of(timeZone) : ZoneId.systemDefault();


      JavaTimeModule javaTimeModule = new JavaTimeModule();


      javaTimeModule.addSerializer(LocalDateTime.class, new ZonedLocalDateTimeSerializer(zoneId));


      javaTimeModule.addDeserializer(LocalDateTime.class, new ZonedLocalDateTimeDeserializer(zoneId));

      //builder.modules(javaTimeModule);

      builder.modulesToInstall(javaTimeModule);

      //builder.serializerByType(PageImpl.class,new JsonPageSerializer());

      // serializers
      builder.serializerByType(BigInteger.class, ToStringSerializer.instance);
      builder.serializerByType(Long.class, ToStringSerializer.instance);
      builder.serializerByType(Long.TYPE, ToStringSerializer.instance);
    };
  }

  @Bean
  @ConditionalOnProperty(prefix = "mis.jackson.long", name = "to-string")
  public Jackson2ObjectMapperBuilderCustomizer jackson2LongMapperBuilder() {

    return builder -> {
      builder.serializerByType(BigInteger.class, ToStringSerializer.instance);
      builder.serializerByType(Long.class, ToStringSerializer.instance);
      builder.serializerByType(Long.TYPE, ToStringSerializer.instance);
    };
  }

}
