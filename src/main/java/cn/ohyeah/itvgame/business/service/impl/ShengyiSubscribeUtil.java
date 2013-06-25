package cn.ohyeah.itvgame.business.service.impl;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sy.service.cpservice.action.TelecomServeStub;
import com.sy.service.cpservice.action.TelecomServeStub.AccountAuth;
import com.sy.service.cpservice.action.TelecomServeStub.AccountAuthForm;
import com.sy.service.cpservice.action.TelecomServeStub.AccountAuthResponse;
import com.sy.service.cpservice.action.TelecomServeStub.AccountRsp;
import com.sy.service.cpservice.action.TelecomServeStub.AccountSetJudgForm;
import com.sy.service.cpservice.action.TelecomServeStub.ServiceOrder;
import com.sy.service.cpservice.action.TelecomServeStub.ServiceOrderForm;
import com.sy.service.cpservice.action.TelecomServeStub.ServiceOrderResponse;
import com.sy.service.cpservice.action.TelecomServeStub.ServiceOrderRsp;
import com.sy.service.cpservice.action.TelecomServeStub.SetAndJudgAccount;
import com.sy.service.cpservice.action.TelecomServeStub.SetAndJudgAccountResponse;

import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.business.service.BusinessException;
import cn.ohyeah.itvgame.business.service.SubscribeException;

public class ShengyiSubscribeUtil {
	private static final Log log = LogFactory.getLog(ShengyiSubscribeUtil.class);
	
	public static ResultInfo recharge(String userId, String appId, int number, String feeCode, 
			  String returnUrl, String notifyUrl, String platformExt, String appExt){
		
		throw new BusinessException("not supported");
	}
	
	public static ResultInfo consumeCoins(String userId, String userToken, int amount,
									  String shengyiCPID, String shengyiCPPassWord, String shengyiUserIdType,
									  String shengyiProductId, String timeStamp, String transactionID, String desc){
		
		ServiceOrderForm form = new ServiceOrderForm();
		form.setUserID(userId);
		form.setUserToken(userToken);
		form.setPrice(-amount);
		form.setCPID(shengyiCPID);
		form.setCPPassWord(shengyiCPPassWord);
		form.setUserIDType(Integer.parseInt(shengyiUserIdType));
		form.setProductID(shengyiProductId);
		form.setTimeStamp(timeStamp);
		form.setTransactionID(transactionID);
		form.setConsumption(desc);
		ServiceOrder order = new ServiceOrder();
		order.setServiceOrderForm(form);
		try {
			TelecomServeStub  stub = new TelecomServeStub();  
			stub._getServiceClient().getOptions().setProperty(HTTPConstants.CHUNKED,"false"); 
			ServiceOrderResponse res = stub.serviceOrder(order);
			ServiceOrderRsp orderRsp = res.get_return();
			log.info("订购结果："+orderRsp.getResult()+",信息:"+orderRsp.getDescription());
			ResultInfo info = new ResultInfo();
	    	//info.setInfo(11);
	    	if(orderRsp.getResult().equals("0")){
	    		info.setInfo(orderRsp.getServiceID());
	    	}else{
	    		info.setErrorCode(ErrorCode.EC_SUBSCRIBE_FAILED);
	    		info.setMessage(orderRsp.getResult()+orderRsp.getDescription());
	    	}
	    	return info;
			
		} catch (AxisFault e1) {
			throw new SubscribeException(e1);
		} catch (RemoteException e) {
			throw new SubscribeException(e);
		}
	}

	/**
	 * 判断用户是否设置童锁
	 * @param userId
	 * @param userToken
	 * @param TimeStamp
	 * @param transactionId
	 * @return
	 */
	public static ResultInfo judgAccount(String userId, String userToken, String TimeStamp, String transactionId, String cpId, String cpPW){
		try {
			TelecomServeStub  stub = new TelecomServeStub();
			SetAndJudgAccount saja = new SetAndJudgAccount();
			AccountSetJudgForm asjForm = new AccountSetJudgForm();
			asjForm.setUserID(userId);
			asjForm.setUserToken(userToken);
			asjForm.setTimeStamp(TimeStamp);
			asjForm.setTransactionID(transactionId);
			asjForm.setCPID(cpId);
			asjForm.setCPPassWord(cpPW);
			saja.setAccountSetJudgForm(asjForm);
			SetAndJudgAccountResponse res = stub.setAndJudgAccount(saja);
			AccountRsp asrsp = res.get_return();
			log.info("setAndJudgAccount return:"+asrsp.getResult()+", message:"+asrsp.getDescription());
			ResultInfo info = new ResultInfo();
			if(asrsp.getResult().equals("0")){
				info.setInfo(asrsp.getResult());
			}else{
				info.setErrorCode(ErrorCode.EC_SUBSCRIBE_FAILED);
				info.setMessage(asrsp.getResult()+asrsp.getDescription());
			}
			return info;
		} catch (AxisFault e) {
			throw new SubscribeException(e);
		} catch (RemoteException e1) {
			throw new SubscribeException(e1);
		}
	}
	
	/**
	 * 验证密码是否错误
	 * @param userId
	 * @param userToken
	 * @param TimeStamp
	 * @param transactionId
	 * @param passWd
	 * @return
	 */
	public static ResultInfo checkPassword(String userId, String userToken, String TimeStamp, String transactionId, String passWd,
			String cpId, String cpPw){
		TelecomServeStub stub;
		try {
			stub = new TelecomServeStub();
			AccountAuth aa = new AccountAuth();
			AccountAuthForm aaForm = new AccountAuthForm();
			aaForm.setUserID(userId);
			aaForm.setUserToken(userToken);
			aaForm.setTimeStamp(TimeStamp);
			aaForm.setTransactionID(transactionId);
			aaForm.setPassword(passWd);
			aaForm.setCPID(cpId);
			aaForm.setCPPassWord(cpPw);
			aa.setAccountAuthForm(aaForm);
			AccountAuthResponse aaRes = stub.accountAuth(aa);
			AccountRsp aaRsp = aaRes.get_return();
			log.info("accountAuth return:"+aaRsp.getResult()+", message:"+aaRsp.getDescription());
			ResultInfo info = new ResultInfo();
			if(aaRsp.getResult().equals("0")){
				info.setInfo(aaRsp.getResult());
			}else{
				info.setErrorCode(ErrorCode.EC_SUBSCRIBE_FAILED);
				info.setMessage(aaRsp.getDescription()+", 错误码："+aaRsp.getResult());
			}
			return info;
		} catch (AxisFault e) {
			throw new SubscribeException(e);
		} catch (RemoteException e1) {
			throw new SubscribeException(e1);
		}
	}
}
