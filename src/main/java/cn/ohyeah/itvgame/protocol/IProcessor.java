package cn.ohyeah.itvgame.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Э�鴦��ӿ�
 * @author maqian
 * @version 1.0
 */
public interface IProcessor {
	/**
	 * Э��������
	 * @param dis
	 * @throws IOException
	 */
	abstract public void processRequest(ProcessorContext context, DataInputStream dis) throws IOException;
	
	/**
	 * Э����Ӧ����
	 * @param dos
	 * @throws IOException
	 */
	abstract public void processResponse(ProcessorContext context, DataOutputStream dos) throws IOException;
}
