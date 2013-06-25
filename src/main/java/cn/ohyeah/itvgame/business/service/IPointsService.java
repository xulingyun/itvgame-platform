package cn.ohyeah.itvgame.business.service;

public interface IPointsService {
	/**
	 * �Ƿ�֧�ֻ��ֶһ�
	 * @return
	 */
	public boolean isSupportPointsService();
	
	/**
	 * ��ȡ��ǰ���û���
	 * @param userId
	 * @return
	 */
	public int queryAvailablePoints(String userId);
	
	
	/**
	 * ��ȡ���ֵ�λ
	 * @return
	 */
	public String getPointsUnit();
	
	
	/**
	 * ��ȡ�ֽ𵽻��ֵĶһ�����
	 * @return
	 */
	public int getCashToPointsRatio();
	
	
}
