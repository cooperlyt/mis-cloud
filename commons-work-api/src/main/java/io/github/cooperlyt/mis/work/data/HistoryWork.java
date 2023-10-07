package io.github.cooperlyt.mis.work.data;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class HistoryWork extends WorkImpl {

  public enum Category{
    PROJECT,
    BUILD,
    HOUSE,

    CORP,

    CORP_RECORD,
  }

  private Category category;

  public HistoryWork updateCategory(Category category){
    this.category = category;
    return this;
  }

  private WorkOperateType workType;

  @JsonRawValue
  private String operators;

  private long infoId;

  private Long beforeInfoId;

  @JsonRawValue
  private String description;

}
