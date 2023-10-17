package io.github.cooperlyt.mis.work.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cooperlyt.mis.work.data.WorkOperator;
import io.github.cooperlyt.mis.work.data.WorkOperatorSample;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@ReadingConverter
public class WorkOperatorHeaderConverter implements Converter<String, WorkOperator> {

  private final ObjectMapper objectMapper;

  public WorkOperatorHeaderConverter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public WorkOperator convert(@NotNull String source) {
    log.debug("WorkOperatorHeaderConverter: {}", source);

    String decodedString = URLDecoder.decode(source, StandardCharsets.UTF_8);

    try {
      return objectMapper.readValue(decodedString, WorkOperatorSample.class);
    } catch (JsonProcessingException e) {
      log.error("WorkOperatorHeaderConverter error: {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
