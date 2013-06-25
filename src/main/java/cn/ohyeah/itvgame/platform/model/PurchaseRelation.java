package cn.ohyeah.itvgame.platform.model;

public class PurchaseRelation implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5459286129490054176L;
	private int purchaseId;
	private String subscribeId;
	private String subscribeType;
	private int amount;
	private int value;		/*��ʾ��Ч�ڣ��룩�� ��Чʱ�����룩�� ��Ч�������Σ�*/
	private String subscribeImplementor;
	private int productId;
	private String description;
	
	public String getSubscribeId() {
		return subscribeId;
	}
	public void setSubscribeId(String subscribeId) {
		this.subscribeId = subscribeId;
	}
	public String getSubscribeType() {
		return subscribeType;
	}
	public void setSubscribeType(String subscribeType) {
		this.subscribeType = subscribeType;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getSubscribeImplementor() {
		return subscribeImplementor;
	}
	public void setSubscribeImplementor(String subscribeImplementor) {
		this.subscribeImplementor = subscribeImplementor;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	public void setPurchaseId(int purchaseId) {
		this.purchaseId = purchaseId;
	}
	public int getPurchaseId() {
		return purchaseId;
	}

	
}
