package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.Action;
import action.MemberJoinProAction;
import vo.ActionForward;

@WebServlet("*.me")
public class MemberFrontcontroller extends HttpServlet {
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		// 공통으로 사용할 변수 선언
		Action action = null;
		ActionForward forward = null;
		
		// 서블릿 주소 추출
		String command = request.getServletPath();
		System.out.println("command : " + command);
		
		if(command.equals("/MemberJoinForm.me")) {
			
			forward = new ActionForward();
			forward.setPath("member/member_join_form.jsp");
			forward.setRedirect(false);
			
		} else if(command.equals("/MemberLoginForm.me")) {
			
			forward = new ActionForward();
			forward.setPath("member/member_login_form.jsp");
			forward.setRedirect(false);
			
		} else if(command.equals("/MemberLoginPro.me")) {
			
		} else if(command.equals("/MemberJoinPro.me")) {
			
			action = new MemberJoinProAction();
			
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else if(command.equals("/MemberLogout.me")) {
			
		}
		
		// --------------------------------------------------------------------------------
		// ActionForward 객체 내용에 따라 각각 다른 포워딩 작업 수행(포워딩 작업 공통 처리)
		// 1. ActionForward 객체가 null 이 아닐 경우 판별
		if(forward != null) {
			// 2. ActionForward 객체에 저장된 포워딩 방식 판별
			if(forward.isRedirect()) { // Redirect 방식
				response.sendRedirect(forward.getPath());
			} else { // Dispatcher 방식
				RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPath());
				dispatcher.forward(request, response);
			}
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

}
