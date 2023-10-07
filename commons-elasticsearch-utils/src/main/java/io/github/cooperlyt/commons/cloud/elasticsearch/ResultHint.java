package io.github.cooperlyt.commons.cloud.elasticsearch;

import com.fasterxml.jackson.annotation.JsonRawValue;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.cooperlyt.commons.cloud.serialize.JsonRawDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultHint {

  private Map<String, List<String>> highlight;

  @JsonDeserialize(using = JsonRawDeserializer.class)
  @JsonRawValue
  private String source;
}
