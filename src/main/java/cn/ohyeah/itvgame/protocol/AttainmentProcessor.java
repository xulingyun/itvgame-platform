package cn.ohyeah.itvgame.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

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
	public void processRequest(ProcessorContext context, DataInputStream dis) throws IOException {
		switch (context.getHeadWrapper().getCommand()) {
		case Constant.ATTAINMENT_CMD_SAVE: 
			processCommandSave(context, dis);
			break;
		case Constant.ATTAINMENT_CMD_READ: 
			processCommandRead(context, dis);
			break;
		case Constant.ATTAINMENT_CMD_UPDATE: 
			processCommandUpdate(context, dis);
			break;
		case Constant.ATTAINMENT_CMD_QUERY_DESC_LIST: 
			processCommandQueryDescList(context, dis);
			break;
		case Constant.ATTAINMENT_CMD_QUERY_RANKING_LIST: 
			processCommandQueryRankingList(context, dis);
			break;
		default: 
			String msg = "无效的协议命令, cmd="+context.getHeadWrapper().getCommand();
			context.setErrorCode(Constant.EC_INVALID_CMD);
			context.setMessage(msg);
			throw new RequestProcessException(msg);
		}
	}

	private void processCommandQueryRankingList(ProcessorContext context, DataInputStream dis) throws IOException {
		int productId = dis.readInt();
		String orderCmd = dis.readUTF();
		long startMillis = dis.readLong();
		long endMillis = dis.readLong();
		int offset = dis.readInt();
		int length = dis.readInt();
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

	private void processCommandQueryDescList(ProcessorContext context, DataInputStream dis) throws IOException {
		int accountId = dis.readInt();
		int productId = dis.readInt();
		String orderCmd = dis.readUTF();
		long startMillis = dis.readLong();
		long endMillis = dis.readLong();
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

	private void processCommandUpdate(ProcessorContext context, DataInputStream dis) throws IOException {
		GameAttainment attainment = extractGameAttainment(context, dis);
		try {
			attainmentServ.saveOrUpdate(attainment, context.getProductId());
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
		int attainmentId = dis.readInt();
		String orderCmd = dis.readUTF();
		long startMillis = dis.readLong();
		long endMillis = dis.readLong();
		
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

	private void processCommandSave(ProcessorContext context, DataInputStream dis) throws IOException {
		GameAttainment attainment = extractGameAttainment(context, dis);
		try {
			attainmentServ.saveOrUpdate(attainment, context.getProductId());
		}
		catch (ServiceException e) {
			context.setErrorCode(ErrorCode.EC_SERVICE_FAILED);
			context.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_SERVICE_FAILED));
			throw new RequestProcessException(e);
		}
	}
	
	private GameAttainment extractGameAttainment(ProcessorContext context, DataInputStream dis) throws IOException {
		int accountId = dis.readInt();
		String userId = dis.readUTF();
		int productId = dis.readInt();
		context.setProductId(productId);
		int attainmentId = dis.readInt();
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
	public void processResponse(ProcessorContext context, DataOutputStream dos) throws IOException {
		switch (context.getHeadWrapper().getCommand()) {
		case Constant.ATTAINMENT_CMD_SAVE: 
			processCommandSave(context, dos);
			break;
		case Constant.ATTAINMENT_CMD_READ: 
			processCommandRead(context, dos);
			break;
		case Constant.ATTAINMENT_CMD_UPDATE: 
			processCommandUpdate(context, dos);
			break;
		case Constant.ATTAINMENT_CMD_QUERY_DESC_LIST: 
			processCommandQueryDescList(context, dos);
			break;
		case Constant.ATTAINMENT_CMD_QUERY_RANKING_LIST: 
			processCommandQueryRankingList(context, dos);
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
	private void processCommandQueryRankingList(ProcessorContext context, DataOutputStream dos) throws IOException {
		dos.writeInt(context.getHeadWrapper().getHead());
		dos.writeInt(0);	/*result*/
		List<GameRanking> rankingList = (List<GameRanking>)context.getResult();
		if (rankingList!=null && rankingList.size()>0) {
			dos.writeShort(rankingList.size());
			for (GameRanking ranking : rankingList) {
				dos.writeInt(ranking.getAccountId());
				dos.writeUTF(ranking.getUserId());
				dos.writeInt(ranking.getPlayDuration());
				dos.writeInt(ranking.getScores());
				dos.writeInt(ranking.getRanking());
				dos.writeUTF(ranking.getRemark());
				dos.writeUTF(DateFormatUtils.format(ranking.getTime(), "yyyy/MM/dd HH:mm:ss"));
			}
		}
		else {
			dos.writeShort(0);
		}
	}

	@SuppressWarnings("unchecked")
	private void processCommandQueryDescList(ProcessorContext context, DataOutputStream dos) throws IOException {
		dos.writeInt(context.getHeadWrapper().getHead());
		dos.writeInt(0);	/*result*/
		List<GameAttainmentDesc> descList = (List<GameAttainmentDesc>)context.getResult();
		if (descList!=null && descList.size()>0) {
			dos.writeShort(descList.size());
			for (GameAttainmentDesc desc : descList) {
				dos.writeInt(desc.getAttainmentId());
				dos.writeInt(desc.getPlayDuration());
				dos.writeInt(desc.getScores());
				dos.writeInt((int)desc.getRanking());
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
		GameAttainment attainment = (GameAttainment)context.getResult();
		if (attainment != null) {
			dos.writeInt(0);	/*result*/
			dos.writeInt(attainment.getAttainmentId());
			dos.writeInt(attainment.getPlayDuration());
			dos.writeInt(attainment.getScores());
			dos.writeInt((int)attainment.getRanking());	/*ranking*/
			dos.writeUTF(attainment.getRemark());
			dos.writeUTF(DateFormatUtils.format(attainment.getTime(), "yyyy/MM/dd HH:mm:ss"));
			byte[] data = attainment.getData();
			if (data!=null && data.length>0) {
				dos.writeInt(data.length);
				dos.write(data, 0, data.length);
			}
			else {
				dos.writeInt(0);
			}
		}
		else {
			dos.writeInt(Constant.EC_ATTAINMENT_NOT_EXIST);	/*result*/
			dos.writeUTF("游戏成就不存在");
		}
	}

}
