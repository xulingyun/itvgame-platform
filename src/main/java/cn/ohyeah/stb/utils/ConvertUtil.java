package cn.ohyeah.stb.utils;

import java.io.UnsupportedEncodingException;

/**
 * 转换工具
 * @author maqian
 * @version 1.0
 */
public class ConvertUtil {
	public static final byte BigEndian = 0;
	public static final byte LittleEndian = 1;
	
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
	public static String bytesToHexStr(byte[] srcBytes) {
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
	public static byte[] hexStrToBytes(String hexStr) {
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
	 * string转换为GB2312编码的16进制字符串
	 * @param str
	 * @return
	 */
	public static String strToHexStr(String str) {
		return strToHexStr(str, "GB2312");
	}
	
	/**
	 * string转换encoding编码的16进制字符串
	 * @param str
	 * @param encoding
	 * @return
	 */
	public static String strToHexStr(String str, String encoding)
	{
		if (str == null) {
			return null;
		}
		try {
			return bytesToHexStr(str.getBytes(encoding));
		}
		catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * GB2312编码的16进制字符串转换为string
	 * @param hexStr
	 * @return
	 */
	public static String hexStrToStr(String hexStr) {
		return hexStrToStr(hexStr, "GB2312");
	}
	
	/**
	 * encoding编码的16进制字符串转换为string
	 * @param hexStr
	 * @param encoding
	 * @return
	 */
	public static String hexStrToStr(String hexStr, String encoding)
	{
		if (hexStr == null) {
			return null;
		}
		if (hexStr.length() <= 0) {
			return "";
		}
		try {
			return new String(hexStrToBytes(hexStr), encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	/**
	 * 将大端字节序的byte数组转换为long
	 * @param bytes
	 * @return
	 */
	public static long toLongBigEndian(byte[] bytes)
	{
		return toLong(bytes, BigEndian);
	}
	
	/**
	 * 将小端字节序的byte数组转换为long
	 * @param bytes
	 * @return
	 */
	public static long toLongLittleEndian(byte[] bytes)
	{
		return toLong(bytes, LittleEndian);
	}
	
	/**
	 * 按照endian指定的字节序将byte数组转换为long
	 * @param bytes
	 * @param endian
	 * @return
	 */
	public static long toLong(byte[] bytes, int endian)
	{
		long value = 0;
		if (endian == BigEndian)
		{
			value = (((int)bytes[0])<<56)
					|(((int)bytes[1]&0XFF)<<48)
					|(((int)bytes[2]&0XFF)<<40)
					|(((int)bytes[3]&0XFF)<<32)
					|(((int)bytes[4]&0XFF)<<24)
					|(((int)bytes[5]&0XFF)<<16)
					|(((int)bytes[6]&0XFF)<<8)
					|((int)bytes[7]&0XFF);
		}
		else
		{
			value = (((int)bytes[7])<<56)
					|(((int)bytes[6]&0XFF)<<48)
					|(((int)bytes[5]&0XFF)<<40)
					|(((int)bytes[4]&0XFF)<<32)
					|(((int)bytes[3]&0XFF)<<24)
					|(((int)bytes[2]&0XFF)<<16)
					|(((int)bytes[1]&0XFF)<<8)
					|((int)bytes[0]&0XFF);
		}
		return value;
	}
	
	/**
	 * 将大端字节序的byte数组转换为int
	 * @param bytes
	 * @return
	 */
	public static int toIntBigEndian(byte[] bytes)
	{
		return toInt(bytes, BigEndian);
	}
	
	/**
	 * 将小端字节序的byte数组转换为int
	 * @param bytes
	 * @return
	 */
	public static int toIntLittleEndian(byte[] bytes)
	{
		return toInt(bytes, LittleEndian);
	}
	
	/**
	 * 按照endian指定的字节序将byte数组转换为int
	 * @param bytes
	 * @param endian
	 * @return
	 */
	public static int toInt(byte[] bytes, int endian)
	{
		int value = 0;
		if (endian == BigEndian)
		{
			value = (((int)bytes[0])<<24)
					|(((int)bytes[1]&0XFF)<<16)
					|(((int)bytes[2]&0XFF)<<8)
					|((int)bytes[3]&0XFF);
		}
		else
		{
			value = (((int)bytes[3])<<24)
					|(((int)bytes[2]&0XFF)<<16)
					|(((int)bytes[1]&0XFF)<<8)
					|((int)bytes[0]&0XFF);
		}
		return value;
	}
	
	/**
	 * 将大端字节序的byte数组转换为short
	 * @param bytes
	 * @return
	 */
	public static short toShortBigEndian(byte[] bytes)
	{
		return toShort(bytes, BigEndian);
	}
	
	/**
	 * 将小端字节序的byte数组转换为short
	 * @param bytes
	 * @return
	 */
	public static short toShortLittleEndian(byte[] bytes)
	{
		return toShort(bytes, LittleEndian);
	}
	
	/**
	 * 按照endian指定的字节序将byte数组转换为short
	 * @param bytes
	 * @param endian
	 * @return
	 */
	public static short toShort(byte[] bytes, int endian)
	{
		short value = 0;
		if (endian == BigEndian)
		{
			value = (short)(((bytes[0]<<8)&0XFF00)|(bytes[1]&0XFF));
		}
		else
		{
			value = (short)(((bytes[1]<<8)&0XFF00)|(bytes[0]&0XFF));
		}
		return value;
	
	}
	
	/**
	 * 按照endian指定的字节序将short转换byte数组
	 * @param value
	 * @param endian
	 * @return
	 */
	public static byte[] toBytes(short value, int endian) 
	{
		byte[] bytes = new byte[2];
		if (endian == BigEndian) {
			bytes[0] = (byte)(value>>>8);
			bytes[1] = (byte)(value&0XFF);
		}
		else {
			bytes[0] = (byte)(value&0XFF);
			bytes[1] = (byte)(value>>>8);
		}
		return bytes;
	}
	
	/**
	 * 将short转换为大端字节序的byte数组
	 * @param value
	 * @return
	 */
	public static byte[] toBytesBigEndian(short value)
	{
		return toBytes(value, BigEndian);
	}
	
	/**
	 * 将short转换为小端字节序的byte数组
	 * @param value
	 * @return
	 */
	public static byte[] toBytesLittleEndian(short value)
	{
		return toBytes(value, LittleEndian);
	}
	
	/**
	 * 按照endian指定的字节序将int转换byte数组
	 * @param value
	 * @param endian
	 * @return
	 */
	public static byte[] toBytes(int value, int endian)
	{
		byte[] bytes = new byte[4];
		if (endian == BigEndian) {
			bytes[0] = (byte)(value>>>24);
			bytes[1] = (byte)((value>>>16)&0XFF);
			bytes[2] = (byte)((value>>>8)&0XFF);
			bytes[3] = (byte)(value&0XFF);
		}
		else {
			bytes[0] = (byte)(value&0XFF);
			bytes[1] = (byte)((value>>>8)&0XFF);
			bytes[2] = (byte)((value>>>16)&0XFF);
			bytes[3] = (byte)(value>>>24);
		}
		return bytes;
	}
	
	/**
	 * 将int转换为大端字节序的byte数组
	 * @param value
	 * @return
	 */
	public static byte[] toBytesBigEndian(int value)
	{
		return toBytes(value, BigEndian);
	}
	
	/**
	 * 将int转换为小端字节序的byte数组
	 * @param value
	 * @return
	 */
	public static byte[] toBytesLittleEndian(int value)
	{
		return toBytes(value, LittleEndian);
	}
	
	/**
	 * 按照endian指定的字节序将long转换byte数组
	 * @param value
	 * @param endian
	 * @return
	 */
	public static byte[] toBytes(long value, int endian) {
		byte[] bytes = new byte[8];
		if (endian == BigEndian) {
			bytes[0] = (byte)(value>>>56);
			bytes[1] = (byte)((value>>>48)&0XFF);
			bytes[2] = (byte)((value>>>40)&0XFF);
			bytes[3] = (byte)((value>>>32)&0XFF);
			bytes[4] = (byte)((value>>>24)&0XFF);
			bytes[5] = (byte)((value>>>16)&0XFF);
			bytes[6] = (byte)((value>>>8)&0XFF);
			bytes[7] = (byte)(value&0XFF);
		}
		else {
			bytes[7] = (byte)(value>>>56);
			bytes[6] = (byte)((value>>>48)&0XFF);
			bytes[5] = (byte)((value>>>40)&0XFF);
			bytes[4] = (byte)((value>>>32)&0XFF);
			bytes[3] = (byte)((value>>>24)&0XFF);
			bytes[2] = (byte)((value>>>16)&0XFF);
			bytes[1] = (byte)((value>>>8)&0XFF);
			bytes[0] = (byte)(value&0XFF);
		}
		return bytes;
	}
	
	/**
	 * 将long转换为小端字节序的byte数组
	 * @param value
	 * @return
	 */
	public static byte[] toBytesLittleEndian(long value) {
		return toBytes(value, LittleEndian);
	}
	
	/**
	 * 将long转换为大端字节序的byte数组
	 * @param value
	 * @return
	 */
	public static byte[] toBytesBigEndian(long value) {
		return toBytes(value, BigEndian);
	}
	
	/**
	 * 将string转换为encoding编码的byte数组
	 * @param value
	 * @param encoding
	 * @return
	 */
	public static byte[] toBytes(String value, String encoding)
	{
		try {
			return value.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 将string转换为GB2312编码的byte数组
	 * @param value
	 * @return
	 */
	public static byte[] toBytes(String value)
	{
		return toBytes(value, "GB2312");
	}
	
	/**
	 * 将GB2312编码的byte数组转换为string
	 * @param value
	 * @return
	 */
	public static String toString(byte[] value) {
		return toString(value, "GB2312");
	}
	
	/**
	 * 将encoding编码的byte数组转换为string
	 * @param value
	 * @return
	 */
	public static String toString(byte[] value, String encoding) {
		try {
			return new String(value, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	
}
