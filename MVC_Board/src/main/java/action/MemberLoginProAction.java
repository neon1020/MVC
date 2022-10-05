package action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import svc.MemberLoginProService;
import vo.ActionForward;
import vo.MemberBean;

public class MemberLoginProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("MemberLoginProAction");
		
		ActionForward forward = null;
		
		String id = request.getParameter("id");
		String passwd = request.getParameter("passwd");
		// System.out.println(id + ", " + passwd);
		
		// 전달받은 파라미터 가져와서 MemberBean 객체에 저장
		MemberBean member = new MemberBean();
		member.setId(id);
		member.setPasswd(passwd);
		
		MemberLoginProService service = new MemberLoginProService();
		
		// MemberLoginProService - isAuthenticatedUser() 메소드 호출하여 인증 여부 확인 요청
		// => 파라미터 : MemberBean 객체  리턴타입 : boolean(isAuthenticatedUser)
		boolean isAuthenticatedUser = service.isAuthenticatedUser(member);
		
		if(!isAuthenticatedUser) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('메일 인증 필수')");
			out.println("history.back()");
			out.println("</script>");
			
		} else {
			// MemberLoginProService - memberLogin() 메소드 호출하여 로그인 작업 요청
			// => 파라미터 : MemberBean 객체  리턴타입 : boolean(isLoginSuccess)
			boolean isLoginSuccess = service.memberLogin(member);
			
			if(!isLoginSuccess) {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('로그인 실패')");
				out.println("history.back()");
				out.println("</script>");
				
			} else {
				HttpSession session = request.getSession();
				session.setAttribute("sId", member.getId());
				
				forward = new ActionForward();
				forward.setPath("./");
				forward.setRedirect(true);
			}
		}
		return forward;
	}

}
