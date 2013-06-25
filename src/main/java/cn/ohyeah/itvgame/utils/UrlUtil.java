package cn.ohyeah.itvgame.utils;

public class UrlUtil {
	public static String addParam(String url, String key, String value) {
		if (url.indexOf('?') < 0) {
			return url+"?"+key+"="+value;
		}
		else {
			return url+"&"+key+"="+value;
		}
	}
	
	public static String addParam(String url, String params) {
		if (url.indexOf('?') < 0) {
			return url+"?"+params;
		}
		else {
			return url+"&"+params;
		}
	}
}
