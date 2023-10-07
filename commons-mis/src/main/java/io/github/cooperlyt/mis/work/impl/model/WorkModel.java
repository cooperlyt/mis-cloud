package io.github.cooperlyt.mis.work.impl.model;

import io.github.cooperlyt.mis.work.data.WorkDefine;
import io.github.cooperlyt.mis.work.data.WorkImpl;
import io.github.cooperlyt.mis.work.message.WorkStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = true)
@Table("work")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkModel extends WorkImpl {

  private final static String SOURCE_FROM_SYSTEM = "SYS";

  @Builder
  public WorkModel(WorkDefine define, long workId, WorkStatus status) {
    this.workId = workId;
    this.workName = define.getWorkName();
    this.defineId = define.getDefineId();
    this.process = define.isProcess();
    this.type = define.getType();
    this.status = status;
    this.dataSource = SOURCE_FROM_SYSTEM;
  }

  @Id
  public long getWorkId() {
    return workId;
  }

  @Version
  private Long version;

  @CreatedDate
  public LocalDateTime getCreatedAt(){
    return super.getCreatedAt();
  }

  @LastModifiedDate
  public LocalDateTime getUpdatedAt(){
    return super.getUpdatedAt();
  }

  public WorkModel updateStatus(WorkStatus status){
    if(WorkStatus.COMPLETED.equals(status)){
      super.setCompletedAt(LocalDateTime.now());
      if (this.getValidateAt() == null){
        this.setValidateAt(this.getCompletedAt());
      }
    }
    if (WorkStatus.VALID.equals(status)){
      this.setValidateAt(LocalDateTime.now());
    }
    this.setStatus(status);
    return this;
  }


}
