package cn.ohyeah.itvgame.platform.model;

public class User implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8737720966943827822L;
	public static final String ROLE_INVALID = "invalid";
	public static final String ROLE_ADMIN = "admin";
	public static final String ROLE_TEST = "test";
	public static final String ROLE_DEVELOP = "develop";
	public static final String ROLE_EDITOR = "editor";
	public static final String ROLE_STAT = "stat";
	public static final String ROLE_PARTNER = "partner";
	
	private int id;
	private String name;
	private String pwdMd5;
	private String role;
	private int valid;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwdMd5() {
		return pwdMd5;
	}
	public void setPwdMd5(String pwdMd5) {
		this.pwdMd5 = pwdMd5;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean isRoleAdmin() {
		return ROLE_ADMIN.equals(role);
	}
	
	public boolean isRoleTest() {
		return ROLE_TEST.equals(role);
	}
	
	public int getValid() {
		return valid;
	}
	public void setValid(int valid) {
		this.valid = valid;
	}
	
}
