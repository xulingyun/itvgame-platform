package cn.ohyeah.itvgame.platform.service;

import java.util.List;
import java.util.Map;

import cn.halcyon.dao.DBException;
import cn.ohyeah.itvgame.business.ErrorCode;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.dao.IAccountPropDao;
import cn.ohyeah.itvgame.platform.dao.IGamePropDao;
import cn.ohyeah.itvgame.platform.model.GameProp;
import cn.ohyeah.itvgame.platform.viewmodel.OwnPropDesc;

/**
 * 道具服务接口
 * @author maqian
 * @vesion 1.0
 */
public class GamePropService {
	private static final IGamePropDao propDao;
	private static final IAccountPropDao accPropDao;
	
	static {
		propDao = (IGamePropDao)BeanManager.getDao("gamePropDao");
		accPropDao = (IAccountPropDao)BeanManager.getDao("accountPropDao");
	}
	
	/**
	 * 查询游戏道具列表
	 * @param productId
	 * @return
	 */
	public List<GameProp> queryPropList(int productId) {
		try {
			return propDao.queryPropList(productId);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * 查询拥有的道具列表
	 * @param accountId
	 * @param productId
	 */
	public  List<OwnPropDesc> queryOwnPropList(int accountId, int productId) {
		try {
			return accPropDao.queryAccountPropList(accountId, productId);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	
	public void save(GameProp prop) {
		try {
			propDao.save(prop);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public GameProp read(int propId) {
		try {
			return propDao.read(propId);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public void useProps(int accountId, int productId, int[] propIds, int[] nums) {
		try {
			accPropDao.useProps(accountId, productId, propIds, nums);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public void synProps(int accountId, int productId, int[] propIds, int[] counts) {
		try {
			accPropDao.synProps(accountId, productId, propIds, counts);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public GameProp validateExist(Map<String, Object> props, int propId) {
		GameProp gameProp = (GameProp)props.get("gameProp");
		if (gameProp == null) {
			gameProp = read(propId);
			if (gameProp == null) {
				throw new ServiceException(ErrorCode.getErrorMessage(ErrorCode.EC_INVALID_PROP));
			}
			props.put("gameProp", gameProp);
		}
		return gameProp;
	}

}
