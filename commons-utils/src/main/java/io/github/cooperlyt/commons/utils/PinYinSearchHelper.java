package io.github.cooperlyt.commons.utils;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

@Slf4j
public class PinYinSearchHelper {

    public static String spell(String chinese) {
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
}
