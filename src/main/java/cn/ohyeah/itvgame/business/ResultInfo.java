package cn.ohyeah.itvgame.business;

public class ResultInfo {
	private int errorCode;
	private String message;
	private Object info;
	
	public boolean isSuccess() {
		return errorCode == 0;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getInfo() {
		return info;
	}
	public void setInfo(Object info) {
		this.info = info;
	}

}
