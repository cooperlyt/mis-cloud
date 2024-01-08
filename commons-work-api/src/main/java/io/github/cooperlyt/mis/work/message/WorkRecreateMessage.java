package io.github.cooperlyt.mis.work.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkRecreateMessage {

  private long originalWorkId;

  private long targetWorkId;
}
