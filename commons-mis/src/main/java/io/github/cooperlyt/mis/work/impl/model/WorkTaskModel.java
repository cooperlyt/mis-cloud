package io.github.cooperlyt.mis.work.impl.model;


import io.github.cooperlyt.mis.work.data.TaskMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("work_task")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkTaskModel implements TaskMessage, Persistable<String> {

  @Id
  @Column("task_id")
  private String id;

  private String message;

  private String taskName;

  private Boolean pass;

  @Override
  public boolean isNew() {
    return true;
  }
}
