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
				log.info("[UserID:"+userId+"], ��ѯ�û���Ϣʧ�ܣ�ԭ��"+uiRst.getDesc());
			}
			return userInfo;
		}
		catch (Exception e) {
			throw new BusinessException("[UserID:"+userId+"], ��ѯ�û���Ϣʧ��", e);
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
				log.info("[UserID:"+userId+"], ��ѯ�û�adslnameʧ�ܣ�ԭ��"+uiRst.getDesc());
			}
			log.debug("[UserID:"+userId+"] adslname ==> "+adslName);
			return adslName;
		}
		catch (Exception e) {
			throw new BusinessException("[UserID:"+userId+"], ��ѯ�û�adslnameʧ��", e);
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
				log.info("[adslname:"+adslName+"], ��ѯ�û�����ʧ�ܣ�ԭ��"+rsp.getResultDesc());
			}
			log.debug("[adslname:"+adslName+"] points ==> "+points);
			return points;
		}
		catch (Exception e) {
			throw new BusinessException("[adslname:"+adslName+"], ��ѯ�û�����ʧ��", e);
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
				log.info("[adslname:"+adslName+"], ��ȡCRMIDʧ�ܣ�ԭ��"+rsp.getResultDesc());
			}
			return crmid;
		}
		catch (Exception e) {
			throw new BusinessException("[adslname:"+adslName+"], ��ȡCRMIDʧ��", e);
		}
	}
	
    public static String getErrorMessage(int result) {
    	String message = null;
    	switch(result) {
    	case 1001:	message = "�ò�Ʒ�Ѿ�������Ч��Ȩ"; break;
    	case 1002:	message = "���ɲ�Ʒ��Ȩ��"; break;
    	case 1003:	message = "��Ʒ��Ȩ�޷��������ݿ�"; break;
    	case 1005:	message = "����Ч��Ʒ��Ȩ"; break;
    	case 1004:	message = "��ֹ��Ȩ��ϵ����"; break;
    	case 1006:	message = "�޷�ִ������˶���"; break;
    	case 1007:	message = "�޷�ִ�������ͣ������ϵ"; break;
    	case 1008:	message = "�޷�ִ����ɻָ�������ϵ"; break;
    	case 1009:	message = "��Ȩ��ϵ�쳣������ͣ״̬"; break;
    	case 1011:	message = "������Ȩ�޷��������ݿ�"; break;
    	case 1021:	message = "������Ȩ�޷��������ݿ�"; break;
    	case 1031:	message = "CDR�޷��������ݿ�"; break;
    	case 1032:	message = "�޷�������ЧCDR"; break;
    	case 1033:	message = "ProductID��Ч"; break;
    	case 1034:	message = "ServiceID��Ч"; break;
    	case 1035:	message = "ContentID��Ч"; break;
    	case 1051:	message = "CRM-�˶��޸���Ȩ��ʧ��"; break;
    	case 2001:	message = "�û�������"; break;
    	case 2002:	message = "�û���ͣ��������"; break;
    	case 2003:	message = "�û���������״̬"; break;
    	case 2011:	message = "UserToken��Ч"; break;
    	case 2012:	message = "UserToken����"; break;
    	case 3001:	message = "��Ʒ������"; break;
    	case 3002:	message = "�ǰ��²�Ʒ"; break;
    	case 3003:	message = "�ǰ��¼�ʱ������Ʒ"; break;
    	case 3004:	message = "��Ʒ��û�й������"; break;
    	case 3005:	message = "��Ʒ���Ƿ���״̬"; break;
    	case 3006:	message = "��Ʒ����"; break;
    	case 3007:	message = "���²�Ʒ��������"; break;
    	case 3008:	message = "CRM��Ʒ�˶���ֻ���ڶ�����������Ч"; break;
    	case 4001:	message = "Ԥ�����˻�����Ԥ�����������"; break;
    	case 4002:	message = "Ԥ�����˻������˻������ڻ�״̬�쳣"; break;
    	case 4003:	message = "Ԥ�����˻�����"; break;
    	case 4004:	message = "Ԥ����δ֪��"; break;
    	case 5001:	message = "CRM�ӿ��޷�����"; break;
    	case 6001:	message = "�޷��������"; break;
    	case 6002:	message = "�޷����ɴ���Ʒ�"; break;
    	default:	message = "δ֪���쳣"; break;
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
						//����֧������
						value = payType;
					}
					else if ("jf_action".equalsIgnoreCase(key)) {
						if ("1".equals(payType)) {	//���ֶһ�
							//����֧����jf_actionֵΪ"3"���˵�֧����Ϊ�մ�
							value = "3";
						}
						else {	//�˵�֧��
							value = "";
						}
					}
					else if ("jf_months".equalsIgnoreCase(key)) {
						if ("1".equals(payType)) {	//���ֶһ�
							//����֧����jf_monthsֵΪ"1"���˵�֧����Ϊ�մ�
							value = "1";
						}
						else {	//�˵�֧��
							value = "";
						}
					}
					else {
						//��������
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
				info.setMessage("�޷���ȡ���Ŷ�����תҳ��");
            }

            body = filterSubRedirect(userId, payType, subUrlPre, body, 3);
            log.debug("body: "+body);
            if (body == null) {
            	info.setErrorCode(ErrorCode.EC_SUBSCRIBE_FAILED);
				info.setMessage("�޷��������Ŷ���ȷ��ҳ��");
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
					info.setMessage("�޷��������Ŷ���������ӣ����������Ϊ����");
            	}
            }
            else {
            	info.setErrorCode(ErrorCode.EC_SUBSCRIBE_FAILED);
				info.setMessage("�޷��������Ŷ����������");
            }
            
		} catch (Exception e) {
			throw new SubscribeException(e);
		}
		return info;
	}
}
