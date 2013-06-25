package cn.ohyeah.itvgame.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;

import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.global.BeanManager;
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
	
	static {
		purchaseServ = (PurchaseService)BeanManager.getBean("purchaseService");
		purchaseRevServ = (PurchaseRecordService)BeanManager.getBean("purchaseRecordService");
	}
	
	@Override
	public void processRequest(ProcessorContext context, DataInputStream dis) throws IOException {
		switch (context.getHeadWrapper().getCommand()) {
		case Constant.PURCHASE_CMD_PURCHASE_PROP: 
			processCommandPurchaseProp(context, dis);
			break;
		case Constant.PURCHASE_CMD_EXPEND: 
			processCommandPurchaseExpend(context, dis);
			break;
		case Constant.PURCHASE_CMD_QUERY_PURCHASE_RECORD: 
			processCommandQueryPurchaseRecord(context, dis);
			break;
		default: 
			String msg = "无效的协议命令, cmd="+context.getHeadWrapper().getCommand();
			context.setErrorCode(Constant.EC_INVALID_CMD);
			context.setMessage(msg);
			throw new RequestProcessException(msg);
		}
	}

	private void processCommandQueryPurchaseRecord(ProcessorContext context, DataInputStream dis) throws IOException {
		int accountId = dis.readInt();
		int productId = dis.readInt();
		int offset = dis.readInt();
		int length = dis.readInt();
		
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

	private void processCommandPurchaseExpend(ProcessorContext context, DataInputStream dis) throws IOException {
		String buyURL = dis.readUTF();
		context.setProp("buyURL", buyURL);
		int accountId = dis.readInt();
		String accountName = dis.readUTF();
		context.setProp("accountName", accountName);
		String userToken = dis.readUTF();
		context.setProp("userToken", userToken);
		int productId = dis.readInt();
		int amount = dis.readInt();
		String remark = dis.readUTF();
		String checkKey = dis.readUTF();
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

	private void processCommandPurchaseProp(ProcessorContext context, DataInputStream dis) throws IOException {
		String buyURL = dis.readUTF();
		context.setProp("buyURL", buyURL);
		int accountId = dis.readInt();
		String accountName = dis.readUTF();
		context.setProp("accountName", accountName);
		String userToken = dis.readUTF();
		context.setProp("userToken", userToken);
		int productId = dis.readInt();
		int propId = dis.readInt();
		int propCount = dis.readInt();
		String remark = dis.readUTF();
		String checkKey = dis.readUTF();
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
	public void processResponse(ProcessorContext context, DataOutputStream dos) throws IOException {
		switch (context.getHeadWrapper().getCommand()) {
		case Constant.PURCHASE_CMD_PURCHASE_PROP: 
			processCommandPurchaseProp(context, dos);
			break;
		case Constant.PURCHASE_CMD_EXPEND: 
			processCommandPurchaseExpend(context, dos);
			break;
		case Constant.PURCHASE_CMD_QUERY_PURCHASE_RECORD: 
			processCommandQueryPurchaseRecord(context, dos);
			break;
		default:break;
		}
	}

	@SuppressWarnings("unchecked")
	private void processCommandQueryPurchaseRecord(ProcessorContext context, DataOutputStream dos) throws IOException {
		dos.writeInt(context.getHeadWrapper().getHead());
		dos.writeInt(0);	/*result*/
		List<PurchaseDesc> descList = (List<PurchaseDesc>)context.getResult();
		if (descList!=null && descList.size()>0) {
			dos.writeShort(descList.size());
			for (PurchaseDesc desc : descList) {
				dos.writeInt(desc.getPropId());
				dos.writeInt(desc.getPropCount());
				dos.writeInt(desc.getAmount());
				dos.writeUTF(desc.getRemark());
				dos.writeUTF(DateFormatUtils.format(desc.getTime(), "yyyy/MM/dd HH:mm:ss"));
			}
		}
		else {
			dos.writeShort(0);
		}
	}

	private void processCommandPurchaseExpend(ProcessorContext context, DataOutputStream dos) throws IOException {
		dos.writeInt(context.getHeadWrapper().getHead());
		dos.writeInt(0);	/*result*/
		dos.writeInt((Integer)context.getResult());	/*balance*/
	}

	private void processCommandPurchaseProp(ProcessorContext context, DataOutputStream dos) throws IOException {
		dos.writeInt(context.getHeadWrapper().getHead());
		dos.writeInt(0);	/*result*/
		dos.writeInt((Integer)context.getResult());	/*balance*/
	}

}
