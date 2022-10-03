package svc;

import java.sql.Connection;

import dao.BoardDAO;
import db.JdbcUtil;
import vo.BoardBean;

public class BoardDetailService {
	
	// 게시물 조회하는 getBoard() 메소드
	// => 파라미터 : 글번호(board_num)  리턴타입 : BoardBean(board)
	public BoardBean getBoard(int board_num, String work) {
		BoardBean board = null;
		
		// 공통 1
		Connection con = JdbcUtil.getConnection();
		
		// 공통 2
		BoardDAO dao = BoardDAO.getInstance();
		
		// 공통 3
		dao.setConnection(con);
		
		// 작업이 "글 상세내용 보기"일 때만 조회수 증가 작업 수행
		// BoardDAO 클래스의 updateReadcount() 메소드 호출하여 게시물 조회수 증가 작업 수행
		// => 파라미터 : 글번호    리턴타입 : void
		if(work.equals("detail")) {
			dao.updateReadcount(board_num);
		}
		
		// BoardDAO 클래스의 selectBoard() 메소드 호출하여 게시물 상세 정보 조회 작업 수행 후
        // 리턴되는 BoardBean 객체 전달받아 저장
		// => 파라미터 : 글번호    리턴타입 : BoardBean(board)
		board = dao.selectBoard(board_num);
		
		// 리턴받은 BoardBean 객체를 리턴받아 null 일 경우 rollback,
		// 아니면 commit 작업 수행
		if(board != null) {
			JdbcUtil.commit(con);
		} else {
			JdbcUtil.rollback(con);
		}
		
		// 공통 4
		JdbcUtil.close(con);
		
		return board;
	}
}
