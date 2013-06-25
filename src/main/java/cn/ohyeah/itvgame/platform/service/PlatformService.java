package cn.ohyeah.itvgame.platform.service;

import java.util.Map;

import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.business.service.IPointsService;
import cn.ohyeah.itvgame.business.service.IRecharge;
import cn.ohyeah.itvgame.business.service.ISubscribe;
import cn.ohyeah.itvgame.business.service.impl.ChinagamesgdUtil;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.AccountPermission;
import cn.ohyeah.itvgame.platform.model.Authorization;
import cn.ohyeah.itvgame.platform.model.ProductDetail;
import cn.ohyeah.itvgame.platform.model.ProductPermission;
import cn.ohyeah.itvgame.platform.model.SubscribeProperties;

public class PlatformService {
	private static final AuthorizationService authServ;
	private static final ProductService productServ;
	private static final AccountService accServ;
	private static final AccountPermissionService apServ;
	private static final ProductPermissionService ppServ;
	private static final IRecharge rechargeImpl;
	private static final ISubscribe subscribeImpl;
	private static final IPointsService pointsServImpl;
	
	static {
		authServ = (AuthorizationService)BeanManager.getBean("authorizationService");
		productServ = (ProductService)BeanManager.getBean("productService");
		accServ = (AccountService)BeanManager.getBean("accountService");
		apServ = (AccountPermissionService)BeanManager.getBean("accountPermissionService");
		ppServ = (ProductPermissionService)BeanManager.getBean("productPermissionService");
		rechargeImpl = (IRecharge)BeanManager.getRechargeImplFacade();
		subscribeImpl = (ISubscribe)BeanManager.getSubscribeImplFacade();
		pointsServImpl = (IPointsService)BeanManager.getPointsServiceImplFacade();
	}
	
	public SubscribeProperties querySubscribeProperties(Map<String, Object> props, int accountId, int productId) {
		try {
			Account account = accServ.validateExist(props, accountId);
			AccountPermission ap = apServ.validateExist(props, account);
			productServ.validateExistProduct(props, productId);
			ProductDetail detail = productServ.validateExistProductDetail(props, productId);
			Authorization auth = authServ.validateExist(props, account, detail);
			ProductPermission pp = ppServ.validateExist(props, account, detail);
			
			SubscribeProperties sp = new SubscribeProperties();
			sp.setSupportSubscribe(ap.isSubscribePermissionValid()&pp.isSubscribePermissionValid());
			sp.setSubscribeAmountUnit(subscribeImpl.getAmountUnit(detail));
			sp.setSubscribeCashToAmountRatio(subscribeImpl.getCashToAmountRatio(detail));
			
			sp.setSupportPointsService(pointsServImpl.isSupportPointsService());
			sp.setPointsUnit(pointsServImpl.getPointsUnit());
			sp.setCashToPointsRatio(pointsServImpl.getCashToPointsRatio());
			if (sp.isSupportPointsService()) {
				sp.setAvailablePoints(pointsServImpl.queryAvailablePoints(account.getUserId()));
			}
			else {
				sp.setAvailablePoints(0);
			}
			
			sp.setSupportRecharge(rechargeImpl.isSupportRecharge(detail));
			sp.setExpendAmountUnit(rechargeImpl.getAmountUnit(detail));
			sp.setExpendCashToAmountUnit(rechargeImpl.getCashToAmountRatio(detail));
			sp.setBalance(rechargeImpl.queryBalance(props, account, detail, auth));
			sp.setRechargeRatio(detail.getRechargeRatio());
			
			return sp;
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	public ResultInfo addFavoritegd(String hosturl, int accountId, String userId, String accountName, int productId,
			String gameid, String spid, String code, String timeStmp) {
		try {
			return ChinagamesgdUtil.addFavorite(hosturl, userId, accountName, gameid, spid, code, timeStmp);
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
}
