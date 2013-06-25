package cn.ohyeah.itvgame.protocolv2;

import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.service.PlatformService;
import cn.ohyeah.itvgame.platform.service.ServiceException;
import cn.ohyeah.stb.utils.ByteBuffer;

public class SysServProcessor implements IProcessor {
	private static final PlatformService platServ;
	
	static {
		platServ = (PlatformService)BeanManager.getBean("platformService");
	}
	
	@Override
	public void processRequest(ProcessorContext context, ByteBuffer req) {
		switch (context.getHeadWrapper().getCommand()) {
		case Constant.SYS_SERV_CMD_SYN_TIME: 
			break;
		case Constant.SYS_SERV_CMD_ADD_FAVORITEGD:
			processCommandAddFavoritegdReq(context, req);
			break;
		default: 
			String msg = "无效的协议命令, cmd="+context.getHeadWrapper().getCommand();
			context.setErrorCode(Constant.EC_INVALID_CMD);
			context.setMessage(msg);
			throw new RequestProcessException(msg);
		}
	}

	private void processCommandAddFavoritegdReq(ProcessorContext context, ByteBuffer req) {
		String hosturl = req.readUTF();
		int accountId = req.readInt();
		String userId = req.readUTF();
		String accountName = req.readUTF();
		int productId = req.readInt();
		String gameid = req.readUTF();
		String spid = req.readUTF();
		String code = req.readUTF();
		String timeStmp = req.readUTF();
		try {
			ResultInfo info = platServ.addFavoritegd(hosturl, accountId, userId, accountName, productId, gameid, spid, code, timeStmp);
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

	@Override
	public void processResponse(ProcessorContext context, ByteBuffer rsp) {
		switch (context.getHeadWrapper().getCommand()) {
		case Constant.SYS_SERV_CMD_SYN_TIME: 
			processCommandSynTimeRsp(context, rsp);
			break;
		case Constant.SYS_SERV_CMD_ADD_FAVORITEGD:
			break;
		default: break;
		}
	}

	private void processCommandSynTimeRsp(ProcessorContext context,
			ByteBuffer rsp) {
		rsp.writeLong(new java.util.Date().getTime());
	}

}
