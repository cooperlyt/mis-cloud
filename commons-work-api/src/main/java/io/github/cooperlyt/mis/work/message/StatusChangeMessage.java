package io.github.cooperlyt.mis.work.message;

import lombok.Builder;
import lombok.Data;

/**
 * 工作状态变更消息，用于通知工作状态变更，变更可能没有操作人
 */
@Data
@Builder
public class StatusChangeMessage {

  private WorkStatus status;

  private long workId;

}
