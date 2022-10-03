package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardDetailService;
import vo.ActionForward;
import vo.BoardBean;

public class BoardModifyFormAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("BoardModifyFormAction");
		
		ActionForward forward = null;
		
		// 글번호 파라미터 가져오기
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		
		BoardDetailService service = new BoardDetailService();
		BoardBean board = service.getBoard(board_num, "modify");
		// System.out.println(board);
		
		request.setAttribute("board", board);
		// 페이지번호는 Dispatcher 방식으로 포워딩 시 URL 에 존재하므로 별도의 저장 생략
		
		// ActionForward 객체를 통해 qna_board_modify.jsp 페이지로 포워딩 => Dispatcher 방식
		forward = new ActionForward();
		forward.setPath("board/qna_board_modify.jsp");
		forward.setRedirect(false);
		
		return forward;
	}

}
