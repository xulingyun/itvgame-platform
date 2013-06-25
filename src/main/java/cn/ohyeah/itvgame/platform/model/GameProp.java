package cn.ohyeah.itvgame.platform.model;

public class GameProp implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -704244188899056764L;
	private int propId;
	private String propName;
	private int price;
	private int validPeriod;
	private int productId;		/*属于哪个游戏*/
	private int feeCode;		/*扣费点*/
	private String description;	/*描述*/
	
	public int getFeeCode() {
		return feeCode;
	}
	public void setFeeCode(int feeCode) {
		this.feeCode = feeCode;
	}
	public int getPropId() {
		return propId;
	}
	public void setPropId(int propId) {
		this.propId = propId;
	}
	public String getPropName() {
		return propName;
	}
	public void setPropName(String propName) {
		this.propName = propName;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getProductId() {
		return productId;
	}
	public void setValidPeriod(int validPeriod) {
		this.validPeriod = validPeriod;
	}
	public int getValidPeriod() {
		return validPeriod;
	}
	
}
