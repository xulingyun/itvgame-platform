package cn.ohyeah.itvgame.protocolv2;

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
import cn.ohyeah.stb.utils.ByteBuffer;

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
	public void processRequest(ProcessorContext context, ByteBuffer req) {
		switch (context.getHeadWrapper().getCommand()) {
		case Constant.ACCOUNT_CMD_QUERY_AUTHORIZATION: 
			processCommandQueryAuthorizationReq(context, req);
			break;
		case Constant.ACCOUNT_CMD_QUERY_SUB_PROPS:
			processCommandQuerySubPropsReq(context, req);
			break;
		case Constant.ACCOUNT_CMD_USER_LOGIN:
			processCommandUserLoginReq(context, req);
			break;
		default: 
			String msg = "无效的协议命令, cmd="+context.getHeadWrapper().getCommand();
			context.setErrorCode(Constant.EC_INVALID_CMD);
			context.setMessage(msg);
			throw new RequestProcessException(msg);
		}
	}

	private void processCommandQuerySubPropsReq(ProcessorContext context,
			ByteBuffer req) {
		String buyURL = req.readUTF();
		int accountId = req.readInt();
		String accountName = req.readUTF();
		String userToken = req.readUTF();
		int productId = req.readInt();
		String checkKey = req.readUTF();
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

	private void processCommandUserLoginReq(ProcessorContext context,
			ByteBuffer req) {
		String buyURL = req.readUTF();
		String userId = req.readUTF();
		String accountName = req.readUTF();
		String userToken = req.readUTF();
		String appName = req.readUTF();
		String checkKey = req.readUTF();
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

	private void processCommandQueryAuthorizationReq(ProcessorContext context,
			ByteBuffer req) {
		int accountId = req.readInt();
		int productId = req.readInt();
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
	public void processResponse(ProcessorContext context, ByteBuffer rsp) {
		switch (context.getHeadWrapper().getCommand()) {
		case Constant.ACCOUNT_CMD_QUERY_AUTHORIZATION: 
			processCommandQueryAuthorizationRsp(context, rsp);
			break;
		case Constant.ACCOUNT_CMD_QUERY_SUB_PROPS:
			processCommandQuerySubPropsRsp(context, rsp);
			break;
		case Constant.ACCOUNT_CMD_USER_LOGIN:
			processCommandUserLoginRsp(context, rsp);
		default: 
			break;
		}
	}

	private void processCommandQuerySubPropsRsp(ProcessorContext context,
			ByteBuffer rsp) {
		SubscribeProperties subProps = (SubscribeProperties)context.getResult();
		writeSubProps(rsp, subProps);
	}

	private void writeSubProps(ByteBuffer rsp, SubscribeProperties subProps) {
		rsp.writeBoolean(subProps.isSupportSubscribe());
		rsp.writeUTF(subProps.getSubscribeAmountUnit());
		rsp.writeInt(subProps.getSubscribeCashToAmountRatio());
		rsp.writeBoolean(subProps.isSupportPointsService());
		rsp.writeUTF(subProps.getPointsUnit());
		rsp.writeInt(subProps.getAvailablePoints());
		rsp.writeInt(subProps.getCashToPointsRatio());
		rsp.writeBoolean(subProps.isSupportRecharge());
		rsp.writeUTF(subProps.getExpendAmountUnit());
		rsp.writeInt(subProps.getExpendCashToAmountUnit());
		rsp.writeInt(subProps.getBalance());
		rsp.writeInt(subProps.getRechargeRatio());
	}

	private void processCommandUserLoginRsp(ProcessorContext context,
			ByteBuffer rsp) {
		LoginInfo info = (LoginInfo)context.getResult();
		/*写入用户及产品信息*/
		rsp.writeInt(info.getAccountId());
		rsp.writeUTF(info.getUserId());
		rsp.writeInt(info.getProductId());
		rsp.writeUTF(info.getProductName());
		rsp.writeUTF(info.getAppName());
		rsp.writeLong(info.getSystemTime().getTime());
		
		/*写入鉴权信息*/
		Authorization auth = info.getAuth();
		writeAuth(rsp, auth);
		
		/*写入计费相关信息*/
		SubscribeProperties subProps = info.getSubProps();
		writeSubProps(rsp, subProps);
	}

	private void processCommandQueryAuthorizationRsp(ProcessorContext context,
			ByteBuffer rsp) {
		Authorization auth = (Authorization)context.getResult();
		writeAuth(rsp, auth);
	}

	private void writeAuth(ByteBuffer rsp, Authorization auth) {
		rsp.writeInt(auth.getAuthorizationType());
		rsp.writeInt(auth.getLeftTryNumber());
		rsp.writeInt(auth.getLeftValidSeconds());
		rsp.writeInt(auth.getLeftValidCount());
		if (auth.getAuthorizationStartTime() != null) {
			rsp.writeLong(auth.getAuthorizationStartTime().getTime());
		}
		else {
			rsp.writeLong(0);
		}
		if (auth.getAuthorizationEndTime() != null) {
			rsp.writeLong(auth.getAuthorizationEndTime().getTime());
		}
		else {
			rsp.writeLong(0);
		}
	}
}
