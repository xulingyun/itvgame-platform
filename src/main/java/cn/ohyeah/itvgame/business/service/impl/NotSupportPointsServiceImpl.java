package cn.ohyeah.itvgame.business.service.impl;

import cn.ohyeah.itvgame.business.service.IPointsService;

public class NotSupportPointsServiceImpl implements IPointsService {

	@Override
	public boolean isSupportPointsService() {
		return false;
	}

	@Override
	public int queryAvailablePoints(String userId) {
		return 0;
	}

	@Override
	public String getPointsUnit() {
		return "����";
	}

	@Override
	public int getCashToPointsRatio() {
		return 100;
	}

}
