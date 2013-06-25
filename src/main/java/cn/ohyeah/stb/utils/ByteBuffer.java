package cn.ohyeah.stb.utils;

import cn.ohyeah.stb.utils.BytesUtil;
import cn.ohyeah.stb.utils.ConvertUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 可读写字节流缓冲区，用于替代java字节流
 * @author maqian
 * @version 1.0
 */
public class ByteBuffer {
	private static final String MSG_UNDER_FLOW = "no availabe bytes for read";
	
	private byte[] buffer;
	private int readerIndex;
	private int writerIndex;
	private int markReaderIndex;
	private int markWriterIndex;
	
	public ByteBuffer() {
		this(128);
	}
	
	public ByteBuffer(int size) {
		this.buffer = new byte[size];
	}
	
	public ByteBuffer(byte[] buffer) {
		this(buffer, 0, buffer.length);
	}
	
	public ByteBuffer(byte[] buffer, int offset, int len) {
		this.buffer = buffer;
		this.readerIndex = offset;
		this.writerIndex = offset+len;
	}
	
	private void checkWriteBounds(int len) {
		int availabe = availabe();
		if (availabe < len) {
			int infactAvailabe = availabe+readerIndex;
			int inc = buffer.length+((len-infactAvailabe)<<1);
			byte[] newbuf = new byte[buffer.length+inc];
			System.arraycopy(buffer, readerIndex, newbuf, 0, length());
			this.buffer = newbuf;
			this.readerIndex = 0;
			this.writerIndex = length();
		}
	}
	
	private void checkReadBounds(int len) {
		if (length() < len) {
			throw new RuntimeException(MSG_UNDER_FLOW);
		}
	}
	
	public void writeByteBuffer(ByteBuffer value, int len) {
		if (len > value.length()) {
			throw new RuntimeException("len exceed the value's length");
		}
		writeBytes(value.buffer, value.readerIndex, len);
		value.readerIndex += len;
	}
	
	public void writeByteBuffer(ByteBuffer value) {
		writeByteBuffer(value, value.length());
	}
	
	public void writeByte(byte value) {
		checkWriteBounds(1);
		setByte(value, writerIndex);
		writerIndex++;
	}
	
	public void setByte(byte value, int index) {
		buffer[index] = value;
	}
	
	public void writeBoolean(boolean value) {
		writeByte(value?(byte)1:(byte)0);
	}
	
	public void setBoolean(boolean value, int index) {
		setByte(value?(byte)1:(byte)0, index);
	}
	
	public void writeShort(short value) {
		checkWriteBounds(2);
		setShort(value, writerIndex);
		writerIndex += 2;
	}
	
	public void setShort(short value, int index) {
		BytesUtil.writeShort(buffer, index, value);
	}
	
	public void writeInt(int value) {
		checkWriteBounds(4);
		setInt(value, writerIndex);
		writerIndex += 4;
	}
	
	public void setInt(int value, int index) {
		BytesUtil.writeInt(buffer, index, value);
	}
	
	public void writeLong(long value) {
		checkWriteBounds(8);
		setLong(value, writerIndex);
		writerIndex += 8;
	}
	
	public void setLong(long value, int index) {
		BytesUtil.writeLong(buffer, index, value);
	}
	
	public void writeFloat(float value) {
		checkWriteBounds(4);
		setFloat(value, writerIndex);
		writerIndex += 4;
	}
	
	public void setFloat(float value, int index) {
		BytesUtil.writeFloat(buffer, index, value);
	}
	
	public void writeDouble(double value) {
		checkWriteBounds(8);
		setDouble(value, writerIndex);
		writerIndex += 8;
	}
	
	public void setDouble(double value, int index) {
		BytesUtil.writeDouble(buffer, index, value);
	}
	
	public void writeUTF(String value) {
		byte[] vbytes = ConvertUtil.toBytes(value, "utf-8");
		checkWriteBounds(2+vbytes.length);
		setShort((short)vbytes.length, writerIndex);
		setBytes(vbytes, writerIndex+2);
		writerIndex += 2+vbytes.length;
	}
	
	public void setUTF(String value, int index) {
		byte[] vbytes = ConvertUtil.toBytes(value, "utf-8");
		setShort((short)vbytes.length, index);
		setBytes(vbytes, index+2);
	}
	
	public void writeString(String value) {
		byte[] vbytes = ConvertUtil.toBytes(value);
		checkWriteBounds(2+vbytes.length);
		setShort((short)vbytes.length, writerIndex);
		setBytes(vbytes, writerIndex+2);
		writerIndex += 2+vbytes.length;
	}
	
	public void setString(String value, int index) {
		byte[] vbytes = ConvertUtil.toBytes(value);
		setShort((short)vbytes.length, index);
		setBytes(vbytes, index+2);
	}
	
	public void writeBytes(byte[] value, int offset, int len) {
		checkWriteBounds(len);
		setBytes(value, offset, len, writerIndex);
		writerIndex += len;
	}
	
	public void writeBytes(byte[] value) {
		writeBytes(value, 0, value.length);
	}

	public void setBytes(byte[] value, int offset, int len, int index) {
		System.arraycopy(value, offset, buffer, index, len);
	}
	
	public void setBytes(byte[] value, int index) {
		setBytes(value, 0, value.length, index);
	}
	
	public int slurp(InputStream is) throws IOException {
		int isLen = is.available();
		int readLen = 0;
		if (isLen > 0) {
			int expLen = availabe();
			if (expLen > isLen) {
				expLen = isLen;
			}
			if ((readLen = is.read(buffer, writerIndex, expLen)) > 0) {
				writerIndex += readLen;
			}
		}
		else {
			readLen = isLen;
		}
		return readLen;
	}
	
	public byte readByte() {
		checkReadBounds(1);
		byte value = getByte(readerIndex);
		readerIndex++;
		if (length() == 0) {
			readerIndex = 0;
			writerIndex = 0;
		}
		return value;
	}
	
	public byte getByte(int index) {
		return buffer[index];
	}
	
	public boolean readBoolean() {
		return readByte()==0?false:true;
	}
	
	public boolean getBoolean(int index) {
		return getByte(index)==0?false:true;
	}
	
	public short readShort() {
		checkReadBounds(2);
		short value = getShort(readerIndex);
		readerIndex += 2;
		if (length() == 0) {
			readerIndex = 0;
			writerIndex = 0;
		}
		return value;
	}
	
	public short getShort(int index) {
		return BytesUtil.readShort(buffer, index);
	}
	
	public int readInt() {
		checkReadBounds(4);
		int value = getInt(readerIndex);
		readerIndex += 4;
		if (length() == 0) {
			readerIndex = 0;
			writerIndex = 0;
		}
		return value;
	}
	
	public int getInt(int index) {
		return BytesUtil.readInt(buffer, index);
	}
	
	public long readLong() {
		checkReadBounds(8);
		long value = getLong(readerIndex);
		readerIndex += 8;
		if (length() == 0) {
			readerIndex = 0;
			writerIndex = 0;
		}
		return value;
	}
	
	public long getLong(int index) {
		return BytesUtil.readLong(buffer, index);
	}
	
	public float readFloat() {
		checkReadBounds(4);
		float value = getFloat(readerIndex);
		readerIndex += 4;
		if (length() == 0) {
			readerIndex = 0;
			writerIndex = 0;
		}
		return value;
	}
	
	public float getFloat(int index) {
		return BytesUtil.readFloat(buffer, index);
	}
	
	public double readDouble() {
		checkReadBounds(8);
		double value = getDouble(readerIndex);
		readerIndex += 8;
		if (length() == 0) {
			readerIndex = 0;
			writerIndex = 0;
		}
		return value;
	}
	
	public double getDouble(int index) {
		return BytesUtil.readDouble(buffer, index);
	}
	
	public String readUTF() {
		checkReadBounds(2);
		short len = getShort(readerIndex);
		checkReadBounds(2+len);
		String value = ConvertUtil.toString(getBytes(len, readerIndex+2), "utf-8");
		readerIndex += 2+len;
		if (length() == 0) {
			readerIndex = 0;
			writerIndex = 0;
		}
		return value;
	}
	
	public String getUTF(int index) {
		short len = getShort(index);
		return ConvertUtil.toString(getBytes(len, index+2), "utf-8");
	}
	
	public String readString() {
		checkReadBounds(2);
		short len = getShort(readerIndex);
		checkReadBounds(2+len);
		String value = ConvertUtil.toString(getBytes(len, readerIndex+2));
		readerIndex += 2+len;
		if (length() == 0) {
			readerIndex = 0;
			writerIndex = 0;
		}
		return value;
	}
	
	public String getString(int index) {
		short len = getShort(index);
		return ConvertUtil.toString(getBytes(len, index+2));
	}
	
	public byte[] readBytes(int len) {
		checkReadBounds(len);
		byte[] value = getBytes(len, readerIndex);
		readerIndex += len;
		if (length() == 0) {
			readerIndex = 0;
			writerIndex = 0;
		}
		return value;
	}
	
	public byte[] readAllBytes() {
		if (length() <= 0) {
			return null;
		}
		return readBytes(length());
	}
	
	public byte[] getBytes(int len, int index) {
		byte[] value = new byte[len];
		System.arraycopy(buffer, index, value, 0, len);
		return value;
	}
	
	public void spit(OutputStream os) throws IOException {
		if (length() > 0) {
			os.write(buffer, readerIndex, length());
			readerIndex = 0;
			writerIndex = 0;
		}
	}
	
	public void slide() {
		if (writerIndex > readerIndex && readerIndex != 0) {
			System.arraycopy(buffer, readerIndex, buffer, 0, length());
			writerIndex = length();
			readerIndex = 0;
		}
	}

    public void expand(int size) {
        expandTo(capacity()+size);
    }

    public void expandTo(int size) {
        if (size > capacity()) {
            byte[] buf = new byte[size];
            BytesUtil.writeBytes(buf, 0, buffer, readerIndex, length());
            buffer = buf;
            writerIndex = length();
            readerIndex = 0;
        }
    }
	
	public void skipWriter(int len) {
		checkWriteBounds(len);
		writerIndex += len;
	}
	
	public void skipReader(int len) {
		checkReadBounds(len);
		readerIndex += len;
	}
	
	public int readerIndex() {
		return readerIndex;
	}
	
	public int markReaderIndex() {
		return markReaderIndex;
	}
	
	public int markWriterIndex() {
		return markWriterIndex;
	}
	
	public void markReader() {
		this.markReaderIndex = readerIndex;
	}
	
	public void markWriter() {
		this.markWriterIndex = writerIndex;
	}
	
	public int writerIndex() {
		return writerIndex;
	}
	
	public int capacity() {
		return buffer.length;
	}
	
	public int length() {
		return writerIndex-readerIndex;
	}
	
	public int availabe() {
		return buffer.length-writerIndex;
	}
	
	public void clear() {
		readerIndex = 0;
		writerIndex = 0;
		markReaderIndex = 0;
		markWriterIndex = 0;
	}
	
	public boolean dataEquals(ByteBuffer other) {
		if (length() == other.length()) {
			for (int i = readerIndex; i < writerIndex; ++i) {
				if (buffer[readerIndex+i] != other.buffer[other.readerIndex+i]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
