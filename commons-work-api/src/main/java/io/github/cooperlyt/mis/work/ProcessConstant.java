package io.github.cooperlyt.mis.work;

public class ProcessConstant {

  public static final String FLOW_DIRECTION_VAR = "process_method";


  //@Deprecated
  //public static final String APPLY_FROM_VAR = "windows_apply";

  public static final String DOCUMENTATION_VAR = "documentation";

  public static final String NOTE_VAR = "note";

  public static final String DISTRICT_VAR = "district_code";

  public static final String SOURCE_VAR = "source";

  public static final String MESSAGE_HEADER_DEFINE_KEY = "define";

  public static String defineToRouterKey(String define) {
    return define.replaceAll("_", ".").toLowerCase();
  }

}
