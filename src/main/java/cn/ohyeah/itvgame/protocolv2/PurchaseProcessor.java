package cn.ohyeah.itvgame.protocolv2;

import java.util.List;

import cn.ohyeah.stb.utils.ByteBuffer;
import org.apache.commons.lang.time.DateFormatUtils;

import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.service.GamePropService;
import cn.ohyeah.itvgame.platform.service.PurchaseRecordService;
import cn.ohyeah.itvgame.platform.service.PurchaseService;
import cn.ohyeah.itvgame.platform.service.ServiceException;
import cn.ohyeah.itvgame.platform.viewmodel.PurchaseDesc;

/**
 * 计费协议处理器
 * @author maqian
 * @version 1.0
 */
public class PurchaseProcessor implements IProcessor {

	private static final PurchaseService purchaseServ;
	private static final PurchaseRecordService purchaseRevServ;
	private static final GamePropService propServ;
	
	static {
		purchaseServ = (PurchaseService)BeanManager.getBean("purchaseService");
		purchaseRevServ = (PurchaseRecordService)BeanManager.getBean("purchaseRecordService");
		propServ = (GamePropService)BeanManager.getBean("gamePropService");
	}
	
	@Override
	public void processRequest(ProcessorContext context, ByteBuffer req) {
		switch (context.getHeadWrapper().getCommand()) {
		case Constant.PURCHASE_CMD_PURCHASE_PROP: 
			processCommandPurchasePropReq(context, req);
			break;
		case Constant.PURCHASE_CMD_EXPEND: 
			processCommandPurchaseExpendReq(context, req);
			break;
		case Constant.PURCHASE_CMD_QUERY_PURCHASE_RECORD: 
			processCommandQueryPurchaseRecordReq(context, req);
			break;
		 case Constant.PURCHASE_CMD_EXPEND_DIJOY:
			 processCommandPurchaseDijoyReq(context, req);
            break;
		 case Constant.PURCHASE_CMD_EXPEND_WINSIDE_LACK:
			 processCommandPurchaseWinsideLackReq(context, req);
            break;
		 case Constant.PURCHASE_CMD_EXPEND_TELCOMSH:
			 processCommandPurchaseTelcomshReq(context, req);
			break;
		default: 
			String msg = "无效的协议命令, cmd="+context.getHeadWrapper().getCommand();
			context.setErrorCode(Constant.EC_INVALID_CMD);
			context.setMessage(msg);
			throw new RequestProcessException(msg);
		}
	}
	
	  private void processCommandPurchaseTelcomshReq(ProcessorContext context,ByteBuffer req) {
		  	String buyURL = req.readUTF();
	        context.setProp("buyURL", buyURL);
	        int accountId = req.readInt();
	        String accountName = req.readUTF();
	        context.setProp("accountName", accountName);
	        String userToken = req.readUTF();
	        context.setProp("userToken", userToken);
	        int productId = req.readInt();
	        int propId = req.readInt();
	        int payType = req.readInt();
	        context.setProp("payType", payType);
	        String remark = req.readUTF();
	        context.setProp("remark", remark);
	        String gameid = req.readUTF();
	        context.setProp("gameid", gameid);
	        //String spid = req.readUTF();
	        //context.setProp("spid", spid);
	        //String payKey = req.readUTF();
	        //context.setProp("checkKey", payKey);
	        try {
	        	//ResultInfo info = purchaseServ.expend(context.getPropsMap(), accountId, productId, amount, remark);
	            ResultInfo info = purchaseServ.purchaseProp(context.getPropsMap(), accountId, productId, propId, 1, remark);
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

	private void processCommandPurchaseWinsideLackReq(ProcessorContext context,
			ByteBuffer req) {
		    String buyURL = req.readUTF();
	        context.setProp("buyURL", buyURL);
	        int accountId = req.readInt();
	        String accountName = req.readUTF();
	        context.setProp("accountName", accountName);
	        String userToken = req.readUTF();
	        context.setProp("userToken", userToken);
	        int productId = req.readInt();
	        int propId = req.readInt();
	        //context.setProp("propId", propId);
	        int amount = req.readInt();
	        context.setProp("amount", amount);
	        int payType = req.readInt();
	        context.setProp("payType", payType);
	        String remark = req.readUTF();
	        String payKey = req.readUTF();
	        context.setProp("checkKey", payKey);
		  try {
	            String password = req.readUTF();
	            context.setProp("password", password);
	        }
	        catch (Exception e) {
	            context.setProp("password", "");
	        }
	        try {
	        	//ResultInfo info = purchaseServ.expend(context.getPropsMap(), accountId, productId, amount, remark);
	            ResultInfo info = purchaseServ.purchaseProp(context.getPropsMap(), accountId, productId, propId, 1, remark);
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

	private void processCommandPurchaseDijoyReq(ProcessorContext context, ByteBuffer req) {
	        String buyURL = req.readUTF();
	        context.setProp("buyURL", buyURL);
	        int accountId = req.readInt();
	        String accountName = req.readUTF();
	        context.setProp("accountName", accountName);
	        String userToken = req.readUTF();
	        context.setProp("userToken", userToken);
	        int productId = req.readInt();
	        int amount = req.readInt();
	        context.setProp("amount", amount);
	        int propId = req.readInt();
	        //context.setProp("propId", propId);
	        int payType = req.readInt();
	        context.setProp("payType", payType);
	        String remark = req.readUTF();
	        String appId = req.readUTF();
	        context.setProp("appId", appId);
	        String payKey = req.readUTF();
	        context.setProp("checkKey", payKey);
	        String platformExt = req.readUTF();
	        context.setProp("platformExt", platformExt);
	        int feeCode = propServ.read(propId).getFeeCode();
	        context.setProp("feeCode", feeCode);
	        try {
	            String password = req.readUTF();
	            context.setProp("password", password);
	        }
	        catch (Exception e) {
	            context.setProp("password", "");
	        }
	        try {
	        	//ResultInfo info = purchaseServ.expend(context.getPropsMap(), accountId, productId, amount, remark);
	            ResultInfo info = purchaseServ.purchaseProp(context.getPropsMap(), accountId, productId, propId, 1, remark);
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


	private void processCommandQueryPurchaseRecordReq(ProcessorContext context, ByteBuffer req) {
		int accountId = req.readInt();
		int productId = req.readInt();
		int offset = req.readInt();
		int length = req.readInt();
		
		try {
			List<PurchaseDesc> descList = purchaseRevServ.queryPurchaseList(accountId, productId, offset, length);
			context.setResult(descList);
		}
		catch (ServiceException e) {
			context.setErrorCode(ErrorCode.EC_SERVICE_FAILED);
			context.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_SERVICE_FAILED));
			throw new RequestProcessException(e);
		}
	}

	private void processCommandPurchaseExpendReq(ProcessorContext context, ByteBuffer req) {
		String buyURL = req.readUTF();
		context.setProp("buyURL", buyURL);
		int accountId = req.readInt();
		String accountName = req.readUTF();
		context.setProp("accountName", accountName);
		String userToken = req.readUTF();
		context.setProp("userToken", userToken);
		int productId = req.readInt();
		int amount = req.readInt();
		String remark = req.readUTF();
		String checkKey = req.readUTF();
		context.setProp("checkKey", checkKey);
		try {
			ResultInfo info = purchaseServ.expend(context.getPropsMap(), accountId, productId, amount, remark);
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

	private void processCommandPurchasePropReq(ProcessorContext context, ByteBuffer req) {
		String buyURL = req.readUTF();
		context.setProp("buyURL", buyURL);
		int accountId = req.readInt();
		String accountName = req.readUTF();
		context.setProp("accountName", accountName);
		String userToken = req.readUTF();
		context.setProp("userToken", userToken);
		int productId = req.readInt();
		int propId = req.readInt();
		int propCount = req.readInt();
		String remark = req.readUTF();
		String checkKey = req.readUTF();
		context.setProp("checkKey", checkKey);
		try {
			ResultInfo info = purchaseServ.purchaseProp(context.getPropsMap(), accountId, productId, propId, propCount, remark);
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

	@Override
	public void processResponse(ProcessorContext context, ByteBuffer rsp) {
		switch (context.getHeadWrapper().getCommand()) {
		case Constant.PURCHASE_CMD_PURCHASE_PROP: 
			processCommandPurchasePropRsp(context, rsp);
			break;
		case Constant.PURCHASE_CMD_EXPEND: 
			processCommandPurchaseExpendRsp(context, rsp);
			break;
		case Constant.PURCHASE_CMD_QUERY_PURCHASE_RECORD: 
			processCommandQueryPurchaseRecordRsp(context, rsp);
			break;
		case Constant.PURCHASE_CMD_EXPEND_DIJOY:
			processCommandPurchaseDijoy(context, rsp);
			break;
		case Constant.PURCHASE_CMD_EXPEND_WINSIDE_LACK:
			processCommandPurchaseWinsideLack(context, rsp);
			break;
		case Constant.PURCHASE_CMD_EXPEND_TELCOMSH:
			processCommandPurchaseTelcomshRsp(context, rsp);
			break;
		default:break;
		}
	}

	private void processCommandPurchaseTelcomshRsp(ProcessorContext context,
			ByteBuffer rsp) {
		rsp.writeInt((Integer) context.getResult());
	}

	@SuppressWarnings("unchecked")
	private void processCommandQueryPurchaseRecordRsp(ProcessorContext context, ByteBuffer rsp) {
		List<PurchaseDesc> descList = (List<PurchaseDesc>)context.getResult();
		if (descList!=null && descList.size()>0) {
			rsp.writeShort((short)descList.size());
			for (PurchaseDesc desc : descList) {
				rsp.writeInt(desc.getPropId());
				rsp.writeInt(desc.getPropCount());
				rsp.writeInt(desc.getAmount());
				rsp.writeUTF(desc.getRemark());
				rsp.writeUTF(DateFormatUtils.format(desc.getTime(), "yyyy/MM/dd HH:mm:ss"));
			}
		}
		else {
			rsp.writeShort((short)0);
		}
	}

	private void processCommandPurchaseDijoy(ProcessorContext context,	ByteBuffer rsp) {
		rsp.writeInt((Integer) context.getResult());
	}
	
	private void processCommandPurchaseWinsideLack(ProcessorContext context,
			ByteBuffer rsp) {
		rsp.writeInt((Integer) context.getResult());
	}

	private void processCommandPurchaseExpendRsp(ProcessorContext context, ByteBuffer rsp) {
		rsp.writeInt((Integer) context.getResult());	/*balance*/
	}

	private void processCommandPurchasePropRsp(ProcessorContext context, ByteBuffer rsp) {
		rsp.writeInt((Integer) context.getResult());	/*balance*/
	}

}
