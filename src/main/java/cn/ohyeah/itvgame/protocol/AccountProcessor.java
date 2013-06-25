package cn.ohyeah.itvgame.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.Authorization;
import cn.ohyeah.itvgame.platform.model.LoginInfo;
import cn.ohyeah.itvgame.platform.model.Product;
import cn.ohyeah.itvgame.platform.model.SubscribeProperties;
import cn.ohyeah.itvgame.platform.service.AccountService;
import cn.ohyeah.itvgame.platform.service.AuthorizationService;
import cn.ohyeah.itvgame.platform.service.PlatformService;
import cn.ohyeah.itvgame.platform.service.ProductService;
import cn.ohyeah.itvgame.platform.service.ServiceException;

public class AccountProcessor implements IProcessor {
	private static final AuthorizationService authServ;
	private static final AccountService accServ;
	private static final ProductService productServ;
	private static final PlatformService platServ;
	
	static {
		authServ = (AuthorizationService)BeanManager.getBean("authorizationService");
		accServ = (AccountService)BeanManager.getBean("accountService");
		productServ = (ProductService)BeanManager.getBean("productService");
		platServ = (PlatformService)BeanManager.getBean("platformService");
	}
	
	@Override
	public void processRequest(ProcessorContext context, DataInputStream dis)
			throws IOException {
		switch (context.getHeadWrapper().getCommand()) {
		case Constant.ACCOUNT_CMD_QUERY_AUTHORIZATION: 
			processCommandQueryAuthorization(context, dis);
			break;
		case Constant.ACCOUNT_CMD_QUERY_SUB_PROPS:
			processCommandQuerySubProps(context, dis);
			break;
		case Constant.ACCOUNT_CMD_USER_LOGIN:
			processCommandUserLogin(context, dis);
			break;
		default: 
			String msg = "无效的协议命令, cmd="+context.getHeadWrapper().getCommand();
			context.setErrorCode(Constant.EC_INVALID_CMD);
			context.setMessage(msg);
			throw new RequestProcessException(msg);
		}
	}

	private void processCommandQuerySubProps(ProcessorContext context,
			DataInputStream dis) throws IOException {
		String buyURL = dis.readUTF();
		int accountId = dis.readInt();
		String accountName = dis.readUTF();
		String userToken = dis.readUTF();
		int productId = dis.readInt();
		String checkKey = dis.readUTF();
		try {
			Map<String, Object> props = new HashMap<String, Object>();
			props.put("buyURL", buyURL);
			props.put("accountName", accountName);
			props.put("userToken", userToken);
			props.put("checkKey", checkKey);
			SubscribeProperties subProps = platServ.querySubscribeProperties(props, accountId, productId);
			context.setResult(subProps);
		}
		catch (ServiceException e) {
			context.setErrorCode(ErrorCode.EC_SERVICE_FAILED);
			context.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_SERVICE_FAILED));
			throw new RequestProcessException(e);
		}
	}

	private void processCommandUserLogin(ProcessorContext context,
			DataInputStream dis) throws IOException {
		String buyURL = dis.readUTF();
		String userId = dis.readUTF();
		String accountName = dis.readUTF();
		String userToken = dis.readUTF();
		String appName = dis.readUTF();
		String checkKey = dis.readUTF();
		try {
			LoginInfo info = new LoginInfo();
			context.setResult(info);
			
			/*用户登录*/
			Account account = accServ.userLogin(userId);
			/*获取产品信息*/
			Product product = productServ.readByAppName(appName);
			/*获取鉴权信息*/
			Authorization auth = authServ.checkAuthorization(account.getAccountId(), product.getProductId());
			/*获取平台信息*/
			Map<String, Object> props = new HashMap<String, Object>();
			props.put("buyURL", buyURL);
			props.put("account", account);
			props.put("authorization", auth);
			props.put("accountName", accountName);
			props.put("userToken", userToken);
			props.put("checkKey", checkKey);
			SubscribeProperties subProps = platServ.querySubscribeProperties(props, account.getAccountId(), product.getProductId());
			
			info.setAccountId(account.getAccountId());
			info.setUserId(userId);
			info.setProductId(product.getProductId());
			info.setProductName(product.getProductName());
			info.setAppName(appName);
			info.setSystemTime(new java.util.Date());
			info.setAuth(auth);
			info.setSubProps(subProps);
		}
		catch (NullPointerException e) {
			context.setErrorCode(ErrorCode.EC_SERVICE_FAILED);
			context.setMessage("根据应用程序名称没有找到相应产品, appName="+appName);
			throw new RequestProcessException(e);
		}
		catch (ServiceException e) {
			context.setErrorCode(ErrorCode.EC_SERVICE_FAILED);
			context.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_SERVICE_FAILED));
			throw new RequestProcessException(e);
		}
	}

	private void processCommandQueryAuthorization(ProcessorContext context,
			DataInputStream dis) throws IOException {
		int accountId = dis.readInt();
		int productId = dis.readInt();
		try {
			Authorization auth = authServ.checkAuthorization(accountId, productId);
			if (auth != null) {
				context.setResult(auth);
			}
			else {
				context.setErrorCode(ErrorCode.EC_INVALID_AUTHORIZATION);
				context.setMessage(ErrorCode.getErrorMessage(ErrorCode.EC_INVALID_AUTHORIZATION));
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
		case Constant.ACCOUNT_CMD_QUERY_AUTHORIZATION: 
			processCommandQueryAuthorization(context, dos);
			break;
		case Constant.ACCOUNT_CMD_QUERY_SUB_PROPS:
			processCommandQuerySubProps(context, dos);
			break;
		case Constant.ACCOUNT_CMD_USER_LOGIN:
			processCommandUserLogin(context, dos);
		default: 
			break;
		}
	}

	private void processCommandQuerySubProps(ProcessorContext context,
			DataOutputStream dos) throws IOException {
		SubscribeProperties subProps = (SubscribeProperties)context.getResult();
		
		dos.writeInt(context.getHeadWrapper().getHead());
		dos.writeInt(0);
		writeSubProps(dos, subProps);
	}

	private void writeSubProps(DataOutputStream dos,
			SubscribeProperties subProps) throws IOException {
		dos.writeBoolean(subProps.isSupportSubscribe());
		dos.writeUTF(subProps.getSubscribeAmountUnit());
		dos.writeInt(subProps.getSubscribeCashToAmountRatio());
		dos.writeBoolean(subProps.isSupportPointsService());
		dos.writeUTF(subProps.getPointsUnit());
		dos.writeInt(subProps.getAvailablePoints());
		dos.writeInt(subProps.getCashToPointsRatio());
		dos.writeBoolean(subProps.isSupportRecharge());
		dos.writeUTF(subProps.getExpendAmountUnit());
		dos.writeInt(subProps.getExpendCashToAmountUnit());
		dos.writeInt(subProps.getBalance());
		dos.writeInt(subProps.getRechargeRatio());
	}

	private void processCommandUserLogin(ProcessorContext context,
			DataOutputStream dos) throws IOException {
		LoginInfo info = (LoginInfo)context.getResult();
		
		dos.writeInt(context.getHeadWrapper().getHead());
		dos.writeInt(0);
		
		/*写入用户及产品信息*/
		dos.writeInt(info.getAccountId());
		dos.writeUTF(info.getUserId());
		dos.writeInt(info.getProductId());
		dos.writeUTF(info.getProductName());
		dos.writeUTF(info.getAppName());
		dos.writeLong(info.getSystemTime().getTime());
		
		/*写入鉴权信息*/
		Authorization auth = info.getAuth();
		writeAuth(dos, auth);
		
		/*写入计费相关信息*/
		SubscribeProperties subProps = info.getSubProps();
		writeSubProps(dos, subProps);
	}

	private void processCommandQueryAuthorization(ProcessorContext context,
			DataOutputStream dos) throws IOException {
		Authorization auth = (Authorization)context.getResult();
		
		dos.writeInt(context.getHeadWrapper().getHead());
		dos.writeInt(0);
		writeAuth(dos, auth);
	}

	private void writeAuth(DataOutputStream dos, Authorization auth)
			throws IOException {
		dos.writeInt(auth.getAuthorizationType());
		dos.writeInt(auth.getLeftTryNumber());
		dos.writeInt(auth.getLeftValidSeconds());
		dos.writeInt(auth.getLeftValidCount());
		if (auth.getAuthorizationStartTime() != null) {
			dos.writeLong(auth.getAuthorizationStartTime().getTime());
		}
		else {
			dos.writeLong(0);
		}
		if (auth.getAuthorizationEndTime() != null) {
			dos.writeLong(auth.getAuthorizationEndTime().getTime());
		}
		else {
			dos.writeLong(0);
		}
	}
}
