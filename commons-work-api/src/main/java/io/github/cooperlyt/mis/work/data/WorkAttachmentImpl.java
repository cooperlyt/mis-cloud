package io.github.cooperlyt.mis.work.data;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class WorkAttachmentImpl extends WorkAttachmentBase implements WorkAttachmentInfo, java.io.Serializable{

  public WorkAttachmentImpl(long id, long workId, WorkAttachmentBase attachmentBase) {
    super(id,attachmentBase.getName(),attachmentBase.isMust());
    this.workId = workId;
    this.have = false;
  }

  public WorkAttachmentImpl(Long id, String name, boolean must, boolean have, long workId) {
    super(id, name, must);
    this.have = have;
    this.workId = workId;
  }

  public WorkAttachmentImpl(long id, long workId, String name){
    super(id,name,false);
    this.workId = workId;
    this.have = false;
  }

  private boolean have;
  private long workId;
}
