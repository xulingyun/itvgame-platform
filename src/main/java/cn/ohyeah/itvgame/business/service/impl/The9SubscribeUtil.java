package cn.ohyeah.itvgame.business.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.datacontract.schemas._2004._07.The9_PlatformCore_Model.AccountInfo;
import org.datacontract.schemas._2004._07.The9_PlatformService_WCF.OrderResultInfo;
import org.tempuri.IUserService;
import org.tempuri.UserServiceLocator;

import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.business.service.SubscribeException;
import cn.ohyeah.itvgame.global.Configuration;

public class The9SubscribeUtil {
	private static final Log log = LogFactory.getLog(The9SubscribeUtil.class);
	private static final String subscribeUrl = Configuration.getSubscribeUrl("the9");
	
	public static ResultInfo subscribe(String userId, String accountName, 
			int amount, String subscribeId, String remark) {
		try {
			UserServiceLocator locater = new UserServiceLocator();
			IUserService service;
			java.net.URL remoteURL = new java.net.URL(subscribeUrl);
			service=locater.getBasicHttpBinding_IUserService(remoteURL);
			OrderResultInfo result = service.consumeMoney(userId, accountName, 
					new java.math.BigDecimal(amount), subscribeId, remark);
			log.debug("[The9 consumeMoney OrderResultInfo] ==> "
					+"(orderCode="+result.getOrderCode()+",errorCode="+result.getErrorCode()+",description="+result.getDescription()+")");
			ResultInfo info = new ResultInfo();
			if (!result.getIsSucess()) {
				info.setErrorCode(ErrorCode.EC_SUBSCRIBE_FAILED);
				info.setMessage(getErrorMessage(result.getErrorCode()));
			}
			else {
				info.setInfo(amount);
			}
			return info;
		}
		catch (Exception e) {
			throw new SubscribeException(e);
		}
	}
	
	public static int queryBalance(String accountName) {
		try {
			UserServiceLocator locater = new UserServiceLocator();
			IUserService service;
			java.net.URL remoteURL = new java.net.URL(subscribeUrl);
			service=locater.getBasicHttpBinding_IUserService(remoteURL);
			AccountInfo accInfo = service.getAccountInfo(accountName);
			if (accInfo == null) {
				throw new SubscribeException(ErrorCode.getErrorMessage(ErrorCode.EC_INVALID_ACCOUNT));
			}
			else {
				return accInfo.getTokenBalance().intValue();
			}
		}
		catch (Exception e) {
			throw new SubscribeException(e);
		}
	}
	
	public static String getErrorMessage(int errorCode) {
		String message = null;
		switch (errorCode) {
		case 1001: message = "��Ϸ������"; break;
		case 1002: message = "��Ʒ������"; break;
		case 1003: message = "�����˺Ų�����"; break;
		case 1011: message = "�ײ���"; break;
		case 1012: message = "���Ҳ���"; break;
		case 1013: message = "��Ʒ��治��"; break;
		case 1021: message = "�ﵽ�������޶�"; break;
		case 1022: message = "�ﵽ�������޶�"; break;
		case 1031: message = "����Խ��"; break;
		default: message = "δ֪����";
		}
		return message;
	}
}