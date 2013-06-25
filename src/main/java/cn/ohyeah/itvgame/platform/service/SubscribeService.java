package cn.ohyeah.itvgame.platform.service;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.halcyon.dao.DBManager;
import cn.halcyon.utils.RequestContext;
import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.business.service.IPointsService;
import cn.ohyeah.itvgame.business.service.ISubscribe;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.global.Configuration;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.AccountPermission;
import cn.ohyeah.itvgame.platform.model.Authorization;
import cn.ohyeah.itvgame.platform.model.ProductDetail;
import cn.ohyeah.itvgame.platform.model.ProductPermission;
import cn.ohyeah.itvgame.platform.model.PurchaseRelation;
import cn.ohyeah.itvgame.platform.model.SubscribePayType;

public class SubscribeService {
	private static final Log log = LogFactory.getLog(SubscribeService.class);
	
	private static final PurchaseRelationService prServ;
	private static final ProductService productServ;
	private static final AccountService accServ;
	private static final AuthorizationService authServ;
	private static final AccountPermissionService apServ;
	private static final ProductPermissionService ppServ;
	private static final ISubscribe subscribeImpl;
	private static final IPointsService pointsServImpl;
	
	static {
		prServ = (PurchaseRelationService)BeanManager.getBean("purchaseRelationService");
		productServ = (ProductService)BeanManager.getBean("productService");
		accServ = (AccountService)BeanManager.getBean("accountService");
		authServ = (AuthorizationService)BeanManager.getBean("authorizationService");
		apServ = (AccountPermissionService)BeanManager.getBean("accountPermissionService");
		ppServ = (ProductPermissionService)BeanManager.getBean("productPermissionService");
		subscribeImpl = BeanManager.getSubscribeImplFacade();
		pointsServImpl = (IPointsService)BeanManager.getPointsServiceImplFacade();
	}
	
	public boolean isSupportSubscribe(int accountId, int productId) {
		Account account = accServ.validateExist(accountId);
		AccountPermission ap = apServ.validateExist(account);
		ProductDetail detail = productServ.validateExistProductDetail(productId);
		ProductPermission pp = ppServ.validateExist(account, detail);
		return ap.isSubscribePermissionValid()&&pp.isSubscribePermissionValid();
	}
	
	public String getSubscribeAmountUnit(int productId) {
		ProductDetail detail = productServ.validateExistProductDetail(productId);
		return subscribeImpl.getAmountUnit(detail);
	}
	
	public ResultInfo subscribeProduct(Map<String, Object> props, int accountId, int productId, 
			String subscribeType, String remark) {
		
		Account account = accServ.validateExist(props, accountId);
		apServ.validateExist(props, account);
		productServ.validateExistProduct(props, productId);
		ProductDetail detail = productServ.validateExistProductDetail(props, productId);
		Authorization auth = authServ.validateExist(props, account, detail);
		ppServ.validateExist(props, account, detail);
		
		String subType = subscribeType.toLowerCase();
		int amount = detail.getPurchaseTypeFee(subType);
		int value = detail.getPurchaseTypeValue(subType);
		int newAmount = amount;
		//如果为积分，转为元
		int payType = (Integer)props.get("payType");
		if (payType == SubscribePayType.PAY_TYPE_POINTS) {
			newAmount /= pointsServImpl.getCashToPointsRatio();
		}
		String subImpl = Configuration.getSubscribeImplementor();
		log.debug("[validate pr] ==> (productId="+productId+",subImpl="+subImpl+",subType="+subscribeType+",value="+value+",amount="+newAmount+")");
		//PurchaseRelation pr = prServ.validateExist(props, productId, subImpl, subscribeType, value, newAmount);
		PurchaseRelation pr = (PurchaseRelation)props.get("purchaseRelation");
		if (pr == null) {
			//pr = read(productId, subscribeImplementor, subscribeType, value, amount);
			pr = subscribeImpl.queryPurchaseRelation(detail, subscribeType, value, newAmount);
			if (pr == null) {
				throw new ServiceException(ErrorCode.getErrorMessage(ErrorCode.EC_INVALID_PURCHASE_ID));
			}
			props.put("purchaseRelation", pr);
		}
		java.util.Date now = new java.util.Date();
		
		try {
			DBManager.setAutoCommit(false);
			ResultInfo info = subscribeImpl.subscribe(props, account, detail, auth, pr, remark, now);
			if (info.isSuccess()){
				authServ.updateAuthorizationAfterSubscribe(account, detail, pr, now);
			}
			DBManager.commit();
			DBManager.setAutoCommit(true);
			return info;
		}
		catch (Exception e) {
			DBManager.rollback();
			throw new ServiceException(e);
		}
	}
	
	public ResultInfo subscribe(Map<String, Object> props, int accountId, int productId, 
			int purchaseId, String remark, java.util.Date time) {
		
		Account account = accServ.validateExist(props, accountId);
		apServ.validateExist(props, account);
		productServ.validateExistProduct(props, productId);
		ProductDetail detail = productServ.validateExistProductDetail(props, productId);
		Authorization auth = authServ.validateExist(props, account, detail);
		ppServ.validateExist(props, account, detail);
		PurchaseRelation pr = prServ.validateExist(props, purchaseId);
		
		try {
			DBManager.setAutoCommit(false);
			ResultInfo info = subscribeImpl.subscribe(props, account, detail, auth, pr, remark, time);
			DBManager.commit();
			DBManager.setAutoCommit(true);
			return info;
		}
		catch (Exception e) {
			DBManager.rollback();
			throw new ServiceException(e);
		}
	}
	
	public ResultInfo subscribeReq(RequestContext rc, Map<String, Object> props, String returnUrl,
			int accountId, int productId, int purchaseId, java.util.Date time) throws IOException {

		Account account = accServ.validateExist(props, accountId);
		apServ.validateExist(props, account);
		productServ.validateExistProduct(props, productId);
		ProductDetail detail = productServ.validateExistProductDetail(props, productId);
		Authorization auth = authServ.validateExist(props, account, detail);
		ppServ.validateExist(props, account, detail);
		PurchaseRelation pr = prServ.validateExist(props, purchaseId);
		
		return subscribeImpl.subscribeReq(rc, props, returnUrl, account, detail, auth, pr, time);
	}
	
	public ResultInfo subscribeRsp(RequestContext rc, Map<String, Object> props,
			int accountId, int productId, int purchaseId, String remark, java.util.Date time) {

		Account account = accServ.validateExist(props, accountId);
		apServ.validateExist(props, account);
		productServ.validateExistProduct(props, productId);
		ProductDetail detail = productServ.validateExistProductDetail(props, productId);
		Authorization auth = authServ.validateExist(props, account, detail);
		ppServ.validateExist(props, account, detail);
		PurchaseRelation pr = prServ.validateExist(props, purchaseId);
		
		try {
			DBManager.setAutoCommit(false);
			ResultInfo info = subscribeImpl.subscribeRsp(rc, props, account, detail, auth, pr, remark, time);
			DBManager.commit();
			DBManager.setAutoCommit(true);
			return info;
		}
		catch (Exception e) {
			DBManager.rollback();
			throw new ServiceException(e);
		}
	}
}
