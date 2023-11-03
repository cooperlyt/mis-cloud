package io.github.cooperlyt.mis.work.data;

public interface WorkOperator {

  /**
   *  userId 存 username 历史原因， 名子起的有问题，以后再改

   *  getUserName 存姓名 也是历史原因，以后再改
   *
   * @return
   */
  String getUserId();

  String getUserName();

  String getOrgName();

  Long getCorpInfoId();

  Long getEmployeeInfoId();

}
