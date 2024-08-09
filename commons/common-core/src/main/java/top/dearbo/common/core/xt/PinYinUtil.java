package top.dearbo.common.core.xt;

import com.github.houbb.pinyin.constant.enums.PinyinStyleEnum;
import com.github.houbb.pinyin.util.PinyinHelper;
import org.apache.commons.lang3.StringUtils;

public class PinYinUtil {

	/**
	 * 获取第一个字符拼音
	 */
	public static String getPinYinFirst(String pinYinStr, String defaultChar) {
		if (StringUtils.isNotBlank(pinYinStr)) {
			char c = pinYinStr.trim().charAt(0);
			return getCharPinYin(c, defaultChar);
		}
		return defaultChar;
	}

	/**
	 * 获取首字母(返回大写)
	 */
	public static String getPinYinFirstChar(String pinYinStr, String defaultChar) {
		String pinYinFirst = getPinYinFirst(pinYinStr, defaultChar);
		if (StringUtils.isNotBlank(pinYinFirst) && pinYinFirst.length() > 1) {
			return pinYinFirst.substring(0, 1).toUpperCase();
		}
		return defaultChar;
	}

	/**
	 * 对单个字进行转换
	 *
	 * @param pinYinStr 需转换的汉字字符串
	 * @return 拼音字符串数组
	 */
	public static String getCharPinYin(char pinYinStr, String defaultChar) {
		String value = String.valueOf(pinYinStr);
		String pinyin = PinyinHelper.toPinyin(value, PinyinStyleEnum.NORMAL);
		//当转换的没有拼音时，返回的原字符
		if (StringUtils.isBlank(pinyin) || pinyin.equals(value)) {
			return defaultChar;
		}
		return pinyin.replace('ü', 'u');
	}

	/**
	 * 对字符串进行转换
	 */
	public static String getPinYin(String pinYinStr) {
		return getPinYin(pinYinStr, " ");
	}

	/**
	 * 对字符串进行转换
	 */
	public static String getPinYin(String pinYinStr, String connector) {
		String pinyin = PinyinHelper.toPinyin(pinYinStr, PinyinStyleEnum.NORMAL, connector);
		return StringUtils.isNotBlank(pinyin) ? pinyin.replace('ü', 'u') : pinyin;
	}

}