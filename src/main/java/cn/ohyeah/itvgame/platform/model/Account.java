package cn.ohyeah.itvgame.platform.model;

public class Account implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7850482305753074587L;
	public static final byte ACCOUNT_INVALID = 0;			//未激活,无效状态
	public static final byte ACCOUNT_ACTIVATION = 1;		//激活状态
	public static final byte ACCOUNT_FROZEN = 2;			//冻结状态
	public static final byte ACCOUNT_DELETED = 3;			//删除状态
	
	public static final byte PRIVILEGE_INVALID = 0;
	public static final byte PRIVILEGE_VISITOR = 1;			//游客
	public static final byte PRIVILEGE_GROUP = 2;			//团体
	public static final byte PRIVILEGE_MEMBER = 3;			//普通注册会员
	public static final byte PRIVILEGE_VIP = 4;				//VIP会员
	public static final byte PRIVILEGE_SUPER_USER = 5;		//超级用户
	
	private int accountId;
	private String nickName;
	private String pwdMd5;
	private int privilege;
	private String userId;
	private int scores;
	private int goldCoin;
	private int gameCoin;
	private java.util.Date createTime;
	private java.util.Date updateTime;
	private int state;
	
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
	
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public int getScores() {
		return scores;
	}
	public void setScores(int scores) {
		this.scores = scores;
	}
	
	public void incScores(int inc) {
		scores += inc;
	}
	
	public void decScores(int dec) {
		scores -= dec;
	}
	
	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}

	public boolean isStateInvalid() {
		return this.state == ACCOUNT_INVALID;
	}
	public void setStateInvalid() {
		this.state = ACCOUNT_INVALID;
	}
	public boolean isStateActivation() {
		return this.state == ACCOUNT_ACTIVATION;
	}
	public void setStateActivation() {
		this.state = ACCOUNT_ACTIVATION;
	}
	public boolean isStateFrozen() {
		return this.state == ACCOUNT_FROZEN;
	}
	public void setStateFrozen() {
		this.state = ACCOUNT_FROZEN;
	}
	public boolean isStateDeleted() {
		return this.state == ACCOUNT_DELETED;
	}
	public void setStateDeleted() {
		this.state = ACCOUNT_DELETED;
	}

	public int getPrivilege() {
		return privilege;
	}
	public void setPrivilege(int privilege) {
		this.privilege = privilege;
	}
	
	public boolean isPrivilegeVisitor() {
		return this.privilege == PRIVILEGE_VISITOR;
	}
	public void setPrivilegeVisitor() {
		this.privilege = PRIVILEGE_VISITOR;
	}
	public boolean isPrivilegeGroup() {
		return this.privilege == PRIVILEGE_GROUP;
	}
	public void setPrivilegeGroup() {
		this.privilege = PRIVILEGE_GROUP;
	}
	public boolean isPrivilegeMember() {
		return this.privilege == PRIVILEGE_MEMBER;
	}
	public void setPrivilegeMember() {
		this.privilege = PRIVILEGE_MEMBER;
	}
	public boolean isPrivilegeVIP() {
		return this.privilege == PRIVILEGE_VIP;
	}
	public void setPrivilegeVIP() {
		this.privilege = PRIVILEGE_VIP;
	}
	public boolean isPrivilegeSuperUser() {
		return this.privilege == PRIVILEGE_SUPER_USER;
	}
	public void setPrivilegeSuperUser() {
		this.privilege = PRIVILEGE_SUPER_USER;
	}
	
	public int getGoldCoin() {
		return goldCoin;
	}
	public void setGoldCoin(int goldCoin) {
		this.goldCoin = goldCoin;
	}
	
	public void incGoldCoin(int inc) {
		goldCoin += inc;
	}
	
	public void decGoldCoin(int dec) {
		goldCoin -= dec;
	}
	
	public int getGameCoin() {
		return gameCoin;
	}
	public void setGameCoin(int gameCoin) {
		this.gameCoin = gameCoin;
	}
	
	public void incGameCoin(int inc) {
		gameCoin += inc;
	}
	
	public void decGameCoin(int dec) {
		gameCoin -= dec;
	}
	
	public void setPwdMd5(String pwdMd5) {
		this.pwdMd5 = pwdMd5;
	}
	public String getPwdMd5() {
		return pwdMd5;
	}
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
	public java.util.Date getUpdateTime() {
		return updateTime;
	}
}
