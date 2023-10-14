package io.github.cooperlyt.mis.work.data;


import io.github.cooperlyt.mis.work.message.WorkMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class WorkAction extends WorkMessage implements WorkOperator {

  public WorkAction(long workId, String userId, String userName,
                    ActionType type, Long corpInfoId, Long employeeInfoId, String orgName) {
    super(workId, userId, userName);
    this.type = type;
    this.employeeInfoId = employeeInfoId;
    this.corpInfoId = corpInfoId;
    this.orgName = orgName;
    this.workTime = LocalDateTime.now();
  }

  public WorkAction(WorkOperator operator, long workId, ActionType type) {
    super(workId, operator.getUserId(), operator.getUserName());
    this.type = type;
    this.corpInfoId = operator.getCorpInfoId();
    this.employeeInfoId = operator.getEmployeeInfoId();
    this.orgName = operator.getOrgName();
    this.workTime = LocalDateTime.now();
    this.type = type;
  }

  public enum ActionType {
    CREATE,
    TASK,

    APPLY,
  }

  private ActionType type;

  private Long corpInfoId;
  private Long employeeInfoId;
  private String orgName;
  private LocalDateTime workTime;
}
