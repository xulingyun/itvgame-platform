package cn.ohyeah.itvgame.business.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.halcyon.utils.ThreadSafeClientConnManagerUtil;
import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.business.model.DijoyResponseEntry;
import cn.ohyeah.itvgame.business.service.BusinessException;
import cn.ohyeah.itvgame.business.service.SubscribeException;
import cn.ohyeah.itvgame.global.Configuration;

public class DijoySubscribeUtil {
	private static final Log log = LogFactory.getLog(DijoySubscribeUtil.class);
	private static final DefaultHttpClient httpClient;
	private static final String consumeUrl;
	private static final String payUrlConfiguration;
	
	static {
		httpClient = ThreadSafeClientConnManagerUtil.buildDefaultHttpClient();
		consumeUrl =  Configuration.getProperty("dijoy", "payUrl");
		payUrlConfiguration =  Configuration.getProperty("dijoy", "payUrlConfiguration");
	}
	
	public static ResultInfo recharge(String userId, String appId, int number, String feeCode, 
			  String returnUrl, String notifyUrl, String platformExt, String appExt){
		
		throw new BusinessException("not supported");
	}
	
	public static ResultInfo consumeCoins(String userId, String appId, int number, int feeCode, 
									  String returnUrl, String notifyUrl, String platformExt, String appExt, 
									  String payKey, String buyUrl){
		
		String url = "";
		if(payUrlConfiguration.equals("true")){
			url = consumeUrl;
		}else{
			url = buyUrl;
		}
		
		try {
			String sign = userId + appId + feeCode + number + returnUrl + notifyUrl + platformExt + appExt + payKey;
			sign = DigestUtils.md5Hex(sign).toUpperCase();
			log.debug("[Dijoy expend UrlPattern] ==> "+url);
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			nvps.add(new BasicNameValuePair("userId", userId));
			nvps.add(new BasicNameValuePair("appId", appId));
			nvps.add(new BasicNameValuePair("feeCode", String.valueOf(feeCode)));
			nvps.add(new BasicNameValuePair("number", String.valueOf(number)));
			nvps.add(new BasicNameValuePair("returnUrl", returnUrl));
			nvps.add(new BasicNameValuePair("notifyUrl", notifyUrl));
			nvps.add(new BasicNameValuePair("platformExt", platformExt));
			nvps.add(new BasicNameValuePair("appExt", appExt));
			nvps.add(new BasicNameValuePair("sign", sign));
			HttpPost httpPost = new HttpPost(url);
			UrlEncodedFormEntity urlEntity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
			httpPost.setEntity(urlEntity);

	    	String body = ThreadSafeClientConnManagerUtil.executeForBodyString(httpClient, httpPost);
	    	ObjectMapper op = new ObjectMapper();
	    	JsonNode node = op.readValue(body, JsonNode.class);
	    	DijoyResponseEntry entry = new DijoyResponseEntry();
	    	entry.setOrder(String.valueOf(node.get("order")));
	    	entry.setFeeCode(String.valueOf(node.get("feeCode")));
	    	entry.setSum(Integer.parseInt(String.valueOf(node.get("Sum")==null?0:node.get("Sum"))));
	    	entry.setPayResult(Integer.parseInt(String.valueOf(node.get("payResult"))));
	    	entry.setSign(String.valueOf(node.get("sign")));
	    	log.info("entry-->"+entry);
	    	ResultInfo info = new ResultInfo();
	    	//info.setInfo(11);
	    	if(entry.getPayResult()==0){
	    		info.setInfo(entry.getSum());
	    	}else{
	    		info.setErrorCode(ErrorCode.EC_SUBSCRIBE_FAILED);
	    		info.setMessage(getRechargegdErrorMessage(entry.getPayResult())+"("+String.valueOf(entry.getPayResult())+")");
	    	}
	    	return info;
		} catch (Exception e) {
			throw new SubscribeException(e);
		}
	}
	
	public static String getRechargegdErrorMessage(int errorCode) {
		String message = null;
		switch (errorCode) {
		case 2: message = "ҵ��Э�鲻�ܽ���"; break;
		case 3: message = "ҵ��Э�鲻���Ϲ淶"; break;
		case 4: message = "ҵ��Э�鲻���Ϲ淶"; break;
		case 5: message = "ҵ��û�ж���"; break;
		case 6: message = "����ϵͳ����ʱ"; break;
		case 7: message = "ϵͳ���񷵻صĽ�������Ϲ淶"; break;
		case 8: message = "ϵͳ���񷵻ص����ݲ����Ϲ淶"; break;
		case 9: message = "����ҵ����������Ͳ����Ϲ淶"; break;
		case 13: message = "���ݿ����"; break;
		case 70005: message = "�޿��õ����ط�����"; break;
		case 70002: message = "�ϴε�¼��֤�������ڴ�����"; break;
		case 70001: message = "�˺�״̬������"; break;
		case 70000: message = "�˺Ż��������"; break;
		case 60006: message = "�һ�ʧ�ܣ����Ų�����"; break;
		case 60005: message = "�һ�ʧ�ܣ����Ÿ�ʽ����"; break;
		case 60004: message = "�һ�ʧ�ܣ������ظ���"; break;
		case 60003: message = "֧����Ŀ������"; break;
		case 60002: message = "֧����Կ������"; break;
		case 60001: message = "��Ӧ�ò����ڻ����¼�"; break;
		case 60000: message = "�һ�ʧ�ܣ����ݿ����"; break;
		case 41001: message = "���ݿ����"; break;
		case 41000: message = "��������ݲ�����"; break;
		case 40009: message = "���ݿ��ʽ����"; break;
		case 40001: message = "���ݿ����"; break;
		case 30000: message = "û�в��ҵ�����"; break;
		case 30011: message = "�������ظ�"; break;
		case 30012: message = "����˻������ڡ�"; break;
		case 30013: message = "��ֵ��������"; break;
		case 30021: message = "ȡ������ظ�"; break;
		case 30022: message = "ȡ���˻������ڡ�"; break;
		case 30023: message = "ȡ����㡣"; break;
		case 30024: message = "��������֧������"; break;
		case 30025: message = "���㣬�뵽���Ӫҵ����ֵ��������ѯ96296��"; break;
		case 20005: message = "û���ҵ�Ӧ��"; break;
		case 20004: message = "û���ҵ�Ŀ¼"; break;
		case 20003: message = "����������������"; break;
		case 20000: message = "û�в��ҵ�����"; break;
		case 10000: message = "û�в��ҵ�����"; break;
		case 2001: message = "ҳ��������ݴ���"; break;
		case 1005: message = "��ȡ�������"; break;
		case 1004: message = "��֤��Ϣ����"; break;
		case 1003: message = "����ҵ����������Ͳ����Ϲ淶"; break;
		case 1002: message = "�û�����ȱʧ"; break;
		case 1001: message = "��֤��Ϣȱʧ"; break;
		case 1000: message = "��̨����ʧ��"; break;
		default: message = "���㣬�뵽���Ӫҵ����ֵ��������ѯ96296"; break;
		}
		return message;
	}

}
