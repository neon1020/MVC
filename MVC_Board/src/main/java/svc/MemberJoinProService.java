package svc;

import java.sql.Connection;

import dao.BoardDAO;
import dao.MemberDAO;
import db.JdbcUtil;
import vo.MemberBean;

public class MemberJoinProService {
	
	// 회원 등록 작업 수행하는 registMember() 메소드
	// => 파라미터 : MemberBean 객체   리턴타입 : boolean(isRegistSuccess)
	public boolean registMember(MemberBean member) {
		boolean isRegistSuccess = false;
		
		Connection con = JdbcUtil.getConnection();
		
		MemberDAO dao = MemberDAO.getInstance();
		
		dao.setConnection(con);
		
		int insertCount = dao.insertMember(member);
		
		if(insertCount > 0) { // 작업 성공 시
			JdbcUtil.commit(con);
			isRegistSuccess = true;
			
		} else { // 작업 실패 시
			JdbcUtil.rollback(con);
		}
		
		JdbcUtil.close(con);
		
		return isRegistSuccess;
	}
	

}
