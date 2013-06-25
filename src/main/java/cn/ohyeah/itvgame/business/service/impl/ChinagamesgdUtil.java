package cn.ohyeah.itvgame.business.service.impl;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import cn.halcyon.utils.ThreadSafeClientConnManagerUtil;
import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.business.service.SubscribeException;
import cn.ohyeah.itvgame.global.Configuration;

public class ChinagamesgdUtil {
	private static final Log log = LogFactory.getLog(ChinagamesgdUtil.class);
	private static final DefaultHttpClient httpClient;
	private static final String favorUrlPattern;
	
	static {
		httpClient = ThreadSafeClientConnManagerUtil.buildDefaultHttpClient();
		favorUrlPattern = Configuration.getProperty("chinagamesgd", "favorUrl");
	}
		
	public static int littleEndianByteArrayToInt(byte[] data) {
		int v = data[0]&0XFF;
		v |= (data[1]&0XFF)<<8;
		v |= (data[2]&0XFF)<<16;
		v |= (data[3]&0XFF)<<24;
		return v;
	}
	
    
	public static ResultInfo addFavorite(String hosturl, String userid, String username, 
			String gameid, String spid, String code, String timeStmp) {
		try {
			String urlPattern = null;
			if (!hosturl.endsWith("/")&&!favorUrlPattern.startsWith("/")) {
				urlPattern = hosturl + "/" +favorUrlPattern;
			}
			else {
				urlPattern = hosturl + favorUrlPattern;
			}
			String favorUrl = String.format(urlPattern, userid, username, gameid, spid, code, timeStmp);
			log.debug("[chinagamesgd favor url] ==> "+favorUrl);
			HttpGet httpget = new HttpGet(favorUrl);
			byte[] data = ThreadSafeClientConnManagerUtil.executeForBodyByteArray(httpClient, httpget);
			int result = littleEndianByteArrayToInt(data);
			log.debug("[chinagamesgd favor result] ==> "+result);
			ResultInfo info = new ResultInfo();
			if (result != 1) {
				info.setErrorCode(ErrorCode.EC_ADD_FAVOR_FAILED);
				info.setMessage(getAddFavoriteErrorMessage(result));
			}
			return info;
		}
		catch (Exception e) {
			throw new SubscribeException(e);
		}
	}
	
	public static String getAddFavoriteErrorMessage(int errorCode) {
		String message = null;
		switch (errorCode) {
		case -1: message = "�ղؼ�����"; break;
		case -2: message = "����Ϸ�Ѿ��ղ�"; break;
		case -11: message = "�û��ʺŲ���ȷ"; break;
		case -12: message = "����id����ȷ"; break;
		case -13: message = "��ϷID����ȷ"; break;
		case -14: message = "spid����ȷ"; break;
		case -15: message = "timeStmp����ȷ"; break;
		case -16: message = "����ʱ"; break;
		case -17: message = "code����ȷ"; break;
		case -101: message = "ϵͳ�쳣"; break;
		default: message = "λ�ô���"; break;
		}
		return message;
	}
}
