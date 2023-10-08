package io.github.cooperlyt.mis.work.data;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public abstract class WorkInstance<T> extends WorkImpl{

  public WorkInstance(WorkInfo workInfo, T instance) {
    super(workInfo.getDefineId(), workInfo.getType(),
        workInfo.isProcess(), workInfo.getWorkId(),
        workInfo.getWorkName(), workInfo.getStatus(),
        workInfo.getDataSource(), workInfo.getCreatedAt(),
        workInfo.getUpdatedAt(), workInfo.getCompletedAt(), workInfo.getValidateAt());
    this.instance = instance;
  }

  private T instance;

  public abstract String getDescription();
}
