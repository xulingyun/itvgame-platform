package cn.ohyeah.itvgame.platform.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.halcyon.dao.QueryHelper;
import cn.ohyeah.itvgame.platform.dao.IGameAttainmentDao;
import cn.ohyeah.itvgame.platform.model.GameAttainment;
import cn.ohyeah.itvgame.platform.viewmodel.GameAttainmentDesc;
import cn.ohyeah.itvgame.platform.viewmodel.GameRanking;

public class MysqlGameAttainmentDaoImpl implements IGameAttainmentDao{

	private String getCompareCmd(String orderCmd) {
		if (StringUtils.equalsIgnoreCase(orderCmd, "asc")) {
			return "<";
		}
		else {
			return ">";
		}
	}
	
	@Override
	public GameAttainment read(String tableName, int accountId, int attainmentId, String orderCmd) {
		String cmd = getCompareCmd(orderCmd);
		GameAttainment attainment = QueryHelper.read(GameAttainment.class, 
				"select A.accountId, A.attainmentId, A.playDuration, A.scores, A.remark, A.time, A.data, "
				+"(select count(*)+1 from "+tableName+" B where B.scores"+cmd+"(select C.scores from "+tableName+" C " 
				+"where C.accountId=A.accountId and C.attainmentId=A.attainmentId)) as ranking "
				+" from "+tableName+" A where A.accountId=? and A.attainmentId=?", 
				accountId, attainmentId);
		return attainment;
	}
	
	@Override
	public GameAttainment read(String tableName, int accountId, int attainmentId, 
			String orderCmd, Date start, Date end) {
		String cmd = getCompareCmd(orderCmd);
		GameAttainment attainment = QueryHelper.read(GameAttainment.class, 
				"select A.accountId, A.attainmentId, A.playDuration, A.scores, A.remark, A.time, A.data, "
				+"(select count(*)+1 from "+tableName+" B where B.scores"+cmd+"(select C.scores from "+tableName+" C " 
				+"where C.accountId=A.accountId and C.attainmentId=A.attainmentId) and B.time>=? and B.time<? ) as ranking "
				+" from "+tableName+" A where A.accountId=? and A.attainmentId=?", 
				start, end, accountId, attainmentId);
		return attainment;
	}

	@Override
	public void save(String tableName, GameAttainment attainment) {
		QueryHelper.update("insert into "+tableName
				+ "(accountId, userId, attainmentId, "
				+ "playDuration, scores, time, remark, data) "
				+ "values(?, ?, ?, ?, ?, ?, ?, ?)", 
				attainment.getAccountId(), attainment.getUserId(), attainment.getAttainmentId(), 
				attainment.getPlayDuration(), attainment.getScores(), 
				attainment.getTime(), attainment.getRemark(), attainment.getData());
	}

	@Override
	public void update(String tableName, GameAttainment attainment) {
		QueryHelper.update("update "+tableName+" set "
				+ "playDuration=?, scores=?, time=?, remark=?, data=? "
				+ "where accountId=? and attainmentId=?",
				attainment.getPlayDuration(), attainment.getScores(), attainment.getTime(), 
				attainment.getRemark(), attainment.getData(), attainment.getAccountId(), 
				attainment.getAttainmentId());
	}

	@Override
	public List<GameAttainmentDesc> queryDescList(String tableName, int accountId, String orderCmd) {
		String cmd = getCompareCmd(orderCmd);
		List<GameAttainmentDesc> descList = QueryHelper.query(GameAttainmentDesc.class, 
				"select A.attainmentId, A.playDuration, A.scores, A.time, A.remark, " 
				+"(select count(*)+1 from "+tableName+" B where B.scores"+cmd+"(select C.scores from "+tableName+" C " 
				+"where C.accountId=A.accountId and C.attainmentId=A.attainmentId)) as ranking "
				+" from "+ tableName
				+" A where A.accountId=? order by accountId asc, attainmentId asc", 
				accountId);
		return descList;
	}
	
	@Override
	public List<GameAttainmentDesc> queryDescList(String tableName, int accountId, 
			String orderCmd, Date start, Date end) {
		String cmd = getCompareCmd(orderCmd);
		List<GameAttainmentDesc> descList = QueryHelper.query(GameAttainmentDesc.class, 
				"select A.attainmentId, A.playDuration, A.scores, A.time, A.remark, " 
				+"(select count(*)+1 from "+tableName+" B where B.scores"+cmd+"(select C.scores from "+tableName+" C " 
				+"where C.accountId=A.accountId and C.attainmentId=A.attainmentId) and B.time>=? and B.time<? ) as ranking "
				+" from "+ tableName
				+" A where A.accountId=? order by accountId asc, attainmentId asc", 
				start, end, accountId);
		return descList;
	}

	@Override
	public void saveOrUpdate(String tableName, GameAttainment attainment) {
		QueryHelper.update("insert into "+tableName
				+ "(accountId, userId, attainmentId, "
				+ "playDuration, scores, time, remark, data) "
				+ "values(?, ?, ?, ?, ?, ?, ?, ?) "
				+ "on duplicate key update "
				+ "playDuration=values(playDuration), scores=values(scores), " 
				+ "time=values(time), remark=values(remark), data=values(data)",
				attainment.getAccountId(), attainment.getUserId(), 
				attainment.getAttainmentId(), attainment.getPlayDuration(), attainment.getScores(), 
				attainment.getTime(), attainment.getRemark(), attainment.getData());
	}
	
	/**
	 * 目前仅支持从第一往后的排
	 */
	@Override
	public List<GameRanking> queryRankingList(String tableName, String orderCmd, int offset, int length) {
		List<GameRanking> descList = QueryHelper.query(GameRanking.class, 
				"select accountId, userId, playDuration, scores, remark, time from "+ tableName
				+ " order by scores "+orderCmd+" limit ?, ?", 
				offset, length);
		generateRanking(offset, descList);
		return descList;
	}

	/**
	 * 目前仅支持从第一往后的排名
	 */
	@Override
	public List<GameRanking> queryRankingList(String tableName, String orderCmd, 
			Date start, Date end, int offset, int length) {
		List<GameRanking> descList = QueryHelper.query(GameRanking.class, 
				"select accountId, userId, playDuration, scores, remark, time from "+ tableName
				+ " where time>=? and time<? order by scores "+orderCmd+" limit ?, ?", 
				start, end, offset, length);
		generateRanking(offset, descList);
		return descList;
	}

	private void generateRanking(int offset, List<GameRanking> descList) {
		if (descList != null && descList.size() > 0) {
			int size = descList.size();
			int preScore = descList.get(0).getScores();
			int inc = 0;
			for (int i = 0; i < size; ++i) {
				int curScore = descList.get(i).getScores();
				if (curScore != preScore) {
					++inc;
					preScore = curScore;
				}
				descList.get(i).setRanking(offset+inc+1);
			}
		}
	}
	

}
