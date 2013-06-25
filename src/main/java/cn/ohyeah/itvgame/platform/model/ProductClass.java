package cn.ohyeah.itvgame.platform.model;

public class ProductClass implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2305840496512723415L;
	private int classID;
	private String className;
	
	public int getClassID() {
		return classID;
	}
	public void setClassID(int classID) {
		this.classID = classID;
	}
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
}
