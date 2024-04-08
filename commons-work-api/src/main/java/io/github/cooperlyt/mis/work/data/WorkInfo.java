package io.github.cooperlyt.mis.work.data;


import io.github.cooperlyt.mis.work.message.WorkStatus;

import java.time.LocalDateTime;

public interface WorkInfo extends java.io.Serializable {

  /**
   * @deprecated use {@link #SOURCE_FROM_PATCH} instead
   */
  @Deprecated
  String SOURCE_FROM_SYSTEM = "SYS";

  String SOURCE_FROM_JOINT = "JOINT";

  String SOURCE_FROM_OFFICE = "OFFICE";

  String SOURCE_FROM_PATCH = "PATCH";

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
