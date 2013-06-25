package cn.ohyeah.itvgame.platform.dao.impl;

import java.util.Date;
import java.util.List;

import cn.halcyon.dao.QueryHelper;
import cn.ohyeah.itvgame.platform.dao.IGameAttainmentDao;
import cn.ohyeah.itvgame.platform.model.GameAttainment;
import cn.ohyeah.itvgame.platform.viewmodel.GameAttainmentDesc;
import cn.ohyeah.itvgame.platform.viewmodel.GameRanking;
import cn.ohyeah.itvgame.utils.DateUtil;

public class SqlserverGameAttainmentDaoImpl implements IGameAttainmentDao{

	@Override
	public GameAttainment read(String tableName, int accountId, int attainmentId, String orderCmd) {
		GameAttainment attainment = QueryHelper.read(GameAttainment.class, "select * from "+
				"(select top 1 *, dense_rank() over( order by scores "+orderCmd+" ) ranking " +
				"from "+tableName+" where accountId=? and attainmentId=?) GA", 
				accountId, attainmentId);
		return attainment;
	}
	
	@Override
	public GameAttainment read(String tableName, int accountId, int attainmentId, String orderCmd, 
			Date start, Date end) {
		GameAttainment attainment = QueryHelper.read(GameAttainment.class, "select * from "+
				"(select top 1 *, dense_rank() over( order by scores "+orderCmd+" ) ranking " +
				"from "+tableName+" where accountId=? and attainmentId=? and time>=? and time<?) GA", 
				accountId, attainmentId,
				DateUtil.convertToSqlDate(start),
				DateUtil.convertToSqlDate(end));
		return attainment;
	}

	@Override
	public void save(String tableName, GameAttainment attainment) {
		QueryHelper.update("insert into "+tableName
				+ "(accountId, userId, attainmentId, "
				+ "playDuration, scores, time, remark, data) "
				+ "values(?, ?, ?, ?, ?, ?, ?, ?)", 
				attainment.getAccountId(), attainment.getUserId(), 
				attainment.getAttainmentId(), attainment.getPlayDuration(), attainment.getScores(), 
				DateUtil.convertToSqlDate(attainment.getTime()), 
				attainment.getRemark(), attainment.getData());
	}

	@Override
	public void update(String tableName, GameAttainment attainment) {
		QueryHelper.update("update "+tableName+" set "
				+ "playDuration=?, scores=?, time=?, remark=?, data=? "
				+ "where accountId=? and attainmentId=?",
				attainment.getPlayDuration(), attainment.getScores(), 
				DateUtil.convertToSqlDate(attainment.getTime()), 
				attainment.getRemark(), attainment.getData(), attainment.getAccountId(), 
				attainment.getAttainmentId());
	}

	@Override
	public List<GameAttainmentDesc> queryDescList(String tableName, int accountId, String orderCmd) {
		List<GameAttainmentDesc> descList = QueryHelper.query(GameAttainmentDesc.class, "select * from " +
				"(select attainmentId, playDuration, scores, remark, time, " +
				"dense_rank() ver(order by scores "+orderCmd+" ) ranking from "+
				tableName+" where accountId=?) GAD" +
				"order by GAD.accountId asc, GAD.attainmentId asc", 
				accountId);
		return descList;
	}
	
	@Override
	public List<GameAttainmentDesc> queryDescList(String tableName, int accountId, 
			String orderCmd, Date start, Date end) {
		List<GameAttainmentDesc> descList = QueryHelper.query(GameAttainmentDesc.class, "select * from " +
				"(select attainmentId, playDuration, scores, remark, time, " +
				"dense_rank() over(order by scores "+orderCmd+" ) ranking from "+
				tableName+" where accountId=? and time>=? and time<?) GAD" +
				"order by GAD.accountId asc, GAD.attainmentId asc", 
				accountId,
				DateUtil.convertToSqlDate(start),
				DateUtil.convertToSqlDate(end));
		return descList;
	}

	@Override
	public void saveOrUpdate(String tableName, GameAttainment attainment) {
		Integer tmp = QueryHelper.read(Integer.class, "select top 1 1 from "
				+tableName+" where accountId=? and attainmentId=?",
				attainment.getAccountId(), attainment.getAttainmentId());
		if (tmp != null) {
			update(tableName, attainment);
		}
		else {
			save(tableName, attainment);
		}
	}
	
	@Override
	public List<GameRanking> queryRankingList(String tableName, String orderCmd, int offset, int length) {
		List<GameRanking> descList = QueryHelper.query(GameRanking.class, 
				"select accountId, userId, playDuration, scores, ranking, remark, time from " +
				"(select top "+(offset+length)+" accountId, userId, playDuration, scores, remark, time, " +
				"row_number() over(order by scores "+orderCmd+" ) rowNumber, " +
				"dense_rank() over(order by scores "+orderCmd+" ) ranking "+
				"from "+tableName+" ) GA " +
				"where GA.rowNumber>?", offset);
		return descList;
	}

	@Override
	public List<GameRanking> queryRankingList(String tableName, String orderCmd,
			Date start, Date end, int offset, int length) {
		List<GameRanking> descList = QueryHelper.query(GameRanking.class, 
				"select accountId, userId, playDuration, scores, ranking, remark, time from " +
				"(select top "+(offset+length)+" accountId, userId, playDuration, scores, remark, time, " +
				"row_number() over(order by scores "+orderCmd+" ) rowNumber, " +
				"dense_rank() over(order by scores "+orderCmd+" ) ranking "+
				"from "+tableName+" where time>=? and time<?) GA " +
				"where GA.rowNumber>?", DateUtil.convertToSqlDate(start), DateUtil.convertToSqlDate(end), offset);
		return descList;
	}

}
