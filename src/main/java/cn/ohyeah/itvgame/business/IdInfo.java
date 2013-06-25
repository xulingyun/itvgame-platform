package cn.ohyeah.itvgame.business;

public class IdInfo {
	private String subscribeId;
	private String subscribeType;
	private int amount;
	private int period;
	
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
	public int getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}
	
	
}
