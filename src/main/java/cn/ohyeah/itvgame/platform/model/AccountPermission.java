package cn.ohyeah.itvgame.platform.model;

import java.util.Calendar;

import org.apache.commons.lang.time.DateUtils;

public class AccountPermission implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6091832937316505350L;
	
	public static final byte PERMISSION_INVALID = 0;
	public static final byte PERMISSION_VALID = 1;
	
	private int accountId;
	private int daySubscribeLimit;
	private int monthSubscribeLimit;
	private java.util.Date lastSubscribeTime;
	private int daySubscribeAmount;
	private int monthSubscribeAmount;
	private int totalSubscribeAmount;
	private int accessPermission;
	private int subscribePermission;
	
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public int getDaySubscribeLimit() {
		return daySubscribeLimit;
	}
	public void setDaySubscribeLimit(int daySubscribeLimit) {
		this.daySubscribeLimit = daySubscribeLimit;
	}
	
	public int getMonthSubscribeLimit() {
		return monthSubscribeLimit;
	}
	public void setMonthSubscribeLimit(int monthSubscribeLimit) {
		this.monthSubscribeLimit = monthSubscribeLimit;
	}
	public java.util.Date getLastSubscribeTime() {
		return lastSubscribeTime;
	}
	public void setLastSubscribeTime(java.util.Date lastSubscribeTime) {
		this.lastSubscribeTime = lastSubscribeTime;
	}
	public int getDaySubscribeAmount() {
		return daySubscribeAmount;
	}
	public void setDaySubscribeAmount(int daySubscribeAmount) {
		this.daySubscribeAmount = daySubscribeAmount;
	}
	public int getMonthSubscribeAmount() {
		return monthSubscribeAmount;
	}
	public void setMonthSubscribeAmount(int monthSubscribeAmount) {
		this.monthSubscribeAmount = monthSubscribeAmount;
	}
	public int getTotalSubscribeAmount() {
		return totalSubscribeAmount;
	}
	public void setTotalSubscribeAmount(int totalSubscribeAmount) {
		this.totalSubscribeAmount = totalSubscribeAmount;
	}
	public boolean isSubscribeSameDay(java.util.Date now) {
		if (lastSubscribeTime == null) {
			return false;
		}
		return DateUtils.isSameDay(lastSubscribeTime, now);
	}
	
	public boolean isSubscribeSameMonth(java.util.Date now) {
		if (lastSubscribeTime == null) {
			return false;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		int nowY = calendar.get(Calendar.YEAR);
		int nowM = calendar.get(Calendar.MONTH);
		calendar.setTime(lastSubscribeTime);
		int rechargeY = calendar.get(Calendar.YEAR);
		int rechargeM = calendar.get(Calendar.MONTH);
		return (nowY==rechargeY)&&(nowM == rechargeM);
	}
	
	public boolean willExceedDayLimit(int limit, int amount) {
		if (daySubscribeAmount+amount > limit) {
			return true;
		}
		return false;
	}
	
	public boolean willExceedMonthLimit(int limit, int amount) {
		if (monthSubscribeAmount+amount > limit) {
			return true;
		}
		return false;
	}
	public int getAccessPermission() {
		return accessPermission;
	}
	public void setAccessPermission(int accessPermission) {
		this.accessPermission = accessPermission;
	}
	public boolean isAccessPermissionValid() {
		return accessPermission == PERMISSION_VALID;
	}
	public boolean isAccessPermissionInvalid() {
		return accessPermission == PERMISSION_INVALID;
	}
	public void setAccessPermissionValid() {
		accessPermission = PERMISSION_VALID;
	}
	public void setAccessPermissionInvalid(){
		accessPermission = PERMISSION_INVALID;
	}
	public int getSubscribePermission() {
		return subscribePermission;
	}
	public void setSubscribePermission(int subscribePermission) {
		this.subscribePermission = subscribePermission;
	}
	public boolean isSubscribePermissionValid() {
		return subscribePermission == PERMISSION_VALID;
	}
	public boolean isSubscribePermissionInvalid() {
		return subscribePermission == PERMISSION_INVALID;
	}
	public void setSubscribePermissionValid() {
		subscribePermission = PERMISSION_VALID;
	}
	public void setSubscribePermissionInvalid() {
		subscribePermission = PERMISSION_INVALID;
	}
	
	public void incDaySubscribeAmount(int inc) {
		daySubscribeAmount += inc;
	}

	public void incMonthSubscribeAmount(int inc) {
		monthSubscribeAmount += inc;
	}
	
	public void incTotalSubscribeAmount(int inc) {
		totalSubscribeAmount += inc;
	}
}
