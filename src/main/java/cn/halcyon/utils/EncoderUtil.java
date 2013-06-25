package cn.halcyon.utils;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 
 * @author maqian
 *
 */
public class EncoderUtil {
	private static String src = "+/=\r\n";
	private static String target = ",_.()";
	public static String encodeUrlBase64(String str) {
		String b64str = new BASE64Encoder().encode(str.getBytes());
		return StringUtils.replaceChars(b64str, src, target);
	}
	
	public static String decodeUrlBase64(String str) throws IOException {
		String b64str = StringUtils.replaceChars(str, target, src);
		return new String(new BASE64Decoder().decodeBuffer(b64str));
	}
	
}
