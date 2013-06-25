package cn.ohyeah.itvgame.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.service.PlatformService;
import cn.ohyeah.itvgame.platform.service.ServiceException;

public class SysServProcessor implements IProcessor {
	private static final PlatformService platServ;
	
	static {
		platServ = (PlatformService)BeanManager.getBean("platformService");
	}
	
	@Override
	public void processRequest(ProcessorContext context, DataInputStream dis)
			throws IOException {
		switch (context.getHeadWrapper().getCommand()) {
		case Constant.SYS_SERV_CMD_SYN_TIME: 
			break;
		case Constant.SYS_SERV_CMD_ADD_FAVORITEGD:
			processCommandAddFavoritegd(context, dis);
			break;
		default: 
			String msg = "无效的协议命令, cmd="+context.getHeadWrapper().getCommand();
			context.setErrorCode(Constant.EC_INVALID_CMD);
			context.setMessage(msg);
			throw new RequestProcessException(msg);
		}
	}

	private void processCommandAddFavoritegd(ProcessorContext context,
			DataInputStream dis) throws IOException {
		String hosturl = dis.readUTF();
		int accountId = dis.readInt();
		String userId = dis.readUTF();
		String accountName = dis.readUTF();
		int productId = dis.readInt();
		String gameid = dis.readUTF();
		String spid = dis.readUTF();
		String code = dis.readUTF();
		String timeStmp = dis.readUTF();
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
	public void processResponse(ProcessorContext context, DataOutputStream dos)
			throws IOException {
		switch (context.getHeadWrapper().getCommand()) {
		case Constant.SYS_SERV_CMD_SYN_TIME: 
			processCommandSynTime(context, dos);
			break;
		case Constant.SYS_SERV_CMD_ADD_FAVORITEGD:
			processCommandAddFavoritegd(context, dos);
			break;
		default: 
			String msg = "无效的协议命令, cmd="+context.getHeadWrapper().getCommand();
			context.setErrorCode(Constant.EC_INVALID_CMD);
			context.setMessage(msg);
			throw new RequestProcessException(msg);
		}
	}

	private void processCommandAddFavoritegd(ProcessorContext context,
			DataOutputStream dos) throws IOException {
		dos.writeInt(context.getHeadWrapper().getHead());
		dos.writeInt(0);	/*result*/
	}

	private void processCommandSynTime(ProcessorContext context,
			DataOutputStream dos) throws IOException {
		dos.writeInt(context.getHeadWrapper().getHead());
		dos.writeInt(0);	/*result*/
		dos.writeLong(new java.util.Date().getTime());
	}

}
