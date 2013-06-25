package cn.ohyeah.itvgame.platform.model;

public class Product implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7356973095402938098L;
	public static final String APP_J2ME = "j2me";		//J2ME
	public static final String APP_JS = "js";		//Javascript

    @Deprecated	public static final String MANAGER_TYPE_NULL = "null";
    /**
     * @deprecated replaced by
     * {@link cn.ohyeah.itvgame.global.Configuration#PLAT_RECHARGE_IMPL}
     */
    @Deprecated	public static final String MANAGER_TYPE_PLATFORM = "platform";
    /**
     * @deprecated replaced by
     * {@link cn.ohyeah.itvgame.global.Configuration#GAME_RECHARGE_IMPL}
     */
    @Deprecated	public static final String MANAGER_TYPE_GAME = "game";
	
	public static final byte PRODUCT_INVALID = 0;		//无效
	public static final byte PRODUCT_ONLINE = 1;		//上线
	public static final byte PRODUCT_OFFLINE = 2;		//下线
	public static final byte PRODUCT_TESTING = 3;		//测试
	
	private int productId;
	private String productName;
	private int productClass;
	private String appName;
	private String appType;
	private String description;
	private boolean supportDataManager;
	private String location;
	private int gameid;
	private java.util.Date updateTime;
	private java.util.Date createTime;
	private int providerID;
	private int state;
	
	public int getGameid() {
		return gameid;
	}
	public void setGameid(int gameid) {
		this.gameid = gameid;
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
	
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	
	public boolean isAppJ2ME() {
		return APP_J2ME.equalsIgnoreCase(appType);
	}
	public void setAppJ2ME() {
		appType = APP_J2ME;
	}
	public boolean isAppJS() {
		return APP_JS.equalsIgnoreCase(appType);
	}
	public void setAppJS() {
		appType = APP_JS;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getState() {
		return state;
	}
	
	public void setState(int state) {
		this.state = state;
	}
	public boolean isStateInvalid() {
		return (state == PRODUCT_INVALID);
	}
	public void setStateInvalid() {
		state = PRODUCT_INVALID;
	}
	public boolean isStateOnline() {
		return (state == PRODUCT_ONLINE);
	}
	public void setStateOnline() {
		state = PRODUCT_ONLINE;
	}
	public boolean isStateOffline() {
		return (state == PRODUCT_OFFLINE);
	}
	public void setStateOffline() {
		state = PRODUCT_OFFLINE;
	}
	public boolean isStateTesting() {
		return (state == PRODUCT_TESTING);
	}
	public void setStateTesting() {
		state = PRODUCT_TESTING;
	}
	
	public java.util.Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	
	public int getProductClass() {
		return productClass;
	}
	public void setProductClass(int productClass) {
		this.productClass = productClass;
	}
	
	public int getProviderID() {
		return providerID;
	}
	public void setProviderID(int providerID) {
		this.providerID = providerID;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLocation() {
		return location;
	}
	public void setSupportDataManager(boolean supportDataManager) {
		this.supportDataManager = supportDataManager;
	}
	public boolean isSupportDataManager() {
		return supportDataManager;
	}
}
