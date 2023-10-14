package io.github.cooperlyt.commons.cloud.serialize;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


@AutoConfiguration
@ConditionalOnClass(ObjectMapper.class)
@EnableConfigurationProperties(TypeScriptJacksonProperties.class)
public class TypeScriptJackson2ObjectConfigure {


  /**
   *
   * LocalDateTime 类型很坑， 没有时区信息，不能转成 yyyy-MM-dd'T'HH:mm:ss.SSSXXX  在使用时需要根据时区转换

   *  LocalDate 更坑，因为没有时间， 连转换都不可能。时区信息直接丢失

   * yyyy-MM-dd'T'HH:mm:ss.SSS'Z'  Z means UTC

   * yyyy-MM-dd'T'HH:mm:ss.SSSXXX XXX means UTC+8 for china
   *
   */
  @Bean
  @ConditionalOnClass(JavaTimeModule.class)
  @ConditionalOnProperty(prefix = "mis.jackson.zoned-date", name = "enable")
  public Jackson2ObjectMapperBuilderCustomizer jackson2LocalDateTimeMapperBuilder(TypeScriptJacksonProperties properties) {

    return builder -> {

      String timeZone = properties.getZonedDate().getLocalTimeZone();

      ZoneId zoneId = StringUtils.hasText(timeZone) ? ZoneId.of(timeZone) : ZoneId.systemDefault();


      JavaTimeModule javaTimeModule = new JavaTimeModule();


      javaTimeModule.addSerializer(LocalDateTime.class, new ZonedLocalDateTimeSerializer(zoneId));


      javaTimeModule.addDeserializer(LocalDateTime.class, new ZonedLocalDateTimeDeserializer(zoneId));

      //builder.modules(javaTimeModule);

      builder.modulesToInstall(javaTimeModule);


    };
  }

  /**
   * 使用此配置后，spring 返回一个Long 类型时 会带引号， 导致前端js无法解析成 number , 字符串也会被解析成 %20XXX%20

   * 为了解决前端js对于long类型的精度丢失问题
   * @return register
   */
  @Bean
  @ConditionalOnProperty(prefix = "mis.jackson.long-type", name = "to-string")
  public Jackson2ObjectMapperBuilderCustomizer jackson2LongMapperBuilder() {

    return builder -> {
      builder.serializerByType(BigInteger.class, ToStringSerializer.instance);
      builder.serializerByType(Long.class, ToStringSerializer.instance);
      builder.serializerByType(Long.TYPE, ToStringSerializer.instance);
    };
  }

}
