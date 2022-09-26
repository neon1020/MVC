package vo;

// 포워딩 정보(포워딩 주소와 포워딩 방식)를 관리하는 ActionForward 클래스 정의 = DTO 객체와 동일
public class ActionForward {
	private String path; // 포워딩 주소(URL)
	private boolean isRedirect; // 포워딩 방식 (true : Redirect, false : Dispatcher)
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public boolean isRedirect() {
		return isRedirect;
	}
	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}
}
