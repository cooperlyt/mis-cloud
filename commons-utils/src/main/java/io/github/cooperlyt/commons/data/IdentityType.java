package io.github.cooperlyt.commons.data;

import lombok.Getter;

@Getter
public enum IdentityType {
  RESIDENT_ID(false,true, "居民身份证"),//居民身份证
  RESIDENT(false, true, "户口簿"), //户口簿
  OFFICER(false , false,"军官证"), //军官证
  SOLDIER(false, false,"士兵证"), //士兵证
  PASSPORT(false, false,"护照"), //护照
  FOREIGNER(false, false,"永久居留证"), //外国人永久居留证
  TAIWAN(false, false,"台湾同胞证"), //台湾同胞证
  SPECIAL(false, false,"港澳身份证"), //港澳身份证


  COMPANY(true, false,"营业执照"), //营业执照
  ORGANIZATION(true, false,"组织机构代码证"), //组织机构代码
  ORG_CODE(true, false,"统一社会信用代码证"), //统一社会信用代码证
  RELIGION(true, false,"宗教证"); //宗教证

//8	回乡证


  private final boolean corp;

  private final boolean resident;

  private final String label;

  IdentityType(boolean corp, boolean resident, String label) {
    this.corp = corp;
    this.resident = resident;
    this.label = label;
  }
}
