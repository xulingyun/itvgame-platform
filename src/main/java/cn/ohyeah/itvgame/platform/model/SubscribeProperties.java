package cn.ohyeah.itvgame.platform.model;

public class SubscribeProperties implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2201921306559861371L;
	
	private boolean supportSubscribe;
	private String subscribeAmountUnit;
	private int subscribeCashToAmountRatio;
	private boolean supportPointsService;
	private String pointsUnit;
	private int availablePoints;
	private int cashToPointsRatio;
	private boolean supportRecharge;
	private String expendAmountUnit;
	private int expendCashToAmountUnit;
	private int rechargeRatio;
	private int balance;
	
	public boolean isSupportRecharge() {
		return supportRecharge;
	}
	public void setSupportRecharge(boolean supportRecharge) {
		this.supportRecharge = supportRecharge;
	}
	public boolean isSupportSubscribe() {
		return supportSubscribe;
	}
	public void setSupportSubscribe(boolean supportSubscribe) {
		this.supportSubscribe = supportSubscribe;
	}
	public String getSubscribeAmountUnit() {
		return subscribeAmountUnit;
	}
	public void setSubscribeAmountUnit(String subscribeAmountUnit) {
		this.subscribeAmountUnit = subscribeAmountUnit;
	}

	public boolean isSupportPointsService() {
		return supportPointsService;
	}
	public void setSupportPointsService(boolean supportPointsService) {
		this.supportPointsService = supportPointsService;
	}
	public String getPointsUnit() {
		return pointsUnit;
	}
	public void setPointsUnit(String pointsUnit) {
		this.pointsUnit = pointsUnit;
	}
	public int getAvailablePoints() {
		return availablePoints;
	}
	public void setAvailablePoints(int availablePoints) {
		this.availablePoints = availablePoints;
	}
	public int getCashToPointsRatio() {
		return cashToPointsRatio;
	}
	public void setCashToPointsRatio(int cashToPointsRatio) {
		this.cashToPointsRatio = cashToPointsRatio;
	}
	public void setExpendAmountUnit(String expendAmountUnit) {
		this.expendAmountUnit = expendAmountUnit;
	}
	public String getExpendAmountUnit() {
		return expendAmountUnit;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public int getBalance() {
		return balance;
	}
	public void setSubscribeCashToAmountRatio(int cashToAmountRatio) {
		this.subscribeCashToAmountRatio = cashToAmountRatio;
	}
	public int getSubscribeCashToAmountRatio() {
		return subscribeCashToAmountRatio;
	}
	
	public int getExpendCashToAmountUnit() {
		return expendCashToAmountUnit;
	}
	public void setExpendCashToAmountUnit(int cashToAmountRatio) {
		this.expendCashToAmountUnit = cashToAmountRatio;
	}
	public int getRechargeRatio() {
		return rechargeRatio;
	}
	public void setRechargeRatio(int rechargeRatio) {
		this.rechargeRatio = rechargeRatio;
	}
	
}
