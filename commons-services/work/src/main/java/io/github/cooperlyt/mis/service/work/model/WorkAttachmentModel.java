package io.github.cooperlyt.mis.service.work.model;

import io.github.cooperlyt.mis.work.data.WorkAttachmentBase;
import io.github.cooperlyt.mis.work.data.WorkAttachmentImpl;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;


@Table("attachment")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public class WorkAttachmentModel extends WorkAttachmentImpl {

  public WorkAttachmentModel(long id, long workId, WorkAttachmentBase attachmentBase) {
    super(id,workId,attachmentBase);
  }

  public WorkAttachmentModel(long id, long workId, String name){
    super(id,workId,name);
  }

  public WorkAttachmentModel updateName(String name){
    this.setName(name);
    return this;
  }

  @Id
  @Override
  public Long getId() {
    return super.getId();
  }

  @Version
  private Long version;
}
