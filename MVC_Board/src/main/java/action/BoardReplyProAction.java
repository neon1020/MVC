package action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import svc.BoardReplyProService;
import svc.BoardWriteProService;
import vo.ActionForward;
import vo.BoardBean;

public class BoardReplyProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("BoardReplyProAction");
		
		ActionForward forward = null;
		
		// 파일 수정 작업에 필요한 객체 작업
		int fileSize = 1024 * 1024 * 10; // 파일 업로드 최대 크기(10MB)
		String uploadFolder = "upload"; // 업로드 가상 경로(이클립스)
		String realFolder = request.getServletContext().getRealPath(uploadFolder); // 업로드 실제 경로(톰캣)
		
		MultipartRequest multi = new MultipartRequest(
				request,
				realFolder,
				fileSize,
				"UTF-8",
				new DefaultFileRenamePolicy()
		);
		
		int board_num = Integer.parseInt(multi.getParameter("board_num"));
		int board_re_ref = Integer.parseInt(multi.getParameter("board_re_ref"));
		int board_re_lev = Integer.parseInt(multi.getParameter("board_re_lev"));
		int board_re_seq = Integer.parseInt(multi.getParameter("board_re_seq"));
		String board_name = multi.getParameter("board_name");
		String board_pass = multi.getParameter("board_pass");
		String board_subject = multi.getParameter("board_subject");
		String board_content = multi.getParameter("board_content");
		String pageNum = multi.getParameter("pageNum");
		
		// ---------------------------------------------------------------------------------------------------
		
		BoardBean board = new BoardBean();
		board.setBoard_num(board_num);
		board.setBoard_name(board_name);
		board.setBoard_pass(board_pass);
		board.setBoard_subject(board_subject);
		board.setBoard_content(board_content);
		board.setBoard_re_ref(board_re_ref);
		board.setBoard_re_lev(board_re_lev);
		board.setBoard_re_seq(board_re_seq);
		board.setBoard_file(multi.getParameter("board_file"));
		String fileElement = multi.getFileNames().nextElement().toString();
		board.setBoard_file(multi.getOriginalFileName(fileElement));
		board.setBoard_real_file(multi.getFilesystemName(fileElement));
		
		// System.out.println(board);
		
		// BoardReplyProService 객체의 registReplyBoard() 메소드 호출하여 답글 등록 요청
		// => 파라미터 : BoardBean 객체   리턴타입 : boolean(isReplySuccess)
		BoardReplyProService service = new BoardReplyProService();
		boolean isReplySuccess = service.registReplyBoard(board);
		
		if(!isReplySuccess) {
			
			// 자바스크립트 통해 "답글쓰기 실패" 출력 후 이전 페이지로 이동
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('답글쓰기 실패')");
			out.println("history.back()");
			out.println("</script>");
			
		} else { // 답글쓰기 작업 성공 시
			// 성공 시 글목록(BoardList.bo) 서블릿 주소 요청(파라미터 : 페이지번호)
			forward = new ActionForward();
			forward.setPath("BoardList.bo?pageNum=" + pageNum);
			forward.setRedirect(true);
		}
		
		return forward;
	}

}
