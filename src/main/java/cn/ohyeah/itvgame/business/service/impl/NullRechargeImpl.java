package cn.ohyeah.itvgame.business.service.impl;

import java.util.Date;
import java.util.Map;

import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.business.service.BusinessException;
import cn.ohyeah.itvgame.business.service.IRecharge;
import cn.ohyeah.itvgame.global.Configuration;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.Authorization;
import cn.ohyeah.itvgame.platform.model.ProductDetail;

public class NullRechargeImpl implements IRecharge{
	@Override
	public ResultInfo expend(Map<String, Object> props, Account account, ProductDetail detail, Authorization auth, int amount) {
		throw new BusinessException("不支持计费操作");
	}

	@Override
	public boolean isSupportRecharge(ProductDetail detail) {
		return false;
	}

	@Override
	public String getAmountUnit(ProductDetail detail) {
		return Configuration.getAmountUnit(Configuration.getSubscribeImplementor());
	}

	@Override
	public int queryBalance(Map<String, Object> props, Account account,
			ProductDetail detail, Authorization auth) {
		return 0;
	}

	@Override
	public ResultInfo recharge(Map<String, Object> props, Account account,
			ProductDetail detail, Authorization auth, int amount, String remark, Date time) {
		throw new BusinessException("不支持充值操作");
	}

	@Override
	public int getCashToAmountRatio(ProductDetail detail) {
		return Configuration.getCashToAmountRatio(Configuration.getSubscribeImplementor());
	}

}
