package cn.ohyeah.itvgame.business.service;

public interface IPointsService {
	/**
	 * 是否支持积分兑换
	 * @return
	 */
	public boolean isSupportPointsService();
	
	/**
	 * 获取当前可用积分
	 * @param userId
	 * @return
	 */
	public int queryAvailablePoints(String userId);
	
	
	/**
	 * 获取积分单位
	 * @return
	 */
	public String getPointsUnit();
	
	
	/**
	 * 获取现金到积分的兑换比例
	 * @return
	 */
	public int getCashToPointsRatio();
	
	
}
