package cn.ohyeah.itvgame.protocol;

import java.util.HashMap;
import java.util.Map;

public class ProcessorContext {
	private Map<String, Object> props;
	private IProcessor processor;
	private HeadWrapper headWrapper;
	private int accountId;
	private int productId;
	private int errorCode;
	private String message;
	private Object result;
	
	public ProcessorContext(){
		props = new HashMap<String, Object>();
	}
	
	public void remove() {
		this.props = null;
		this.processor = null;
		this.headWrapper = null;
		this.message = null;
		this.result = null;
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

	public void setResult(Object result) {
		this.result = result;
	}

	public Object getResult() {
		return result;
	}

	public void setHeadWrapper(HeadWrapper headWrapper) {
		this.headWrapper = headWrapper;
	}

	public HeadWrapper getHeadWrapper() {
		return headWrapper;
	}

	public void setProcessor(IProcessor processor) {
		this.processor = processor;
	}

	public IProcessor getProcessor() {
		return processor;
	}

	public Map<String, Object> getPropsMap() {
		return props;
	}
	
	public Object getProp(String key) {
		return props.get(key);
	}
	
	public void setProp(String key, Object value) {
		props.put(key, value);
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getProductId() {
		return productId;
	}
}
