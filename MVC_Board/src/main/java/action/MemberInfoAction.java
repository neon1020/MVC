package action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import svc.MemberInfoService;
import vo.ActionForward;
import vo.MemberBean;

public class MemberInfoAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("MemberInfoAction");
		
		ActionForward forward = null;
		
		// 세션 객체에 저장된 세션 아이디(sId) 가 존재하는지 판별
		// => 존재하지 않을 경우 자바스크립트를 통해 "잘못된 접근입니다" 출력 후 메인페이지 포워딩
		// => 존재할 경우 MemberInfoService - getMemberInfo() 메소드 호출하여 회원 정보 조회 요청
		
		HttpSession session = request.getSession();
		String sId = (String)session.getAttribute("sId");
		
		if(sId == null) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('잘못된 접근입니다.')");
			out.println("history.back()");
			out.println("</script>");
		} else {
			
			// MemberInfoService - getMember() 메소드 호출하여 회원 정보 조회 요청
			// => 파라미터 : 아이디     리턴타입 : MemberBean(member)
			MemberInfoService service = new MemberInfoService();
			MemberBean member = service.getMemberInfo(sId);
			
			// request 객체에 회원 정보 저장된 MemberBean 객체 저장
			request.setAttribute("member", member);
			
			// ActionForward 객체를 통해 member_info.jsp 페이지 포워딩(Dispatcher)
			forward = new ActionForward();
			forward.setPath("member/member_info.jsp");
			forward.setRedirect(false);
		}
		
		return forward;
	}

}
