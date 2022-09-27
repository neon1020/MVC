package action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardListService;
import vo.ActionForward;
import vo.BoardBean;
import vo.PageInfo;

public class BoardListAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// BoardListService 클래스를 통해 게시물 목록을 조회하여
		// 조회된 결과(List 객체)를 request 객체에 저장 후 qna_board_list.jsp 페이지로 포워딩
		// => 이 때, request 객체 유지 및 URL 유지를 위해 Dispatcher 방식으로 포워딩
		System.out.println("BoardListAction");
		
		ActionForward forward = null;
		
		// 페이징 처리를 위한 변수 선언
		int pageNum = 1; // 현재 페이지 번호를 저장할 변수(기본값 1)
		int listLimit = 10; // 한 페이지에서 표시할 게시물 목록 수를 저장할 변수(기본값 10) 
		
		// 전달된 파라미터 중 "pageNum" 파라미터가 null 이 아닐 경우
		// 해당 파라미터값을 pageNum 변수에 저장
		if(request.getParameter("pageNum") != null) {
			pageNum = Integer.parseInt(request.getParameter("pageNum"));
		}
		
		// BoardListService 클래스 인스턴스 생성 후 getListCount() 메소드를 호출하여
		// 전체 게시물 갯수 리턴받기
		// => 파라미터 : 없음   리턴타입 : int(listCount)
		BoardListService service = new BoardListService();
		
		int listCount = service.getListCount();
		
		// System.out.println("총 게시물 수 : " + listCount);
		
		// BoardListService 객체의 getBoardList() 메소드 호출하여 전체 게시물 목록 가져오기
		// => 파라미터 : 페이지번호(pageNum), 페이지당 목록 갯수(listLimit)
		//	  리턴타입 : List<BoardBean> (boardList)
		List<BoardBean> boardList = service.getBoardList(pageNum, listLimit);
		
		// --------------------------------------------------------------------------------------------------------------
		
		// 페이지 계산 작업 수행
		// 한 페이지에서 표시할 페이지 갯수 설정
		int pageListLimit = 10; // 한 페이지에서 표시할 페이지 목록을 10개로 제한
		
		// 전체 페이지 수 계산
		int maxPage = listCount / pageListLimit;
		if(listCount / pageListLimit > 0) {	maxPage++; }
		
		// 시작 페이지 번호 계산
		int startPage = (pageNum - 1) / pageListLimit * pageListLimit + 1;
		
		// 끝 페이지 번호 계산
		int endPage = startPage + pageListLimit - 1;
		
		// 만약, 끝 페이지 번호(endPage)가 최대 페이지 번호(maxPage)보다 클 경우 
		// 끝 페이지 번호를 최대 페이지 번호로 교체
		if(endPage > maxPage) { endPage = maxPage; }
		
		// 페이징 처리 정보 저장하는 PageInfo 클래스 인스턴스 생성 및 데이터 저장
		PageInfo pageInfo = new PageInfo(pageNum, listLimit, listCount, pageListLimit, maxPage, startPage, endPage);
		
		// --------------------------------------------------------------------------------------------------------------
		
		// request 객체에 뷰페이지로 전달할 데이터(객체) 저장 = request.setAttribute()
		request.setAttribute("boardList", boardList);
		request.setAttribute("pageInfo", pageInfo);
		
		// ActionForward 객체 생성 후 board 폴더의 qna_board_list.jsp 페이지 포워딩 설정
		// => URL 및 request 객체가 유지되어야 하므로 Dispatcher 방식 포워딩 
		forward = new ActionForward();
		forward.setPath("board/qna_board_list.jsp");
		forward.setRedirect(false);
		
		return forward;
	}

}
