package cn.ohyeah.itvgame.platform.model;

/*°üÔÂ¶©¹º±í*/
public class PayMonth implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 657504156933978896L;
	private int accountId;
	private String userId;
	private String subscribeId;
	private int amount;
	private int valid;
	private java.util.Date subscribeTime;
	private java.util.Date unsubscribeTime;
	
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSubscribeId() {
		return subscribeId;
	}
	public void setSubscribeId(String subscribeId) {
		this.subscribeId = subscribeId;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getValid() {
		return valid;
	}
	public void setValid(int valid) {
		this.valid = valid;
	}
	public java.util.Date getSubscribeTime() {
		return subscribeTime;
	}
	public void setSubscribeTime(java.util.Date subscribeTime) {
		this.subscribeTime = subscribeTime;
	}
	public java.util.Date getUnsubscribeTime() {
		return unsubscribeTime;
	}
	public void setUnsubscribeTime(java.util.Date unsubscribeTime) {
		this.unsubscribeTime = unsubscribeTime;
	}
	
	
}
