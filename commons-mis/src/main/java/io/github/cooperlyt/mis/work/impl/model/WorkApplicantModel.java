package io.github.cooperlyt.mis.work.impl.model;

import io.github.cooperlyt.commons.data.PowerBody;
import io.github.cooperlyt.commons.data.PowerBodyImpl;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

@EqualsAndHashCode(callSuper = true)
@Table("work_applicant")
@Data
@NoArgsConstructor
public class WorkApplicantModel extends PowerBodyImpl {

  public WorkApplicantModel(PowerBody powerBody, Long workId) {
    super(powerBody);
    this.workId = workId;
  }

  @Id
  private Long workId;


  @Version
  private Long version;

}
