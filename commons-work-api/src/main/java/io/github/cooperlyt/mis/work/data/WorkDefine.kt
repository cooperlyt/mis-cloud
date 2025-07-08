package io.github.cooperlyt.mis.work.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkDefine implements java.io.Serializable {

  private String defineId;
  private String workName;
  private String type;
  private boolean process;
  private boolean enabled;

  /**
   * 用于 设置业务都更改了那部分数据， 用于消息订阅区分数据是否需要更新
   */
  private Set<String> tags = Collections.emptySet();

}
