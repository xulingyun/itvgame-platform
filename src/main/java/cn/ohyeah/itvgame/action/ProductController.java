package cn.ohyeah.itvgame.action;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.halcyon.utils.RequestContext;
import cn.ohyeah.itvgame.business.IpInfo;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.global.Configuration;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.Product;
import cn.ohyeah.itvgame.platform.service.AccountService;
import cn.ohyeah.itvgame.platform.service.ProductService;
import cn.ohyeah.itvgame.utils.UrlUtil;

public class ProductController {
	private static final Log log = LogFactory.getLog(ProductController.class);
	
	private static final AccountService accServ;
	private static final ProductService productServ;
	
	static {
		accServ = (AccountService)BeanManager.getBean("accountService");
		productServ = (ProductService)BeanManager.getBean("productService");
	}
	
	public void innerLogin(RequestContext rc) throws ServletException, IOException {
		int productId = rc.param("productId", -1);
		Product product = productServ.read(productId);
		if (product!=null) {
			int accountId = (Integer)rc.sessionAttr("accountId");
			Account account = accServ.read(accountId);
			if (account != null) {
				String returnUrl = Configuration.formatProductLogoutUrl(rc, 
						accountId, productId, rc.param("returnUrl"));
				String accountName = (String)rc.sessionAttr("accountName");
				String userToken = (String)(String)rc.sessionAttr("userToken");
				log.debug("[login param]: requestIp ==> "+IpInfo.ip());
				log.debug("[login param]: accountId ==> "+accountId);
				log.debug("[login param]: userId ==> "+account.getUserId());
				log.debug("[login param]: accountName ==> "+accountName);
				log.debug("[login param]: accountId ==> "+userToken);
				log.debug("[login param]: productId ==> "+productId);
				log.debug("[login param]: returnUrl ==> "+returnUrl);
				
				String url = product.getLocation();
				if (StringUtils.isEmpty(url) || "null".equalsIgnoreCase(url)) {
					url = Configuration.getClientLoaderUrl();
					if (!url.startsWith("http://")) {
						url = rc.contextPath()+url;
					}
					rc.requestAdd("accountId", accountId);
					rc.requestAdd("userId", account.getUserId());
					rc.requestAdd("accountName", accountName);
					rc.requestAdd("userToken", userToken);
					rc.requestAdd("productId", productId);
					rc.requestAdd("returnUrl", returnUrl);
					
					String clientUrl = Configuration.getClientLoaderUrl();
					log.debug("[login param]: clientUrl ==> "+clientUrl);
					rc.forward(clientUrl);
				}
				else {
					if (!url.startsWith("http://")) {
						url = rc.contextPath()+url;
					}
					String params = Configuration.formatClientLoaderParams(accountId, account.getUserId(), 
							productId, returnUrl);
					url = UrlUtil.addParam(url, params);
					url = UrlUtil.addParam(url, "accountName", accountName);
					url = UrlUtil.addParam(url, "userToken", userToken);
					
					log.debug("[login param]: clientUrl ==> "+url);
					rc.redirect(url);
				}
			}
			else {
				rc.print("产品登入错误，用户不存在, accountId="+accountId);
			}
		}
		else {
			rc.print("产品登入错误，产品不存在, productId="+productId);
		}
	}
	
	public void login(RequestContext rc) throws ServletException, IOException {
		int result = rc.param("result", -1);
		if (result == 0) {
			innerLogin(rc);
		}
		else {
			String hideShow = rc.param("hideShow");
			if (hideShow != null && "true".equalsIgnoreCase(hideShow)) {
				rc.redirect(rc.param("returnUrl"));
			}
			else {
				String promptText = rc.param("promptText");
				if (promptText != null) {
					promptText = new String(Base64.decodeBase64(promptText), "gbk");
				}
				else {
					promptText = "未知的异常";
				}
				rc.requestAdd("promptText", "载入游戏失败，原因："+promptText);
				rc.requestAdd("returnUrl", rc.param("returnUrl"));
				rc.forward(Configuration.getErrorPage());
			}
		}
	}

	public void logout(RequestContext rc) throws IOException {
		String returnUrl = rc.param("returnUrl");
		log.debug("[logout]: returnUrl ==> "+returnUrl);
		rc.redirect(returnUrl);
	}
}
