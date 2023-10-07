package io.github.cooperlyt.mis.work.impl.model;

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
public class WorkOperatorModel extends WorkOperator implements Persistable<String> {

  public WorkOperatorModel(WorkOperator operator, String id) {
    super(operator);
    this.id = id;
  }

  @Builder(builderMethodName = "operatorBuilder")
  public WorkOperatorModel(long workId, String empId, String empName,
                           OperatorType type, Long orgId, String orgName, String id) {
    super(workId, empId, empName, type, orgId, orgName);
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
