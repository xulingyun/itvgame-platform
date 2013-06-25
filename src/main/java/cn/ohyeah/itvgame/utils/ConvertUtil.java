package cn.ohyeah.itvgame.utils;

import java.io.UnsupportedEncodingException;

public class ConvertUtil {
	/*16进制转换表*/
	private static char[] hexTable = {
		'0', '1', '2', '3',
		'4', '5', '6', '7',
		'8', '9', 'A', 'B',
		'C', 'D', 'E', 'F'
	};
	
	/**
	 * 字节数组转换为16进制字符串
	 * @param srcBytes
	 * @return
	 */
	public static String byteArrayToHexStr(byte[] srcBytes) {
		String dest = "";
		for (int i = 0; i < srcBytes.length; ++i) {
			int high = (srcBytes[i]>>4)&0XF;
			int low = srcBytes[i]&0XF;
			dest += hexTable[high];
			dest += hexTable[low];
		}
		return dest;
	}
	
	/**
	 * 16进制字符串转换为字节数组
	 * @param hexStr
	 * @return
	 */
	public static byte[] hexStrToByteArray(String hexStr) {
		char[] srcChars = hexStr.toCharArray(); 
		byte[] srcBytes = new byte[srcChars.length>>1];
		int pos = 0;
		for (int i = 0; i < srcBytes.length; ++i) {
			int high = Character.digit(srcChars[pos], 16);
			++pos;
			int low = Character.digit(srcChars[pos], 16);
			++pos;
			srcBytes[i] = (byte)(((high<<4)|low)&0XFF);
		}
		return srcBytes;
	}
	
	/**
	 * GBK字符串转换为16进制字符串
	 * @param str
	 * @return
	 */
	public static String strToHexStr(String str) {
		if (str == null) {
			return null;
		}
		try {
			return byteArrayToHexStr(str.getBytes("gbk"));
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param str
	 * @param charsetName
	 * @return
	 */
	public static String strToHexStr(String str, String charsetName) {
		if (str == null) {
			return null;
		}
		try {
			return byteArrayToHexStr(str.getBytes(charsetName));
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 16进制字符串转换为GBK字符串
	 * @param hexStr
	 * @return
	 */
	public static String hexStrToStr(String hexStr) {
		if (hexStr == null) {
			return null;
		}
		if (hexStr.length() <= 0) {
			return "";
		}
		try {
			return new String(hexStrToByteArray(hexStr), "gbk");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param hexStr
	 * @param charsetName
	 * @return
	 */
	public static String hexStrToStr(String hexStr, String charsetName) {
		if (hexStr == null) {
			return null;
		}
		if (hexStr.length() <= 0) {
			return "";
		}
		try {
			return new String(hexStrToByteArray(hexStr), charsetName);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
