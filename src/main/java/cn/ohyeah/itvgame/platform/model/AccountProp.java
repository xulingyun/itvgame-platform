package cn.ohyeah.itvgame.platform.model;

public class AccountProp implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8861021337874192172L;
	private int accountId;
	private int propId;
	private int productId;	/*�����ĸ���Ϸ*/
	private int count;		/*����*/	
	private java.util.Date expiryDate;	/*����ʱ��*/
	
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public int getPropId() {
		return propId;
	}
	public void setPropId(int propId) {
		this.propId = propId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public void incCount(int inc) {
		count += inc;
	}
	
	public void decCount(int dec) {
		count -= dec;
	}
	public void setExpiryDate(java.util.Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	public java.util.Date getExpiryDate() {
		return expiryDate;
	}
	
}
