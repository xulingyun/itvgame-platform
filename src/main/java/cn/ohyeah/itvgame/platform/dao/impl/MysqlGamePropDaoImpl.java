package cn.ohyeah.itvgame.platform.dao.impl;

import java.util.List;

import cn.halcyon.dao.QueryHelper;
import cn.ohyeah.itvgame.global.Configuration;
import cn.ohyeah.itvgame.platform.dao.IGamePropDao;
import cn.ohyeah.itvgame.platform.model.GameProp;

public class MysqlGamePropDaoImpl implements IGamePropDao {

	@Override
	public List<GameProp> queryPropList(int productId) {
		return QueryHelper.query(GameProp.class, "select * from GameProp where productId=?", productId);
	}

	@Override
	public GameProp read(int propId) {
		return QueryHelper.read_cache(GameProp.class, "gameProp", propId,
				"select * from GameProp where propId=?", propId);
	}

	@Override
	public void save(GameProp prop) {
		QueryHelper.update("insert into " +
				"GameProp(propName, price, validPeriod, productId, feeCode, description) values(?, ?, ?, ?, ?, ?)", 
				prop.getPropName(), prop.getPrice(), prop.getValidPeriod(), 
				prop.getProductId(), prop.getFeeCode(), prop.getDescription());
		int propId =  QueryHelper.read(long.class, "select LAST_INSERT_ID()").intValue();
		prop.setPropId(propId);
		Configuration.setCache("gameProp", prop.getPropId(), prop);
	}

}
