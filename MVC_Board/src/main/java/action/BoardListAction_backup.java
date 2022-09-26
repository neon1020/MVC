package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vo.ActionForward;

public class BoardListAction_backup {
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("BoardListAction");
		
		// 글 목록 작업 오는 곳
		
		// Dispatcher 방식 포워딩 설정
		ActionForward forward = new ActionForward();
		forward.setPath("board/qna_board_list.jsp");
		forward.setRedirect(false);
		
		return forward;
	}
}
