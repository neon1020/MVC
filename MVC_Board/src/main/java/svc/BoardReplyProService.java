package svc;

import java.sql.Connection;

import dao.BoardDAO;
import db.JdbcUtil;
import vo.BoardBean;

public class BoardReplyProService {
	
	// 답글 쓰기 작업 수행하는 registReplyBoard() 메소드
	// => 파라미터 : BoardBean 객체   리턴타입 : boolean(isReplySuccess)
	public boolean registReplyBoard(BoardBean board) {
		System.out.println("BoardReplyProService - registReplyBoard() 호출됨");
		
		boolean isReplySuccess = false;
		
		Connection con = JdbcUtil.getConnection();
		
		BoardDAO dao = BoardDAO.getInstance();
		
		dao.setConnection(con);
		
		// 5. insertReplyBoard() 메소드를 호출하여 답글쓰기 작업 수행 및 결과 리턴받기
		// => 파라미터 : BoardBean 객체   리턴타입 : int(insertCount)
		int insertCount = dao.insertReplyBoard(board);
		
		if(insertCount > 0) { // 작업 성공 시
			JdbcUtil.commit(con);
			isReplySuccess = true;
		} else { // 작업 실패 시
			JdbcUtil.rollback(con);
		}
		
		JdbcUtil.close(con);

		return isReplySuccess;
	}
	
}
