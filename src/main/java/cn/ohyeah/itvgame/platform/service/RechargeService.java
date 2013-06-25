package cn.ohyeah.itvgame.platform.service;

import java.util.Map;

import cn.halcyon.dao.DBManager;
import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.business.service.IPointsService;
import cn.ohyeah.itvgame.business.service.IRecharge;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.Authorization;
import cn.ohyeah.itvgame.platform.model.ProductDetail;
import cn.ohyeah.itvgame.platform.model.SubscribePayType;

/**
 * 充值服务
 * @author maqian
 * @version 1.0
 */
public class RechargeService {
	private static final ProductService productServ;
	private static final AccountService accServ;
	private static final AuthorizationService authServ;
	private static final AccountPermissionService apServ;
	private static final ProductPermissionService ppServ;
	private static final IRecharge rechargeImpl;
	private static final IPointsService pointsServImpl;
	
	static {
		productServ = (ProductService)BeanManager.getBean("productService");
		accServ = (AccountService)BeanManager.getBean("accountService");
		authServ = (AuthorizationService)BeanManager.getBean("authorizationService");
		apServ = (AccountPermissionService)BeanManager.getBean("accountPermissionService");
		ppServ = (ProductPermissionService)BeanManager.getBean("productPermissionService");
		rechargeImpl = (IRecharge)BeanManager.getRechargeImplFacade();
		pointsServImpl = (IPointsService)BeanManager.getPointsServiceImplFacade();
	}
	
	public boolean isSupportRecharge(int productId) {
		ProductDetail detail = productServ.validateExistProductDetail(productId);
		return rechargeImpl.isSupportRecharge(detail);
	}
	
	public String getExpendAmountUnit(int productId) {
		ProductDetail detail = productServ.validateExistProductDetail(productId);
		return rechargeImpl.getAmountUnit(detail);
	}
	
	public ResultInfo recharge(Map<String, Object> props, int accountId, int productId, 
			int amount, String remark, java.util.Date time) {

		Account account = accServ.validateExist(props, accountId);
		apServ.validateExist(props, account);
		productServ.validateExistProduct(props, productId);
		ProductDetail detail = productServ.validateExistProductDetail(props, productId);
		Authorization auth = authServ.validateExist(props, account, detail);
		ppServ.validateExist(props, account, detail);
		
		int newAmount = amount;
		//如果为积分，转为元
		int payType = (Integer)props.get("payType");
		if (payType == SubscribePayType.PAY_TYPE_POINTS) {
			newAmount /= pointsServImpl.getCashToPointsRatio();
		}
		
		try {
			DBManager.setAutoCommit(false);
			ResultInfo info = rechargeImpl.recharge(props, account, detail, auth, newAmount, remark, time);
			DBManager.commit();
			DBManager.setAutoCommit(true);
			return info;
		}
		catch (Exception e) {
			DBManager.rollback();
			throw new ServiceException(e);
		}
	}
	
	public ResultInfo queryBanlance(Map<String, Object> props, int accountId, int productId) {
		
		Account account = accServ.validateExist(props, accountId);
		productServ.validateExistProduct(props, productId);
		ProductDetail detail = productServ.validateExistProductDetail(props, productId);
		Authorization auth = authServ.validateExist(props, account, detail);
		
		try {
			ResultInfo info = new ResultInfo();
			int balance = rechargeImpl.queryBalance(props, account, detail, auth);
			info.setInfo(balance);
			return info;
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}
