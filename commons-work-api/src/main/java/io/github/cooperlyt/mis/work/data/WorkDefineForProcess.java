package io.github.cooperlyt.mis.work.data;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkDefineForProcess extends WorkDefineForCreate implements java.io.Serializable{

  @Builder
  public WorkDefineForProcess(long workId, WorkDefine workDefine, List<WorkAttachmentInfo> attachments) {
    super(workId,workDefine);
    this.attachments = attachments;
  }

  @JsonSerialize(contentAs = WorkAttachmentInfo.class)
  private List<WorkAttachmentInfo> attachments;


}
