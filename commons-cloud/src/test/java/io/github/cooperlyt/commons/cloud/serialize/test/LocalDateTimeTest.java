package io.github.cooperlyt.commons.cloud.serialize.test;


import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.cooperlyt.commons.cloud.serialize.ZonedLocalDateTimeDeserializer;
import io.github.cooperlyt.commons.cloud.serialize.ZonedLocalDateTimeSerializer;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import io.github.cooperlyt.commons.cloud.serialize.TestApplication;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
public class LocalDateTimeTest {


  static class TestDateTime{



    @Getter
    @Setter
    private ZonedDateTime zonedDateTime = ZonedDateTime.now();

    @Getter
    @Setter
    private LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();


  }

  static class TestDateTimeResult{



    @Getter
    @Setter
    private ZonedDateTime zonedDateTime;

    @Getter
    @Setter
    private ZonedDateTime localDateTime ;


  }



  @Test
  public void testDateTime(){

    TestDateTime testDateTime = new TestDateTime();

    System.out.println(testDateTime.getZonedDateTime());
    System.out.println(testDateTime.getLocalDateTime());

    try {


      //DateTimeFormatter dateTimeFormatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
      ZoneId zoneId = ZoneId.of("Asia/Shanghai");

      JavaTimeModule javaTimeModule = new JavaTimeModule();


      javaTimeModule.addSerializer(LocalDateTime.class, ZonedLocalDateTimeSerializer.INSTANCE);


      javaTimeModule.addDeserializer(LocalDateTime.class, new ZonedLocalDateTimeDeserializer(zoneId));

      //builder.modules(javaTimeModule);



      ObjectMapper objectMapper = new ObjectMapper();
      //objectMapper.setDateFormat(new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
      objectMapper.setTimeZone(TimeZone.getTimeZone("UTC"));

      objectMapper.registerModule(javaTimeModule);
      String json = objectMapper.writeValueAsString(testDateTime);

      System.out.println("serializer test---------------------------------------------------");

      System.out.println(json);

      TestDateTimeResult testDateTime11 = objectMapper.readValue(json,TestDateTimeResult.class);

      System.out.println(testDateTime11.getZonedDateTime());
      System.out.println(testDateTime11.getLocalDateTime());

      assert testDateTime11.getZonedDateTime().equals(testDateTime11.getLocalDateTime());
      System.out.println("deserializer test-------------------------------------------------");



      String testStr = "{\"zonedDateTime\":\"2023-08-02T16:00:00.000Z\"," +
          "\"localDateTime\":\"2023-08-02T16:00:00.000Z\"}";


      System.out.println(testStr);

      TestDateTime testDateTime1 = objectMapper.readValue(testStr,TestDateTime.class);

      System.out.println(testDateTime1.getZonedDateTime().withZoneSameInstant(ZoneId.of("Asia/Shanghai")).toLocalDateTime());
      System.out.println(testDateTime1.getLocalDateTime());

      assert testDateTime1.getZonedDateTime().withZoneSameInstant(ZoneId.of("Asia/Shanghai")).toLocalDateTime().equals(testDateTime1.getLocalDateTime());


      System.out.println("deserializer UTC test-------------------------------------------------");

      String testUTCStr =       "{\"zonedDateTime\":\"2023-08-02T16:00:00.000+08:00\"," +
          "\"localDateTime\":\"2023-08-02T16:00:00.000+08:00\"}";
      System.out.println(testUTCStr);

      TestDateTime testUTCDateTime1 = objectMapper.readValue(testUTCStr,TestDateTime.class);

      System.out.println(testUTCDateTime1.getZonedDateTime().withZoneSameInstant(ZoneId.of("Asia/Shanghai")).toLocalDateTime());
      System.out.println(testUTCDateTime1.getLocalDateTime());

      assert testUTCDateTime1.getZonedDateTime().withZoneSameInstant(ZoneId.of("Asia/Shanghai")).toLocalDateTime().equals(testUTCDateTime1.getLocalDateTime());


      System.out.println("deserializer UTC test-------------------------------------------------");

      String testTimestamp =       "{\"zonedDateTime\":\"1696954607.968220000\"," +
          "\"localDateTime\":\"1696954607.968220000\"}";
      System.out.println(testTimestamp);

      TestDateTime testTimestampTime1 = objectMapper.readValue(testTimestamp,TestDateTime.class);

      System.out.println(testTimestampTime1.getZonedDateTime().withZoneSameInstant(ZoneId.of("Asia/Shanghai")).toLocalDateTime());
      System.out.println(testTimestampTime1.getLocalDateTime());

      assert testTimestampTime1.getZonedDateTime().withZoneSameInstant(ZoneId.of("Asia/Shanghai")).toLocalDateTime().equals(testTimestampTime1.getLocalDateTime());


    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    //objectMapper.readValue("{\"date\":\"2020-01-01T00:00:00.000Z\",\"localDateTime\":\"2020-01-01T00:00:00.000\",\"localDate\":\"2020-01-01\",\"localTime\":\"00:00:00.000\"}",TestDateTime.class);
  }



}
