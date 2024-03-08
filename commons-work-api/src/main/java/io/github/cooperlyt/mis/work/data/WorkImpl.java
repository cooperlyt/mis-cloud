package io.github.cooperlyt.mis.work.data;


import io.github.cooperlyt.mis.work.message.WorkStatus;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class WorkImpl implements WorkInfo {


  protected String defineId;
  protected String type;
  protected boolean process;

  protected long workId;
  protected String workName;
  protected WorkStatus status;
  protected String dataSource;

  @Timestamp
  private LocalDateTime createdAt;

  @Timestamp
  private LocalDateTime updatedAt;
  protected LocalDateTime completedAt;
  protected LocalDateTime validateAt;
}
