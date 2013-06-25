package cn.ohyeah.itvgame.action;

import java.io.IOException;

import cn.halcyon.utils.CryptUtils;
import cn.halcyon.utils.RequestContext;
import cn.ohyeah.itvgame.global.Configuration;
import cn.ohyeah.itvgame.platform.model.User;
import cn.ohyeah.itvgame.utils.InitUtil;

public class ToolsController {
	
	public void init(RequestContext rc) throws IOException {
		String userName = rc.param("userName");
		if (userName != null) {
			User user = Configuration.getUser(userName);
			if (user!=null && user.isRoleAdmin()) {
				String psw = rc.param("pwd");
				if (psw != null) {
					String pswMd5 = CryptUtils.MD5(psw);
					if (pswMd5.equalsIgnoreCase(user.getPwdMd5())) {
						new InitUtil().init();
						rc.print("init database success");
					}
					else {
						rc.print("passord error");
					}
				}
				else {
					rc.print("please input password");
				}
			}
			else {
				rc.print("account not exist or without authority");
			}
		}
		else {
			rc.print("please input account");
		}
	}
}
