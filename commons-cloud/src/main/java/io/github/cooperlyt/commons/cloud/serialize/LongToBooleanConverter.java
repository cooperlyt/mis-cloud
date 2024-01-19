package io.github.cooperlyt.commons.cloud.serialize;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@Slf4j
@ReadingConverter
public class LongToBooleanConverter implements Converter<Long,Boolean> {
  @Override
  public Boolean convert(@NotNull Long source) {
    return source != 0;
  }
}
