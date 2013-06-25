package cn.ohyeah.itvgame.utils;

import org.apache.commons.lang.StringUtils;

public class ParamUtil {
	public static String parse(String src, String key) {
		if (StringUtils.isEmpty(key)) {
			throw new RuntimeException("key is null");
		}
		String param = null;
		int pos = src.indexOf(key+"$");
		if (pos >= 0) {
			pos = pos+key.length()+1;
			int end = src.indexOf("!", pos);
			if (end > 0) {
				param = src.substring(pos, end);
			}
			else {
				param = src.substring(pos);
			}
		}
		return param;
	}
	
	public static String add(String src, String key, String value) {
		if (StringUtils.isEmpty(key)) {
			throw new RuntimeException("key is null");
		}
		if (StringUtils.isNotEmpty(value)) {
			if (StringUtils.isNotEmpty(src)) {
				src += "!"+key+"$"+value;
			}
			else {
				src = key+"$"+value;
			}
		}
		return src;
	}
	
	public static String judgeReplace(String src, String key, String value) {
		if (StringUtils.isEmpty(key)) {
			throw new RuntimeException("key is null");
		}
		if (StringUtils.isNotEmpty(value)) {
			if (StringUtils.isNotEmpty(src)) {
				int pos = src.indexOf(key+"$");
				if (pos >= 0) {
					String left = (pos==0)?"":src.substring(0, pos-1);
					pos = pos+key.length()+1;
					int end = src.indexOf("!", pos);
					String right = (end>0)?src.substring(end+1):"";
					src = left+"!"+key+"$"+value+right;
				}
				else {
					src += "!"+key+"$"+value;
				}
			}
			else {
				src = key+"$"+value;
			}
		}
		return src;
	}
}
