package io.github.cooperlyt.mis.work.message;


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
public class WorkChangeMessage extends WorkMessage implements java.io.Serializable{


  private String message;

  private String taskName;

  private String taskId;

  private boolean pass;
}
