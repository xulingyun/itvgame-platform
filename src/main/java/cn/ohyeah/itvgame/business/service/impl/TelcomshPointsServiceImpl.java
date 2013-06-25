package cn.ohyeah.itvgame.business.service.impl;

import cn.ohyeah.itvgame.business.service.IPointsService;
import cn.ohyeah.itvgame.global.Configuration;

public class TelcomshPointsServiceImpl implements IPointsService {
	private static final boolean supportPoints;
	private static final String pointsUnit;
	private static final int cashToPointsRatio;
	
	static {
		supportPoints = Configuration.isSupportPointsService("telcomsh");
		pointsUnit = Configuration.getPointsUnit("telcomsh");
		cashToPointsRatio = Configuration.getCashToPointsRatio("telcomsh");
	}
	
	@Override
	public boolean isSupportPointsService() {
		return supportPoints;
	}

	@Override
	public int queryAvailablePoints(String userId) {
		String adslName = TelcomshSubscribeUtil.queryAdslName(userId);
		return TelcomshSubscribeUtil.queryAvailablePoints(adslName);
	}

	@Override
	public String getPointsUnit() {
		return pointsUnit;
	}

	@Override
	public int getCashToPointsRatio() {
		return cashToPointsRatio;
	}
}
