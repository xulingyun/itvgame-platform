package cn.ohyeah.itvgame.platform.dao;

import java.util.List;

import cn.ohyeah.itvgame.platform.model.GameProp;

public interface IGamePropDao {
	public void save(GameProp prop);
	public GameProp read(int propId);
	public List<GameProp> queryPropList(int productId);
}
