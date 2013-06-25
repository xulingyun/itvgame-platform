package cn.ohyeah.stb.utils;

/**
 * 字节数组处理工具
 * @author maqian
 * @version 1.0
 */
public class BytesUtil {

	/**
	 * 将byte类型的value写入字节数组dest的index位置
	 * @param dest
	 * @param index
	 * @param value
	 */
	public static void writeByte(byte[] dest, int index, byte value) {
		dest[index] = value;
	}
	
	/**
	 * 将byte[]类型的value写入字节数组dest的index位置,在写入数据前先写入数组的长度
	 * @param dest
	 * @param index
	 * @param value
	 */
	public static void writeByteArray(byte[] dest, int index, byte[] value) {
		writeInt(dest, index, value.length);
		writeBytes(dest, index+4, value);
	}
	
	/**
	 * 将short类型的value写入字节数组dest的index位置
	 * @param dest
	 * @param index
	 * @param value
	 */
	public static void writeShort(byte[] dest, int index, short value) {
		dest[index] = (byte)(value>>>8);
		dest[index+1] = (byte)(value&0XFF);
	}
	
	/**
	 * 将int类型的value写入字节数组dest的index位置
	 * @param dest
	 * @param index
	 * @param value
	 */
	public static void writeInt(byte[] dest, int index, int value) {
		dest[index] = (byte)(value>>>24);
		dest[index+1] = (byte)((value>>>16)&0XFF);
		dest[index+2] = (byte)((value>>>8)&0XFF);
		dest[index+3] = (byte)(value&0XFF);
	}
	
	/**
	 * 将long类型的value写入字节数组dest的index位置
	 * @param dest
	 * @param index
	 * @param value
	 */
	public static void writeLong(byte[] dest, int index, long value) {
		dest[index] = (byte)(value>>>56);
		dest[index+1] = (byte)((value>>>48)&0XFF);
		dest[index+2] = (byte)((value>>>40)&0XFF);
		dest[index+3] = (byte)((value>>>32)&0XFF);
		dest[index+4] = (byte)((value>>>24)&0XFF);
		dest[index+5] = (byte)((value>>>16)&0XFF);
		dest[index+6] = (byte)((value>>>8)&0XFF);
		dest[index+7] = (byte)(value&0XFF);
	}
	
	/**
	 * 将float类型的value写入字节数组dest的index位置
	 * @param dest
	 * @param index
	 * @param value
	 */
	public static void writeFloat(byte[] dest, int index, float value) {
		writeInt(dest, index, Float.floatToIntBits(value));
	}
	
	/**
	 * 将double类型的value写入字节数组dest的index位置
	 * @param dest
	 * @param index
	 * @param value
	 */
	public static void writeDouble(byte[] dest, int index, double value) {
		writeLong(dest, index, Double.doubleToLongBits(value));
	}
	
	/**
	 * 将byte[]类型的value写入字节数组dest的index位置,不写入数组长度，直接写入数据
	 * @param dest
	 * @param index
	 * @param value
	 */
	public static void writeBytes(byte[] dest, int index, byte[] value) {
		System.arraycopy(value, 0, dest, index, value.length);
	}
	
	/**
	 * 将byte[]类型value的指定部分，写入字节数组dest的index位置,不写入数组长度，直接写入数据
	 * @param dest
	 * @param index
	 * @param value
	 * @param offset
	 * @param len
	 */
	public static void writeBytes(byte[] dest, int index, byte[] value, int offset, int len) {
		System.arraycopy(value, offset, dest, index, len);
	}
	
	/**
	 * 在src的index位置，读取byte
	 * @param src
	 * @param index
	 * @return
	 */
	public static byte readByte(byte[] src, int index) {
		return src[index];
	}
	
	/**
	 * 在src的index位置，读取byte[]，先读取int类型的数组长度len，再根据len读取数据
	 * @param src
	 * @param index
	 * @return
	 */
	public static byte[] readByteArray(byte[] src, int index) {
		int len = readInt(src, index);
		return readBytes(src, index+4, len);
	}
	
	/**
	 * 在src的index位置，读取short
	 * @param src
	 * @param index
	 * @return
	 */
	public static short readShort(byte[] src, int index) {
		return (short)(((src[index]<<8)&0XFF00)|(src[index+1]&0XFF));
	}
	
	/**
	 * 在src的index位置，读取int
	 * @param src
	 * @param index
	 * @return
	 */
	public static int readInt(byte[] src, int index) {
		return (((int)src[index])<<24)
				|(((int)src[index+1]&0XFF)<<16)
				|(((int)src[index+2]&0XFF)<<8)
				|((int)src[index+3]&0XFF);
	}
	
	/**
	 * 在src的index位置，读取long
	 * @param src
	 * @param index
	 * @return
	 */
	public static long readLong(byte[] src, int index) {
		return (((int)src[index])<<56)
				|(((int)src[index+1]&0XFF)<<48)
				|(((int)src[index+2]&0XFF)<<40)
				|(((int)src[index+3]&0XFF)<<32)
				|(((int)src[index+4]&0XFF)<<24)
				|(((int)src[index+5]&0XFF)<<16)
				|(((int)src[index+6]&0XFF)<<8)
				|((int)src[index+7]&0XFF);
	}
	
	/**
	 * 在src的index位置，读取float
	 * @param src
	 * @param index
	 * @return
	 */
	public static float readFloat(byte[] src, int index) {
		return Float.intBitsToFloat(readInt(src, index));
	}
	
	/**
	 * 在src的index位置，读取double
	 * @param src
	 * @param index
	 * @return
	 */
	public static double readDouble(byte[] src, int index) {
		return Double.longBitsToDouble(readLong(src, index));
	}
	
	/**
	 * 在src的index位置，读取指定长度len的byte[]
	 * @param src
	 * @param index
	 * @param len
	 * @return
	 */
	public static byte[] readBytes(byte[] src, int index, int len) {
		byte[] value = new byte[len];
		System.arraycopy(src, index, value, 0, len);
		return value;
	}
	
}
