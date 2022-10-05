package svc;

import java.sql.Connection;

import dao.MemberDAO;
import db.JdbcUtil;
import vo.MemberBean;

public class MemberLoginProService {
	
	// 로그인 작업 요청하는 memberLogin() 메소드
	// => 파라미터 : MemberBean 객체  리턴타입 : boolean(isLoginSuccess)
	public boolean memberLogin(MemberBean member) {
		boolean isLoginSuccess= false;
		
		Connection con = JdbcUtil.getConnection();
		
		MemberDAO dao = MemberDAO.getInstance();
		
		dao.setConnection(con);
		
		// MemberDAO 클래스의 memberLogin() 메소드
		isLoginSuccess = dao.memberLogin(member);
		
		return isLoginSuccess;
	}

	// -------------------------------------------------------------------------------------
	
	public boolean isAuthenticatedUser(MemberBean member) {
		boolean isAuthenticatedUser = false;
		
		Connection con = JdbcUtil.getConnection();
		
		MemberDAO dao = MemberDAO.getInstance();
		
		dao.setConnection(con);
		
		isAuthenticatedUser = dao.isAuthenticatedUser(member);
		
		return isAuthenticatedUser;
	}
	
}
