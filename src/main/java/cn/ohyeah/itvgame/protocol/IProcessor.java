package cn.ohyeah.itvgame.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 协议处理接口
 * @author maqian
 * @version 1.0
 */
public interface IProcessor {
	/**
	 * 协议请求处理
	 * @param dis
	 * @throws IOException
	 */
	abstract public void processRequest(ProcessorContext context, DataInputStream dis) throws IOException;
	
	/**
	 * 协议响应处理
	 * @param dos
	 * @throws IOException
	 */
	abstract public void processResponse(ProcessorContext context, DataOutputStream dos) throws IOException;
}
