package io.github.cooperlyt.mis.service.work.model;


import io.github.cooperlyt.mis.work.data.WorkFileImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Table("work_file")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class WorkFileModel extends WorkFileImpl implements Persistable<String> {

  public WorkFileModel(Long attachId, WorkFileImpl workFile) {
    super(workFile.getFid(), workFile.getSha256(), workFile.getTaskId(), workFile.getSize(), workFile.getMime(), workFile.getETag(), workFile.getFilename());
    this.attachId = attachId;
  }

  private Long attachId;

  private int orderNum;

  @Id
  @Override
  public String getFid() {
    return super.getFid();
  }
  @Override
  public String getId() {
    return getFid();
  }

  @Override
  public boolean isNew() {
    return true;
  }
}
