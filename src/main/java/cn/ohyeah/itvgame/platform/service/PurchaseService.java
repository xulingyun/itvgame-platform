package cn.ohyeah.itvgame.platform.service;

import java.util.Map;

import cn.halcyon.dao.DBManager;
import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.business.service.IPurchase;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.Authorization;
import cn.ohyeah.itvgame.platform.model.GameProp;
import cn.ohyeah.itvgame.platform.model.ProductDetail;

/**
 * 计费服务
 * @author maqian
 * @version 1.0
 */
public class PurchaseService {
	private static final AccountService accServ;
	private static final ProductService productServ;
	private static final GamePropService propServ;
	private static final AuthorizationService authServ;
	private static final AccountPermissionService apServ;
	private static final ProductPermissionService ppServ;
	private static final IPurchase purchaseImpl;
	
	static {
		accServ = (AccountService)BeanManager.getBean("accountService");
		productServ = (ProductService)BeanManager.getBean("productService");
		propServ = (GamePropService)BeanManager.getBean("gamePropService");
		authServ = (AuthorizationService)BeanManager.getBean("authorizationService");
		apServ = (AccountPermissionService)BeanManager.getBean("accountPermissionService");
		ppServ = (ProductPermissionService)BeanManager.getBean("productPermissionService");
		purchaseImpl = (IPurchase)BeanManager.getPurchaseImplFacade();
	}
	
	/**
	 * 购买道具
	 * @param accountId
	 * @param productId
	 * @param propId
	 * @param remark
	 * @param ip
	 */

	public ResultInfo purchaseProp(Map<String, Object> props, int accountId, int productId, 
			int propId, int propCount, String remark) {
		
		Account account = accServ.validateExist(props, accountId);
		apServ.validateExist(props, account);
		productServ.validateExistProduct(props, productId);
		ProductDetail detail = productServ.validateExistProductDetail(props, productId);
		Authorization auth = authServ.validateExist(props, account, detail);
		ppServ.validateExist(props, account, detail);
		GameProp gameProp = propServ.validateExist(props, propId);
		
		try {
			DBManager.setAutoCommit(false);
			ResultInfo info = purchaseImpl.purchase(props, account, detail, auth, gameProp, propCount, remark);
			DBManager.commit();
			DBManager.setAutoCommit(true);
			return info;
		}
		catch (Exception e) {
			DBManager.rollback();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * 花费金币
	 * @param accountId
	 * @param productId
	 * @param amount
	 * @param remark
	 * @param ip
	 */
	public ResultInfo expend(Map<String, Object> props, int accountId, int productId, 
			int amount, String remark) {
		
		Account account = accServ.validateExist(props, accountId);
		apServ.validateExist(props, account);
		productServ.validateExistProduct(props, productId);
		ProductDetail detail = productServ.validateExistProductDetail(props, productId);
		Authorization auth = authServ.validateExist(props, account, detail);
		ppServ.validateExist(props, account, detail);
		
		try {
			DBManager.setAutoCommit(false);
			ResultInfo info = purchaseImpl.expend(props, account, detail, auth, amount, remark);
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
