package cn.ohyeah.itvgame.platform.dao;

import java.util.List;

import cn.ohyeah.itvgame.platform.model.GameAttainment;
import cn.ohyeah.itvgame.platform.viewmodel.GameAttainmentDesc;
import cn.ohyeah.itvgame.platform.viewmodel.GameRanking;

public interface IGameAttainmentDao {

	public void save(String tableName, GameAttainment attainment);
	public void saveOrUpdate(String tableName, GameAttainment attainment);
	
	public GameAttainment read(String tableName, int accountId, int attainmentId, String orderCmd);
	public GameAttainment read(String tableName, int accountId, int attainmentId, String orderCmd, 
			java.util.Date start, java.util.Date end);
	
	public void update(String tableName, GameAttainment attainment);

	public List<GameRanking> queryRankingList(String tableName, String orderCmd, int offset, int length);
	public List<GameRanking> queryRankingList(String tableName, String orderCmd, 
			java.util.Date start, java.util.Date end, int offset, int length);
	
	public List<GameAttainmentDesc> queryDescList(String tableName, int accountId, String orderCmd);
	public List<GameAttainmentDesc> queryDescList(String tableName, int accountId, String orderCmd, 
			java.util.Date start, java.util.Date end);
	
}
