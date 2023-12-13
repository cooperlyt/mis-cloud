package io.github.cooperlyt.mis.work.data;


import io.github.cooperlyt.mis.work.message.WorkStatus;

import java.time.LocalDateTime;

public interface WorkInfo extends java.io.Serializable {

  public final static String SOURCE_FROM_SYSTEM = "SYS";

  public final static String SOURCE_FROM_PATCH = "PATCH";

  long getWorkId();

  String getDataSource();

  LocalDateTime getCreatedAt();

  LocalDateTime getUpdatedAt();

  LocalDateTime getCompletedAt();

  LocalDateTime getValidateAt();

  String getWorkName();

  WorkStatus getStatus();

  String getType();

  boolean isProcess();

  String getDefineId();


}
