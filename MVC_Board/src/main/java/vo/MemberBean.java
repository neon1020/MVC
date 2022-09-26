package vo;

public class MemberBean {
	private int idx;
	private String name;
	private String id;
	private String passwd;
	private String email;
	private String gender;
	private String date;
	
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	// toString 오버라이딩
	@Override
	public String toString() {
		return "MemberBean [idx=" + idx + ", name=" + name + ", id=" + id + ", passwd=" + passwd + ", email=" + email
				+ ", gender=" + gender + ", date=" + date + "]";
	}
}
