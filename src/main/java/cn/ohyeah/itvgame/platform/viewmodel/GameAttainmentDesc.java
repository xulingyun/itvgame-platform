package cn.ohyeah.itvgame.platform.viewmodel;

public class GameAttainmentDesc {
	private int attainmentId;
	private int playDuration;	/*游戏时长(单位:秒)*/
	private int scores;			/*分数*/
	private long ranking;
	private String remark;
	private java.util.Date time;

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
	public void setRanking(long ranking) {
		this.ranking = ranking;
	}
	public long getRanking() {
		return ranking;
	}
	public void setAttainmentId(int attainmentId) {
		this.attainmentId = attainmentId;
	}
	public int getAttainmentId() {
		return attainmentId;
	}
}
