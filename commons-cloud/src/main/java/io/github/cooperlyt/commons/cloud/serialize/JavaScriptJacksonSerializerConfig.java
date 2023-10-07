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


  @Value("${spring.jackson.date-format:yyyy-MM-dd'T'HH:mm:ss.SSS'Z'}")
  private String pattern;

  @Bean
  @ConditionalOnProperty(prefix = "spring.jackson", name = "date-format" )
  public Jackson2ObjectMapperBuilderCustomizer jackson2OLocalDateTimeMapperBuilder() {

    return builder -> {

      // formatter
      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'00:00:00:000'Z'");
      DateTimeFormatter dateTimeFormatter =  DateTimeFormatter.ofPattern(pattern);

      // deserializers
      builder.deserializers(new LocalDateDeserializer(dateFormatter));
      builder.deserializers(new LocalDateTimeDeserializer(dateTimeFormatter));

      // serializers
      builder.serializers(new LocalDateSerializer(dateFormatter));
      builder.serializers(new LocalDateTimeSerializer(dateTimeFormatter));
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
