package io.github.cooperlyt.mis.work.data;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkOperatorSample implements WorkOperator {

  @Builder
  public WorkOperatorSample(String userId, String userName,
                            String orgName, Long corpInfoId, Long employeeInfoId){
    this.userId = userId;
    this.userName = userName;
    this.orgName = orgName;
    this.corpInfoId = corpInfoId;
    this.employeeInfoId = employeeInfoId;
  }

  private String userId;

  private String userName;

  private String orgName;

  private Long corpInfoId;

  private Long employeeInfoId;

  private WorkAction.ActionType type;

}
