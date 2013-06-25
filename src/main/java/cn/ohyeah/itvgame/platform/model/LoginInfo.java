package cn.ohyeah.itvgame.platform.model;

/**
 * µÇÂ¼ÐÅÏ¢
 * @author maqian
 * @version 1.0
 */
public class LoginInfo {
	private int accountId;
	private String userId;
	private int productId;
	private String productName;
	private String appName;
	private java.util.Date systemTime;
	private Authorization auth;
	private SubscribeProperties subProps;
	
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
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public java.util.Date getSystemTime() {
		return systemTime;
	}
	public void setSystemTime(java.util.Date systemTime) {
		this.systemTime = systemTime;
	}
	public Authorization getAuth() {
		return auth;
	}
	public void setAuth(Authorization auth) {
		this.auth = auth;
	}
	public SubscribeProperties getSubProps() {
		return subProps;
	}
	public void setSubProps(SubscribeProperties subProps) {
		this.subProps = subProps;
	}
}
