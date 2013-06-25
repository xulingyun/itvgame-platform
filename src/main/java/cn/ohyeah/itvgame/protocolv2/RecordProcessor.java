package cn.ohyeah.itvgame.protocolv2;

import java.util.List;

import cn.ohyeah.stb.utils.ByteBuffer;
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
	public void processRequest(ProcessorContext context, ByteBuffer req) {
		switch (context.getHeadWrapper().getCommand()) {
		case Constant.RECORD_CMD_SAVE: 
			processCommandSaveReq(context, req);
			break;
		case Constant.RECORD_CMD_READ: 
			processCommandReadReq(context, req);
			break;
		case Constant.RECORD_CMD_QUERY_DESC_LIST: 
			processCommandQueryDescListReq(context, req);
			break;
		case Constant.RECORD_CMD_UPDATE: 
			processCommandUpdateReq(context, req);
			break;
		default: 
			String msg = "无效的协议命令, cmd="+context.getHeadWrapper().getCommand();
			context.setErrorCode(Constant.EC_INVALID_CMD);
			context.setMessage(msg);
			throw new RequestProcessException(msg);
		}
	}

	private void processCommandUpdateReq(ProcessorContext context, ByteBuffer req) {
		GameRecord record = extractGameRecord(context, req);
		try {
			recordServ.saveOrUpdate(record, context.getProductId());
		}
		catch (ServiceException e) {
			context.setErrorCode(ErrorCode.EC_SERVICE_FAILED);
			context.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_SERVICE_FAILED));
			throw new RequestProcessException(e);
		}
	}

	private void processCommandQueryDescListReq(ProcessorContext context, ByteBuffer req) {
		int accountId = req.readInt();
		int productId = req.readInt();
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

	private void processCommandReadReq(ProcessorContext context, ByteBuffer req) {
		int accountId = req.readInt();
		int productId = req.readInt();
		int recordId = req.readInt();
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

	private void processCommandSaveReq(ProcessorContext context, ByteBuffer req) {
		GameRecord record = extractGameRecord(context, req);
		try {
			recordServ.saveOrUpdate(record, context.getProductId());
		}
		catch (ServiceException e) {
			context.setErrorCode(ErrorCode.EC_SERVICE_FAILED);
			context.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_SERVICE_FAILED));
			throw new RequestProcessException(e);
		}
	}
	
	private GameRecord extractGameRecord(ProcessorContext context, ByteBuffer req) {
		int accountId = req.readInt();
		int productId = req.readInt();
		context.setProductId(productId);
		int recordId = req.readInt();
		int playDuration = req.readInt();
		int scores = req.readInt();
		String remark = req.readUTF();
		int dataLen = req.readInt();
		byte[] data = null;
		if (dataLen > 0) {
            data = req.readBytes(dataLen);
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
	public void processResponse(ProcessorContext context, ByteBuffer rsp) {
		switch (context.getHeadWrapper().getCommand()) {
		case Constant.RECORD_CMD_SAVE: 
			break;
		case Constant.RECORD_CMD_READ: 
			processCommandReadRsp(context, rsp);
			break;
		case Constant.RECORD_CMD_QUERY_DESC_LIST: 
			processCommandQueryDescListRsp(context, rsp);
			break;
		case Constant.RECORD_CMD_UPDATE: 
			break;
		default: 
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private void processCommandQueryDescListRsp(ProcessorContext context, ByteBuffer rsp) {
		List<GameRecordDesc> descList = (List<GameRecordDesc>)context.getResult();
		if (descList!=null && descList.size()>0) {
			rsp.writeShort((short)descList.size());
			for (GameRecordDesc desc : descList) {
				rsp.writeInt(desc.getRecordId());
				rsp.writeInt(desc.getPlayDuration());
				rsp.writeInt(desc.getScores());
				rsp.writeUTF(desc.getRemark());
				rsp.writeUTF(DateFormatUtils.format(desc.getTime(), "yyyy/MM/dd HH:mm:ss"));
			}
		}
		else {
			rsp.writeShort((short)0);
		}
	}

	private void processCommandReadRsp(ProcessorContext context, ByteBuffer rsp) {
		GameRecord record = (GameRecord)context.getResult();
		if (record != null) {
            if (record.getData() != null) {
                rsp.expand(record.getData().length+128);
            }
			rsp.writeInt(record.getRecordId());
			rsp.writeInt(record.getPlayDuration());
			rsp.writeInt(record.getScores());
			rsp.writeUTF(record.getRemark());
			rsp.writeUTF(DateFormatUtils.format(record.getTime(), "yyyy/MM/dd HH:mm:ss"));
			byte[] data = record.getData();
			if (data!=null && data.length>0) {
				rsp.writeInt(data.length);
				rsp.writeBytes(data, 0, data.length);
			}
			else {
				rsp.writeInt(0);
			}
		}
	}

}
