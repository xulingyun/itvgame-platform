package cn.ohyeah.itvgame.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.model.GameProp;
import cn.ohyeah.itvgame.platform.service.GamePropService;
import cn.ohyeah.itvgame.platform.service.ServiceException;
import cn.ohyeah.itvgame.platform.viewmodel.OwnPropDesc;

/**
 * 道具协议处理器
 * @author maqian
 * @version 1.0
 */
public class PropProcessor implements IProcessor {
	private static final GamePropService propServ;
	
	static {
		propServ = (GamePropService)BeanManager.getBean("gamePropService");
	}
	
	@Override
	public void processRequest(ProcessorContext context, DataInputStream dis) throws IOException {
		switch (context.getHeadWrapper().getCommand()) {
		case Constant.PROP_CMD_QUERY_PROP_LIST: 
			processCommandQueryPropList(context, dis);
			break;
		case Constant.PROP_CMD_QUERY_OWN_PROP_LIST: 
			processCommandQueryOwnPropList(context, dis);
			break;
		case Constant.PROP_CMD_USE_PROPS:
			processCommandUseProps(context, dis);
			break;
		case Constant.PROP_CMD_SYN_PROPS:
			processCommandSynProps(context, dis);
			break;
		default: 
			String msg = "无效的协议命令, cmd="+context.getHeadWrapper().getCommand();
			context.setErrorCode(Constant.EC_INVALID_CMD);
			context.setMessage(msg);
			throw new RequestProcessException(msg);
		}
	}

	private void processCommandSynProps(ProcessorContext context,
			DataInputStream dis) throws IOException {
		int accountId = dis.readInt();
		int productId = dis.readInt();
		short num = dis.readShort();
		int[] propIds = new int[num];
		int[] counts = new int[num];
		for (int i = 0; i < num; ++i) {
			propIds[i] = dis.readInt();
			counts[i] = dis.readInt();
		}
		try {
			propServ.synProps(accountId, productId, propIds, counts);
		}
		catch (ServiceException e) {
			context.setErrorCode(ErrorCode.EC_SERVICE_FAILED);
			context.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_SERVICE_FAILED));
			throw new RequestProcessException(e);
		}
	}

	private void processCommandUseProps(ProcessorContext context, DataInputStream dis) throws IOException {
		int accountId = dis.readInt();
		int productId = dis.readInt();
		short num = dis.readShort();
		int[] propIds = new int[num];
		int[] nums = new int[num];
		for (int i = 0; i < num; ++i) {
			propIds[i] = dis.readInt();
			nums[i] = dis.readInt();
		}
		try {
			propServ.useProps(accountId, productId, propIds, nums);
		}
		catch (ServiceException e) {
			context.setErrorCode(ErrorCode.EC_SERVICE_FAILED);
			context.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_SERVICE_FAILED));
			throw new RequestProcessException(e);
		}
	}

	private void processCommandQueryOwnPropList(ProcessorContext context, DataInputStream dis) throws IOException {
		int accountId = dis.readInt();
		int productId = dis.readInt();
		try {
			List<OwnPropDesc> ownPropList = propServ.queryOwnPropList(accountId, productId);
			context.setResult(ownPropList);
		}
		catch (ServiceException e) {
			context.setErrorCode(ErrorCode.EC_SERVICE_FAILED);
			context.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_SERVICE_FAILED));
			throw new RequestProcessException(e);
		}
	}

	private void processCommandQueryPropList(ProcessorContext context, DataInputStream dis) throws IOException {
		int productId = dis.readInt();
		try {
			List<GameProp> propList = propServ.queryPropList(productId);
			context.setResult(propList);
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
		case Constant.PROP_CMD_QUERY_PROP_LIST: 
			processCommandQueryPropList(context, dos);
			break;
		case Constant.PROP_CMD_QUERY_OWN_PROP_LIST: 
			processCommandQueryOwnPropList(context, dos);
			break;
		case Constant.PROP_CMD_USE_PROPS:
			processCommandUseProps(context, dos);
			break;
		case Constant.PROP_CMD_SYN_PROPS:
			processCommandSynProps(context, dos);
		default: 
			break;
		}
	}

	private void processCommandSynProps(ProcessorContext context, DataOutputStream dos) throws IOException {
		dos.writeInt(context.getHeadWrapper().getHead());
		dos.writeInt(0);	/*result*/
	}

	private void processCommandUseProps(ProcessorContext context, DataOutputStream dos) throws IOException {
		dos.writeInt(context.getHeadWrapper().getHead());
		dos.writeInt(0);	/*result*/
	}

	@SuppressWarnings("unchecked")
	private void processCommandQueryOwnPropList(ProcessorContext context, DataOutputStream dos) throws IOException {
		dos.writeInt(context.getHeadWrapper().getHead());
		dos.writeInt(0);	/*result*/
		List<OwnPropDesc> ownPropList = (List<OwnPropDesc>)context.getResult();
		if (ownPropList!=null && ownPropList.size()>0) {
			dos.writeShort(ownPropList.size());
			for (OwnPropDesc desc : ownPropList) {
				dos.writeInt(desc.getPropId());
				dos.writeInt(desc.getCount());
			}
		}
		else {
			dos.writeInt(0);
		}
	}

	@SuppressWarnings("unchecked")
	private void processCommandQueryPropList(ProcessorContext context, DataOutputStream dos) throws IOException {
		dos.writeInt(context.getHeadWrapper().getHead());
		dos.writeInt(0);	/*result*/
		List<GameProp> propList = (List<GameProp>)context.getResult();
		if (propList!=null && propList.size()>0) {
			dos.writeShort(propList.size());
			for (GameProp prop : propList) {
				dos.writeInt(prop.getPropId());
				dos.writeUTF(prop.getPropName());
				dos.writeInt(prop.getPrice());
				dos.writeUTF(prop.getDescription());
			}
		}
		else {
			dos.writeInt(0);
		}
	}

}
