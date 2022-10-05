package svc;

import java.sql.Connection;

import dao.MemberDAO;
import db.JdbcUtil;
import vo.AuthInfoBean;

public class MemberSendAuthMailService {
	
	// 회원 인증 정보 저장하는 registAuthInfo() 메소드
	// => 파라미터 : AuthInfoBean 객체     리턴타입 : boolean(isRegistSuccess)
	public boolean registAuthInfo(AuthInfoBean authInfo) {
		boolean isRegistSuccess = false;
		
		Connection con = JdbcUtil.getConnection();
		
		MemberDAO dao = MemberDAO.getInstance();
		
		dao.setConnection(con);
		
		int insertCount = dao.insertAuthInfo(authInfo);
		
		if(insertCount > 0) {
			JdbcUtil.commit(con);
			isRegistSuccess = true;
		} else {
			JdbcUtil.rollback(con);
		}
		
		return isRegistSuccess;
	}
}
