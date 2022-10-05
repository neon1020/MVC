package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.JdbcUtil;
import vo.AuthInfoBean;
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
			String sql = "INSERT INTO member VALUES(0,?,?,?,?,?,now(),?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getName());
			pstmt.setString(2, member.getId());
			pstmt.setString(3, member.getPasswd());
			pstmt.setString(4, member.getEmail());
			pstmt.setString(5, member.getGender());
			pstmt.setString(6, "N");
			
			insertCount = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("SQL 구문 오류 발생 - insertMember()");
		} finally {
			JdbcUtil.close(pstmt);
		}
		
		return insertCount;
	}
	// ----------------------------------------------------------------------------------------------

	// 로그인 성공 여부 판별
	// => 파라미터 : MemberBean 객체    리턴타입 : boolean(isLoginSuccess)
	public boolean memberLogin(MemberBean member) {
		boolean isLoginSuccess = false;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT * FROM member WHERE id=? AND passwd=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getPasswd());
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				isLoginSuccess = true;
			}
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 - memberLogin()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
		
		return isLoginSuccess;
	}

	// ----------------------------------------------------------------------------------------------
	
	// 회원정보 조회
	// => 파라미터 : (세션)아이디     리턴타입 : MemberBean(member)
	public MemberBean selectMember(String sId) {
		MemberBean member = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT * FROM member WHERE id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, sId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				member = new MemberBean();
				member.setIdx(rs.getInt("idx"));
				member.setId(rs.getString("id"));
				member.setName(rs.getString("name"));
				member.setPasswd(rs.getString("passwd"));
				member.setEmail(rs.getString("email"));
				member.setGender(rs.getString("gender"));
				member.setDate(rs.getDate("date"));
			}
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 - selectMember()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
		
		return member;
	}

	// ----------------------------------------------------------------------------------------------
	
	// 회원 정보 수정
	// 파라미터 : MemberBean 객체, sId   리턴타입 : int(updateCount)
	public int memberModify(MemberBean member, String sId) {
		int updateCount = 0;
		
		PreparedStatement pstmt = null;
		
		try {
			String sql = "UPDATE member SET name=?, id=?, passwd=?, email=?, gender=? WHERE id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getName());
			pstmt.setString(2, member.getId());
			pstmt.setString(3, member.getPasswd());
			pstmt.setString(4, member.getEmail());
			pstmt.setString(5, member.getGender());
			pstmt.setString(6, sId);
			
			updateCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생 - memberModify()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(pstmt);
		}
		
		return updateCount;
	}

	// ----------------------------------------------------------------------------------------------

	// 회원 정보 수정 전 비밀번호 일치 여부 판별
	// => 파라미터 : MemberBean 객체    리턴타입 : boolean(isLoginSuccess)
	public boolean isMember(MemberBean member, String sId) {
		boolean isMember = false;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT * FROM member WHERE id=? AND passwd=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, sId);
			pstmt.setString(2, member.getPasswd());
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				isMember = true;
			}
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 - isMember()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
		
		return isMember;
	}

	// ----------------------------------------------------------------------------------------------
	
	// 회원 인증 정보 등록 작업 수행
	// => 파라미터 : AuthInfoBean 객체     리턴타입 : int(insertCount)
	public int insertAuthInfo(AuthInfoBean authInfo) {
		int insertCount = 0;
		
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT * FROM auth_info WHERE id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, authInfo.getId());
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				sql = "UPDATE auth_info SET auth_code=? WHERE id=?";
				pstmt2 = con.prepareStatement(sql);
				pstmt2.setString(1, authInfo.getAuth_code());
				pstmt2.setString(2, authInfo.getId());
				insertCount = pstmt2.executeUpdate();
				
			} else {
				sql = "INSERT INTO auth_info VALUES(?,?)";
				pstmt2 = con.prepareStatement(sql);
				pstmt2.setString(1, authInfo.getId());
				pstmt2.setString(2, authInfo.getAuth_code());
				insertCount = pstmt2.executeUpdate();
			}
			
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 - insertAuthInfo()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt2);
			JdbcUtil.close(pstmt);
		}
		
		return insertCount;
	}
	
	// ----------------------------------------------------------------------------------------------
	
	// 인증 완료한 회원인지 판별하는 isAuthenticatedUser() 메소드
	// => 파라미터 : MemberBean 객체    리턴타입 : boolean(isAuthenticatedUser)
	public boolean isAuthenticatedUser(MemberBean member) {
		boolean isAuthenticatedUser = false;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT * FROM member WHERE id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getId());
			rs = pstmt.executeQuery();
			
			rs.next();
			if(rs.getString("auth_status").equals("Y")) {
				isAuthenticatedUser = true;
			}
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 - isAuthenticatedUser()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
		
		return isAuthenticatedUser;
	}
	
	// ----------------------------------------------------------------------------------------------

	public int checkAuthInfo(AuthInfoBean authInfo) {
		int updateCount = 0;
		
		
		
		return updateCount;
	}

}
