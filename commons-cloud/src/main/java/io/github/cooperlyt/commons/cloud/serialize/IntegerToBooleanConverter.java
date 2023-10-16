package io.github.cooperlyt.commons.cloud.serialize;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@Slf4j
@ReadingConverter
public class IntegerToBooleanConverter implements Converter<Integer,Boolean> {
  @Override
  public Boolean convert(Integer source) {
    log.debug("IntegerToBooleanConverter: {}",source);
    return source != 0;
  }
}
