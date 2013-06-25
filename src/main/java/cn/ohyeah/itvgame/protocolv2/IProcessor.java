package cn.ohyeah.itvgame.protocolv2;

import cn.ohyeah.stb.utils.ByteBuffer;

import java.io.IOException;

/**
 * 协议处理接口
 * @author maqian
 * @version 1.0
 */
public interface IProcessor {
	/**
	 * 协议请求处理
	 * @param context
	 * @throws IOException
	 */
	abstract public void processRequest(ProcessorContext context, ByteBuffer req);
	
	/**
	 * 协议响应处理
	 * @param context
	 * @throws IOException
	 */
	abstract public void processResponse(ProcessorContext context, ByteBuffer rsp);
}
