package cn.ohyeah.itvgame.platform.dao;

import java.util.List;

import cn.ohyeah.itvgame.platform.model.GameRecord;
import cn.ohyeah.itvgame.platform.viewmodel.GameRecordDesc;

public interface IGameRecordDao {
	public void save(String tableName, GameRecord record);
	public void saveOrUpdate(String tableName, GameRecord record);
	public GameRecord read(String tableName, int accountId, int recordId);
	public void update(String tableName, GameRecord record);
	public List<GameRecordDesc> queryDescList(String tableName, int accountId);
}
