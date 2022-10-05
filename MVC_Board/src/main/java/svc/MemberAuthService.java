package svc;

import java.sql.Connection;

import dao.MemberDAO;
import db.JdbcUtil;
import vo.AuthInfoBean;

public class MemberAuthService {

	public boolean checkAuthInfo(AuthInfoBean authInfo) {
		boolean isAuthenticationSuccess = false;
		
		Connection con = JdbcUtil.getConnection();
		
		MemberDAO dao = MemberDAO.getInstance();
		
		dao.setConnection(con);
		
		int updateCount = dao.checkAuthInfo(authInfo);
		
		if(updateCount > 0) {
			JdbcUtil.commit(con);
			isAuthenticationSuccess = true;
		} else {
			JdbcUtil.rollback(con);
		}
		
		return isAuthenticationSuccess;
	}
	
}
