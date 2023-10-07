package io.github.cooperlyt.mis.work.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class WorkAttachmentBase {

  private Long id;
  private String name;
  private boolean must;
}
