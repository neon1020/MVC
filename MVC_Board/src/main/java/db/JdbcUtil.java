package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class JdbcUtil {
	
	// 1. DB 접속을 수행하는 getConnection() 메소드 정의
	// => 파라미터 : 없음   리턴타입 : java.sql.Connection(con)
	
	public static Connection getConnection() {
		Connection con = null;
		
		// -------------------------------------------------------------------------------------------
		try {
			// context.xml 에 설정된 DBCP(커넥션풀) 로부터 Connection 객체 가져오기
			Context initCtx = new InitialContext();
			
			DataSource ds = (DataSource)initCtx.lookup("java:comp/env/jdbc/MySQL");
			
			con = ds.getConnection();
			
			// 추가사항(옵션)
			// 트랜잭션 처리를 위해 데이터베이스(MySQL)의 Auto Commit 기능 해제
			// => Connection 객체의 setAutoCommit() 메소드를 호출하여 false 값 전달
			con.setAutoCommit(false);
			
			// => 주의! 이 이후로 DML(INSERT, UPDATE, DELETE) 및 DDL 등의 작업 수행 후
			//    반드시 commit 작업을 수동으로 실행해야함
			//    (Connection 객체의 commit() 메소드 호출)
			// => 또한, 이전 상태로 되돌리려면 rollback 작업을 수동으로 실행해야함
			//    (Connection 객체의 rollback() 메소드 호출)
			
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return con;
	}
	
	// -----------------------------------------------------------------------------------------------
	
	// 2. DB 자원 반환을 수행하는 close() 메소드 정의
	// => 반환해야하는 대상 객체 : Connection, PreparedStatement, ResultSet
	// => 메소드 이름은 close() 로 통일하고, 파라미터 타입만 다르게 하는 오버로딩 활용하여 정의
	
	public static void close(Connection con) {
		try {
			if(con != null) { con.close(); }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(PreparedStatement pstmt) {
		try {
			if(pstmt != null) { pstmt.close(); }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(ResultSet rs) {
		try {
			if(rs != null) { rs.close(); }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// -----------------------------------------------------------------------------------------------
	
	// 트랜잭션 처리에 필요한 commit, rollback 작업을 수행할 메소드 정의
	// => 단, Connection 객체에 대해 Auto Commit 기능 해제가 선행되어야 함
	// => 파라미터 : Connection 객체(con)
	
	public static void commit(Connection con) {
		try {
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void rollback(Connection con) {
		try {
			con.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}