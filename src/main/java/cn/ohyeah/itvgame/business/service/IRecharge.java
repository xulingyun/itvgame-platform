package cn.ohyeah.itvgame.business.service;

import java.util.Map;

import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.Authorization;
import cn.ohyeah.itvgame.platform.model.ProductDetail;

public interface IRecharge {
	/**
	 * 充值
	 * 
	 * @param props
	 * @param account
	 * @param detail
	 * @param auth
     * @param amount
	 * @param remark
	 * @param time
	 * @return
	 */
	public ResultInfo recharge(Map<String, Object> props, Account account,
			ProductDetail detail, Authorization auth, int amount,
			String remark, java.util.Date time);

	/**
	 * 查询余额
	 * 
	 * @param props
	 * @param account
	 * @param detail
	 * @param auth
	 * @return
	 */
	public int queryBalance(Map<String, Object> props, Account account,
			ProductDetail detail, Authorization auth);

	/**
	 * 花费金币
	 * 
	 * @param props
	 * @param amount
	 * @return
	 */
	public ResultInfo expend(Map<String, Object> props, Account account,
			ProductDetail detail, Authorization auth, int amount);

	/**
	 * 是否支持充值
	 * 
	 * @return
	 */
	public boolean isSupportRecharge(ProductDetail detail);

	/**
	 * 查询货币单位
	 * 
	 * @return
	 */
	public String getAmountUnit(ProductDetail detail);
	
	
	/**
	 * 
	 * @param detail
	 * @return
	 */
	public int getCashToAmountRatio(ProductDetail detail);
	
}
