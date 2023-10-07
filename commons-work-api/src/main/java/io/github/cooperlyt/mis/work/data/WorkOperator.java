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
public class WorkOperator extends WorkMessage {

  public WorkOperator(WorkOperator operator) {
    super(operator.getWorkId(), operator.getEmpId(), operator.getEmpName());
    this.type = operator.getType();
    this.orgId = operator.getOrgId();
    this.orgName = operator.getOrgName();
    this.workTime = operator.getWorkTime();
  }

  public WorkOperator(long workId, String empId, String empName,
                      OperatorType type, Long orgId, String orgName) {
    super(workId, empId, empName);
    this.type = type;
    this.orgId = orgId;
    this.orgName = orgName;
  }

  public enum OperatorType{
    CREATE,
    TASK,

    APPLY,
  }

  private OperatorType type;
  private Long orgId;
  private String orgName;
  private LocalDateTime workTime;
}
