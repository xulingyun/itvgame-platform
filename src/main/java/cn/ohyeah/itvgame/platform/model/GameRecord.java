package cn.ohyeah.itvgame.platform.model;

import java.util.Date;

public class GameRecord implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5241581093218645236L;
	private int accountId;
	private int recordId;
	private int playDuration;	/*游戏时长*/
	private int scores;			/*分数*/
	private java.util.Date time;
	private String remark;
	private byte []data;
	
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public int getRecordId() {
		return recordId;
	}
	public void setData(byte [] data) {
		this.data = data;
	}
	public byte [] getData() {
		return data;
	}
	public int getScores() {
		return scores;
	}
	public void setScores(int scores) {
		this.scores = scores;
	}
	public int getPlayDuration() {
		return playDuration;
	}
	public void setPlayDuration(int playDuration) {
		this.playDuration = playDuration;
	} 
	
}
