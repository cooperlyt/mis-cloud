package io.github.cooperlyt.utils;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@Slf4j
@ReadingConverter
public class IntegerToBooleanConverter implements Converter<Integer,Boolean> {
  @Override
  public Boolean convert(@NotNull Integer source) {
    log.debug("IntegerToBooleanConverter: {}",source);
    return source != 0;
  }
}
