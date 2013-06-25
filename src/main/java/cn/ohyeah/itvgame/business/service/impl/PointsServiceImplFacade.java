package cn.ohyeah.itvgame.business.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ohyeah.itvgame.business.service.BusinessException;
import cn.ohyeah.itvgame.business.service.IPointsService;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.global.Configuration;

public class PointsServiceImplFacade implements IPointsService {
	private static final Log log = LogFactory.getLog(PointsServiceImplFacade.class);
	
	@Override
	public boolean isSupportPointsService() {
		try {
			IPointsService pointsServImpl = BeanManager.getPointsServiceImpl();
			return pointsServImpl.isSupportPointsService();
		}
		catch (Exception e) {
			log.error("[error in isSupportPointsService]", e);
			throw new BusinessException(e);
		}
	}

	@Override
	public int queryAvailablePoints(String userId) {
		try {
			IPointsService pointsServImpl = BeanManager.getPointsServiceImpl();
			return pointsServImpl.queryAvailablePoints(userId);
		}
		catch (Exception e) {
			log.error("[error in queryAvailablePoints]", e);
			throw new BusinessException(e);
		}
	}

	@Override
	public String getPointsUnit() {
		return Configuration.getTelcomOperatorPointsUnit();
	}

	@Override
	public int getCashToPointsRatio() {
		return Configuration.getTelcomOperatorCashToPointsRatio();
	}

}
