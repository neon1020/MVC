package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardDetailService;
import vo.ActionForward;
import vo.BoardBean;

public class BoardDetailAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("BoardDetailAction");

		ActionForward forward = null;
		
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		// String pageNum = request.getParameter("pageNum");
		
		// => "pageNum" 파라미터는 현재 작업에서 실제 데이터로 활용되지 않고 
		//     다음 페이지 포워딩 시 URL 또는 request 객체에 함께 전달되므로 정수 변환 불필요함
		// => 만약, Dispatcher 방식으로 포워딩 할 경우 주소가 유지되므로 파라미터 가져오기 불필요
		
		// System.out.println(board_num + ", " + pageNum);
		
		BoardDetailService service = new BoardDetailService();
		BoardBean board = service.getBoard(board_num);
		System.out.println(board);
		
		// request 객체에 뷰페이지로 전달할 데이터(객체) 저장
		request.setAttribute("board", board);
		
		// ActionForward 객체 생성 및 qna_board_view.jsp 페이지 포워딩 설정
		// => URL 유지 및 request 객체 유지를 위해 Dispatcher 방식 포워딩
		forward = new ActionForward();
		forward.setPath("board/qna_board_view.jsp");
		forward.setRedirect(false);
		
		return forward;
	}

}
