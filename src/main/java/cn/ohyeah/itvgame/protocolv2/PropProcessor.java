package cn.ohyeah.itvgame.protocolv2;

import java.util.List;

import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.model.GameProp;
import cn.ohyeah.itvgame.platform.service.GamePropService;
import cn.ohyeah.itvgame.platform.service.ServiceException;
import cn.ohyeah.itvgame.platform.viewmodel.OwnPropDesc;
import cn.ohyeah.stb.utils.ByteBuffer;

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
	public void processRequest(ProcessorContext context, ByteBuffer req) {
		switch (context.getHeadWrapper().getCommand()) {
		case Constant.PROP_CMD_QUERY_PROP_LIST: 
			processCommandQueryPropListReq(context, req);
			break;
		case Constant.PROP_CMD_QUERY_OWN_PROP_LIST: 
			processCommandQueryOwnPropListReq(context, req);
			break;
		case Constant.PROP_CMD_USE_PROPS:
			processCommandUsePropsReq(context, req);
			break;
		case Constant.PROP_CMD_SYN_PROPS:
			processCommandSynPropsReq(context, req);
			break;
		default: 
			String msg = "无效的协议命令, cmd="+context.getHeadWrapper().getCommand();
			context.setErrorCode(Constant.EC_INVALID_CMD);
			context.setMessage(msg);
			throw new RequestProcessException(msg);
		}
	}

	private void processCommandSynPropsReq(ProcessorContext context, ByteBuffer req) {
		int accountId = req.readInt();
		int productId = req.readInt();
		short num = req.readShort();
		int[] propIds = new int[num];
		int[] counts = new int[num];
		for (int i = 0; i < num; ++i) {
			propIds[i] = req.readInt();
			counts[i] = req.readInt();
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

	private void processCommandUsePropsReq(ProcessorContext context, ByteBuffer req) {
		int accountId = req.readInt();
		int productId = req.readInt();
		short num = req.readShort();
		int[] propIds = new int[num];
		int[] nums = new int[num];
		for (int i = 0; i < num; ++i) {
			propIds[i] = req.readInt();
			nums[i] = req.readInt();
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

	private void processCommandQueryOwnPropListReq(ProcessorContext context, ByteBuffer req) {
		int accountId = req.readInt();
		int productId = req.readInt();
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

	private void processCommandQueryPropListReq(ProcessorContext context, ByteBuffer req) {
		int productId = req.readInt();
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
	public void processResponse(ProcessorContext context, ByteBuffer rsp) {
		switch (context.getHeadWrapper().getCommand()) {
		case Constant.PROP_CMD_QUERY_PROP_LIST: 
			processCommandQueryPropListRsp(context, rsp);
			break;
		case Constant.PROP_CMD_QUERY_OWN_PROP_LIST: 
			processCommandQueryOwnPropListRsp(context, rsp);
			break;
		case Constant.PROP_CMD_USE_PROPS:
			break;
		case Constant.PROP_CMD_SYN_PROPS:
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private void processCommandQueryOwnPropListRsp(ProcessorContext context, ByteBuffer rsp) {
		List<OwnPropDesc> ownPropList = (List<OwnPropDesc>)context.getResult();
		if (ownPropList!=null && ownPropList.size()>0) {
			rsp.writeShort((short)ownPropList.size());
			for (OwnPropDesc desc : ownPropList) {
				rsp.writeInt(desc.getPropId());
				rsp.writeInt(desc.getCount());
			}
		}
		else {
			rsp.writeInt(0);
		}
	}

	@SuppressWarnings("unchecked")
	private void processCommandQueryPropListRsp(ProcessorContext context, ByteBuffer rsp) {
		List<GameProp> propList = (List<GameProp>)context.getResult();
		if (propList!=null && propList.size()>0) {
			rsp.writeShort((short)propList.size());
			for (GameProp prop : propList) {
				rsp.writeInt(prop.getPropId());
				rsp.writeUTF(prop.getPropName());
				rsp.writeInt(prop.getPrice());
				rsp.writeUTF(prop.getDescription());
			}
		}
		else {
			rsp.writeInt(0);
		}
	}

}
