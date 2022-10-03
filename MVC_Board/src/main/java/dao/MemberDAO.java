package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.JdbcUtil;
import vo.MemberBean;

public class MemberDAO {
	
	// 싱글톤 디자인 패턴 활용하여 객체 생성
	private MemberDAO() {};
	
	private static MemberDAO instance = new MemberDAO();

	public static MemberDAO getInstance() {
		return instance;
	}
	
	// ----------------------------------------------------------------------------------------------
	
	private Connection con;

	public void setConnection(Connection con) {
		this.con = con;
	}
	
	// ----------------------------------------------------------------------------------------------
	
	// 회원 등록 작업 수행하는 insertMember() 메소드
	// => 파라미터 : MemberBean 객체   리턴타입 : int(insetCount)
	public int insertMember(MemberBean member) {
		int insertCount = 0;
		
		PreparedStatement pstmt = null;
		
		try {
			String sql = "INSERT INTO member VALUES(0,?,?,?,?,?,now())";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getName());
			pstmt.setString(2, member.getId());
			pstmt.setString(3, member.getPasswd());
			pstmt.setString(4, member.getEmail());
			pstmt.setString(5, member.getGender());
			
			insertCount = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("SQL 구문 오류 발생 - insertMember()");
		} finally {
			JdbcUtil.close(pstmt);
		}
		
		return insertCount;
	}
}
