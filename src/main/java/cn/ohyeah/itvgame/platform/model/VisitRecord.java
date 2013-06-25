package cn.ohyeah.itvgame.platform.model;

public class VisitRecord implements java.io.Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4011998655708740218L;
	private long id;
	private int accountId;
	private String userId;
	private int productId;
	private int providerId;
	private java.util.Date loginTime;
	private java.util.Date logoutTime;	
	private int onlineSeconds;
	private String loginIp;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public java.util.Date getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(java.util.Date loginTime) {
		this.loginTime = loginTime;
	}
	
	public java.util.Date getLogoutTime() {
		return logoutTime;
	}
	public void setLogoutTime(java.util.Date logoutTime) {
		this.logoutTime = logoutTime;
	}
	
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getProviderId() {
		return providerId;
	}
	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}
	public int getOnlineSeconds() {
		return onlineSeconds;
	}
	public void setOnlineSeconds(int onlineSeconds) {
		this.onlineSeconds = onlineSeconds;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public int getAccountId() {
		return accountId;
	}
}
