package cn.ohyeah.itvgame.protocolv2;

import java.util.List;

import cn.ohyeah.stb.utils.ByteBuffer;
import org.apache.commons.lang.time.DateFormatUtils;

import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.model.GameAttainment;
import cn.ohyeah.itvgame.platform.service.GameAttainmentService;
import cn.ohyeah.itvgame.platform.service.ServiceException;
import cn.ohyeah.itvgame.platform.viewmodel.GameAttainmentDesc;
import cn.ohyeah.itvgame.platform.viewmodel.GameRanking;

/**
 * 游戏成就协议处理器
 * @author maqian
 * @version 1.0
 */
public class AttainmentProcessor implements IProcessor {
	private static final GameAttainmentService attainmentServ;
	
	static {
		attainmentServ = (GameAttainmentService)BeanManager.getBean("gameAttainmentService");
	}
	
	@Override
	public void processRequest(ProcessorContext context, ByteBuffer req) {
		switch (context.getHeadWrapper().getCommand()) {
		case Constant.ATTAINMENT_CMD_SAVE: 
			processCommandSaveReq(context, req);
			break;
		case Constant.ATTAINMENT_CMD_READ: 
			processCommandReadReq(context, req);
			break;
		case Constant.ATTAINMENT_CMD_UPDATE: 
			processCommandUpdateReq(context, req);
			break;
		case Constant.ATTAINMENT_CMD_QUERY_DESC_LIST: 
			processCommandQueryDescListReq(context, req);
			break;
		case Constant.ATTAINMENT_CMD_QUERY_RANKING_LIST: 
			processCommandQueryRankingListReq(context, req);
			break;
		default: 
			String msg = "无效的协议命令, cmd="+context.getHeadWrapper().getCommand();
			context.setErrorCode(Constant.EC_INVALID_CMD);
			context.setMessage(msg);
			throw new RequestProcessException(msg);
		}
	}

	private void processCommandQueryRankingListReq(ProcessorContext context, ByteBuffer req) {
		int productId = req.readInt();
		String orderCmd = req.readUTF();
		long startMillis = req.readLong();
		long endMillis = req.readLong();
		int offset = req.readInt();
		int length = req.readInt();
		try {
			List<GameRanking> rankList = null;
			if (startMillis > 0 && endMillis > 0) {
				java.util.Date start = new java.util.Date(startMillis);
				java.util.Date end = new java.util.Date(endMillis);
				rankList = attainmentServ.queryRankingList(productId, orderCmd, start, end, offset, length);
			}
			else {
				rankList = attainmentServ.queryRankingList(productId, orderCmd, null, null, offset, length);
			}
			context.setResult(rankList);
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
		String orderCmd = req.readUTF();
		long startMillis = req.readLong();
		long endMillis = req.readLong();
		try {
			List<GameAttainmentDesc> descList = null;
			if (startMillis > 0 && endMillis > 0) {
				java.util.Date start = new java.util.Date(startMillis);
				java.util.Date end = new java.util.Date(endMillis);
				descList = attainmentServ.queryAttainmentDescList(accountId, productId, orderCmd, start, end);
			}
			else {
				descList = attainmentServ.queryAttainmentDescList(accountId, productId, orderCmd, null, null);
			}
			context.setResult(descList);
		}
		catch (ServiceException e) {
			context.setErrorCode(ErrorCode.EC_SERVICE_FAILED);
			context.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_SERVICE_FAILED));
			throw new RequestProcessException(e);
		}
	}

	private void processCommandUpdateReq(ProcessorContext context, ByteBuffer req) {
		GameAttainment attainment = extractGameAttainment(context, req);
		try {
			attainmentServ.saveOrUpdate(attainment, context.getProductId());
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
		int attainmentId = req.readInt();
		String orderCmd = req.readUTF();
		long startMillis = req.readLong();
		long endMillis = req.readLong();
		
		try {
			GameAttainment attainment = null;
			if (startMillis > 0 && endMillis > 0) {
				java.util.Date start = new java.util.Date(startMillis);
				java.util.Date end = new java.util.Date(endMillis);
				attainment = attainmentServ.read(accountId, productId, attainmentId, orderCmd, start, end);
			}
			else {
				attainment = attainmentServ.read(accountId, productId, attainmentId, orderCmd, null, null);
			}
			context.setResult(attainment);
		}
		catch (ServiceException e) {
			context.setErrorCode(ErrorCode.EC_SERVICE_FAILED);
			context.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_SERVICE_FAILED));
			throw new RequestProcessException(e);
		}
	}

	private void processCommandSaveReq(ProcessorContext context, ByteBuffer req) {
		GameAttainment attainment = extractGameAttainment(context, req);
		try {
			attainmentServ.saveOrUpdate(attainment, context.getProductId());
		}
		catch (ServiceException e) {
			context.setErrorCode(ErrorCode.EC_SERVICE_FAILED);
			context.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_SERVICE_FAILED));
			throw new RequestProcessException(e);
		}
	}
	
	private GameAttainment extractGameAttainment(ProcessorContext context, ByteBuffer req) {
		int accountId = req.readInt();
		String userId = req.readUTF();
		int productId = req.readInt();
		context.setProductId(productId);
		int attainmentId = req.readInt();
		int playDuration = req.readInt();
		int scores = req.readInt();
		String remark = req.readUTF();
		int dataLen = req.readInt();
		byte[] data = null;
		if (dataLen > 0) {
            data = req.readBytes(dataLen);
		}
		
		GameAttainment attainment = new GameAttainment();
		attainment.setAccountId(accountId);
		attainment.setUserId(userId);
		attainment.setAttainmentId(attainmentId);
		attainment.setPlayDuration(playDuration);
		attainment.setScores(scores);
		attainment.setRemark(remark);
		attainment.setTime(new java.util.Date());
		attainment.setData(data);
		
		return attainment;
	}
 
	@Override
	public void processResponse(ProcessorContext context, ByteBuffer rsp) {
		switch (context.getHeadWrapper().getCommand()) {
		case Constant.ATTAINMENT_CMD_SAVE: 
			break;
		case Constant.ATTAINMENT_CMD_READ: 
			processCommandReadRsp(context, rsp);
			break;
		case Constant.ATTAINMENT_CMD_UPDATE: 
			break;
		case Constant.ATTAINMENT_CMD_QUERY_DESC_LIST: 
			processCommandQueryDescListRsp(context, rsp);
			break;
		case Constant.ATTAINMENT_CMD_QUERY_RANKING_LIST: 
			processCommandQueryRankingListRsp(context, rsp);
			break;
		default: 
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private void processCommandQueryRankingListRsp(ProcessorContext context, ByteBuffer rsp) {
		List<GameRanking> rankingList = (List<GameRanking>)context.getResult();
		if (rankingList!=null && rankingList.size()>0) {
			rsp.writeShort((short)rankingList.size());
			for (GameRanking ranking : rankingList) {
				rsp.writeInt(ranking.getAccountId());
				rsp.writeUTF(ranking.getUserId());
				rsp.writeInt(ranking.getPlayDuration());
				rsp.writeInt(ranking.getScores());
				rsp.writeInt(ranking.getRanking());
				rsp.writeUTF(ranking.getRemark());
				rsp.writeUTF(DateFormatUtils.format(ranking.getTime(), "yyyy/MM/dd HH:mm:ss"));
			}
		}
		else {
			rsp.writeShort((short)0);
		}
	}

	@SuppressWarnings("unchecked")
	private void processCommandQueryDescListRsp(ProcessorContext context, ByteBuffer rsp) {
		List<GameAttainmentDesc> descList = (List<GameAttainmentDesc>)context.getResult();
		if (descList!=null && descList.size()>0) {
			rsp.writeShort((short)descList.size());
			for (GameAttainmentDesc desc : descList) {
				rsp.writeInt(desc.getAttainmentId());
				rsp.writeInt(desc.getPlayDuration());
				rsp.writeInt(desc.getScores());
				rsp.writeInt((int) desc.getRanking());
				rsp.writeUTF(desc.getRemark());
				rsp.writeUTF(DateFormatUtils.format(desc.getTime(), "yyyy/MM/dd HH:mm:ss"));
			}
		}
		else {
			rsp.writeShort((short)0);
		}
	}

	private void processCommandReadRsp(ProcessorContext context, ByteBuffer rsp) {
		GameAttainment attainment = (GameAttainment)context.getResult();
		if (attainment != null) {
            if (attainment.getData() != null) {
                rsp.expand(attainment.getData().length+128);
            }
			rsp.writeInt(attainment.getAttainmentId());
			rsp.writeInt(attainment.getPlayDuration());
			rsp.writeInt(attainment.getScores());
			rsp.writeInt((int) attainment.getRanking());	/*ranking*/
			rsp.writeUTF(attainment.getRemark());
			rsp.writeUTF(DateFormatUtils.format(attainment.getTime(), "yyyy/MM/dd HH:mm:ss"));
			byte[] data = attainment.getData();
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
