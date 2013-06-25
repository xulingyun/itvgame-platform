package cn.ohyeah.itvgame.platform.model;

public class PurchaseRecord implements java.io.Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6393756701907331139L;
	private long id;
	private int accountId;
	private String userId;
	private int productId;
	private String productName;
	private int propId;
	private String propName;
	private int propCount;
	private int amount;
	private String remark;
	private java.util.Date time;
	private String ip;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	
	public java.util.Date getTime() {
		return time;
	}
	public void setTime(java.util.Date time) {
		this.time = time;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setPropId(int propId) {
		this.propId = propId;
	}
	public int getPropId() {
		return propId;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getAmount() {
		return amount;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRemark() {
		return remark;
	}
	public void setPropCount(int propCount) {
		this.propCount = propCount;
	}
	public int getPropCount() {
		return propCount;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductName() {
		return productName;
	}
	public void setPropName(String propName) {
		this.propName = propName;
	}
	public String getPropName() {
		return propName;
	}
}
