package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardDetailService;
import vo.ActionForward;
import vo.BoardBean;

public class BoardReplyFormAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("BoardReplyFormAction");
		
		ActionForward forward = null;
		
		// 글번호 파라미터 가져오기
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		
		// 기존에 정의되어 있는 BoardDetailService 의 getBoard() 메소드를 호출하여 게시물 조회 요청
		// => 파라미터 : 글번호    리턴타입 : BoardBean(board)
		BoardDetailService service = new BoardDetailService();
		BoardBean board = service.getBoard(board_num, "reply");
		// System.out.println(board);
		
		request.setAttribute("board", board);
		// 페이지번호는 Dispatcher 방식으로 포워딩 시 URL 에 존재하므로 별도의 저장 생략
		
		// ActionForward 객체를 통해 qna_board_modify.jsp 페이지로 포워딩 => Dispatcher 방식
		forward = new ActionForward();
		forward.setPath("board/qna_board_reply.jsp");
		forward.setRedirect(false);
		
		return forward;
	}

}
