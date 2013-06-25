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
		case -1: message = "收藏夹已满"; break;
		case -2: message = "该游戏已经收藏"; break;
		case -11: message = "用户帐号不正确"; break;
		case -12: message = "中游id不正确"; break;
		case -13: message = "游戏ID不正确"; break;
		case -14: message = "spid不正确"; break;
		case -15: message = "timeStmp不正确"; break;
		case -16: message = "请求超时"; break;
		case -17: message = "code不正确"; break;
		case -101: message = "系统异常"; break;
		default: message = "位置错误"; break;
		}
		return message;
	}
}
