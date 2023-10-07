package io.github.cooperlyt.commons.data;

public enum IdentityType {
  RESIDENT_ID(false,true),//居民身份证
  RESIDENT(false, true), //户口簿
  OFFICER(false , false), //军官证
  SOLDIER(false, false), //士兵证
  PASSPORT(false, false), //护照
  TAIWAN(false, false), //台湾同胞证
  SPECIAL(false, false), //港澳身份证

  COMPANY(true, false), //营业执照
  ORGANIZATION(true, false), //组织机构代码
  ORG_CODE(true, false), //统一社会信用代码证
  RELIGION(true, false); //宗教证


  private final boolean corp;

  private final boolean resident;

  public boolean isResident() {
    return resident;
  }

  public boolean isCorp() {
    return this.corp;
  }

  IdentityType(boolean corp, boolean resident) {
    this.corp = corp;
    this.resident = resident;
  }
}
