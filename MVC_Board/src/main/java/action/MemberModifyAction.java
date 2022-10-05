package action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import svc.MemberModifyService;
import vo.ActionForward;
import vo.MemberBean;

public class MemberModifyAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("MemberModifyAction");
		
		ActionForward forward = null;
		
		HttpSession session = request.getSession();
		String sId = (String)session.getAttribute("sId");
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String gender = request.getParameter("gender");
		String email = request.getParameter("email1") + "@" + request.getParameter("email2");
		String passwd = request.getParameter("passwd");
		String newPasswd = request.getParameter("newPasswd");
		String newPasswd2 = request.getParameter("newPasswd2");
		
		// System.out.println(sId + name + gender + email + passwd);
		// System.out.println("newPasswd : " + newPasswd);
		// System.out.println("newPasswd2 : " + newPasswd2);
		
		MemberBean member = new MemberBean();
		member.setId(id);
		member.setName(name);
		member.setPasswd(passwd);
		member.setGender(gender);
		member.setEmail(email);
		
		MemberModifyService service = new MemberModifyService();
		boolean isMember = service.isMember(member, sId);
		
		if(!isMember) {
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('비밀번호가 틀렸습니다.')");
			out.println("history.back()");
			out.println("</script>");
			
		} else {
			if(!newPasswd.equals("") && !newPasswd2.equals("")) {
				if(newPasswd.equals(newPasswd2)) {
					member.setPasswd(newPasswd);
				} else {
					response.setContentType("text/html;charset=UTF-8");
					PrintWriter out = response.getWriter();
					out.println("<script>");
					out.println("alert('변경할 비밀번호를 확인해주세요.')");
					out.println("history.back()");
					out.println("</script>");
				}
			} 
			
			boolean isModifyMember = service.isModifyMember(member, sId);
			
			if(isModifyMember) {
				
				// response.setContentType("text/html;charset=UTF-8");
				// PrintWriter out = response.getWriter();
				// out.println("<script>");
				// out.println("alert('회원 정보 수정 완료 (재로그인 해주세요)')");
				// out.println("</script>");
				
				forward = new ActionForward();
				forward.setPath("MemberLogout.me");
				forward.setRedirect(true);
			}
			
		}
		return forward;
	}

}
