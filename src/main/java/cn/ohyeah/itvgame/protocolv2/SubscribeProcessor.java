package cn.ohyeah.itvgame.protocolv2;

import java.util.List;

import cn.ohyeah.stb.utils.ByteBuffer;
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
	public void processRequest(ProcessorContext context, ByteBuffer req) {

		switch (context.getHeadWrapper().getCommand()) {
		case Constant.SUBSCRIBE_CMD_SUBSCRIBE: 
			processCommandSubscribeReq(context, req);
			break;
		case Constant.SUBSCRIBE_CMD_RECHARGE: 
			processCommandRechargeReq(context, req);
			break;
		case Constant.SUBSCRIBE_CMD_QUERY_BALANCE:
			processCommandQueryBalanceReq(context, req);
			break;
		case Constant.SUBSCRIBE_CMD_QUERY_SUBSCRIBE_RECORD:
			processCommandQuerySubscribeRecordReq(context, req);
			break;
		case Constant.SUBSCRIBE_CMD_SUBSCRIBE_PRODUCT:
			processCommandSubscribeProductReq(context, req);
			break;
		case Constant.SUBSCRIBE_CMD_RECHARGE_WINSIDEGD:
			processCommandRechargeWinsidegdReq(context, req);
			break;
		case Constant.SUBSCRIBE_CMD_RECHARGE_SHENGYI:
			processCommandRechargeShengYiReq(context, req);
			break;
		case Constant.SUBSCRIBE_CMD_SUBSCRIBE_PRODUCT_SHENGYI:
			processCommandSubscribeProductShengyiReq(context, req);
			break;
		case Constant.SUBSCRIBE_CMD_RECHARGE_SHIXIAN:
			processCommandSubscribeProductShixianReq(context, req);
			break;
		default: 
			String msg = "无效的协议命令, cmd="+context.getHeadWrapper().getCommand();
			context.setErrorCode(Constant.EC_INVALID_CMD);
			context.setMessage(msg);
			throw new RequestProcessException(msg);
		}
	}

  
    private void processCommandSubscribeProductShixianReq(
			ProcessorContext context, ByteBuffer req) {
    	String buyURL = req.readUTF();
		context.setProp("buyURL", buyURL);
		int accountId = req.readInt();
		String accountName = req.readUTF();
		context.setProp("accountName", accountName);
		String userToken = req.readUTF();
		context.setProp("userToken", userToken);
		int productId = req.readInt();
		int amount = req.readInt();
		int ratio = req.readInt();
		context.setProp("ratio", ratio);
		int payType = req.readInt();
		context.setProp("payType", payType);
		String remark = req.readUTF();
		String checkKey = req.readUTF();
		context.setProp("checkKey", checkKey);
		String feeaccount = req.readUTF();
		context.setProp("feeaccount", feeaccount);
		String returnurl = req.readUTF();
		context.setProp("returnurl", returnurl);
		String dwjvl = req.readUTF();
		context.setProp("dwjvl", dwjvl);
		String opcomkey = req.readUTF();
		context.setProp("opcomkey", opcomkey);
		String paysubway = req.readUTF();
		context.setProp("paysubway", paysubway);
		String gameid = req.readUTF();
		context.setProp("gameid", gameid);
		String user_group_id = req.readUTF();
		context.setProp("user_group_id", user_group_id);
		String appId = req.readUTF();
		context.setProp("appId", appId);
		try {
			String password = req.readUTF();
			context.setProp("password", password);
		}
		catch (Exception e) {
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


	private void processCommandSubscribeProductShengyiReq(	ProcessorContext context, ByteBuffer req) {
		int accountId = req.readInt();
		String accountName = req.readUTF();
		context.setProp("accountName", accountName);
		String userToken = req.readUTF();
		context.setProp("userToken", userToken);
		int productId = req.readInt();
		String subscribeType = req.readUTF();
		int payType = req.readInt();
		context.setProp("payType", payType);
		String remark = req.readUTF();
		String shengyiCPID = req.readUTF();
		context.setProp("shengyiCPID", shengyiCPID);
		String shengyiCPPassWord = req.readUTF();
		context.setProp("shengyiCPPassWord", shengyiCPPassWord);
		String shengyiUserIdType = req.readUTF();
		context.setProp("shengyiUserIdType", shengyiUserIdType);
		String shengyiProductId = req.readUTF();
		context.setProp("shengyiProductId", shengyiProductId);
		context.setProp("subscribe", "subscribeProduct");
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


	private void processCommandRechargeShengYiReq(ProcessorContext context,
			ByteBuffer req) {
    	String buyURL = req.readUTF();
		context.setProp("buyURL", buyURL);
		int accountId = req.readInt();
		String accountName = req.readUTF();
		context.setProp("accountName", accountName);
		String userToken = req.readUTF();
		context.setProp("userToken", userToken);
		int productId = req.readInt();
		int amount = req.readInt();
		int ratio = req.readInt();
		context.setProp("ratio", ratio);
		int payType = req.readInt();
		context.setProp("payType", payType);
		String remark = req.readUTF();
		String checkKey = req.readUTF();
		context.setProp("checkKey", checkKey);
		String shengyiCPID = req.readUTF();
		context.setProp("shengyiCPID", shengyiCPID);
		String shengyiCPPassWord = req.readUTF();
		context.setProp("shengyiCPPassWord", shengyiCPPassWord);
		String shengyiUserIdType = req.readUTF();
		context.setProp("shengyiUserIdType", shengyiUserIdType);
		String shengyiProductId = req.readUTF();
		context.setProp("shengyiProductId", shengyiProductId);
		try {
			String password = req.readUTF();
			context.setProp("password", password);
		}
		catch (Exception e) {
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


	private void processCommandRechargeWinsidegdReq(ProcessorContext context,
			ByteBuffer req) {
		String buyURL = req.readUTF();
		context.setProp("buyURL", buyURL);
		int accountId = req.readInt();
		String accountName = req.readUTF();
		context.setProp("accountName", accountName);
		String userToken = req.readUTF();
		context.setProp("userToken", userToken);
		int productId = req.readInt();
		int amount = req.readInt();
		int ratio = req.readInt();
		context.setProp("ratio", ratio);
		int payType = req.readInt();
		context.setProp("payType", payType);
		String remark = req.readUTF();
		String checkKey = req.readUTF();
		context.setProp("checkKey", checkKey);
		String spid = req.readUTF();
		context.setProp("spid", spid);
		String gameid = req.readUTF();
		context.setProp("gameid", gameid);
		String enterURL = req.readUTF();
		context.setProp("enterURL", enterURL);
		context.setProp("zyUserToken", userToken);
		String stbType = req.readUTF();
		context.setProp("stbType", stbType);
		try {
			String password = req.readUTF();
			context.setProp("password", password);
		}
		catch (Exception e) {
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

	private void processCommandSubscribeProductReq(ProcessorContext context, ByteBuffer req) {
		String buyURL = req.readUTF();
		context.setProp("buyURL", buyURL);
		int accountId = req.readInt();
		String accountName = req.readUTF();
		context.setProp("accountName", accountName);
		String userToken = req.readUTF();
		context.setProp("userToken", userToken);
		int productId = req.readInt();
		String subscribeType = req.readUTF();
		int payType = req.readInt();
		context.setProp("payType", payType);
		String remark = req.readUTF();
		String checkKey = req.readUTF();
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

	private void processCommandQueryBalanceReq(ProcessorContext context, ByteBuffer req) {
		String buyURL = req.readUTF();
		context.setProp("buyURL", buyURL);
		int accountId = req.readInt();
		String accountName = req.readUTF();
		context.setProp("accountName", accountName);
		int productId = req.readInt();
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

	private void processCommandRechargeReq(ProcessorContext context, ByteBuffer req) {
		String buyURL = req.readUTF();
		context.setProp("buyURL", buyURL);
		int accountId = req.readInt();
		String accountName = req.readUTF();
		context.setProp("accountName", accountName);
		String userToken = req.readUTF();
		context.setProp("userToken", userToken);
		int productId = req.readInt();
		int amount = req.readInt();
		int ratio = req.readInt();
		context.setProp("ratio", ratio);
		int payType = req.readInt();
		context.setProp("payType", payType);
		String remark = req.readUTF();
		String checkKey = req.readUTF();
		context.setProp("checkKey", checkKey);
		String spid = req.readUTF();
		context.setProp("spid", spid);
		try {
			String password = req.readUTF();
			context.setProp("password", password);
		}
		catch (Exception e) {
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

	private void processCommandSubscribeReq(ProcessorContext context, ByteBuffer req) {
        context.setErrorCode(ErrorCode.EC_SERVICE_FAILED);
        context.setMessage("不支持此操作");
	}

	private void processCommandQuerySubscribeRecordReq(ProcessorContext context, ByteBuffer req) {
		String userId = req.readUTF();
		int productId = req.readInt();
		int offset = req.readInt();
		int length = req.readInt();
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
	public void processResponse(ProcessorContext context, ByteBuffer rsp) {
		switch (context.getHeadWrapper().getCommand()) {
		case Constant.SUBSCRIBE_CMD_SUBSCRIBE: 
			break;
		case Constant.SUBSCRIBE_CMD_RECHARGE: 
			processCommandRechargeRsp(context, rsp);
			break;
		case Constant.SUBSCRIBE_CMD_QUERY_BALANCE:
			processCommandQueryBalanceRsp(context, rsp);
			break;
		case Constant.SUBSCRIBE_CMD_QUERY_SUBSCRIBE_RECORD:
			processCommandQuerySubscribeRecordRsp(context, rsp);
			break;
		case Constant.SUBSCRIBE_CMD_SUBSCRIBE_PRODUCT:
			break;
		case Constant.SUBSCRIBE_CMD_RECHARGE_WINSIDEGD:
			processCommandRechargeWinsidegdRsp(context, rsp);
			break;
		case Constant.SUBSCRIBE_CMD_RECHARGE_SHENGYI:
			processCommandRechargeShengYiRsp(context, rsp);
			break;
		case Constant.SUBSCRIBE_CMD_RECHARGE_SHIXIAN:
			processCommandRechargeShixianRsp(context, rsp);
			break;
		default: break;
		}
	}

	private void processCommandRechargeShixianRsp(ProcessorContext context,
			ByteBuffer rsp) {
		rsp.writeInt((Integer) context.getResult());
	}


	private void processCommandRechargeShengYiRsp(ProcessorContext context,
			ByteBuffer rsp) {
		rsp.writeInt((Integer) context.getResult());
	}


	private void processCommandRechargeWinsidegdRsp(ProcessorContext context,
			ByteBuffer rsp) {
		rsp.writeInt((Integer) context.getResult());
	}

	@SuppressWarnings("unchecked")
	private void processCommandQuerySubscribeRecordRsp(ProcessorContext context, ByteBuffer rsp) {
		List<SubscribeDesc> descList = (List<SubscribeDesc>)context.getResult();
		if (descList!=null && descList.size()>0) {
			rsp.writeShort((short)descList.size());
			for (SubscribeDesc desc : descList) {
				rsp.writeInt(desc.getAmount());
				rsp.writeUTF(desc.getRemark());
				rsp.writeUTF(DateFormatUtils.format(desc.getTime(), "yyyy/MM/dd HH:mm:ss"));
			}
		}
		else {
			rsp.writeShort((short)0);
		}
	}

	private void processCommandQueryBalanceRsp(ProcessorContext context, ByteBuffer rsp) {
		rsp.writeInt((Integer) context.getResult());	/*balance*/
	}

	private void processCommandRechargeRsp(ProcessorContext context, ByteBuffer rsp) {
		rsp.writeInt((Integer) context.getResult());
	}


}
