package cn.ohyeah.itvgame.business.service.impl;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import cn.halcyon.utils.ThreadSafeClientConnManagerUtil;
import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.business.service.SubscribeException;
import cn.ohyeah.itvgame.global.Configuration;

public class ShixianSubscribeUtil {
	private static final Log log = LogFactory.getLog(ShixianSubscribeUtil.class);
	private static final DefaultHttpClient httpClient;
	private static final String rechargeUrlShixian;
	//private static final String appid;
	
	static {
		httpClient = ThreadSafeClientConnManagerUtil.buildDefaultHttpClient();
		rechargeUrlShixian = Configuration.getProperty("shixian", "urlPattern");
		//appid = Configuration.getProperty("shixian", "appid");
	}
	
	public static ResultInfo consumeCoins(Map<String, Object> props){
		String buyUrl = (String) props.get("buyURL")+rechargeUrlShixian;
		String feeaccount = (String) props.get("feeaccount");
		//String returnurl = (String) props.get("returnurl");
		String dwjvl = (String) props.get("dwjvl");
		String opcomkey = (String) props.get("opcomkey");
		String paysubway = (String) props.get("paysubway");
		//String gameid = (String) props.get("gameid");
		//String user_group_id = (String) props.get("user_group_id");
		//String userToken = (String) props.get("userToken");
		int ammount = (Integer) props.get("amount");
		String userId = (String) props.get("userId");
		String appId = (String) props.get("appId");
		//String	buyUrl = rechargeUrlShixian;
		String params = "tvplat#feeaccount="+feeaccount+";tvplat#returnurl="+/*returnurl*/""+"; tvplat#numbercode="+userId
				+"; tvplat#dwjvl="+dwjvl+"; tvplat#opcomkey="+opcomkey+"; tvplat#paysubway="+paysubway/*+"; tvplat#gameid="+gameid
				+"; USER_TOKEN="+userToken+"; USER_GROUP_ID="+user_group_id*/;
		log.info("cookie:"+params);
		String rechargeUrl = String.format(buyUrl, ammount, appId);
		log.info("rechargeUrl:"+rechargeUrl);
		ResultInfo info = new ResultInfo();
		HttpGet httpget = new HttpGet(rechargeUrl);
		
		//往header中写入cookie，cookie中的是接口所需参数
		httpget.addHeader("cookie", params);
    	String body;
		try {
			body = ThreadSafeClientConnManagerUtil.executeForBodyString(httpClient, httpget);
			log.info("body==>"+body);
			String ss = body.substring(body.indexOf("*")+1,body.lastIndexOf("*"));
			log.info("returnMessage:"+ss);
			if(!isErrorMessage(ss)){
				if(ss.startsWith("恭喜您")){
					info.setInfo(ammount);
				}else if(ss.equalsIgnoreCase("password")){
					info.setErrorCode(ErrorCode.EC_SUBSCRIBE_FAILED);
					info.setMessage(ss);
				}else{
					int code = Integer.parseInt(ss);
					if(0==code){
						info.setInfo(ammount);
					}else{
						info.setErrorCode(ErrorCode.EC_SUBSCRIBE_FAILED);
						info.setMessage(getErrorMessage(code));
					}
				}
			} else {
				info.setErrorCode(ErrorCode.EC_SUBSCRIBE_FAILED);
				info.setMessage(ss);
			}
			return info;
		} catch (Exception e) {
			throw new SubscribeException(e);
		}
	}

	private static String getErrorMessage(int errorCode){
		String message = null;
		switch (errorCode) {
		case 2012: message = "江苏童锁"; break;
		case 2: message = "数字ID不存在或者用户已被冻结"; break;
		case -1001: message = "充值的账号数字ID类型异常"; break;
		case -1002: message = "用户名不存在或者用户已被冻结"; break;
		case -1004: message = "数字ID不存在或者用户已被冻结"; break;
		case -1218: message = "用户充值超出限额"; break;
		case -1999: message = "系统异常"; break;
		case -9305: message = "用户充值超出限额"; break;
		default: message = "未知错误"; break;
		}
		return message;
	}
	
	private static boolean isErrorMessage(String message){
		if(message.equals("电信接口调用失败")){
			return true;
		}else if(message.equals("充值失败")){
			return true;
		}else if(message.equals("用户充值超出限额")){
			return true;
		}else if(message.equals("数字ID不存在或者")){
			return true;
		}else if(message.equals("您的该次登陆")){
			return true;
		}else{
			return false;
		}
	}
}
