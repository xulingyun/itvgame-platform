package cn.ohyeah.itvgame.protocol;

/**
 * 协议头包装器
 * @author maqian
 * @version 1.0
 */
public class HeadWrapper implements IHeadAccessor {

	private int head;

    public HeadWrapper() {}

    public HeadWrapper(int head) {
        this.head = head;
    }
	
	public int getHead() {
		return head;
	}
	public void setHead(int head) {
		this.head = head;
	}

	public int getVersion() {
		return (head>>29)&0x07;
	}
	public void setVersion(int version) {
		head &= 0x1FFFFFFF;
		head |= version<<29;
	}
	
	public int getPadding() {
		return (head>>28)&0x01;
	}
	public void setPadding(int padding) {
		head &= 0xEFFFFFFF;
		head |= padding<<28;
	}
	
	public int getSplit() {
		return (head>>26)&0x03;
	}
	public void setSplit(int split) {
		head &= 0xF3FFFFFF;
		head |= split<<26;
	}
	
	public int getCrypt() {
		return (head>>25)&0x01;
	}
	public void setCrypt(int crypt) {
		head &= 0xFDFFFFFF;
		head |= crypt<<25;
	}
	
	public int getCompress() {
		return (head>>24)&0x01;
	}
	public void setCompress(int compress) {
		head &= 0XFEFFFFFF;
		head |= compress<<24;
	}
	
	public int getAck() {
		return (head>>23)&0x01;
	}
	public void setAck(int ack) {
		head &= 0XFF7FFFFF;
		head |= ack<<23;
	}
	
	public int getAckparam() {
		return (head>>22)&0x01;
	}
	public void setAckparam(int ackparam) {
		head &= 0XFFBFFFFF;
		head |= ackparam<<22;
	}
	
	public int getType() {
		return (head>>20)&0x03;
	}
	public void setType(int type) {
		head &= 0XFFCFFFFF;
		head |= type<<20;
	}
	
	public int getTag() {
		return (head>>16)&0x0f;
	}
	public void setTag(int tag) {
		head &= 0XFFF0FFFF;
		head |= tag<<16;
	}
	
	public int getCommand() {
		return (head>>8)&0xff;
	}
	public void setCommand(int command) {
		head &= 0XFFFF00FF;
		head |= command<<8;
	}
	
	public int getUserdata() {
		return head&0xff;
	}
	public void setUserdata(int userdata) {
		head &= 0XFFFFFF00;
		head |= userdata;
	}
	
	public static class Builder {
		private byte version;
		private byte padding;
		private byte split;
		private byte crypt;
		private byte compress;
		private byte ack;
		private byte ackparam;
		private byte type;
		private byte tag;
		private byte command;
		private byte userdata;
		
		public Builder version(int version) {
			this.version = (byte)version;
			return this;
		}
		
		public Builder padding(int padding) {
			this.padding = (byte)padding;
			return this;
		}
		
		public Builder split(int split) {
			this.split = (byte)split;
			return this;
		}
		
		public Builder crypt(int crypt) {
			this.crypt = (byte)crypt;
			return this;
		}
		
		public Builder compress(int compress) {
			this.compress = (byte)compress;
			return this;
		}
		
		public Builder ack(int ack) {
			this.ack = (byte)ack;
			return this;
		}
		
		public Builder ackparam(int ackparam) {
			this.ackparam = (byte)ackparam;
			return this;
		}
		
		public Builder type(int type) {
			this.type = (byte)type;
			return this;
		}
		
		public Builder tag(int tag) {
			this.tag = (byte)tag;
			return this;
		}
		
		public Builder command(int command) {
			this.command = (byte)command;
			return this;
		}
		
		public Builder userdata(int userdata) {
			this.userdata = (byte)userdata;
			return this;
		}
		
		public HeadWrapper build() {
			HeadWrapper head = new HeadWrapper();
			head.setVersion(version);
			head.setPadding(padding);
			head.setSplit(split);
			head.setCrypt(crypt);
			head.setCompress(compress);
			head.setAck(ack);
			head.setAckparam(ackparam);
			head.setType(type);
			head.setTag(tag);
			head.setCommand(command);
			head.setUserdata(userdata);
			return head;
		}
	}

}
