package cn.ohyeah.itvgame.protocol;

/**
 * 协议头访问接口
 * @author maqian
 * @version 1.0
 */
public interface IHeadAccessor {

	/**
	 * 协议头数据
	 * @return
	 */
	public abstract int getHead();
	public abstract void setHead(int head);
	
	/**
	 * 协议版本号
	 * @return
	 */
	public abstract int getVersion();
	public abstract void setVersion(int version);
	
	/**
	 * 对原数据或者加密前的数据进行填充（AES加密是16字节对齐）。
	 * 0:无填充
	 * 1:16字节填充
	 * @return
	 */
	public abstract int getPadding();
	public abstract void setPadding(int padding);
	
	/**
	 * 表明本数据报所加载的数据是否经过分块发送。
	 * 0:单数据报，未经分块
	 * 1:分块，第一个数据报
	 * 2:分块，其它数据报
	 * 3:未定义
	 * @return
	 */
	public abstract int getSplit();
	public abstract void setSplit(int split);
	
	/**
	 * 标识本数据报是否经过加密。加密方法由系统配置指定。数据报所使用的加密方法由协议本身确定。
	 * 0:非加密
	 * 1:加密
	 * @return
	 */
	public abstract int getCrypt();
	public abstract void setCrypt(int crypt);
	
	/**
	 * 表示本数据报是否经过压缩
	 * 0:表示未压缩
	 * 1:表示压缩
	 * @return
	 */
	public abstract int getCompress();
	public abstract void setCompress(int compress);
	
	/**
	 * 1表示接收到此数据时应该返回应答(UDP有效)
	 * @return
	 */
	public abstract int getAck();
	public abstract void setAck(int ack);
	
	/**
	 * 1表示协议要求返回参数仅为应答包
	 * @return
	 */
	public abstract int getAckparam();
	public abstract void setAckparam(int ackParam);
	
	/**
	 * 当传输会话数据时表示会话类型
	 * 而当传输数据为媒体数据时表示数据帧类型
	 * @return
	 */
	public abstract int getType();
	public abstract void setType(int type);
	
	/**
	 * 控制协议标识
	 * @return
	 */
	public abstract int getTag();
	public abstract void setTag(int tag);
	
	/**
	 * 控制协议命令
	 * @return
	 */
	public abstract int getCommand();
	public abstract void setCommand(int command);
	
	
	/**
	 * 用户数据
	 * @return
	 */
	public abstract int getUserdata();
	public abstract void setUserdata(int userData);

}