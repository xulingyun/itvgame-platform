package cn.ohyeah.itvgame.platform.model;

public class SubscribeRecord implements java.io.Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1247822388879248886L;
	public static final byte SUBSCRIBE_COMMAND_NEWSUB = 1;		/*新用户订购*/
	public static final byte SUBSCRIBE_COMMAND_SUB = 2;			/*用户订购*/
	public static final byte SUBSCRIBE_COMMAND_UNSUB = 3;		/*用户退订*/
	public static final byte SUBSCRIBE_COMMAND_TELUNSUB = 4;	/*客服退订*/
	
	private long id;
	private int accountId;
	private String userId;
	private int amount;
	private String subscribeImplementor;
	private String subscribeType;
	private int accountSubscribeCommand;
	private int productSubscribeCommand;
	private int payType;
	private String subscribeId;
	private String remark;
	private int productId;
	private String productName;
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
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
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
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public void setSubscribeId(String subscribeId) {
		this.subscribeId = subscribeId;
	}
	public String getSubscribeId() {
		return subscribeId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getProductId() {
		return productId;
	}
	public void setSubscribeImplementor(String subscribeImplementor) {
		this.subscribeImplementor = subscribeImplementor;
	}
	public String getSubscribeImplementor() {
		return subscribeImplementor;
	}
	public void setSubscribeType(String subscribeType) {
		this.subscribeType = subscribeType;
	}
	public String getSubscribeType() {
		return subscribeType;
	}
	
	public int getAccountSubscribeCommand() {
		return accountSubscribeCommand;
	}
	
	public void setAccountSubscribeCommand(int accountSubscribeCommand) {
		this.accountSubscribeCommand = accountSubscribeCommand;
	}
	public int getProductSubscribeCommand() {
		return productSubscribeCommand;
	}
	public void setProductSubscribeCommand(int productSubscribeCommand) {
		this.productSubscribeCommand = productSubscribeCommand;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductName() {
		return productName;
	}
	public void setPayType(int payType) {
		this.payType = payType;
	}
	public int getPayType() {
		return payType;
	}

	public void setPayTypeBill() {
		payType = SubscribePayType.PAY_TYPE_BILL;
	}
	
	public void setPayTypePoints() {
		payType = SubscribePayType.PAY_TYPE_POINTS;
	}
	
	public boolean isPayTypeBill() {
		return payType == SubscribePayType.PAY_TYPE_BILL;
	}
	
	public boolean isPayTypePoints() {
		return payType == SubscribePayType.PAY_TYPE_POINTS;
	}
}
