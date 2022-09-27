package svc;

import java.sql.Connection;

import dao.BoardDAO;
import db.JdbcUtil;
import vo.BoardBean;

//Action 클래스로부터 요청을 받아 DAO 클래스와 상호작용을 통해 실제 DB 작업을 수행하는 클래스

public class BoardWriteProService {
	// 글쓰기 작업 요청을 위한 registBoard() 메소드 정의
	// => 파라미터 : BoardBean 객체  리턴타입 : boolean(isWriteSuccess)
	public boolean registBoard(BoardBean board) {
		System.out.println("BoardWriteProService - registBoard() 호출됨");
		
		// 1. 글쓰기 작업 요청 처리 결과를 저장할 boolean 타입 변수 선언
		boolean isWriteSuccess = false;
		
		// 2. JdbcUtil 객체로부터 Connection Pool 에 저장된 Connection 객체 가져오기 (공통)
		// => 트랜잭션 관리를 Service 클래스가 수행하므로 
		//    DAO 가 아닌 Service 클래스가 Connection 객체를 관리함
		Connection con = JdbcUtil.getConnection();
		
		// 3. BoardDAO 클래스로부터 BoardDAO 객체 가져오기 (공통)
		BoardDAO dao = BoardDAO.getInstance();
		
		// 4. BoardDAO 객체의 setConnection() 메소드를 호출하여 Connection 객체 전달 (공통)
		dao.setConnection(con);
		
		// 5. BoardDAO 객체의 XXX 메소드를 호출하여 XXX 작업 수행 요청 및 결과 리턴받기
		
		// insertBoard() 메소드 호출하여 글쓰기 작업 수행 및 결과 리턴받기
		// => 파라미터 : BoardBean 객체   리턴타입 : int(insertCount)
		int insertCount = dao.insertBoard(board);
		
		// 6. 작업 처리 결과에 따른 트랜잭션 처리
		if(insertCount > 0) { // 작업 성공 시
			
			// INSERT 작업이 성공했을 경우 트랜잭션 처리(적용 = commit)을 위해
			// JdbcUtil 클래스의 commit() 메소드를 호출하여 commit 작업 수행
			// Service 클래스가 관리하는 Connection 객체 필요
			
			JdbcUtil.commit(con);
			
			// 작업 처리 결과를 성공으로 표시하여 리턴하기 위해 isWriteSuccess 를 true 로 변경
			isWriteSuccess = true;
			
		} else { // 작업 실패 시
			
			// INSERT 작업이 실패했을 경우 트랜잭션 처리의 취소를 위해
			// JdbcUtil 클래스의 rollback() 메소드를 호출하여 rollback 작업 수행
			
			JdbcUtil.rollback(con);
		}
		
		// 7. Connection Pool 로부터 가져온 Connection 객체 반환(공통)
		// => 주의! DAO 객체 내에서 Connection 객체를 반환하지 않도록 해야한다!
		// 	  Connection 객체는 무조건 Service에서 관리!!
		JdbcUtil.close(con);
		
		// 8. 작업 요청 처리 결과 리턴
		return isWriteSuccess; // BoardWriteProAction 으로 리턴
	}
}
