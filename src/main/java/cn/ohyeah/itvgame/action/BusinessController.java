package cn.ohyeah.itvgame.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.codec.binary.Base64;

import cn.halcyon.utils.RequestContext;
import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.business.IpInfo;
import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.business.service.ISubscribe;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.global.Configuration;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.Authorization;
import cn.ohyeah.itvgame.platform.model.ProductDetail;
import cn.ohyeah.itvgame.platform.model.PurchaseRelation;
import cn.ohyeah.itvgame.platform.service.AccountService;
import cn.ohyeah.itvgame.platform.service.AuthorizationService;
import cn.ohyeah.itvgame.platform.service.ProductService;
import cn.ohyeah.itvgame.platform.service.PurchaseRelationService;
import cn.ohyeah.itvgame.platform.service.SubscribeService;
import cn.ohyeah.itvgame.utils.UrlUtil;

public class BusinessController {
	private static final ProductService productServ;
	private static final PurchaseRelationService prServ;
	private static final SubscribeService subServ;
	private static final AuthorizationService authServ;
	private static final AccountService accServ;
	private static final ISubscribe	subscribeImpl;
	
	static{
		productServ = (ProductService)BeanManager.getBean("productService");
		subServ = (SubscribeService)BeanManager.getBean("subscribeService");
		prServ = (PurchaseRelationService)BeanManager.getBean("purchaseRelationService");
		authServ = (AuthorizationService)BeanManager.getBean("authorizationService");
		accServ = (AccountService)BeanManager.getBean("accountService");
		subscribeImpl = BeanManager.getSubscribeImplFacade();
	}
	
	public void subscribe(RequestContext rc) throws IOException, ServletException {
		String returnUrl = rc.param("returnUrl");
		int productId = rc.param("productId", -1);
		int accountId = rc.param("accountId", -1);
		
		if (rc.sessionAttr("accountName")!=null && rc.sessionAttr("userToken")!=null) {
			String accountName = (String)rc.sessionAttr("accountName");
			String userToken = (String)rc.sessionAttr("userToken");
			ProductDetail detail = productServ.readProductDetail(productId);
			if (detail != null) {
				String fee = null;
				String promptText = null;
				String remark = null;
				PurchaseRelation pr = null;
				if (detail.isPurchaseTypeCount()) {
					remark = "按次订购产品"+detail.getProductName();
					promptText = "按次收费，可累计玩游戏"+detail.getValidCount()+"次，无需退订";
					fee = detail.getCountFee()+subscribeImpl.getAmountUnit(detail);
					pr = subscribeImpl.queryPurchaseRelation(detail, detail.getPurchaseType(), detail.getValidCount(), detail.getCountFee());
				}
				else if (detail.isPurchaseTypePeriod()) {
					remark = "按有效期订购产品"+detail.getProductName();
					int seconds = detail.getValidPeriod();
					if (seconds > 24*60*60) {
						promptText = "按次收费，有效期"+(seconds/(24*60*60))+"天，无需退订";
					}
					else {
						promptText = "按次收费，有效期"+seconds/60+"小时，无需退订";
					}
					fee = detail.getPeriodFee()+subscribeImpl.getAmountUnit(detail);
					pr = subscribeImpl.queryPurchaseRelation(detail, detail.getPurchaseType(), detail.getValidPeriod(), detail.getPeriodFee());
				}
				else {
					String url = UrlUtil.addParam(returnUrl, "result", Integer.toString(ErrorCode.EC_INVALID_PURCHASE_TYPE));
					String text = ErrorCode.getErrorMessage(ErrorCode.EC_INVALID_PURCHASE_TYPE);
					text = Base64.encodeBase64URLSafeString(text.getBytes("gbk"));
					url = UrlUtil.addParam(url, "promptText", text);
					rc.redirect(url);
					return;
				}
				
				if (pr==null || pr.getSubscribeId()==null) {
					String url = UrlUtil.addParam(returnUrl, "result", Integer.toString(ErrorCode.EC_INVALID_PURCHASE_ID));
					String text = ErrorCode.getErrorMessage(ErrorCode.EC_INVALID_PURCHASE_ID);
					text = Base64.encodeBase64URLSafeString(text.getBytes("gbk"));
					url = UrlUtil.addParam(url, "promptText", text);
					rc.redirect(url);
					return;
				}
				
				ResultInfo info = null;
				if (Configuration.isSubscribeModeRedirect(pr.getSubscribeImplementor())) {
					Map<String, Object> props = new HashMap<String, Object>();
					props.put("accountName", accountName);
					props.put("userToken", userToken);
					props.put("productDetail", detail);
					props.put("purchaseRelation", pr);
					
					String subUrl = Configuration.formatProductSubActionUrl(rc);
					subUrl = UrlUtil.addParam(subUrl, "accountId", Integer.toString(accountId));
					subUrl = UrlUtil.addParam(subUrl, "accountName", accountName);
					subUrl = UrlUtil.addParam(subUrl, "userToken", userToken);
					subUrl = UrlUtil.addParam(subUrl, "productId", Integer.toString(productId));
					subUrl = UrlUtil.addParam(subUrl, "purchaseId", Integer.toString(pr.getPurchaseId()));
					subUrl = UrlUtil.addParam(subUrl, "remark", Base64.encodeBase64URLSafeString(remark.getBytes("gbk")));
					subUrl = UrlUtil.addParam(subUrl, "returnUrl", Base64.encodeBase64URLSafeString(returnUrl.getBytes("gbk")));
					
					info = subServ.subscribeReq(rc, props, subUrl, accountId, productId, pr.getPurchaseId(), new java.util.Date());
					if (!info.isSuccess()) {
						returnUrl = UrlUtil.addParam(returnUrl, "result", Integer.toString(info.getErrorCode()));
						String text = Base64.encodeBase64URLSafeString(info.getMessage().getBytes("gbk"));
						returnUrl = UrlUtil.addParam(returnUrl, "promptText", text);
						rc.redirect(returnUrl);
					}
				}
				else {
					rc.requestAdd("subscribeUrl", Configuration.formatProductSubActionUrl(rc));
					rc.requestAdd("accountId", Integer.toString(accountId));
					rc.requestAdd("accountName", accountName);
					rc.requestAdd("userToken", userToken);
					rc.requestAdd("productId", Integer.toString(productId));
					rc.requestAdd("purchaseId", Integer.toString(pr.getPurchaseId()));
					rc.requestAdd("returnUrl", Base64.encodeBase64URLSafeString(returnUrl.getBytes("gbk")));
					String cancelUrl = UrlUtil.addParam(returnUrl, "result", Integer.toString(ErrorCode.EC_USER_CANCELED));
					cancelUrl = UrlUtil.addParam(cancelUrl, "hideShow", "true");
					rc.requestAdd("cancelUrl", cancelUrl);
					rc.requestAdd("productName", detail.getProductName());
					rc.requestAdd("fee", fee);
					rc.requestAdd("promptText", promptText);
					rc.requestAdd("remark", Base64.encodeBase64URLSafeString(remark.getBytes("gbk")));
					
					rc.forward(Configuration.getSubscribeConfirmUrl());
				}
			}
			else {
				rc.print("产品不存在, productId="+productId);
			}
		}
		else {
			rc.print("session已过期");
		}
	}
	
	public void subAction(RequestContext rc) throws IOException, ServletException {
		int accountId = rc.param("accountId", -1);
		int productId = rc.param("productId", -1);
		int purchaseId = rc.param("purchaseId", -1);
		String remark = new String(Base64.decodeBase64(rc.param("remark")), "gbk");
		String returnUrl = new String(Base64.decodeBase64(rc.param("returnUrl")), "gbk");
		
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("accountName", rc.param("accountName"));
		props.put("userToken", rc.param("userToken"));
		props.put("ip", IpInfo.ip());
		java.util.Date now = new java.util.Date();
		
		Account account = accServ.read(accountId);
		ProductDetail detail = productServ.readProductDetail(productId);
		PurchaseRelation pr = prServ.read(purchaseId);
		
		props.put("account", account);
		props.put("productDetail", detail);
		props.put("purchaseRelation", pr);
		
		ResultInfo info = null;
		if (Configuration.isSubscribeModeRedirect(pr.getSubscribeImplementor())) {
			info = subServ.subscribeRsp(rc, props, accountId, productId, purchaseId, remark, now);
			if (info.isSuccess()) {
				authServ.updateAuthorizationAfterSubscribe(account, detail, pr, now);
			}
			returnUrl = UrlUtil.addParam(returnUrl, "result", Integer.toString(info.getErrorCode()));
			returnUrl = UrlUtil.addParam(returnUrl, "hideShow", "true");
			rc.redirect(returnUrl);
		}
		else {
			info = subServ.subscribe(props, accountId, productId, purchaseId, remark, now);
			returnUrl = UrlUtil.addParam(returnUrl, "result", Integer.toString(info.getErrorCode()));
			returnUrl = UrlUtil.addParam(returnUrl, "hideShow", "true");
			String msg = null;
			if (info.isSuccess()) {
				msg = "订购成功";
				authServ.updateAuthorizationAfterSubscribe(account, detail, pr, now);
			}
			else {
				msg = "订购失败，"+info.getMessage();
			}
			rc.requestAdd("promptText", msg);
			rc.requestAdd("returnUrl", returnUrl);
			rc.forward(Configuration.getSubscribeResultUrl());
		}
	}
	
	public void tryGame(RequestContext rc) throws IOException, ServletException {
		int accountId = rc.param("accountId", -1);
		int productId = rc.param("productId", -1);
		String returnUrl = rc.param("returnUrl");
		
		ProductDetail detail = productServ.readProductDetail(productId);
		if (detail != null) {
			Authorization auth = authServ.read(accountId, productId);
			if (auth != null) {
				rc.requestAdd("productName", detail.getProductName());
				rc.requestAdd("leftTryNum", auth.getLeftTryNumber());
				rc.requestAdd("tryUrl", Configuration.formatProductTryActionUrl(rc));
				String cancelUrl = UrlUtil.addParam(returnUrl, "result", Integer.toString(ErrorCode.EC_USER_CANCELED));
				cancelUrl = UrlUtil.addParam(cancelUrl, "hideShow", "true");
				rc.requestAdd("cancelUrl", cancelUrl);
				rc.requestAdd("returnUrl", Base64.encodeBase64URLSafeString(returnUrl.getBytes("gbk")));
				rc.requestAdd("accountId", Integer.toString(accountId));
				rc.requestAdd("productId", Integer.toString(productId));
				String url = Configuration.getTryConfirmUrl();
				rc.forward(url);
			}
			else {
				rc.print("鉴权信息不存在, accountId="+accountId+", productId="+productId);
			}
		}
		else {
			rc.print("产品不存在, productId="+productId);
		}
	}
	
	public void tryAction(RequestContext rc) throws IOException, ServletException {
		int accountId = rc.param("accountId", -1);
		int productId = rc.param("productId", -1);
		String returnUrl = rc.param("returnUrl");
		if (returnUrl != null) {
			returnUrl = new String(Base64.decodeBase64(returnUrl), "gbk");
		}
		
		Account account = accServ.read(accountId);
		if (account != null) {
			ProductDetail detail = productServ.readProductDetail(productId);
			if (detail != null) {
				authServ.updateAuthorizationAfterTry(account, detail);
				returnUrl = UrlUtil.addParam(returnUrl, "result", "0");
				rc.redirect(returnUrl);
			}
			else {
				rc.print("产品不存在,productId="+productId);
			}
		}
		else {
			rc.print("账号不存在, accountId="+accountId);
		}
	}

}
