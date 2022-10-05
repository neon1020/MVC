package svc;

import java.sql.Connection;

import dao.MemberDAO;
import db.JdbcUtil;
import vo.MemberBean;

public class MemberInfoService {

	public MemberBean getMemberInfo(String sId) {
		MemberBean member = null;
		
		Connection con = JdbcUtil.getConnection();
		
		MemberDAO dao = MemberDAO.getInstance();
		
		dao.setConnection(con);
		
		member = dao.selectMember(sId);
		
		return member;
	}
	
}
