package cn.ohyeah.itvgame.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;

import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.service.RechargeService;
import cn.ohyeah.itvgame.platform.service.ServiceException;
import cn.ohyeah.itvgame.platform.service.SubscribeRecordService;
import cn.ohyeah.itvgame.platform.service.SubscribeService;
import cn.ohyeah.itvgame.platform.viewmodel.SubscribeDesc;

public class SubscribeProcessor implements IProcessor {

	private static final RechargeService rechargeServ;
	private static final SubscribeService	subServ;
	private static final SubscribeRecordService subRevServ;
	
	static {
		rechargeServ = (RechargeService)BeanManager.getBean("rechargeService");
		subServ = (SubscribeService)BeanManager.getBean("subscribeService");
		subRevServ = (SubscribeRecordService)BeanManager.getBean("subscribeRecordService");
	}
	
	@Override
	public void processRequest(ProcessorContext context, DataInputStream dis) throws IOException {

		switch (context.getHeadWrapper().getCommand()) {
		case Constant.SUBSCRIBE_CMD_SUBSCRIBE: 
			processCommandSubscribe(context, dis);
			break;
		case Constant.SUBSCRIBE_CMD_RECHARGE: 
			processCommandRecharge(context, dis);
			break;
		case Constant.SUBSCRIBE_CMD_QUERY_BALANCE:
			processCommandQueryBalance(context, dis);
			break;
		case Constant.SUBSCRIBE_CMD_QUERY_SUBSCRIBE_RECORD:
			processCommandQuerySubscribeRecord(context, dis);
			break;
		case Constant.SUBSCRIBE_CMD_SUBSCRIBE_PRODUCT:
			processCommandSubscribeProduct(context, dis);
			break;
		case Constant.SUBSCRIBE_CMD_RECHARGE_WINSIDEGD:
			processCommandRechargeWinsidegd(context, dis);
			break;
		case Constant.SUBSCRIBE_CMD_RECHARGE_DIJOY:
			processCommandRechargeDijoy(context, dis);
			break;
		default: 
			String msg = "无效的协议命令, cmd="+context.getHeadWrapper().getCommand();
			context.setErrorCode(Constant.EC_INVALID_CMD);
			context.setMessage(msg);
			throw new RequestProcessException(msg);
		}
	}

	private void processCommandRechargeWinsidegd(ProcessorContext context,
			DataInputStream dis) throws IOException {
		String buyURL = dis.readUTF();
		context.setProp("buyURL", buyURL);
		int accountId = dis.readInt();
		String accountName = dis.readUTF();
		context.setProp("accountName", accountName);
		String userToken = dis.readUTF();
		context.setProp("userToken", userToken);
		int productId = dis.readInt();
		int amount = dis.readInt();
		int ratio = dis.readInt();
		context.setProp("ratio", ratio);
		int payType = dis.readInt();
		context.setProp("payType", payType);
		String remark = dis.readUTF();
		String checkKey = dis.readUTF();
		context.setProp("checkKey", checkKey);
		String spid = dis.readUTF();
		context.setProp("spid", spid);
		String gameid = dis.readUTF();
		context.setProp("gameid", gameid);
		String enterURL = dis.readUTF();
		context.setProp("enterURL", enterURL);
		context.setProp("zyUserToken", userToken);
		String stbType = dis.readUTF();
		context.setProp("stbType", stbType);
		try {
			String password = dis.readUTF();
			context.setProp("password", password);
		}
		catch (IOException e) {
			context.setProp("password", "");
		}
		try {
			ResultInfo info = rechargeServ.recharge(context.getPropsMap(), accountId, productId, 
					amount, remark, new java.util.Date());
			if (info.isSuccess()) {
				context.setResult((Integer)info.getInfo());
			}
			else {
				context.setErrorCode(info.getErrorCode());
				context.setMessage(info.getMessage());
			}
		}
		catch (ServiceException e) {
			context.setErrorCode(ErrorCode.EC_SERVICE_FAILED);
			context.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_SERVICE_FAILED));
			throw new RequestProcessException(e);
		}
	}
	
	private void processCommandRechargeDijoy(ProcessorContext context,
			DataInputStream dis) throws IOException {
		String buyURL = dis.readUTF();
		context.setProp("buyURL", buyURL);
		int accountId = dis.readInt();
		String accountName = dis.readUTF();
		context.setProp("accountName", accountName);
		String userToken = dis.readUTF();
		context.setProp("userToken", userToken);
		int productId = dis.readInt();
		int amount = dis.readInt();
		int payType = dis.readInt();
		context.setProp("payType", payType);
		String remark = dis.readUTF();
		String checkKey = dis.readUTF();
		context.setProp("checkKey", checkKey);
		String appId = dis.readUTF();
		context.setProp("appId", appId);
		String platformExt = dis.readUTF();
		context.setProp("platformExt", platformExt);
		String appExt = dis.readUTF();
		context.setProp("appExt", appExt);
        try {
            String password = dis.readUTF();
            context.setProp("password", password);
        }
        catch (IOException e) {
            context.setProp("password", "");
        }
		try {
			ResultInfo info = rechargeServ.recharge(context.getPropsMap(), accountId, productId, 
					amount, remark, new java.util.Date());
			if (info.isSuccess()) {
				context.setResult((Integer)info.getInfo());
			}
			else {
				context.setErrorCode(info.getErrorCode());
				context.setMessage(info.getMessage());
			}
		}
		catch (ServiceException e) {
			context.setErrorCode(ErrorCode.EC_SERVICE_FAILED);
			context.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_SERVICE_FAILED));
			throw new RequestProcessException(e);
		}
	}

	private void processCommandSubscribeProduct(ProcessorContext context,
			DataInputStream dis) throws IOException {
		String buyURL = dis.readUTF();
		context.setProp("buyURL", buyURL);
		int accountId = dis.readInt();
		String accountName = dis.readUTF();
		context.setProp("accountName", accountName);
		String userToken = dis.readUTF();
		context.setProp("userToken", userToken);
		int productId = dis.readInt();
		String subscribeType = dis.readUTF();
		int payType = dis.readInt();
		context.setProp("payType", payType);
		String remark = dis.readUTF();
		String checkKey = dis.readUTF();
		context.setProp("checkKey", checkKey);
		try {
			ResultInfo info = subServ.subscribeProduct(context.getPropsMap(), 
					accountId, productId, subscribeType, remark);
			if (!info.isSuccess()) {
				context.setErrorCode(info.getErrorCode());
				context.setMessage(info.getMessage());
			}
		}
		catch (ServiceException e) {
			context.setErrorCode(ErrorCode.EC_SERVICE_FAILED);
			context.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_SERVICE_FAILED));
			throw new RequestProcessException(e);
		}
		
	}

	private void processCommandQueryBalance(ProcessorContext context, DataInputStream dis) throws IOException {
		String buyURL = dis.readUTF();
		context.setProp("buyURL", buyURL);
		int accountId = dis.readInt();
		String accountName = dis.readUTF();
		context.setProp("accountName", accountName);
		int productId = dis.readInt();
		try {
			ResultInfo info = rechargeServ.queryBanlance(context.getPropsMap(), accountId, productId);
			if (info.isSuccess()) {
				context.setResult((Integer)info.getInfo());
			}
			else {
				context.setErrorCode(info.getErrorCode());
				context.setMessage(info.getMessage());
			}
			
		}
		catch (ServiceException e) {
			context.setErrorCode(ErrorCode.EC_SERVICE_FAILED);
			context.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_SERVICE_FAILED));
			throw new RequestProcessException(e);
		}
	}

	private void processCommandRecharge(ProcessorContext context, DataInputStream dis) throws IOException {
		String buyURL = dis.readUTF();
		context.setProp("buyURL", buyURL);
		int accountId = dis.readInt();
		String accountName = dis.readUTF();
		context.setProp("accountName", accountName);
		String userToken = dis.readUTF();
		context.setProp("userToken", userToken);
		int productId = dis.readInt();
		int amount = dis.readInt();
		int ratio = dis.readInt();
		context.setProp("ratio", ratio);
		int payType = dis.readInt();
		context.setProp("payType", payType);
		String remark = dis.readUTF();
		String checkKey = dis.readUTF();
		context.setProp("checkKey", checkKey);
		String spid = dis.readUTF();
		context.setProp("spid", spid);
		try {
			String password = dis.readUTF();
			context.setProp("password", password);
		}
		catch (IOException e) {
			context.setProp("password", "");
		}
		try {
			ResultInfo info = rechargeServ.recharge(context.getPropsMap(), accountId, productId, 
					amount, remark, new java.util.Date());
			if (info.isSuccess()) {
				context.setResult((Integer)info.getInfo());
			}
			else {
				context.setErrorCode(info.getErrorCode());
				context.setMessage(info.getMessage());
			}
		}
		catch (ServiceException e) {
			context.setErrorCode(ErrorCode.EC_SERVICE_FAILED);
			context.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_SERVICE_FAILED));
			throw new RequestProcessException(e);
		}
	}

	private void processCommandSubscribe(ProcessorContext context, DataInputStream dis) throws IOException {
		String buyURL = dis.readUTF();
		context.setProp("buyURL", buyURL);
		int accountId = dis.readInt();
		String accountName = dis.readUTF();
		context.setProp("accountName", accountName);
		String userToken = dis.readUTF();
		context.setProp("userToken", userToken);
		int productId = dis.readInt();
		int purchaseId = dis.readInt();
		int payType = dis.readInt();
		context.setProp("payType", payType);
		String remark = dis.readUTF();
		String checkKey = dis.readUTF();
		context.setProp("checkKey", checkKey);
		try {
			ResultInfo info = subServ.subscribe(context.getPropsMap(), accountId, productId, 
					purchaseId, remark, new java.util.Date());
			if (!info.isSuccess()) {
				context.setErrorCode(info.getErrorCode());
				context.setMessage(info.getMessage());
			}
		}
		catch (ServiceException e) {
			context.setErrorCode(ErrorCode.EC_SERVICE_FAILED);
			context.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_SERVICE_FAILED));
			throw new RequestProcessException(e);
		}
	}

	private void processCommandQuerySubscribeRecord(ProcessorContext context, DataInputStream dis) throws IOException {
		String userId = dis.readUTF();
		int productId = dis.readInt();
		int offset = dis.readInt();
		int length = dis.readInt();
		try {
			List<SubscribeDesc> descList = subRevServ.querySubscribeDescList(userId, productId, offset, length);
			context.setResult(descList);
		}
		catch (ServiceException e) {
			context.setErrorCode(ErrorCode.EC_SERVICE_FAILED);
			context.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_SERVICE_FAILED));
			throw new RequestProcessException(e);
		}
	}

	@Override
	public void processResponse(ProcessorContext context, DataOutputStream dos) throws IOException {
		switch (context.getHeadWrapper().getCommand()) {
		case Constant.SUBSCRIBE_CMD_SUBSCRIBE: 
			processCommandSubscribe(context, dos);
			break;
		case Constant.SUBSCRIBE_CMD_RECHARGE: 
			processCommandRecharge(context, dos);
			break;
		case Constant.SUBSCRIBE_CMD_QUERY_BALANCE:
			processCommandQueryBalance(context, dos);
			break;
		case Constant.SUBSCRIBE_CMD_QUERY_SUBSCRIBE_RECORD:
			processCommandQuerySubscribeRecord(context, dos);
			break;
		case Constant.SUBSCRIBE_CMD_SUBSCRIBE_PRODUCT:
			processCommandSubscribeProduct(context, dos);
			break;
		case Constant.SUBSCRIBE_CMD_RECHARGE_WINSIDEGD:
			processCommandRechargeWinsidegd(context, dos);
			break;
		case Constant.SUBSCRIBE_CMD_RECHARGE_DIJOY:
			processCommandRechargeDijoy(context, dos);
			break;
		default: break;
		}
	}

	private void processCommandRechargeDijoy(ProcessorContext context,
			DataOutputStream dos) throws IOException {
		dos.writeInt(context.getHeadWrapper().getHead());
		dos.writeInt(0);	/*result*/
		dos.writeInt((Integer)context.getResult());
	}
	
	private void processCommandRechargeWinsidegd(ProcessorContext context,
			DataOutputStream dos) throws IOException {
		dos.writeInt(context.getHeadWrapper().getHead());
		dos.writeInt(0);	/*result*/
		dos.writeInt((Integer)context.getResult());
	}

	private void processCommandSubscribeProduct(ProcessorContext context,
			DataOutputStream dos) throws IOException {
		dos.writeInt(context.getHeadWrapper().getHead());
		dos.writeInt(0);	/*result*/
	}

	@SuppressWarnings("unchecked")
	private void processCommandQuerySubscribeRecord(ProcessorContext context, DataOutputStream dos) throws IOException {
		dos.writeInt(context.getHeadWrapper().getHead());
		dos.writeInt(0);	/*result*/
		List<SubscribeDesc> descList = (List<SubscribeDesc>)context.getResult();
		if (descList!=null && descList.size()>0) {
			dos.writeShort(descList.size());
			for (SubscribeDesc desc : descList) {
				dos.writeInt(desc.getAmount());
				dos.writeUTF(desc.getRemark());
				dos.writeUTF(DateFormatUtils.format(desc.getTime(), "yyyy/MM/dd HH:mm:ss"));
			}
		}
		else {
			dos.writeShort(0);
		}
	}

	private void processCommandQueryBalance(ProcessorContext context, DataOutputStream dos) throws IOException {
		dos.writeInt(context.getHeadWrapper().getHead());
		dos.writeInt(0);	/*result*/
		dos.writeInt((Integer)context.getResult());	/*balance*/
	}

	private void processCommandRecharge(ProcessorContext context, DataOutputStream dos) throws IOException {
		dos.writeInt(context.getHeadWrapper().getHead());
		dos.writeInt(0);	/*result*/
		dos.writeInt((Integer)context.getResult());
	}

	private void processCommandSubscribe(ProcessorContext context, DataOutputStream dos) throws IOException {
		dos.writeInt(context.getHeadWrapper().getHead());
		dos.writeInt(0);	/*result*/
	}

}
