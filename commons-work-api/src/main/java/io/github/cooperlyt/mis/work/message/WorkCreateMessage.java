package io.github.cooperlyt.mis.work.message;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class WorkCreateMessage implements java.io.Serializable{

  @Serial
  private static final long serialVersionUID = 1L;

  private long workId;

  private Map<String,Object> data;
}
