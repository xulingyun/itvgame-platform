package cn.ohyeah.itvgame.platform.model;

public class GameAttainment implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7673578797244735856L;
	private int accountId;
	private String userId;
	private int attainmentId;
	private int playDuration;	/*游戏时长*/
	private int scores;			/*分数*/
	private long ranking;
	private String remark;
	private java.util.Date time;
	private byte[] data;
	
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public int getAttainmentId() {
		return attainmentId;
	}
	public void setAttainmentId(int attainmentId) {
		this.attainmentId = attainmentId;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public java.util.Date getTime() {
		return time;
	}
	public void setTime(java.util.Date time) {
		this.time = time;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserId() {
		return userId;
	}
	public void setRanking(long ranking) {
		this.ranking = ranking;
	}
	public long getRanking() {
		return ranking;
	}
	
}
