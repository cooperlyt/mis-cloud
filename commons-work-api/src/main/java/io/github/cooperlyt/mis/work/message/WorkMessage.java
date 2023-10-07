package io.github.cooperlyt.mis.work.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class WorkMessage implements java.io.Serializable{

  public static final String MESSAGE_HEADER_WORK_TYPE = "type";
  public static final String MESSAGE_HEADER_WORK_DEFINE = "define";
  public static final String MESSAGE_HEADER_DATA_ID = "work_id";

  private long workId;
  private String empId;
  private String empName;

}
