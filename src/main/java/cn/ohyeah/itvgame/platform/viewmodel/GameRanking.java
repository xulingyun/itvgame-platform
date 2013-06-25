package cn.ohyeah.itvgame.platform.viewmodel;

public class GameRanking {
	private int accountId;
	private String userId;
	private int playDuration;	/*游戏时长(单位:秒)*/
	private int scores;			/*分数*/
	private int ranking;
	private String remark;
	private java.util.Date time;
	
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getPlayDuration() {
		return playDuration;
	}
	public void setPlayDuration(int playDuration) {
		this.playDuration = playDuration;
	}
	public int getScores() {
		return scores;
	}
	public void setScores(int scores) {
		this.scores = scores;
	}
	public int getRanking() {
		return ranking;
	}
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
	public java.util.Date getTime() {
		return time;
	}
	public void setTime(java.util.Date time) {
		this.time = time;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserId() {
		return userId;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRemark() {
		return remark;
	}
	
}
