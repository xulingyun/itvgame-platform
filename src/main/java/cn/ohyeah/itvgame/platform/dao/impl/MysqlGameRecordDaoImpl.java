package cn.ohyeah.itvgame.platform.dao.impl;

import java.util.List;

import cn.halcyon.dao.QueryHelper;
import cn.ohyeah.itvgame.platform.dao.IGameRecordDao;
import cn.ohyeah.itvgame.platform.model.GameRecord;
import cn.ohyeah.itvgame.platform.viewmodel.GameRecordDesc;

public class MysqlGameRecordDaoImpl implements IGameRecordDao {

	@Override
	public List<GameRecordDesc> queryDescList(String tableName, int accountId) {
		List<GameRecordDesc> descList = QueryHelper.query(GameRecordDesc.class, 
				"select recordId, playDuration, scores, time, remark from "+ tableName
				+ " where accountId=? and recordId>=0 order by accountId asc, recordId asc", 
				accountId);
		return descList;
	}

	@Override
	public GameRecord read(String tableName, int accountId, int recordId) {
		return QueryHelper.read(GameRecord.class, 
				"select * from "+tableName+" where accountId=? and recordId=?", 
				accountId, recordId);
	}

	@Override
	public void save(String tableName, GameRecord record) {
		QueryHelper.update("insert into "+tableName
				+ "(accountId, recordId, "
				+ "playDuration, scores, time, remark, data) "
				+ "values(?, ?, ?, ?, ?, ?, ?)", 
				record.getAccountId(), record.getRecordId(), 
				record.getPlayDuration(), record.getScores(), record.getTime(), 
				record.getRemark(), record.getData());
	}

	@Override
	public void update(String tableName, GameRecord record) {
		QueryHelper.update("update "+tableName+" set "
				+ "playDuration=?, scores=?, time=?, remark=?, data=? "
				+ "where accountId=? and recordId=?",
				record.getPlayDuration(), record.getScores(), record.getTime(), 
				record.getRemark(), record.getData(), record.getAccountId(), 
				record.getRecordId());
	}

	@Override
	public void saveOrUpdate(String tableName, GameRecord record) {
		QueryHelper.update("insert into "+tableName
				+ "(accountId, recordId, "
				+ "playDuration, scores, time, remark, data) "
				+ "values(?, ?, ?, ?, ?, ?, ?) "
				+ "on duplicate key update "
				+ "playDuration=values(playDuration), scores=values(scores), " 
				+ "time=values(time), remark=values(remark), data=values(data)",
				record.getAccountId(), record.getRecordId(), 
				record.getPlayDuration(), record.getScores(), record.getTime(), 
				record.getRemark(), record.getData());
	}

}
