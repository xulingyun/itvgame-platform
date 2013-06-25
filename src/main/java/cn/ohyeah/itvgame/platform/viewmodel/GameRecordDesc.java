package cn.ohyeah.itvgame.platform.viewmodel;

import java.util.Date;

public class GameRecordDesc {
	private int recordId;
	private int playDuration;	/*游戏时长(单位:秒)*/
	private int scores;			/*分数*/
	private java.util.Date time;
	private String remark;
	
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