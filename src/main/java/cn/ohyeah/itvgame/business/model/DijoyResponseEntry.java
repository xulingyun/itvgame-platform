package cn.ohyeah.itvgame.business.model;

import java.util.Date;

public class DijoyResponseEntry {

	//{"order":"","feeCode":"","Sum":0,"payResult":1003,"appExt":"","sign":"4E3A70B7D33060B85039A37854D5CFBB"}
	//{"order":"","userId":0,"payTime":"","payments":0,"payResult":1003,"sign":""}
	
	private int userId;
	private String order;
	private String feeCode;
	private int sum;
	private int payments;
	private Date payTime;
	private int payResult;
	private String appExt;
	private String sign;
	
	
	public int getSum() {
		return sum;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getFeeCode() {
		return feeCode;
	}
	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}
	public int getPayments() {
		return payments;
	}
	public void setPayments(int payments) {
		this.payments = payments;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public int getPayResult() {
		return payResult;
	}
	public void setPayResult(int payResult) {
		this.payResult = payResult;
	}
	public String getAppExt() {
		return appExt;
	}
	public void setAppExt(String appExt) {
		this.appExt = appExt;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	@Override
	public String toString() {
		return "userId:"+userId+"; order:"+order+"; feeCode:"+feeCode+"; sum:"+sum+
				"; payments:"+payments+"; payTime:"+payTime+"; payResult:"+payResult+
				"; sign:"+sign+"; appExt:"+appExt;
	}
	
}
