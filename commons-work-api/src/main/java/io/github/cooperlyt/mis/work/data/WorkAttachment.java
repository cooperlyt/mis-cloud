package io.github.cooperlyt.mis.work.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkAttachment  extends WorkAttachmentImpl implements java.io.Serializable{

  public WorkAttachment(WorkAttachmentInfo info){
    super(info.getId(),info.getName(),info.isMust(),info.isHave(),info.getWorkId());
  }

//  private List<WorkFileInfo> files;

  private int fileCount;


}
