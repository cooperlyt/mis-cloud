package io.github.cooperlyt.mis.work.message;

public enum WorkStatus {

  PREPARE(false), //准备中
//  PRIVATE(false), //私有的，只有本人或本组可以看到
//
  RUNNING(false), //运行中

  COMPLETED(true), //已完成
  ACCEPTED(false), //已接受

  VALID(true), //已生效，但业务还未完成
  REJECT(false), //业务被驳加

  RESTART(false), //业务被驳回，需要重新开始

  IMMINENT(false), //业务即将完成， 已生成部分数据， 目录用于打证

  ABORT(false), //业务未完成，被中止
  DELETED(false); //业务已完成，后被删除

//  static final EnumSet<WorkStatus> create = EnumSet.of(PREPARE,PRIVATE,RUNNING,COMPLETED);
//
//  static final EnumSet<WorkStatus> change = EnumSet.of(VALID,COMPLETED,REJECT,ABORT,DELETED);

  public final boolean valid;

  public WorkStatus getDisplayWorkStatus() {
    return switch (this) {
      case RESTART -> REJECT;
      case IMMINENT -> RUNNING;
      default -> this;
    };
  }
  WorkStatus(boolean valid) {
    this.valid = valid;
  }
}
