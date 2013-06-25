package cn.ohyeah.itvgame.business;

import cn.halcyon.utils.RequestContext;

public class IpInfo {
	public static String ip() {
		String ip = RequestContext.get().ip();
		if (ip!=null && ip.length()>15) {
			ip = "ipv6";
		}
		return ip;
	}
}
