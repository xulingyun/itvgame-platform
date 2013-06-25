package cn.ohyeah.itvgame.platform.model;

public class Authorization implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1308542610791339376L;
	
	public static final byte AUTHORIZATION_INVALID = 0;		//无效授权
	public static final byte AUTHORIZATION_FREE = 1;		//免费
	public static final byte AUTHORIZATION_TRY = 2;			//试玩权限
	public static final byte AUTHORIZATION_PERIOD = 3;		//按有效期权限
	public static final byte AUTHORIZATION_COUNT = 4;		//按游戏次数权限
	public static final byte AUTHORIZATION_TIME = 5;		//按游戏时长权限
	public static final byte AUTHORIZATION_MONTH = 6;		//包月权限
	public static final byte AUTHORIZATION_MEMBER = 7;		//普通会员权限
	public static final byte AUTHORIZATION_GROUP = 8;		//团体权限
	public static final byte AUTHORIZATION_VIP = 9;			//VIP权限
	public static final byte AUTHORIZATION_SUPER = 10;		//超级权限
	
	private int accountId;
	private int productId;
	private int authorizationType;
	private int leftTryNumber;
	private int leftValidSeconds;
	private int leftValidCount;
	private int goldCoin;
	private int gameCoin;
	private java.util.Date authorizationStartTime;
	private java.util.Date authorizationEndTime;
	
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getAuthorizationType() {
		return authorizationType;
	}
	public void setAuthorizationType(int authorizationType) {
		this.authorizationType = authorizationType;
	}
	
	public boolean isAuthorizationInvalid() {
		return authorizationType==AUTHORIZATION_INVALID;
	}
	public void setAuthorizationInvalid() {
		authorizationType=AUTHORIZATION_INVALID;
	}
	public boolean isAuthorizationFree() {
		return authorizationType==AUTHORIZATION_FREE;
	}
	public void setAuthorizationFree() {
		authorizationType=AUTHORIZATION_FREE;
	}
	public boolean isAuthorizationTry() {
		return authorizationType==AUTHORIZATION_TRY;
	}
	public void setAuthorizationTry() {
		authorizationType=AUTHORIZATION_TRY;
	}
	public boolean isAuthorizationPeriod() {
		return authorizationType==AUTHORIZATION_PERIOD;
	}
	public void setAuthorizationPeriod() {
		authorizationType=AUTHORIZATION_PERIOD;
	}
	public boolean isAuthorizationCount() {
		return authorizationType==AUTHORIZATION_COUNT;
	}
	public void setAuthorizationCount() {
		authorizationType=AUTHORIZATION_COUNT;
	}
	public boolean isAuthorizationTime() {
		return authorizationType==AUTHORIZATION_TIME;
	}
	public void setAuthorizationTime() {
		authorizationType=AUTHORIZATION_TIME;
	}
	public boolean isAuthorizationMonth() {
		return authorizationType==AUTHORIZATION_MONTH;
	}
	public void setAuthorizationMonth() {
		authorizationType=AUTHORIZATION_MONTH;
	}
	public boolean isAuthorizationMember() {
		return authorizationType==AUTHORIZATION_MEMBER;
	}
	public void setAuthorizationMember() {
		authorizationType=AUTHORIZATION_MEMBER;
	}
	public boolean isAuthorizationGroup() {
		return authorizationType==AUTHORIZATION_GROUP;
	}
	public void setAuthorizationGroup() {
		authorizationType=AUTHORIZATION_GROUP;
	}
	
	public int getLeftTryNumber() {
		return leftTryNumber;
	}
	public void setLeftTryNumber(int leftTryNumber) {
		this.leftTryNumber = leftTryNumber;
	}
	
	public int getLeftValidCount() {
		return leftValidCount;
	}
	public void setLeftValidCount(int leftValidCount) {
		this.leftValidCount = leftValidCount;
	}

	public java.util.Date getAuthorizationStartTime() {
		return authorizationStartTime;
	}
	public void setAuthorizationStartTime(java.util.Date authorizationStartTime) {
		this.authorizationStartTime = authorizationStartTime;
	}
	public java.util.Date getAuthorizationEndTime() {
		return authorizationEndTime;
	}
	public void setAuthorizationEndTime(java.util.Date authorizationEndTime) {
		this.authorizationEndTime = authorizationEndTime;
	}
	public int getLeftValidSeconds() {
		return leftValidSeconds;
	}
	public void setLeftValidSeconds(int leftValidSeconds) {
		this.leftValidSeconds = leftValidSeconds;
	}

	public int getGoldCoin() {
		return goldCoin;
	}
	public void setGoldCoin(int goldCoin) {
		this.goldCoin = goldCoin;
	}
	public int getGameCoin() {
		return gameCoin;
	}
	public void setGameCoin(int gameCoin) {
		this.gameCoin = gameCoin;
	}
	
	public void incGoldCoin(int inc) {
		goldCoin += inc;
	}
	
	public void decGoldCoin(int dec) {
		goldCoin -= dec;
	}
	
	public void incGameCoin(int inc) {
		gameCoin += inc;
	}
	
	public void decGameCoin(int dec) {
		gameCoin -= dec;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public int getAccountId() {
		return accountId;
	}
}
