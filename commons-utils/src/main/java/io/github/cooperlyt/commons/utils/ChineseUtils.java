package io.github.cooperlyt.commons.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class ChineseUtils {

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class PersonName{

    private String firstName;

    private String lastName;
  }

  public static String spellTop(String chinese) {
    if (chinese == null) {
      return null;
    }
    HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
    format.setCaseType(HanyuPinyinCaseType.UPPERCASE);// 小写
    format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不标声调
    format.setVCharType(HanyuPinyinVCharType.WITH_V);// u:的声母替换为v
    try {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < chinese.length(); i++) {
        String[] array = PinyinHelper.toHanyuPinyinStringArray(chinese
            .charAt(i), format);
        if (array == null || array.length == 0) {
          continue;
        }
        sb.append(array[0].charAt(0));
      }
      return sb.toString();
    } catch (BadHanyuPinyinOutputFormatCombination e) {
      log.error("py code error",e);
    }
    return null;
  }

  public static final String[] DOUBLE_FAMILY_NAME = { "欧阳", "太史", "上官", "端木", "司马", "东方", "独孤", "南宫", "万俟", "闻人", "夏侯", "诸葛", "尉迟", "公羊", "赫连", "澹台",
      "皇甫", "宗政", "濮阳", "公冶", "太叔", "申屠", "公孙", "慕容", "仲孙", "钟离", "长孙", "宇文", "司徒", "鲜于", "司空", "闾丘", "子车",
      "亓官", "司寇", "巫马", "公西", "颛孙", "壤驷", "公良", "漆雕", "乐正", "宰父", "谷梁", "拓跋", "夹谷", "轩辕", "令狐", "段干", "百里",
      "呼延", "东郭", "南门", "羊舌", "微生", "公户", "公玉", "公仪", "梁丘", "公仲", "公上", "公门", "公山", "公坚", "左丘", "公伯", "西门",
      "公祖", "第五", "公乘", "贯丘", "公皙", "南荣", "东里", "东宫", "仲长", "子书", "子桑", "即墨", "达奚", "褚师", "吴铭" };

  public static PersonName nameSplit(String name) {
    if (StringUtils.isEmpty(name)) return null;
    if (name.length() == 1) return PersonName.builder().lastName(name).firstName("").build();
    return Arrays.stream(DOUBLE_FAMILY_NAME).filter(name::startsWith)
        .map(familyName -> PersonName.builder().lastName(familyName).firstName(name.substring(familyName.length())).build())
        .findFirst().orElseGet(() -> PersonName.builder().lastName(name.substring(0, 1)).firstName(name.substring(1)).build());
  }


    /**
     * 汉语中数字大写
     */
    private static final String[] CN_UPPER_NUMBER = { "零", "壹", "贰", "叁", "肆",
        "伍", "陆", "柒", "捌", "玖" };
    /**
     * 汉语中货币单位大写，这样的设计类似于占位符
     */
    private static final String[] CN_UPPER_MONETRAY_UNIT = { "分", "角", "元",
        "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾",
        "佰", "仟" };
    /**
     * 特殊字符：整
     */
    private static final String CN_FULL = "整";
    /**
     * 特殊字符：负
     */
    private static final String CN_NEGATIVE = "负";
    /**
     * 金额的精度，默认值为2
     */
    private static final int MONEY_PRECISION = 2;
    /**
     * 特殊字符：零元整
     */
    private static final String CN_ZERO_FULL = "零元" + CN_FULL;

    /**
     * 把输入的金额转换为汉语中人民币的大写
     *
     * @param numberOfMoney
     *            输入的金额
     * @return 对应的汉语大写
     */
    public static String currencyChinese(BigDecimal numberOfMoney) {
      StringBuffer sb = new StringBuffer();
      // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
      // positive.
      int signum = numberOfMoney.signum();
      // 零元整的情况
      if (signum == 0) {
        return CN_ZERO_FULL;
      }
      //这里会进行金额的四舍五入
      long number = numberOfMoney.movePointRight(MONEY_PRECISION).setScale(0, 4).abs().longValue();
      // 得到小数点后两位值
      long scale = number % 100;
      int numUnit = 0;
      int numIndex = 0;
      boolean getZero = false;
      // 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
      if (!(scale > 0)) {
        numIndex = 2;
        number = number / 100;
        getZero = true;
      }
      if ((scale > 0) && (!(scale % 10 > 0))) {
        numIndex = 1;
        number = number / 10;
        getZero = true;
      }
      int zeroSize = 0;
      while (number > 0) {
        // 每次获取到最后一个数
        numUnit = (int) (number % 10);
        if (numUnit > 0) {
          if ((numIndex == 9) && (zeroSize >= 3)) {
            sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
          }
          if ((numIndex == 13) && (zeroSize >= 3)) {
            sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
          }
          sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
          sb.insert(0, CN_UPPER_NUMBER[numUnit]);
          getZero = false;
          zeroSize = 0;
        } else {
          ++zeroSize;
          if (!(getZero)) {
            sb.insert(0, CN_UPPER_NUMBER[numUnit]);
          }
          if (numIndex == 2) {
            sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
          } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
            sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
          }
          getZero = true;
        }
        // 让number每次都去掉最后一个数
        number = number / 10;
        ++numIndex;
      }
      // 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
      if (signum == -1) {
        sb.insert(0, CN_NEGATIVE);
      }
      // 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
      if (!(scale > 0)) {
        sb.append(CN_FULL);
      }
      return sb.toString();
    }


  //身份证



    /** 中国公民身份证号码最小长度。 */
    public static final int CHINA_ID_MIN_LENGTH = 15;

    /** 中国公民身份证号码最大长度。 */
    public static final int CHINA_ID_MAX_LENGTH = 18;

    /** 省、直辖市代码表 */
    public static final String cityCode[] = {
        "11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35", "36", "37", "41",
        "42", "43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64", "65", "71",
        "81", "82", "91"
    };

    /** 每位加权因子 */
    public static final int power[] = {
        7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2
    };

    /** 第18位校检码 */
    public static final String verifyCode[] = {
        "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"
    };
    /** 最低年限 */
    public static final int MIN = 1930;
    public static Map<String, String> cityCodes = new HashMap<String, String>();
    /** 台湾身份首字母对应数字 */
    public static Map<String, Integer> twFirstCode = new HashMap<String, Integer>();
    /** 香港身份首字母对应数字 */
    public static Map<String, Integer> hkFirstCode = new HashMap<String, Integer>();
    static {
      cityCodes.put("11", "北京");
      cityCodes.put("12", "天津");
      cityCodes.put("13", "河北");
      cityCodes.put("14", "山西");
      cityCodes.put("15", "内蒙古");
      cityCodes.put("21", "辽宁");
      cityCodes.put("22", "吉林");
      cityCodes.put("23", "黑龙江");
      cityCodes.put("31", "上海");
      cityCodes.put("32", "江苏");
      cityCodes.put("33", "浙江");
      cityCodes.put("34", "安徽");
      cityCodes.put("35", "福建");
      cityCodes.put("36", "江西");
      cityCodes.put("37", "山东");
      cityCodes.put("41", "河南");
      cityCodes.put("42", "湖北");
      cityCodes.put("43", "湖南");
      cityCodes.put("44", "广东");
      cityCodes.put("45", "广西");
      cityCodes.put("46", "海南");
      cityCodes.put("50", "重庆");
      cityCodes.put("51", "四川");
      cityCodes.put("52", "贵州");
      cityCodes.put("53", "云南");
      cityCodes.put("54", "西藏");
      cityCodes.put("61", "陕西");
      cityCodes.put("62", "甘肃");
      cityCodes.put("63", "青海");
      cityCodes.put("64", "宁夏");
      cityCodes.put("65", "新疆");
      cityCodes.put("71", "台湾");
      cityCodes.put("81", "香港");
      cityCodes.put("82", "澳门");
      cityCodes.put("91", "国外");
      twFirstCode.put("A", 10);
      twFirstCode.put("B", 11);
      twFirstCode.put("C", 12);
      twFirstCode.put("D", 13);
      twFirstCode.put("E", 14);
      twFirstCode.put("F", 15);
      twFirstCode.put("G", 16);
      twFirstCode.put("H", 17);
      twFirstCode.put("J", 18);
      twFirstCode.put("K", 19);
      twFirstCode.put("L", 20);
      twFirstCode.put("M", 21);
      twFirstCode.put("N", 22);
      twFirstCode.put("P", 23);
      twFirstCode.put("Q", 24);
      twFirstCode.put("R", 25);
      twFirstCode.put("S", 26);
      twFirstCode.put("T", 27);
      twFirstCode.put("U", 28);
      twFirstCode.put("V", 29);
      twFirstCode.put("X", 30);
      twFirstCode.put("Y", 31);
      twFirstCode.put("W", 32);
      twFirstCode.put("Z", 33);
      twFirstCode.put("I", 34);
      twFirstCode.put("O", 35);
      hkFirstCode.put("A", 1);
      hkFirstCode.put("B", 2);
      hkFirstCode.put("C", 3);
      hkFirstCode.put("R", 18);
      hkFirstCode.put("U", 21);
      hkFirstCode.put("Z", 26);
      hkFirstCode.put("X", 24);
      hkFirstCode.put("W", 23);
      hkFirstCode.put("O", 15);
      hkFirstCode.put("N", 14);
    }

    /**
     * 将15位身份证号码转换为18位
     *
     * @param idCard
     *            15位身份编码
     * @return 18位身份编码
     */
    public static String convert15CardTo18(String idCard) {
      String idCard18 = "";
      if (idCard.length() != CHINA_ID_MIN_LENGTH) {
        return null;
      }
      if (isNum(idCard)) {
        // 获取出生年月日
        String birthday = idCard.substring(6, 12);
        Date birthDate = null;
        try {
          birthDate = new SimpleDateFormat("yyMMdd").parse(birthday);
        } catch (ParseException e) {
          throw new RuntimeException(e);
        }
        Calendar cal = Calendar.getInstance();
        if (birthDate != null)
          cal.setTime(birthDate);
        // 获取出生年(完全表现形式,如：2010)
        String sYear = String.valueOf(cal.get(Calendar.YEAR));
        idCard18 = idCard.substring(0, 6) + sYear + idCard.substring(8);
        // 转换字符数组
        char[] cArr = idCard18.toCharArray();
        int[] iCard = converCharToInt(cArr);
        int iSum17 = getPowerSum(iCard);
        // 获取校验位
        String sVal = getCheckCode18(iSum17);
        if (!sVal.isEmpty()) {
          idCard18 += sVal;
        } else {
          return null;
        }
      } else {
        return null;
      }
      return idCard18;
    }

    /**
     * 验证身份证是否合法
     */
    public static boolean validateCard(String idCard) {
      String card = idCard.trim().toUpperCase();
      if (validateIdCard18(card)) {
        return true;
      }
      if (validateIdCard15(card)) {
        return true;
      }
      String[] cardSpecial = validateIdCard10(card);
      assert cardSpecial != null;
      return cardSpecial[2].equals("true");
    }

    /**
     * 验证18位身份编码是否合法
     *
     * @param idCard 身份编码
     * @return 是否合法
     */
    public static boolean validateIdCard18(String idCard) {
      boolean bTrue = false;
      if (idCard.length() == CHINA_ID_MAX_LENGTH) {
        // 前17位
        String code17 = idCard.substring(0, 17);
        // 第18位
        String code18 = idCard.substring(17, CHINA_ID_MAX_LENGTH);
        if (isNum(code17)) {
          char[] cArr = code17.toCharArray();
          int[] iCard = converCharToInt(cArr);
          int iSum17 = getPowerSum(iCard);
          // 获取校验位
          String val = getCheckCode18(iSum17);
          if (!val.isEmpty()) {
            if (val.equalsIgnoreCase(code18)) {
              bTrue = true;
            }
          }
        }
      }
      return bTrue;
    }

    /**
     * 验证15位身份编码是否合法
     *
     * @param idCard
     *            身份编码
     * @return 是否合法
     */
    public static boolean validateIdCard15(String idCard) {
      if (idCard.length() != CHINA_ID_MIN_LENGTH) {
        return false;
      }
      if (isNum(idCard)) {
        String proCode = idCard.substring(0, 2);
        if (cityCodes.get(proCode) == null) {
          return false;
        }
        String birthCode = idCard.substring(6, 12);
        Date birthDate = null;
        try {
          birthDate = new SimpleDateFormat("yy").parse(birthCode.substring(0, 2));
        } catch (ParseException e) {
          return false;
        }
        Calendar cal = Calendar.getInstance();
        if (birthDate != null)
          cal.setTime(birthDate);
        return validDate(cal.get(Calendar.YEAR), Integer.parseInt(birthCode.substring(2, 4)),
            Integer.parseInt(birthCode.substring(4, 6)));
      } else {
        return false;
      }
    }

    /**
     * 验证10位身份编码是否合法
     *
     * @param idCard 身份编码
     * @return 身份证信息数组
     *         <p>
     *         [0] - 台湾、澳门、香港 [1] - 性别(男M,女F,未知N) [2] - 是否合法(合法true,不合法false)
     *         若不是身份证件号码则返回null
     *         </p>
     */
    public static String[] validateIdCard10(String idCard) {
      String[] info = new String[3];
      String card = idCard.replaceAll("[\\(|\\)]", "");
      if (card.length() != 8 && card.length() != 9 && idCard.length() != 10) {
        return null;
      }
      if (idCard.matches("^[a-zA-Z][0-9]{9}$")) { // 台湾
        info[0] = "台湾";
        System.out.println("11111");
        String char2 = idCard.substring(1, 2);
        if (char2.equals("1")) {
          info[1] = "M";
          System.out.println("MMMMMMM");
        } else if (char2.equals("2")) {
          info[1] = "F";
          System.out.println("FFFFFFF");
        } else {
          info[1] = "N";
          info[2] = "false";
          System.out.println("NNNN");
          return info;
        }
        info[2] = validateTWCard(idCard) ? "true" : "false";
      } else if (idCard.matches("^[1|5|7][0-9]{6}\\(?[0-9A-Z]\\)?$")) { // 澳门
        info[0] = "澳门";
        info[1] = "N";
        // TODO
      } else if (idCard.matches("^[A-Z]{1,2}[0-9]{6}\\(?[0-9A]\\)?$")) { // 香港
        info[0] = "香港";
        info[1] = "N";
        info[2] = validateHKCard(idCard) ? "true" : "false";
      } else {
        return null;
      }
      return info;
    }

    /**
     * 验证台湾身份证号码
     *
     * @param idCard
     *            身份证号码
     * @return 验证码是否符合
     */
    public static boolean validateTWCard(String idCard) {
      String start = idCard.substring(0, 1);
      String mid = idCard.substring(1, 9);
      String end = idCard.substring(9, 10);
      Integer iStart = twFirstCode.get(start);
      Integer sum = iStart / 10 + (iStart % 10) * 9;
      char[] chars = mid.toCharArray();
      Integer iflag = 8;
      for (char c : chars) {
        sum = sum + Integer.valueOf(c + "") * iflag;
        iflag--;
      }
      return (sum % 10 == 0 ? 0 : (10 - sum % 10)) == Integer.valueOf(end) ? true : false;
    }

    /**
     * 验证香港身份证号码(存在Bug，部份特殊身份证无法检查)
     * <p>
     * 身份证前2位为英文字符，如果只出现一个英文字符则表示第一位是空格，对应数字58 前2位英文字符A-Z分别对应数字10-35
     * 最后一位校验码为0-9的数字加上字符"A"，"A"代表10
     * </p>
     * <p>
     * 将身份证号码全部转换为数字，分别对应乘9-1相加的总和，整除11则证件号码有效
     * </p>
     *
     * @param idCard 身份证号码
     * @return 验证码是否符合
     */
    public static boolean validateHKCard(String idCard) {
      String card = idCard.replaceAll("[\\(|\\)]", "");
      Integer sum = 0;
      if (card.length() == 9) {
        sum = (Integer.valueOf(card.substring(0, 1).toUpperCase().toCharArray()[0]) - 55) * 9
            + (Integer.valueOf(card.substring(1, 2).toUpperCase().toCharArray()[0]) - 55) * 8;
        card = card.substring(1, 9);
      } else {
        sum = 522 + (Integer.valueOf(card.substring(0, 1).toUpperCase().toCharArray()[0]) - 55) * 8;
      }
      String mid = card.substring(1, 7);
      String end = card.substring(7, 8);
      char[] chars = mid.toCharArray();
      Integer iflag = 7;
      for (char c : chars) {
        sum = sum + Integer.valueOf(c + "") * iflag;
        iflag--;
      }
      if (end.toUpperCase().equals("A")) {
        sum = sum + 10;
      } else {
        sum = sum + Integer.valueOf(end);
      }
      return (sum % 11 == 0) ? true : false;
    }

    /**
     * 将字符数组转换成数字数组
     *
     * @param ca
     *            字符数组
     * @return 数字数组
     */
    public static int[] converCharToInt(char[] ca) {
      int len = ca.length;
      int[] iArr = new int[len];
      try {
        for (int i = 0; i < len; i++) {
          iArr[i] = Integer.parseInt(String.valueOf(ca[i]));
        }
      } catch (NumberFormatException e) {
        e.printStackTrace();
      }
      return iArr;
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
     *
     * @param iArr
     * @return 身份证编码。
     */
    public static int getPowerSum(int[] iArr) {
      int iSum = 0;
      if (power.length == iArr.length) {
        for (int i = 0; i < iArr.length; i++) {
          for (int j = 0; j < power.length; j++) {
            if (i == j) {
              iSum = iSum + iArr[i] * power[j];
            }
          }
        }
      }
      return iSum;
    }

    /**
     * 将power和值与11取模获得余数进行校验码判断
     *
     * @param iSum
     * @return 校验位
     */
    public static String getCheckCode18(int iSum) {
      String sCode = "";
      switch (iSum % 11) {
        case 10:
          sCode = "2";
          break;
        case 9:
          sCode = "3";
          break;
        case 8:
          sCode = "4";
          break;
        case 7:
          sCode = "5";
          break;
        case 6:
          sCode = "6";
          break;
        case 5:
          sCode = "7";
          break;
        case 4:
          sCode = "8";
          break;
        case 3:
          sCode = "9";
          break;
        case 2:
          sCode = "X";
          break;
        case 1:
          sCode = "0";
          break;
        case 0:
          sCode = "1";
          break;
      }
      return sCode;
    }

    /**
     * 根据身份编号获取年龄
     *
     * @param idCard
     *            身份编号
     * @return 年龄
     */
    public static int getAgeByIdCard(String idCard) {
      int iAge = 0;
      if (idCard.length() == CHINA_ID_MIN_LENGTH) {
        idCard = convert15CardTo18(idCard);
      }
      assert idCard != null;
      String year = idCard.substring(6, 10);
      Calendar cal = Calendar.getInstance();
      int iCurrYear = cal.get(Calendar.YEAR);
      iAge = iCurrYear - Integer.parseInt(year);
      return iAge;
    }

    /**
     * 根据身份编号获取生日
     *
     * @param idCard 身份编号
     * @return 生日(yyyyMMdd)
     */
    public static String getBirthByIdCard(String idCard) {
      int len = idCard.length();
      if (len < CHINA_ID_MIN_LENGTH) {
        return null;
      } else if (len == CHINA_ID_MIN_LENGTH) {
        idCard = convert15CardTo18(idCard);
      }
      assert idCard != null;
      return idCard.substring(6, 14);
    }

    /**
     * 根据身份编号获取生日年
     *
     * @param idCard 身份编号
     * @return 生日(yyyy)
     */
    public static Short getYearByIdCard(String idCard) {
      int len = idCard.length();
      if (len < CHINA_ID_MIN_LENGTH) {
        return null;
      } else if (len == CHINA_ID_MIN_LENGTH) {
        idCard = convert15CardTo18(idCard);
      }
      assert idCard != null;
      return Short.valueOf(idCard.substring(6, 10));
    }

    /**
     * 根据身份编号获取生日月
     *
     * @param idCard
     *            身份编号
     * @return 生日(MM)
     */
    public static Short getMonthByIdCard(String idCard) {
      int len = idCard.length();
      if (len < CHINA_ID_MIN_LENGTH) {
        return null;
      } else if (len == CHINA_ID_MIN_LENGTH) {
        idCard = convert15CardTo18(idCard);
      }
      assert idCard != null;
      return Short.valueOf(idCard.substring(10, 12));
    }

    /**
     * 根据身份编号获取生日天
     *
     * @param idCard
     *            身份编号
     * @return 生日(dd)
     */
    public static Short getDateByIdCard(String idCard) {
      int len = idCard.length();
      if (len < CHINA_ID_MIN_LENGTH) {
        return null;
      } else if (len == CHINA_ID_MIN_LENGTH) {
        idCard = convert15CardTo18(idCard);
      }
      assert idCard != null;
      return Short.valueOf(idCard.substring(12, 14));
    }

    /**
     * 根据身份编号获取性别
     *
     * @param idCard 身份编号
     * @return 性别(M-男，F-女，N-未知)
     */
    public static String getGenderByIdCard(String idCard) {
      String sGender = "N";
      if (idCard.length() == CHINA_ID_MIN_LENGTH) {
        idCard = convert15CardTo18(idCard);
      }
      assert idCard != null;
      String sCardNum = idCard.substring(16, 17);
      if (Integer.parseInt(sCardNum) % 2 != 0) {
        sGender = "M";
      } else {
        sGender = "F";
      }
      return sGender;
    }

    /**
     * 根据身份编号获取户籍省份
     *
     * @param idCard 身份编码
     * @return 省级编码。
     */
    public static String getProvinceByIdCard(String idCard) {
      int len = idCard.length();
      String sProvince = null;
      String sProvinNum = "";
      if (len == CHINA_ID_MIN_LENGTH || len == CHINA_ID_MAX_LENGTH) {
        sProvinNum = idCard.substring(0, 2);
      }
      sProvince = cityCodes.get(sProvinNum);
      return sProvince;
    }

    /**
     * 数字验证
     *
     * @param val
     * @return 提取的数字。
     */
    public static boolean isNum(String val) {
      return val != null && !val.isEmpty() && val.matches("^[0-9]*$");
    }

    /**
     * 验证小于当前日期 是否有效
     *
     * @param iYear
     *            待验证日期(年)
     * @param iMonth
     *            待验证日期(月 1-12)
     * @param iDate
     *            待验证日期(日)
     * @return 是否有效
     */
    public static boolean validDate(int iYear, int iMonth, int iDate) {
      Calendar cal = Calendar.getInstance();
      int year = cal.get(Calendar.YEAR);
      int datePerMonth;
      if (iYear < MIN || iYear >= year) {
        return false;
      }
      if (iMonth < 1 || iMonth > 12) {
        return false;
      }
      datePerMonth = switch (iMonth) {
        case 4, 6, 9, 11 -> 30;
        case 2 -> {
          boolean dm = (iYear % 4 == 0 && iYear % 100 != 0 || iYear % 400 == 0) && iYear > MIN;
          yield dm ? 29 : 28;
        }
        default -> 31;
      };
      return (iDate >= 1) && (iDate <= datePerMonth);
    }


}
