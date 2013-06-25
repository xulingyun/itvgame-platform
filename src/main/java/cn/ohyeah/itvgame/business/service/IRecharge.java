package cn.ohyeah.itvgame.business.service;

import java.util.Map;

import cn.ohyeah.itvgame.business.ResultInfo;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.Authorization;
import cn.ohyeah.itvgame.platform.model.ProductDetail;

public interface IRecharge {
	/**
	 * ��ֵ
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
	 * ��ѯ���
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
	 * ���ѽ��
	 * 
	 * @param props
	 * @param amount
	 * @return
	 */
	public ResultInfo expend(Map<String, Object> props, Account account,
			ProductDetail detail, Authorization auth, int amount);

	/**
	 * �Ƿ�֧�ֳ�ֵ
	 * 
	 * @return
	 */
	public boolean isSupportRecharge(ProductDetail detail);

	/**
	 * ��ѯ���ҵ�λ
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
