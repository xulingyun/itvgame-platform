package cn.ohyeah.itvgame.business.service;

import java.io.IOException;
import java.util.Map;

import cn.halcyon.utils.RequestContext;
import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.Authorization;
import cn.ohyeah.itvgame.platform.model.ProductDetail;
import cn.ohyeah.itvgame.platform.model.PurchaseRelation;

public interface ISubscribe {
	/**
	 * 获取人民币到消费单位的比例
	 * @return
	 */
	public int getCashToAmountRatio(ProductDetail detail);
	
	/**
	 * 获取计费单位
	 * @param detail
	 * @return
	 */
	public String getAmountUnit(ProductDetail detail);
	
	public PurchaseRelation queryPurchaseRelation(ProductDetail detail, String subscribeType, int period, int amount);

	/**
	 * 订购，ws方式
	 * @param props
	 * @param account
	 * @param detail
	 * @param auth
	 * @param pr
	 * @param remark
	 * @param time
	 * @return
	 */
	public ResultInfo subscribe(Map<String, Object> props, Account account,
			ProductDetail detail, Authorization auth, PurchaseRelation pr,
			String remark, java.util.Date time);

	/**
	 * 订购，处理重定向方式的响应
	 * @param rc
	 * @param props
	 * @param account
	 * @param detail
	 * @param auth
	 * @param pr
	 * @param remark
	 * @param time
	 * @return
	 */
	public ResultInfo subscribeRsp(RequestContext rc,
			Map<String, Object> props, Account account, ProductDetail detail,
			Authorization auth, PurchaseRelation pr, String remark,
			java.util.Date time);

	/**
	 * 订购，发起重定向方式的请求
	 * @param rc
	 * @param props
	 * @param returnUrl
	 * @param account
	 * @param detail
	 * @param auth
	 * @param pr
	 * @param time
	 * @return
	 * @throws IOException
	 */
	public ResultInfo subscribeReq(RequestContext rc,
			Map<String, Object> props, String returnUrl, Account account,
			ProductDetail detail, Authorization auth, PurchaseRelation pr,
			java.util.Date time) throws IOException;
}
