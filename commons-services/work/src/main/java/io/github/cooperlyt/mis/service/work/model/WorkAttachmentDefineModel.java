package io.github.cooperlyt.mis.service.work.model;

import io.github.cooperlyt.mis.work.data.WorkAttachmentBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

@Table("attachment_define")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class WorkAttachmentDefineModel extends WorkAttachmentBase {

  private String defineId;

  private String description;

  @Version
  private Long version;

}
