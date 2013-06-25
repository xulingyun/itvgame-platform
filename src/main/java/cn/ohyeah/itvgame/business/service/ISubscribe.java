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
	 * ��ȡ����ҵ����ѵ�λ�ı���
	 * @return
	 */
	public int getCashToAmountRatio(ProductDetail detail);
	
	/**
	 * ��ȡ�Ʒѵ�λ
	 * @param detail
	 * @return
	 */
	public String getAmountUnit(ProductDetail detail);
	
	public PurchaseRelation queryPurchaseRelation(ProductDetail detail, String subscribeType, int period, int amount);

	/**
	 * ������ws��ʽ
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
	 * �����������ض���ʽ����Ӧ
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
	 * �����������ض���ʽ������
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
