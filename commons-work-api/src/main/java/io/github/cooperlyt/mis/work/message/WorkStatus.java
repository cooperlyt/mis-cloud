package io.github.cooperlyt.mis.work.message;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

public enum WorkStatus {

  PREPARE(false, false, false, false), //准备中
//  PRIVATE(false), //私有的，只有本人或本组可以看到
//
  RUNNING(false, true, false, true), //运行中

  COMPLETED(true, false, true, false), //已完成

  UNACCEPTED(false, true, false, true), //未接受
  ACCEPTED(false, true, false, true), //已接受

  VALID(true,true, true, false), //已生效，但业务还未完成
  REJECT(false, false, true, false), //业务被驳加

  RESTART(false, false, true, false), //业务被驳回，需要重新开始

  IMMINENT(false, true, true, false), //业务即将完成， 已生成部分数据， 目录用于打证

  ABORT(false, false, true, false); //业务未完成，被中止
  //DELETED(false); //业务已完成，后被删除 , 无此状态，完成后不可删除，走撤消流程
  //CANCEL(false); //业务被撤消 ， 无此状态，因为一个Work可以包括多个业务，只有一个业务被撤消，其他业务还是有效的，此状态无法表达部分撤消的情况， 是否撤消查询业务表

//  static final EnumSet<WorkStatus> create = EnumSet.of(PREPARE,PRIVATE,RUNNING,COMPLETED);
//
//  static final EnumSet<WorkStatus> change = EnumSet.of(VALID,COMPLETED,REJECT,ABORT,DELETED);

  public final boolean valid;

  public final boolean processing;

  public final boolean running;

  public final boolean fixed;

  public static Set<WorkStatus> getRunningStatus() {
    return EnumSet.allOf(WorkStatus.class).stream()
        .filter(it -> it.running)
        .collect(Collectors.toSet());
  }

  public WorkStatus getDisplayWorkStatus() {
    return switch (this) {
      case RESTART -> REJECT;
      case IMMINENT -> RUNNING;
      default -> this;
    };
  }
  WorkStatus(boolean valid, boolean processing, boolean fixed, boolean running ) {
    this.valid = valid;
    this.processing = processing;
    this.fixed = fixed;
    this.running = running;
  }
}
