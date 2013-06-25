package cn.ohyeah.itvgame.platform.model;

public class ProductProvider implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -561125261821253251L;
	public static final String TYPE_CORP = "corp";
	public static final String TYPE_PERSON = "person";
	
	private int providerID;
	private String providerName;
	private String type;
	
	public int getProviderID() {
		return providerID;
	}
	public void setProviderID(int providerID) {
		this.providerID = providerID;
	}
	
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
}
