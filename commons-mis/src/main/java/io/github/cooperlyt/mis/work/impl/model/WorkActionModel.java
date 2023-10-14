package io.github.cooperlyt.mis.work.impl.model;

import io.github.cooperlyt.mis.work.data.WorkAction;
import io.github.cooperlyt.mis.work.data.WorkOperator;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Table("work_operator")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkActionModel extends WorkAction implements Persistable<String> {

  @Builder(builderMethodName = "operatorBuilder", builderClassName = "OperatorBuilder")
  public WorkActionModel(WorkOperator operator, long workId, ActionType type, String id) {
    super(operator, workId, type);
    this.id = id;
  }

  @Builder(builderMethodName = "actionBuilder", builderClassName = "ActionBuilder")
  public WorkActionModel(long workId, String userId, String userName, ActionType type,
                         Long corpInfoId, Long employeeInfoId, String orgName, String id) {
    super(workId, userId, userName, type, corpInfoId, employeeInfoId, orgName);
    this.id = id;
  }

  @Id
  @Column("task_id")
  private String id;

  @CreatedDate
  @Override
  public LocalDateTime getWorkTime() {
    return super.getWorkTime();
  }

  @Override
  public boolean isNew() {
    return true;
  }
}
