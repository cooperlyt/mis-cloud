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

import java.util.Arrays;

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

}
