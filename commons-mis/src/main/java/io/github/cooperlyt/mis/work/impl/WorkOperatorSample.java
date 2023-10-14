package io.github.cooperlyt.mis.work.impl;

import io.github.cooperlyt.commons.cloud.keycloak.auth.ReactiveKeycloakSecurityContextHolder;
import io.github.cooperlyt.mis.work.data.WorkAction;
import io.github.cooperlyt.mis.work.data.WorkOperator;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkOperatorSample implements WorkOperator {

  @Builder
  public WorkOperatorSample(ReactiveKeycloakSecurityContextHolder.UserInfo userInfo,
                            String orgName, Long corpInfoId, Long employeeInfoId){
    this.userId = userInfo.getUsername();
    this.userName = userInfo.getName();
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
