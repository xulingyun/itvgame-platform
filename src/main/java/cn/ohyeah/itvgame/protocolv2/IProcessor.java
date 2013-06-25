package cn.ohyeah.itvgame.protocolv2;

import cn.ohyeah.stb.utils.ByteBuffer;

import java.io.IOException;

/**
 * Э�鴦��ӿ�
 * @author maqian
 * @version 1.0
 */
public interface IProcessor {
	/**
	 * Э��������
	 * @param context
	 * @throws IOException
	 */
	abstract public void processRequest(ProcessorContext context, ByteBuffer req);
	
	/**
	 * Э����Ӧ����
	 * @param context
	 * @throws IOException
	 */
	abstract public void processResponse(ProcessorContext context, ByteBuffer rsp);
}
