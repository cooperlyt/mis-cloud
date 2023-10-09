package io.github.cooperlyt.commons.cloud.serialize;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;
import java.time.format.DateTimeFormatter;

@Configuration
public class JavaScriptJacksonSerializerConfig {

  /**
   * yyyy-MM-dd'T'HH:mm:ss.SSS'Z'  Z means UTC
   *
   * yyyy-MM-dd'T'HH:mm:ss.SSSXXX XXX means UTC+8 for china
   *
   */

  @Value("${spring.jackson.date-time-format:yyyy-MM-dd'T'HH:mm:ss.SSSXXX}")
  private String dateTimePattern;

  @Value("${spring.jackson.date-format:yyyy-MM-dd'T'00:00:00:000XXX}")
  private String datePattern;

  @Bean
  @ConditionalOnProperty(prefix = "spring.jackson", name = "date-time-format" )
  public Jackson2ObjectMapperBuilderCustomizer jackson2OLocalDateTimeMapperBuilder() {

    return builder -> {

      // formatter
      DateTimeFormatter dateTimeFormatter =  DateTimeFormatter.ofPattern(dateTimePattern);

      // deserializers
      builder.deserializers(new LocalDateTimeDeserializer(dateTimeFormatter));

      // serializers
      builder.serializers(new LocalDateTimeSerializer(dateTimeFormatter));
    };
  }

  @Bean
  @ConditionalOnProperty(prefix = "spring.jackson", name = "date-format" )
  public Jackson2ObjectMapperBuilderCustomizer jackson2OLocalDateMapperBuilder(){
    return builder -> {

      // formatter
      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);

      // deserializers
      builder.deserializers(new LocalDateDeserializer(dateFormatter));

      // serializers
      builder.serializers(new LocalDateSerializer(dateFormatter));
    };
  }


  @Bean
  @ConditionalOnProperty(prefix = "spring.jackson", name = "long-to-string")
  public Jackson2ObjectMapperBuilderCustomizer jackson2OLongMapperBuilder() {

    return builder -> {

      //TODO deserializers

      // serializers
      builder.serializerByType(BigInteger.class, ToStringSerializer.instance);
      builder.serializerByType(Long.class, ToStringSerializer.instance);
      builder.serializerByType(Long.TYPE, ToStringSerializer.instance);
    };
  }

}
