package cn.ohyeah.itvgame.platform.viewmodel;

public class PurchaseDesc {
	private int propId;
	private String propName;
	private int propCount;
	private int amount;
	private String remark;
	private java.util.Date time;
	
	public int getPropId() {
		return propId;
	}
	public void setPropId(int propId) {
		this.propId = propId;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
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
	public void setPropCount(int propCount) {
		this.propCount = propCount;
	}
	public int getPropCount() {
		return propCount;
	}
	public void setPropName(String propName) {
		this.propName = propName;
	}
	public String getPropName() {
		return propName;
	}
	
}
