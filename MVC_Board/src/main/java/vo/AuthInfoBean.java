package vo;

public class AuthInfoBean {
	private String id;
	private String auth_code;
	
	public AuthInfoBean() {}
	
	public AuthInfoBean(String id, String auth_code) {
		this.id = id;
		this.auth_code = auth_code;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAuth_code() {
		return auth_code;
	}
	public void setAuth_code(String auth_code) {
		this.auth_code = auth_code;
	}
}
