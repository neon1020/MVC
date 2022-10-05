package action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.MemberAuthService;
import vo.ActionForward;
import vo.AuthInfoBean;

public class MemberAuthAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("MemberAuthAction");
		
		ActionForward forward = null;
		
		String id = request.getParameter("id");
		String authCode = request.getParameter("authCode");
		
		AuthInfoBean authInfo = new AuthInfoBean();
		authInfo.setId(id);
		authInfo.setAuth_code(authCode);
		
		// MemberAuthService - checkAuthInfo() 메소드 호출하여 인증 정보 확인 요청
		// => 파라미터 : AuthInfoBean 객체    리턴타입 : boolean(isAuthenticationSuccess)
		MemberAuthService service = new MemberAuthService();
		boolean isAuthenticationSuccess = service.checkAuthInfo(authInfo);
		
		if(!isAuthenticationSuccess) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('인증 실패!\n인증 정보를 확인하세요!')");
			out.println("history.back()");
			out.println("</script>");
			
		} else {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('인증 성공!\n로그인 페이지로 이동합니다!')");
			out.println("MemberLoginForm.me");
			out.println("</script>");
		}
		
		return forward;
	}

}
