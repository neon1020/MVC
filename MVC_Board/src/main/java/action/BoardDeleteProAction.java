package action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardDeleteProService;
import vo.ActionForward;

public class BoardDeleteProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("BoardDeleteProAction");
		ActionForward forward = null;
		
		// 글번호, 페이지번호, 패스워드 파라미터 가져오기
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		String board_pass = request.getParameter("board_pass");
		String pageNum = request.getParameter("pageNum");
		
		// System.out.println(board_num + ", " + board_pass + ", " + pageNum);
		
		BoardDeleteProService service = new BoardDeleteProService();
		
		// BoardDeleteProService 클래스의 isBoardWriter() 메소드 호출하여 패스워드 일치 여부 확인
		// => 파라미터 : 글번호, 패스워드    리턴타입 : boolean(isBoardWriter)
		boolean isBoardWriter = service.isBoardWriter(board_num, board_pass);
		
		
		// 게시물 삭제 권한이 없는 경우(= 패스워드 틀린 경우)
		// 자바스크립트 사용하여 "삭제 권한이 없습니다!" 출력 후 이전 페이지로 돌아가기
		if(!isBoardWriter) {
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('삭제 권한이 없습니다.')");
			out.println("history.back()");
			out.println("</script>");
			
		} else {
			System.out.println("삭제 가능!");
		}
		
		return forward;
	}

}
