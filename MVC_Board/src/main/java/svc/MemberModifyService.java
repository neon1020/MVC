package svc;

import java.sql.Connection;

import dao.BoardDAO;
import dao.MemberDAO;
import db.JdbcUtil;
import vo.MemberBean;

public class MemberModifyService {
	
	// 회원 정보 수정 전 본인 확인 하는 isMember() 메소드
	// 파라미터 : 패스워드  리턴타입 : boolean(isMember)
	public boolean isMember(MemberBean member, String sId) {
		boolean isMember = false;
		
		Connection con = JdbcUtil.getConnection();
		
		MemberDAO dao = MemberDAO.getInstance();
		
		dao.setConnection(con);
		
		isMember = dao.isMember(member, sId);
		
		JdbcUtil.close(con);
		
		return isMember;
	}

	// -----------------------------------------------------------------------------
	
	// 회원 정보 수정하는 isModifyMember() 메소드
	// 파라미터 : MemberBean  리턴타입 : boolean
	public boolean isModifyMember(MemberBean member, String sId) {
		boolean isModifyMember = false;
		
		Connection con = JdbcUtil.getConnection();
		
		MemberDAO dao = MemberDAO.getInstance();
		
		dao.setConnection(con);
		
		int updateCount = dao.memberModify(member, sId);
		
		if(updateCount > 0) {
			JdbcUtil.commit(con);
			isModifyMember = true;
		} else {
			JdbcUtil.rollback(con);
		}
		
		JdbcUtil.close(con);
		
		return isModifyMember;
	}
	
}
