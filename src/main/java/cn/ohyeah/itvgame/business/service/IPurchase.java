package cn.ohyeah.itvgame.business.service;

import java.util.Map;

import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.Authorization;
import cn.ohyeah.itvgame.platform.model.GameProp;
import cn.ohyeah.itvgame.platform.model.ProductDetail;

public interface IPurchase {
	/**
	 * 购买道具
	 * @param props
	 * @param account
	 * @param detail
	 * @param auth
	 * @param prop
	 * @param propCount
	 * @param remark
	 * @return
	 */
	public ResultInfo purchase(Map<String, Object> props, Account account,
			ProductDetail detail, Authorization auth, GameProp prop,
			int propCount, String remark);

	/**
	 * 消费元宝
	 * @param props
	 * @param account
	 * @param detail
	 * @param auth
	 * @param amount
	 * @param remark
	 * @return
	 */
	public ResultInfo expend(Map<String, Object> props, Account account,
			ProductDetail detail, Authorization auth, int amount, String remark);
}
