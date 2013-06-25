package cn.ohyeah.itvgame.action;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.ServletException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.halcyon.utils.CryptUtils;
import cn.halcyon.utils.RequestContext;
import cn.ohyeah.itvgame.business.IpInfo;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.global.Configuration;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.AccountPermission;
import cn.ohyeah.itvgame.platform.model.Authorization;
import cn.ohyeah.itvgame.platform.model.ProductDetail;
import cn.ohyeah.itvgame.platform.model.ProductPermission;
import cn.ohyeah.itvgame.platform.service.AccountPermissionService;
import cn.ohyeah.itvgame.platform.service.AccountService;
import cn.ohyeah.itvgame.platform.service.AuthorizationService;
import cn.ohyeah.itvgame.platform.service.ProductPermissionService;
import cn.ohyeah.itvgame.platform.service.ProductService;
import cn.ohyeah.itvgame.utils.UrlUtil;

public class GatewayController {
	private static final Log log = LogFactory.getLog(GatewayController.class);
	
	private static final ProductService productServ;
	private static final AccountService accServ;
	private static final AuthorizationService authServ;
	private static final AccountPermissionService apServ;
	private static final ProductPermissionService ppServ;
	
	static{
		productServ = (ProductService)BeanManager.getBean("productService");
		accServ = (AccountService)BeanManager.getBean("accountService");
		apServ = (AccountPermissionService)BeanManager.getBean("accountPermissionService");
		authServ = (AuthorizationService)BeanManager.getBean("authorizationService");
		ppServ = (ProductPermissionService)BeanManager.getBean("productPermissionService");
	}
	
	private void authorize(RequestContext rc, int accountId, String accountName, String userId, 
			String userToken, int productId, String returnUrl) throws IOException, ServletException {
		
		log.debug("[authorize param]: requestIp ==> "+IpInfo.ip());
		log.debug("[authorize param]: accountId ==> "+accountId);
		log.debug("[authorize param]: userId ==> "+userId);
		log.debug("[authorize param]: accountName ==> "+accountName);
		log.debug("[authorize param]: accountId ==> "+userToken);
		log.debug("[authorize param]: productId ==> "+productId);
		log.debug("[authorize param]: returnUrl ==> "+returnUrl);
		
		Account account = accServ.read(accountId);
		if (account != null) {
			AccountPermission ap = apServ.validateExist(account);
			if (ap.isAccessPermissionValid()){
				ProductDetail detail = productServ.readProductDetail(productId);
				if (detail != null) {
					ProductPermission pp = ppServ.validateExist(account, detail);
					if (pp.isAccessPermissionValid()) {
						String loginUrl = Configuration.formatProductLoginUrl(rc, productId, returnUrl);
						Authorization auth = authServ.checkAuthorization(account, detail);
						if (auth.isAuthorizationInvalid()) {	//没有权限
							java.util.Date date = Configuration.getSubscribeEnableDate(detail.getProductId());
							if (date==null || date.before(new java.util.Date())) {
								rc.redirect(Configuration.formatProductSubscribeUrl(rc, accountId, productId, loginUrl));
							}
							else {
								rc.redirect(UrlUtil.addParam(loginUrl, "result", "0"));
							}
						}
						else {	//有权限
							if (auth.isAuthorizationTry()) {
								rc.redirect(Configuration.formatProductTryUrl(rc, accountId, productId, loginUrl));
							}
						 	else {
								rc.redirect(UrlUtil.addParam(loginUrl, "result", "0"));
							}
						}
					}
					else {
						log.info("[access permission denied]: requestIp ==> "+IpInfo.ip());
						log.info("[access permission denied]: accountId ==> "+accountId);
						log.info("[access permission denied]: userId ==> "+userId);
						log.info("[access permission denied]: accountName ==> "+accountName);
						log.info("[access permission denied]: accountId ==> "+userToken);
						log.info("[access permission denied]: productId ==> "+productId);
						log.info("[access permission denied]: returnUrl ==> "+returnUrl);
						rc.print("此账号没有本产品的访问权限，请联系管理员");
					}
				}
				else {
					String hallUrl = Configuration.getGameHallUrl()
							+"?entrance="+(String)rc.sessionAttr("entrance")
							+"&returnUrl="+URLEncoder.encode(returnUrl, "gbk");
					rc.forward(hallUrl);
				}
			}
			else {
				log.info("[access permission denied]: requestIp ==> "+IpInfo.ip());
				log.info("[access permission denied]: accountId ==> "+accountId);
				log.info("[access permission denied]: userId ==> "+userId);
				log.info("[access permission denied]: accountName ==> "+accountName);
				log.info("[access permission denied]: accountId ==> "+userToken);
				log.info("[access permission denied]: productId ==> "+productId);
				log.info("[access permission denied]: returnUrl ==> "+returnUrl);
				rc.print("此账号没有访问权限，请联系管理员");
			}
		}
		else {
			rc.print("鉴权错误，用户不存在, accountId: "+accountId);
		}
	}
	
	public void telcomshSsoResult(RequestContext rc) throws IOException, ServletException {
		int result = -1;
		String userId = null;
		String userToken = null;
		String returnUrl = null;
		String returnInfo = null;
		int productId = -1;
		Enumeration<String> params = rc.params();
		while (params.hasMoreElements()) {
			String name = params.nextElement();
			String lname = name.toLowerCase();
			if ("result".equals(lname)) {
				result = rc.param(name, -1);
			}
			if (userId==null && "userid".equals(lname)) {
				userId = rc.param(name);
			}
			if (userToken==null && "usertoken".equals(lname)) {
				userToken = rc.param(name);
			}
			if (returnInfo==null && "returninfo".equals(lname)) {
				returnInfo = rc.param(name);
			}
		}
		String[] infos = StringUtils.split(returnInfo, "!!!");
		returnUrl = new String(Base64.decodeBase64(infos[0]), "GBK");
		productId = Integer.parseInt(infos[1]);
		if (result == 0) {
			Account account = accServ.userLogin(userId);
			String accountName = userId;
			rc.sessionAdd("accountId", account.getAccountId());
			rc.sessionAdd("userId", userId);
			rc.sessionAdd("accountName", accountName);
			rc.sessionAdd("userToken", userToken);
			authorize(rc, account.getAccountId(), accountName, userId, userToken, productId, returnUrl);
		}
		else {
			rc.print("SSO错误，result="+result);
		}
	}
	
	public void sso(RequestContext rc, String returnUrl) throws ServletException, IOException {
		String spid = Configuration.getSpid();
		String returnInfo = Base64.encodeBase64URLSafeString(returnUrl.getBytes("GBK"));
		String resultUrl = java.net.URLEncoder.encode(rc.baseUrl()+"/action/gateway/telcomshSsoResult", "GBK");
		returnInfo = returnInfo+"!!!"+rc.param("productId", -1);
		String ssoUrl = String.format(Configuration.getSsoUrl(), spid, resultUrl, returnInfo);
		log.debug("ssoUrl ==>"+ssoUrl);
		rc.redirect(ssoUrl);
	}
	
	public void login(RequestContext rc, String returnUrl) throws ServletException, IOException {
		String userId = rc.param("uid");
		if (userId != null) {
			Account account = accServ.read(userId);
			if (account!=null&&account.isPrivilegeSuperUser()) {
				String psw = rc.param("pwd");
				if (psw != null) {
					String pswMd5 = CryptUtils.MD5(psw);
					if (pswMd5.equalsIgnoreCase(account.getPwdMd5())) {
						String accountName = userId;
						String userToken = "inner";
						rc.sessionAdd("accountId", account.getAccountId());
						rc.sessionAdd("userId", userId);
						rc.sessionAdd("userToken", userToken);
						rc.sessionAdd("accountName", accountName);
						authorize(rc, account.getAccountId(), accountName, userId, userToken, rc.param("productId", -1), returnUrl);
					}
					else {
						sso(rc, returnUrl);
					}
				}
				else {
					sso(rc, returnUrl);
				}
			}
			else {
				sso(rc, returnUrl);
			}
		}
		else {
			sso(rc, returnUrl);
		}
	}
	
	public void telcomshEntry(RequestContext rc) throws ServletException, IOException {
		String returnUrl = null;
		int productId = -1;
		Enumeration<String> params = rc.params();
		while (params.hasMoreElements()) {
			String name = params.nextElement();
			String lname = name.toLowerCase();
			if (returnUrl==null && "returnurl".equals(lname)) {
				returnUrl = rc.param(name);
			}
			if (productId==-1 && "productid".equals(lname)) {
				productId=rc.param(name, -1);
			}
		}
		rc.sessionAdd("entrance", "telcomsh");
		if (StringUtils.isEmpty(returnUrl)) {
			returnUrl = (String)rc.sessionAttr("returnUrl");
		}
		if (StringUtils.isEmpty(returnUrl)||"bestv".equalsIgnoreCase(returnUrl)) {
			returnUrl = Configuration.formatEpgUrl(rc);
		}
		rc.sessionAdd("returnUrl", returnUrl);
		log.debug("[telcomshEntry]: returnUrl ==> "+returnUrl);

		Integer accountId = (Integer)rc.sessionAttr("accountId");
		String accountName = (String)rc.sessionAttr("accountName");
		String userId = (String)rc.sessionAttr("userId");
		String userToken = (String)rc.sessionAttr("userToken");
		if (accountId!=null
				&& StringUtils.isNotEmpty(accountName)
				&& StringUtils.isNotEmpty(userId)
				&& StringUtils.isNotEmpty(userToken)) {
			authorize(rc, accountId, accountName, userId, userToken, productId, returnUrl);
		}
		else {
			login(rc, returnUrl);
		}
	}
	
	public void chinagamesEntry(RequestContext rc) throws IOException, ServletException {
		String returnUrl = null;
		int productId = -1;
		Enumeration<String> params = rc.params();
		while (params.hasMoreElements()) {
			String name = params.nextElement();
			String lname = name.toLowerCase();
			if (returnUrl==null && "returnurl".equals(lname)) {
				returnUrl = rc.param(name);
			}
			if (productId==-1 && "productid".equals(lname)) {
				productId = rc.param(name, -1);
			}
		}
		rc.sessionAdd("entrance", "chinagames");
		if (StringUtils.isEmpty(returnUrl)) {
			returnUrl = (String)rc.sessionAttr("returnUrl");
		}
		if (StringUtils.isEmpty(returnUrl)||"bestv".equalsIgnoreCase(returnUrl)) {
			returnUrl = Configuration.formatEpgUrl(rc);
		}
		rc.sessionAdd("returnUrl", returnUrl);
		log.debug("[chinagamesEntry]: returnUrl ==> "+returnUrl);
		
		Integer accountId = (Integer)rc.sessionAttr("accountId");
		String accountName = (String)rc.sessionAttr("accountName");
		String userId = (String)rc.sessionAttr("userId");
		String userToken = (String)rc.sessionAttr("userToken");
		if (accountId!=null
				&& StringUtils.isNotEmpty(accountName)
				&& StringUtils.isNotEmpty(userId)
				&& StringUtils.isNotEmpty(userToken)) {
			authorize(rc, accountId, accountName, userId, userToken, productId, returnUrl);
		}
		else {
			login(rc, returnUrl);
		}
	}
	
	public void shengyiEntry(RequestContext rc) throws IOException, ServletException {
		String returnUrl = null;
		int productId = -1;
		String userId = null;
		String userToken = null;
		String stbID = null;
		Enumeration<String> params = rc.params();
		while (params.hasMoreElements()) {
			String name = params.nextElement();
			String lname = name.toLowerCase();
			if (returnUrl==null && "returnurl".equals(lname)) {
				returnUrl = rc.param(name);
			}
			if (productId==-1 && "productid".equals(lname)) {
				productId = rc.param(name, -1);
			}
			if (userId==null && "userid".equals(lname)) {
				userId = rc.param(name);
				rc.sessionAdd("userId", userId);
			}
			if (userToken==null && "usertoken".equals(lname)) {
				userToken = rc.param(name);
				rc.sessionAdd("userToken", userToken);
			}
			if (stbID==null && "stbid".equals(lname)) {
				stbID = rc.param(name);
				rc.sessionAdd("stbID", stbID);
			}
		}
		rc.sessionAdd("entrance", "shengyi");
		if (StringUtils.isEmpty(returnUrl)) {
			returnUrl = (String)rc.sessionAttr("returnUrl");
		}
		if (StringUtils.isEmpty(returnUrl)||"bestv".equalsIgnoreCase(returnUrl)) {
			returnUrl = Configuration.formatEpgUrl(rc);
		}
		rc.sessionAdd("returnUrl", returnUrl);
		log.debug("[chinagamesEntry]: returnUrl ==> "+returnUrl);
		
		String accountName = (String)rc.sessionAttr("accountName");
		if (StringUtils.isNotEmpty(userId)
				&& StringUtils.isNotEmpty(userToken)) {
			Integer accountId = (Integer)rc.sessionAttr("accountId");
			if (accountId == null) {
				Account account = accServ.read(userId);
				if (account == null) {
					account = accServ.addNewStbUser(userId);
				}
				accountId = account.getAccountId();
				rc.sessionAdd("accountId", account.getAccountId());
			}
			authorize(rc, accountId, accountName, userId, userToken, productId, returnUrl);
		}
		else {
			login(rc, returnUrl);
		}
	}
	
	public void the9Entry(RequestContext rc) throws ServletException, IOException {
		String returnUrl = null;
		String accountName = null;
		String userId = null;
		String nickName = null;
		int productId = -1;
		Enumeration<String> params = rc.params();
		while (params.hasMoreElements()) {
			String name = params.nextElement();
			String lname = name.toLowerCase();
			//adAccount==>userID
			if (userId==null && "adaccount".equals(lname)) {
				userId = rc.param(name);
				rc.sessionAdd("userId", userId);
			}
			//nickName
			if (nickName==null && "nickname".equals(lname)) {
				nickName = rc.param(name);
				rc.sessionAdd("nickName", nickName);
			}
			//Account==>accountName
			if (accountName==null && "account".equals(lname)) {
				accountName = rc.param(name);
				rc.sessionAdd("accountName", accountName);
			}
			//returnUrl
			if (returnUrl==null && "returnurl".equals(lname)) {
				returnUrl = rc.param(name);
			}
			//productId
			if (productId==-1 && "productid".equals(lname)) {
				productId = rc.param(name, -1);
			}
		}
		rc.sessionAdd("entrance", "the9");
		log.debug("[the9Entry]: userId ==> "+userId);
		log.debug("[the9Entry]: accountName ==> "+accountName);
		if (StringUtils.isEmpty(returnUrl)) {
			returnUrl = (String)rc.sessionAttr("returnUrl");
		}
		if (StringUtils.isEmpty(returnUrl)||"bestv".equalsIgnoreCase(returnUrl)) {
			returnUrl = Configuration.formatEpgUrl(rc);
		}
		rc.sessionAdd("returnUrl", returnUrl);
		log.debug("[the9Entry]: returnUrl ==> "+returnUrl);
		
		userId = (String)rc.sessionAttr("userId");
		accountName = (String)rc.sessionAttr("accountName");
		if (StringUtils.isNotEmpty(userId)
				&& StringUtils.isNotEmpty(accountName)) {
			Integer accountId = (Integer)rc.sessionAttr("accountId");
			if (accountId == null) {
				Account account = accServ.read(userId);
				if (account == null) {
					account = accServ.addNewStbUser(userId);
				}
				accountId = account.getAccountId();
				rc.sessionAdd("accountId", account.getAccountId());
			}
			String userToken = "inner";
			rc.sessionAdd("userToken", userToken);
			authorize(rc, accountId, accountName, userId, userToken, productId, returnUrl);
		}
		else {
			login(rc, returnUrl);
		}
	}
}
