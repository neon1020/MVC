package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.xdevapi.PreparableStatement;

import db.JdbcUtil;
import vo.BoardBean;

// 실제 비즈니스 로직을 수행하는 BoardDAO 클래스 정의
// => 각 Service 인스턴스에서 BoardDAO 인스턴스에 접근할 때 고유 데이터가 불필요하므로
//    BoardDAO 인스턴스는 단 하나만 생성하여 Service 에서 공유해도 됨
//    싱글톤 디자인 패턴을 적용하여 클래스를 정의하면 메모리 낭비에 효율적임

public class BoardDAO {
	
	// 싱글톤 디자인 패턴 활용하여 객체 생성
	private BoardDAO() {};
	
	private static BoardDAO instance = new BoardDAO();

	public static BoardDAO getInstance() {
		return instance;
	}
	
	// ----------------------------------------------------------------------------------------------
	
	// 데이터베이스 접근에 사용될 Connection 객체를 Service 클래스로부터 전달받기 위한
	// 멤버변수 및 Setter 메서드 정의
	private Connection con;

	public void setConnection(Connection con) {
		this.con = con;
	}
	
	// ----------------------------------------------------------------------------------------------
	
	// Service 클래스로부터 BoardBean 객체를 전달받아 글쓰기 작업 수행 및 결과 리턴하는 insertBoard() 메소드
	// => 파라미터 : BoardBean 객체  리턴타입 : int (insertCount)
	
	public int insertBoard(BoardBean board) {
		System.out.println("BoardDAO - insertBoard() 호출됨");
		
		// INSERT 작업 결과를 리턴받아 저장할 변수 선언
		int insertCount = 0;
		
		// DB 작업에 필요한 변수 선언
		PreparedStatement pstmt = null, pstmt2 = null;
		ResultSet rs = null;
		
		// board 테이블의 게시물 최대 번호 조회하여 새 글 번호 결정
		// 새 글 번호 저장할 변수 선언 (기본값 = 1)
		int num;
		try {
			num = 1;
			
			String sql = "SELECT MAX(board_num) FROM board";
			// 밖에서 전달받아 저장된 Connection 객체 사용
			pstmt = con.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			// 기존 게시물이 존재하여 최대 번호가 조회되었을 경우
			// 새 글 번호 = 현재 최대 글 번호 + 1
			if(rs.next()) {
				num = rs.getInt(1) + 1;
				System.out.println("새 글 번호 : " + num);
			}
			
			// -------------------------------------------------------------
			
			// 전달받은 데이터(BoardBean 객체)를 사용하여 INSERT 작업 수행
			// => 참조글번호(board_re_ref)는 새 글 번호와 동일한 번호로 지정
			// => 들여쓰기레벨(board_re_lev)과 순서번호(board_re_seq)는 0으로 지정
			
			sql = "INSERT INTO board VALUES(?,?,?,?,?,?,?,?,?,?,?,now())";
			pstmt2 = con.prepareStatement(sql);
			pstmt2.setInt(1, num);
			pstmt2.setString(2, board.getBoard_name());
			pstmt2.setString(3, board.getBoard_pass());
			pstmt2.setString(4, board.getBoard_subject());
			pstmt2.setString(5, board.getBoard_content());
			pstmt2.setString(6, board.getBoard_file());
			pstmt2.setString(7, board.getBoard_real_file());
			pstmt2.setInt(8, num);
			pstmt2.setInt(9, 0);
			pstmt2.setInt(10, 0);
			pstmt2.setInt(11, 0);
			
			insertCount = pstmt2.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("SQL 구문 오류 발생! - insertBoard()");
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
			JdbcUtil.close(pstmt2);
			
			// 주의! DAO 객체 내에서 Connection 객체 반환하지 않도록 함
			// => Service 클래스에서 commit, rollback 작업을 수행하기 위해 접근해야 하기 때문
			// JdbcUtil.close(con); => 하면 안 됨!
		}
		return insertCount; // BoardWriteProService 로 리턴
	}
	
	// ----------------------------------------------------------------------------------------------
	
	// 총 게시물 수를 조회하여 리턴하는 selectListCount() 메소드 정의
	// => 파라미터 : 없음   리턴타입 : int(listCount)
	
	public int selectListCount() {
		System.out.println("BoardDAO - selectListCount() 호출됨");
		int listCount = 0;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// board 테이블의 총 게시물 수 조회
			String sql = "SELECT COUNT(*) FROM board";
			
			pstmt = con.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			// 조회 결과가 있을 경우 첫번째 컬럼 데이터 저장
			if(rs.next()) {
				listCount = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("SQL 구문 오류 - selectListCount()");
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
		
		return listCount;
	}

	// ----------------------------------------------------------------------------------------------
	
	// 게시물 목록 작업을 수행하는 selectBoardList() 메소드 정의
	// 리턴타입 : List<BoardBean> (boardList)  파라미터 : int(pageNum, listLimit)
	
	public List<BoardBean> selectBoardList(int pageNum, int listLimit) {
		System.out.println("BoardDAO - selectBoardList() 호출됨");
		
		List<BoardBean> boardList = null;
		
		// 조회 시작 레코드(게시물) 행 번호 계산
		int startRow = (pageNum - 1) * 10;
		
		// 게시물 조회 구문 작성
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// 게시물 조회 구문 작성
			// => 정렬 : 참조글번호(board_re_ref) 기준 내림차순, 순서번호(board_re_seq) 기준 오름차순
			// => 조회 시작 레코드 행번호(startRow) 부터 listLimit 갯수(10) 만큼만 조회
			String sql = "SELECT * FROM board ORDER BY board_re_ref DESC, board_re_seq LIMIT ?,?";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, listLimit);
			
			rs = pstmt.executeQuery();
			
				
			// 읽어올 게시물 존재할 경우
			// BoardBean 객체 생성하여 1개 레코드(게시물)를 저장 후
			// BoardBean 객체를 List<BoardBean> 객체에 반복하여 추가
			// => 단, 반복분 위에서 먼저 List<BoardBean> 객체 생성이 선행되어야 함
			
			boardList = new ArrayList<BoardBean>();
			
			while(rs.next()) {
				
				// BoardBean 객체에 읽어온 게시물 1개씩 저장
				BoardBean boardBean = new BoardBean();
				
				boardBean.setBoard_num(rs.getInt("board_num"));
				boardBean.setBoard_name(rs.getString("board_name"));
				boardBean.setBoard_pass(rs.getString("board_pass"));
				boardBean.setBoard_subject(rs.getString("board_subject"));
				boardBean.setBoard_content(rs.getString("board_content"));
				boardBean.setBoard_file(rs.getString("board_file"));
				boardBean.setBoard_real_file(rs.getString("board_real_file"));
				boardBean.setBoard_re_ref(rs.getInt("board_re_ref"));
				boardBean.setBoard_re_lev(rs.getInt("board_re_lev"));
				boardBean.setBoard_re_seq(rs.getInt("board_re_seq"));
				boardBean.setBoard_date(rs.getDate("board_date"));
				
				// 1개 게시물 정보가 저장된 BoardBean 객체를 ArrayList 객체에 추가
				boardList.add(boardBean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("SQL 구문 오류 - selectBoardList()");
		}
		
		return boardList;
	}
	
	// ----------------------------------------------------------------------------------------------
	
	// 게시물 조회수 증가 작업 수행하는 updateReadcount() 메소드
	// => 파라미터 : 글번호    리턴타입 : void
	public void updateReadcount(int board_num) {
		
		PreparedStatement pstmt = null;
		
		try {
			String sql = "UPDATE board SET board_readcount=board_readcount+1 WHERE board_num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, board_num);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("SQL 구문 오류 발생! - updateReadcount()");
		} finally {
			JdbcUtil.close(pstmt);
		}
	}
	
	// ----------------------------------------------------------------------------------------------
	
	// 게시물 상세 정보 조회 수행하는 selectBoard() 메소드
	// => 파라미터 : 글번호    리턴타입 : BoardBean(board)
	public BoardBean selectBoard(int board_num) {
		BoardBean board = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT * FROM board WHERE board_num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, board_num);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
				board = new BoardBean();
				
				board.setBoard_num(rs.getInt("board_num"));
				board.setBoard_name(rs.getString("board_name"));
				// board.setBoard_pass(rs.getString("board_pass")); => 패스워드 불필요
				board.setBoard_subject(rs.getString("board_subject"));
				board.setBoard_content(rs.getString("board_content"));
				board.setBoard_file(rs.getString("board_file"));
				board.setBoard_real_file(rs.getString("board_real_file"));
				board.setBoard_re_ref(rs.getInt("board_re_ref"));
				board.setBoard_re_lev(rs.getInt("board_re_lev"));
				board.setBoard_re_seq(rs.getInt("board_re_seq"));
				board.setBoard_readcount(rs.getInt("board_readcount"));
				board.setBoard_date(rs.getDate("board_date"));
			}
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생! - selectBoard()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
		
		return board;
	}

	// ----------------------------------------------------------------------------------------------
	
	// 글번호에 해당하는 레코드의 패스워드 일치 여부 확인 하는 isBoardWriter() 메소드
	// => 파라미터 : 글번호, 패스워드   리턴타입 : boolean(isBoardWriter)
	public boolean isBoardWriter(int board_num, String board_pass) {
		
		boolean isBoardWriter = false;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// 글번호와 패스워드가 일치하는 레코드 검색
			String sql = "SELECT * FROM board WHERE board_num=? AND board_pass=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, board_num);
			pstmt.setString(2, board_pass);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				isBoardWriter = true;
			}
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생! - isBoardWriter()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
		
		return isBoardWriter;
	}
	
	// ----------------------------------------------------------------------------------------------
	
		// 게시물 삭제 작업 수행하는 deleteBoard() 메소드
		// => 파라미터 : 글번호    리턴타입 : int(deleteCount)
		public int deleteBoard(int board_num) {
			int deleteCount = 0;
			
			PreparedStatement pstmt = null;
			
			try {
				// 글번호에 해당하는 레코드 삭제(패스워드는 이미 판별 완료되었으므로 제외)
				String sql = "DELETE FROM board WHERE board_num=?";
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, board_num);
				
				deleteCount = pstmt.executeUpdate();
			} catch (SQLException e) {
				System.out.println("SQL 구문 오류 발생! - deleteBoard()");
				e.printStackTrace();
			} finally {
				JdbcUtil.close(pstmt);
			}
			
			return deleteCount;
		}

		// ----------------------------------------------------------------------------------------------
		
		// 글 수정 작업 수행하는 updateBoard() 메소드
		// => 파라미터 : BoardBean(board)  리턴타입 : int (updateCount)
		public int updateBoard(BoardBean board) {
			
			int updateCount = 0;
			
			PreparedStatement pstmt = null;
			
			try {
				// 글번호가 일치하는 레코드의 작성자, 제목, 내용, 원본파일, 실제파일명 변경
				// => 단, 수정할 파일이 null 일 경우 파일 관련 컬럼은 수정 대상에서 제외
				if(board.getBoard_file() == null) { // 수정할 파일 없을 경우
					String sql = "UPDATE board SET board_name=?, board_subject=?, board_content=? WHERE board_num=?";
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, board.getBoard_name());
					pstmt.setString(2, board.getBoard_subject());
					pstmt.setString(3, board.getBoard_content());
					pstmt.setInt(4, board.getBoard_num());
					
				} else { // 수정할 파일 있을 경우
				
					String sql = "UPDATE board SET board_name=?, board_subject=?, board_content=?, board_file=?, board_real_file=? WHERE board_num=?";
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, board.getBoard_name());
					pstmt.setString(2, board.getBoard_subject());
					pstmt.setString(3, board.getBoard_content());
					pstmt.setString(4, board.getBoard_file());
					pstmt.setString(5, board.getBoard_real_file());
					pstmt.setInt(6, board.getBoard_num());
				}
				
				// 수정할 파일의 존재 여부와 상관없이 UPDATE 구문은 공통으로 실행
				updateCount = pstmt.executeUpdate();
			} catch (SQLException e) {
				System.out.println("SQL 구문 오류 발생 - updateBoard()");
				e.printStackTrace();
			} finally {
				JdbcUtil.close(pstmt);
			}
			
			return updateCount;
		}

		// ----------------------------------------------------------------------------------------------
		
		// 답글 등록 수행하는 insertReplyBoard() 메소드
		// => 파라미터 : BoardBean(board)  리턴타입 : int(insertCount)
		public int insertReplyBoard(BoardBean board) {
			int insertCount = 0;
			
			PreparedStatement pstmt = null, pstmt2 = null;
			ResultSet rs = null;
			
			// board 테이블의 게시물 최대 번호 조회하여 새 글 번호 결정
			// 새 글 번호 저장할 변수 선언 (기본값 = 1)
			int num;
			try {
				num = 1;
				
				String sql = "SELECT MAX(board_num) FROM board";
				// 밖에서 전달받아 저장된 Connection 객체 사용
				pstmt = con.prepareStatement(sql);
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					num = rs.getInt(1) + 1;
				}
				
				// -------------------------------------------------------------
				
				int re_ref = board.getBoard_re_ref(); // 원본글의 참조글 번호
				int re_lev = board.getBoard_re_lev(); // 원본글의 들여쓰기 레벨
				int re_seq = board.getBoard_re_seq(); // 원본글의 순서 번호
				
				// 기존 답글들에 대한 순서번호 증가(UPDATE 구문 사용)
				// => 원본글의 참조글번호(board_re_ref)와 같은 레코드들 중에서
				//    원본글의 순서번호 보다 큰 게시물들의 순서번호를 + 1 씩 처리
				sql = "UPDATE board SET board_re_seq=board_re_seq+1 WHERE board_re_ref=? AND board_re_seq>?";
				pstmt2 = con.prepareStatement(sql);
				pstmt2.setInt(1, re_ref);
				pstmt2.setInt(2, re_seq);
				pstmt2.executeUpdate();
				pstmt2.close(); // JdbcUtil.close(pstmt2); 랑 똑같음
				
				// 새 답글의 들여쓰기 레벨과 순서번호를 원본글의 번호 + 1 처리
				re_lev++;
				re_seq++;
				
				// 답글 INSERT 작업 수행
				sql = "INSERT INTO board VALUES(?,?,?,?,?,?,?,?,?,?,?,now())";
				pstmt2 = con.prepareStatement(sql);
				pstmt2.setInt(1, num);
				pstmt2.setString(2, board.getBoard_name());
				pstmt2.setString(3, board.getBoard_pass());
				pstmt2.setString(4, board.getBoard_subject());
				pstmt2.setString(5, board.getBoard_content());
				pstmt2.setString(6, board.getBoard_file());
				pstmt2.setString(7, board.getBoard_real_file());
				pstmt2.setInt(8, re_ref); // 참조글번호(board_re_ref) = 원본글의 참조글번호
				pstmt2.setInt(9, re_lev); // 들여쓰기레벨(board_re_lev) = 계산된 레벨
				pstmt2.setInt(10, re_seq); // 순서번호(board_re_seq) = 계산된 순서번호
				pstmt2.setInt(11, 0);
				
				insertCount = pstmt2.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("SQL 구문 오류 발생! - insertReplyBoard()");
			} finally {
				JdbcUtil.close(rs);
				JdbcUtil.close(pstmt);
				JdbcUtil.close(pstmt2);
			}
			
			return insertCount;
		}
	
}
