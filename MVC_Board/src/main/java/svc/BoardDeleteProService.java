package svc;

import java.sql.Connection;

import dao.BoardDAO;
import db.JdbcUtil;

public class BoardDeleteProService {
	public boolean isBoardWriter(int board_num, String board_pass) {
		System.out.println("BoardDeleteProService - isBoardWriter() 호출됨");
		
		boolean isBoardWriter = false;
		
		Connection con = JdbcUtil.getConnection();
		
		BoardDAO dao = BoardDAO.getInstance();
		
		dao.setConnection(con);
		
		isBoardWriter = dao.isBoardWriter(board_num, board_pass);
		// System.out.println(isBoardWriter);
		
		JdbcUtil.close(con);
		
		return isBoardWriter;
	}
}
