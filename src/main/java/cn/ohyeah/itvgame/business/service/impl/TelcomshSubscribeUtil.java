package cn.ohyeah.itvgame.business.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.sanss.iptvinfo.ws.userinfo.UserInfoElement;
import com.sanss.iptvinfo.ws.userinfo.UserInfoResult;
import com.sanss.iptvinfo.ws.userinfo.UserInfoService;
import com.sanss.iptvinfo.ws.userinfo.UserInfoServiceServiceLocator;
import com.sanss.wtjf.service.interfac.PointQryRequest;
import com.sanss.wtjf.service.interfac.PointQryResponse;
import com.sanss.wtjf.service.interfac.PointQueryInterfaceDelegate;
import com.sanss.wtjf.service.interfac.PointQueryInterfaceServiceLocator;

import cn.halcyon.utils.ThreadSafeClientConnManagerUtil;
import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.business.service.BusinessException;
import cn.ohyeah.itvgame.business.service.SubscribeException;
import cn.ohyeah.itvgame.global.Configuration;

public final class TelcomshSubscribeUtil {
	private static final Log log = LogFactory.getLog(TelcomshSubscribeUtil.class);
	private static final DefaultHttpClient httpClient;
	public static final Pattern formPattern;
    public static final Pattern inputPattern;
    private static final Pattern anchorPattern;
    private static final Pattern resultPattern;
    private static final String subscribeUrl;
    
    static {
    	subscribeUrl = Configuration.getSubscribeUrl("telcomsh");
    	formPattern = Pattern.compile(Configuration.getActionPattern("telcomsh"), Pattern.CASE_INSENSITIVE);
		inputPattern = Pattern.compile(Configuration.getParamPattern("telcomsh"), Pattern.CASE_INSENSITIVE);
		anchorPattern = Pattern.compile(Configuration.getBackUrlPattern("telcomsh"), Pattern.CASE_INSENSITIVE);
		resultPattern = Pattern.compile(Configuration.getResultPattern("telcomsh"), Pattern.CASE_INSENSITIVE);
		httpClient = ThreadSafeClientConnManagerUtil.buildDefaultHttpClient();
    }
	
	public static Map<String, String> queryUserInfo(String userId) {
		try {
			UserInfoServiceServiceLocator uilocater = new UserInfoServiceServiceLocator();
			java.net.URL uiUrl = new java.net.URL(Configuration.getQueryUserInfoUrl("telcomsh"));
			UserInfoService uiServ = uilocater.getUserInfoService(uiUrl);
			
			UserInfoResult uiRst = uiServ.getUserInfo(userId);
			UserInfoElement[] uiItems = null;
			String result = uiRst.getResult();
			Map<String, String> userInfo = new HashMap<String, String>();
			if ("0".equals(result)) {
				uiItems = uiRst.getElements();
				for (UserInfoElement item : uiItems) {
					userInfo.put(item.getKey(), item.getValue());
				}
			}
			else {
				log.info("[UserID:"+userId+"], 查询用户信息失败，原因："+uiRst.getDesc());
			}
			return userInfo;
		}
		catch (Exception e) {
			throw new BusinessException("[UserID:"+userId+"], 查询用户信息失败", e);
		}
	}
	
	public static String queryAdslName(String userId) {
		try {
			UserInfoServiceServiceLocator uilocater = new UserInfoServiceServiceLocator();
			java.net.URL uiUrl = new java.net.URL(Configuration.getQueryUserInfoUrl("telcomsh"));
			UserInfoService uiServ = uilocater.getUserInfoService(uiUrl);
			
			UserInfoResult uiRst = uiServ.getUserInfo(userId);
			UserInfoElement[] uiItems = null;
			String adslName = null;
			String result = uiRst.getResult();
			if ("0".equals(result)) {
				uiItems = uiRst.getElements();
				for (UserInfoElement item : uiItems) {
					if ("adslname".equalsIgnoreCase(item.getKey())) {
						adslName = item.getValue();
						break;
					}
				}
			}
			else {
				log.info("[UserID:"+userId+"], 查询用户adslname失败，原因："+uiRst.getDesc());
			}
			log.debug("[UserID:"+userId+"] adslname ==> "+adslName);
			return adslName;
		}
		catch (Exception e) {
			throw new BusinessException("[UserID:"+userId+"], 查询用户adslname失败", e);
		}
	}
	
	public static int queryAvailablePoints(String adslName) {
		if (StringUtils.isEmpty(adslName)) {
			return 0;
		}
		try {
			PointQueryInterfaceServiceLocator locater = new PointQueryInterfaceServiceLocator();
			java.net.URL remoteUrl = new java.net.URL(Configuration.getQueryPointsUrl("telcomsh"));
			PointQueryInterfaceDelegate service = locater.getPointQueryInterfacePort(remoteUrl);
			
			PointQryRequest req = new PointQryRequest();
			String checkCodeKey = Configuration.getProperty("telcomsh", "queryPointsCheckCodeKey");
			String srcCK = adslName.toUpperCase()+checkCodeKey;
			String checkCode = DigestUtils.md5Hex(Base64.encodeBase64String(srcCK.getBytes()));
			req.setAdslname(adslName);
			req.setCheckcode(checkCode);
			
			PointQryResponse rsp = service.qryPoints(req);
			int points = 0;
			if (rsp.getResult() == 0) {
				points = rsp.getPoints();
			}
			else {
				log.info("[adslname:"+adslName+"], 查询用户积分失败，原因："+rsp.getResultDesc());
			}
			log.debug("[adslname:"+adslName+"] points ==> "+points);
			return points;
		}
		catch (Exception e) {
			throw new BusinessException("[adslname:"+adslName+"], 查询用户积分失败", e);
		}
	}
	
	public static String queryCrmidByUserId(String userId) {
		String adslName = queryAdslName(userId);
		return queryCrmid(adslName);
	}
	
	public static String queryCrmid(String adslName) {
		if (StringUtils.isEmpty(adslName)) {
			return null;
		}
		try {
			PointQueryInterfaceServiceLocator locater = new PointQueryInterfaceServiceLocator();
			java.net.URL remoteUrl = new java.net.URL(Configuration.getQueryPointsUrl("telcomsh"));
			PointQueryInterfaceDelegate service = locater.getPointQueryInterfacePort(remoteUrl);
			
			PointQryRequest req = new PointQryRequest();
			String checkCodeKey = Configuration.getProperty("telcomsh", "queryPointsCheckCodeKey");
			String srcCK = adslName.toUpperCase()+checkCodeKey;
			String checkCode = DigestUtils.md5Hex(Base64.encodeBase64String(srcCK.getBytes()));
			req.setAdslname(adslName);
			req.setCheckcode(checkCode);
			
			PointQryResponse rsp = service.qryPoints(req);
			String crmid = null;
			if (rsp.getResult() == 0) {
				crmid = rsp.getCrmID();
			}
			else {
				log.info("[adslname:"+adslName+"], 获取CRMID失败，原因："+rsp.getResultDesc());
			}
			return crmid;
		}
		catch (Exception e) {
			throw new BusinessException("[adslname:"+adslName+"], 获取CRMID失败", e);
		}
	}
	
    public static String getErrorMessage(int result) {
    	String message = null;
    	switch(result) {
    	case 1001:	message = "该产品已经存在有效授权"; break;
    	case 1002:	message = "生成产品授权错"; break;
    	case 1003:	message = "产品授权无法插入数据库"; break;
    	case 1005:	message = "无有效产品授权"; break;
    	case 1004:	message = "终止授权关系出错"; break;
    	case 1006:	message = "无法执行完成退订购"; break;
    	case 1007:	message = "无法执行完成暂停订购关系"; break;
    	case 1008:	message = "无法执行完成恢复订购关系"; break;
    	case 1009:	message = "授权关系异常：非暂停状态"; break;
    	case 1011:	message = "服务授权无法插入数据库"; break;
    	case 1021:	message = "内容授权无法插入数据库"; break;
    	case 1031:	message = "CDR无法插入数据库"; break;
    	case 1032:	message = "无法计算有效CDR"; break;
    	case 1033:	message = "ProductID无效"; break;
    	case 1034:	message = "ServiceID无效"; break;
    	case 1035:	message = "ContentID无效"; break;
    	case 1051:	message = "CRM-退订修改授权期失败"; break;
    	case 2001:	message = "用户不存在"; break;
    	case 2002:	message = "用户已停机或销户"; break;
    	case 2003:	message = "用户不是正常状态"; break;
    	case 2011:	message = "UserToken无效"; break;
    	case 2012:	message = "UserToken过期"; break;
    	case 3001:	message = "产品不存在"; break;
    	case 3002:	message = "非包月产品"; break;
    	case 3003:	message = "非包月即时订购产品"; break;
    	case 3004:	message = "产品还没有过免费期"; break;
    	case 3005:	message = "产品不是发布状态"; break;
    	case 3006:	message = "产品过期"; break;
    	case 3007:	message = "包月产品即将下线"; break;
    	case 3008:	message = "CRM产品退订购只有在订购后下月起效"; break;
    	case 4001:	message = "预付费账户错误，预付费网络错误"; break;
    	case 4002:	message = "预付费账户错误，账户不存在或状态异常"; break;
    	case 4003:	message = "预付费账户余额不足"; break;
    	case 4004:	message = "预付费未知错"; break;
    	case 5001:	message = "CRM接口无法调用"; break;
    	case 6001:	message = "无法计算费用"; break;
    	case 6002:	message = "无法生成代理计费"; break;
    	default:	message = "未知的异常"; break;
    	}
    	return message;
    }
    
    private static String getUrlPrefix(String url) {
    	return url.substring(0, url.indexOf('/', "http://".length()));
    }
    
    static String filterSubRedirect(String userId, String payType, String subUrlPre, String body, int deep) throws Exception{
    	if (deep == 0) {
    		return body;
    	}
		
		Matcher m = formPattern.matcher(body);
		if (m.find()) {
			String action = m.group(1);
			if (!action.startsWith("http://")) {
				action = subUrlPre+action;
			}
			else {
				subUrlPre = getUrlPrefix(action);
			}
			
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			m = inputPattern.matcher(body);
			while (m.find()) {
				String key = m.group(1);
				String value = m.group(2);
				if (deep == 1) {
					if("payType".equalsIgnoreCase(key)){
						//设置支付类型
						value = payType;
					}
					else if ("jf_action".equalsIgnoreCase(key)) {
						if ("1".equals(payType)) {	//积分兑换
							//积分支付，jf_action值为"3"，账单支付设为空串
							value = "3";
						}
						else {	//账单支付
							value = "";
						}
					}
					else if ("jf_months".equalsIgnoreCase(key)) {
						if ("1".equals(payType)) {	//积分兑换
							//按次支付，jf_months值为"1"，账单支付设为空串
							value = "1";
						}
						else {	//账单支付
							value = "";
						}
					}
					else {
						//不做处理
					}
				}
				log.debug("[userId:"+userId+";deep:"+deep+"] "+key+" ==> "+value);
				nvps.add(new BasicNameValuePair(key, value));
			}
			
			HttpPost httpost = new HttpPost(action);
			UrlEncodedFormEntity urlEntity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
			httpost.setEntity(urlEntity);
			body = ThreadSafeClientConnManagerUtil.executeForBodyString(httpClient, httpost);
			body = filterSubRedirect(userId, payType, subUrlPre, body, deep-1);
		}
		return body;
    }

    private static String execSubRequest(String subUrl) throws Exception {
    	HttpGet httpget = new HttpGet(subUrl);
    	return ThreadSafeClientConnManagerUtil.executeForBodyString(httpClient, httpget);
    }
    
    static String extractSubResult(String url) {
    	Matcher m = resultPattern.matcher(url);
    	String result = null;
		if (m.find()) {
			result = m.group(1);
		}
		return result;
    }
    
    static String extractBackUrl(String body) {
    	Matcher m = anchorPattern.matcher(body);
    	String backUrl = null;
		if (m.find()) {
			backUrl = m.group(1);
		}
		return backUrl;
    }

	public static ResultInfo subscribe(String userId, String subscribeId, String userToken, String spid, String payType) {
		ResultInfo info = new ResultInfo();
        try {
        	String subUrlPre = getUrlPrefix(subscribeUrl);
        	String returnUrl = "/itvgame/subresult";
            String subUrl = String.format(subscribeUrl, userId, subscribeId, userToken, spid, returnUrl);
            log.info("[subscribe url] ==> "+subUrl);
            String body = execSubRequest(subUrl);
            log.debug("body: "+body);
            if (body == null) {
            	info.setErrorCode(ErrorCode.EC_SUBSCRIBE_FAILED);
				info.setMessage("无法获取电信订购跳转页面");
            }

            body = filterSubRedirect(userId, payType, subUrlPre, body, 3);
            log.debug("body: "+body);
            if (body == null) {
            	info.setErrorCode(ErrorCode.EC_SUBSCRIBE_FAILED);
				info.setMessage("无法解析电信订购确认页面");
            }
            String backUrl = extractBackUrl(body);
            String result = extractSubResult(backUrl);
            log.debug("result: "+result);
            if (result != null) {
            	try {
            		int rst = Integer.parseInt(result);
            		if (rst != 0) {
            			info.setErrorCode(ErrorCode.EC_SUBSCRIBE_FAILED);
						info.setMessage(getErrorMessage(rst));
            		}
            	}
            	catch (NumberFormatException e) {
            		info.setErrorCode(ErrorCode.EC_SUBSCRIBE_FAILED);
					info.setMessage("无法解析电信订购结果链接，订购结果不为整数");
            	}
            }
            else {
            	info.setErrorCode(ErrorCode.EC_SUBSCRIBE_FAILED);
				info.setMessage("无法解析电信订购结果链接");
            }
            
		} catch (Exception e) {
			throw new SubscribeException(e);
		}
		return info;
	}
}
