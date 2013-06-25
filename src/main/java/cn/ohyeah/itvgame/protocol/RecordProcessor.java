package cn.ohyeah.itvgame.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;

import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.model.GameRecord;
import cn.ohyeah.itvgame.platform.service.ServiceException;
import cn.ohyeah.itvgame.platform.service.GameRecordService;
import cn.ohyeah.itvgame.platform.viewmodel.GameRecordDesc;

/**
 * 游戏记录协议处理器
 * @author maqian
 * @version 1.0
 */
public class RecordProcessor implements IProcessor {
	private static final GameRecordService recordServ;
	
	static {
		recordServ = (GameRecordService)BeanManager.getBean("gameRecordService");
	}

	@Override
	public void processRequest(ProcessorContext context, DataInputStream dis) throws IOException {
		switch (context.getHeadWrapper().getCommand()) {
		case Constant.RECORD_CMD_SAVE: 
			processCommandSave(context, dis);
			break;
		case Constant.RECORD_CMD_READ: 
			processCommandRead(context, dis);
			break;
		case Constant.RECORD_CMD_QUERY_DESC_LIST: 
			processCommandQueryDescList(context, dis);
			break;
		case Constant.RECORD_CMD_UPDATE: 
			processCommandUpdate(context, dis);
			break;
		default: 
			String msg = "无效的协议命令, cmd="+context.getHeadWrapper().getCommand();
			context.setErrorCode(Constant.EC_INVALID_CMD);
			context.setMessage(msg);
			throw new RequestProcessException(msg);
		}
	}

	private void processCommandUpdate(ProcessorContext context, DataInputStream dis) throws IOException {
		GameRecord record = extractGameRecord(context, dis);
		try {
			recordServ.saveOrUpdate(record, context.getProductId());
		}
		catch (ServiceException e) {
			context.setErrorCode(ErrorCode.EC_SERVICE_FAILED);
			context.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_SERVICE_FAILED));
			throw new RequestProcessException(e);
		}
	}

	private void processCommandQueryDescList(ProcessorContext context, DataInputStream dis) throws IOException {
		int accountId = dis.readInt();
		int productId = dis.readInt();
		try {
			List<GameRecordDesc> descList = recordServ.queryRecordDescList(accountId, productId);
			context.setResult(descList);
		}
		catch (ServiceException e) {
			context.setErrorCode(ErrorCode.EC_SERVICE_FAILED);
			context.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_SERVICE_FAILED));
			throw new RequestProcessException(e);
		}
	}

	private void processCommandRead(ProcessorContext context, DataInputStream dis) throws IOException {
		int accountId = dis.readInt();
		int productId = dis.readInt();
		int recordId = dis.readInt();
		try {
			GameRecord record = recordServ.read(accountId, productId, recordId);
			context.setResult(record);
		}
		catch (ServiceException e) {
			context.setErrorCode(ErrorCode.EC_SERVICE_FAILED);
			context.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_SERVICE_FAILED));
			throw new RequestProcessException(e);
		}
	}

	private void processCommandSave(ProcessorContext context, DataInputStream dis) throws IOException {
		GameRecord record = extractGameRecord(context, dis);
		try {
			recordServ.saveOrUpdate(record, context.getProductId());
		}
		catch (ServiceException e) {
			context.setErrorCode(ErrorCode.EC_SERVICE_FAILED);
			context.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_SERVICE_FAILED));
			throw new RequestProcessException(e);
		}
	}
	
	private GameRecord extractGameRecord(ProcessorContext context, DataInputStream dis) throws IOException {
		int accountId = dis.readInt();
		int productId = dis.readInt();
		context.setProductId(productId);
		int recordId = dis.readInt();
		int playDuration = dis.readInt();
		int scores = dis.readInt();
		String remark = dis.readUTF();
		int dataLen = dis.readInt();
		byte[] data = null;
		if (dataLen > 0) {
			data = new byte[dataLen];
			int readLen = 0;
			int curReadLen = 0;
			while (readLen < data.length) {
				curReadLen = dis.read(data, readLen, data.length-readLen);
				if (curReadLen > 0) {
					readLen += curReadLen;
				}
			}
		}
		
		GameRecord record = new GameRecord();
		record.setAccountId(accountId);
		record.setRecordId(recordId);
		record.setPlayDuration(playDuration);
		record.setScores(scores);
		record.setRemark(remark);
		record.setTime(new java.util.Date());
		record.setData(data);
		
		return record;
	}
	
	@Override
	public void processResponse(ProcessorContext context, DataOutputStream dos) throws ServiceException, IOException {
		switch (context.getHeadWrapper().getCommand()) {
		case Constant.RECORD_CMD_SAVE: 
			processCommandSave(context, dos);
			break;
		case Constant.RECORD_CMD_READ: 
			processCommandRead(context, dos);
			break;
		case Constant.RECORD_CMD_QUERY_DESC_LIST: 
			processCommandQueryDescList(context, dos);
			break;
		case Constant.RECORD_CMD_UPDATE: 
			processCommandUpdate(context, dos);
			break;
		default: 
			break;
		}
	}

	private void processCommandUpdate(ProcessorContext context, DataOutputStream dos) throws IOException {
		dos.writeInt(context.getHeadWrapper().getHead());
		dos.writeInt(0);	/*result*/
	}

	private void processCommandSave(ProcessorContext context, DataOutputStream dos) throws IOException {
		dos.writeInt(context.getHeadWrapper().getHead());
		dos.writeInt(0);	/*result*/
	}

	@SuppressWarnings("unchecked")
	private void processCommandQueryDescList(ProcessorContext context, DataOutputStream dos) throws IOException {
		dos.writeInt(context.getHeadWrapper().getHead());
		dos.writeInt(0);	/*result*/
		List<GameRecordDesc> descList = (List<GameRecordDesc>)context.getResult();
		if (descList!=null && descList.size()>0) {
			dos.writeShort(descList.size());
			for (GameRecordDesc desc : descList) {
				dos.writeInt(desc.getRecordId());
				dos.writeInt(desc.getPlayDuration());
				dos.writeInt(desc.getScores());
				dos.writeUTF(desc.getRemark());
				dos.writeUTF(DateFormatUtils.format(desc.getTime(), "yyyy/MM/dd HH:mm:ss"));
			}
		}
		else {
			dos.writeShort(0);
		}
	}

	private void processCommandRead(ProcessorContext context, DataOutputStream dos) throws IOException {
		dos.writeInt(context.getHeadWrapper().getHead());
		GameRecord record = (GameRecord)context.getResult();
		if (record != null) {
			dos.writeInt(0);
			dos.writeInt(record.getRecordId());
			dos.writeInt(record.getPlayDuration());
			dos.writeInt(record.getScores());
			dos.writeUTF(record.getRemark());
			dos.writeUTF(DateFormatUtils.format(record.getTime(), "yyyy/MM/dd HH:mm:ss"));
			byte[] data = record.getData();
			if (data!=null && data.length>0) {
				dos.writeInt(data.length);
				dos.write(data, 0, data.length);
			}
			else {
				dos.writeInt(0);
			}
		}
		else {
			dos.writeInt(Constant.EC_RECORD_NOT_EXIST);
			dos.writeUTF("游戏记录不存在");
		}
	}

}
