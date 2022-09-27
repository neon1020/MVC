package svc;

import java.sql.Connection;
import java.util.List;

import dao.BoardDAO;
import db.JdbcUtil;
import vo.BoardBean;

public class BoardListService {
	
	// 전체 게시물 수 조회 작업 요청하는 getListCount() 메소드 정의
	// => 파라미터 : 없음  리턴타입 : int(listCount)
	
	public int getListCount() {
		System.out.println("BoardListService - getListCount() 호출됨");
		
		int listCount = 0;
		
		// 1. Connection 객체 가져오기 (공통)
		Connection con = JdbcUtil.getConnection();
		
		// 2. BoardDAO 객체 가져오기 (공통)
		BoardDAO dao = BoardDAO.getInstance();
		
		// 3. BoardDAO 객체에 Connection 객체 전달하기 (공통)
		dao.setConnection(con);
		
		// BoardDAO 객체의 selectListCount() 메소드 호출하여 게시물 총 갯수 가져오기
		// => 파라미터 : 없음   리턴타입 : int (listCount)
		listCount = dao.selectListCount();
		
		// 게시물 조회 수 작업은 commit or rollback 작업 불필요하므로 트랜잭션 처리 과정 없음
		
		// 4. Connection 객체 반환 (공통)
		JdbcUtil.close(con);
		
		return listCount;
	}
	
	// --------------------------------------------------------------------------------------------------------
	
	// 전체 게시물 목록 조회 작업을 요청하는 getBoardList() 메소드 정의
	// => 파라미터 : 페이지번호(pageNum), 페이지 당 목록 갯수(listLimit)
	//    리턴타입 : List<BoardBean>(boardList)
	
	public List<BoardBean> getBoardList(int pageNum, int listLimit) {
		System.out.println("BoardListService - getBoardList() 호출됨");
		
		List<BoardBean> boardList = null;
		
		// 1. Connection 객체 가져오기 (공통)
		Connection con = JdbcUtil.getConnection();
		
		// 2. BoardDAO 객체 가져오기 (공통)
		BoardDAO dao = BoardDAO.getInstance();
		
		// 3. BoardDAO 객체에 Connection 객체 전달하기 (공통)
		dao.setConnection(con);
		
		// BoardDAO 객체의 selectBoardList() 메소드 호출하여 게시물 목록 가져오기
		// => 파라미터 : 페이지번호(pageNum), 페이지당 목록 갯수(listLimit)
		//	  리턴타입 : List<BoardBean> (boardList)
		boardList = dao.selectBoardList(pageNum, listLimit);
		
		// 게시물 목록 조회 작업의 경우 commit or rollback 이 불필요하므로 트랜잭션 처리 과정 없음
		
		// 4. Connection 객체 반환 (공통)
		JdbcUtil.close(con);
		
		return boardList;
	}
}
