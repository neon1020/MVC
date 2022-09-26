package action;

import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import vo.ActionForward;
import vo.BoardBean;

public class BoardWriteProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("BoardWriteProAction");
		
		ActionForward forward = null;
		
		int fileSize = 1024 * 1024 * 10; // 파일 업로드 최대 크기(10MB)
		String uploadFolder = "upload"; // 업로드 가상 경로(이클립스)
		String realFolder = ""; // 업로드 실제 경로(톰캣)
		
		// 톰캣 객체 가져와서 실제 경로 알아내기
		// ServletContext servletContext = request.getServletContext();
		// realFolder = servletContext.getRealPath(uploadFolder);
		
		// 한 문장으로 압축
		realFolder = request.getServletContext().getRealPath(uploadFolder);
		System.out.println(realFolder);
		// D:\workspace_jsp3\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\MVC_Board\ upload
		
		// -------------------------------------------------------------------------------------------------------
		// 추가
		// 게시물 작성자(클라이언트)의 IP 주소를 가져오는 경우
		String writerIpAddr = request.getRemoteAddr();
		System.out.println("작성자 IP 주소 : " + writerIpAddr);
		// -------------------------------------------------------------------------------------------------------
		
		// 파일 업로드 처리(enctype="multipart/form-data")를 위한 MultipartRequest 객체 생성
		// => cos.jar 라이브러리 필요
		MultipartRequest multi = new MultipartRequest(
				request, // 1) 실제 요청 정보(파라미터)가 포함된 request 객체
				realFolder, // 2) 실제 업로드 되는 폴더 경로
				fileSize, // 3) 업로드 파일 최대 크기
				"UTF-8", // 4) 한글 파일명을 처리하기 위한 인코딩 방식
				new DefaultFileRenamePolicy() // 5) 중복 파일명에 대한 기본 처리 담당 객체 생성
		);
		
		// 전달받은 파라미터 데이터를 BoardBean 객체 생성 후 저장
		BoardBean board = new BoardBean();
		board.setBoard_name(multi.getParameter("board_name"));
		board.setBoard_pass(multi.getParameter("board_pass"));
		board.setBoard_subject(multi.getParameter("board_subject"));
		board.setBoard_content(multi.getParameter("board_content"));
		board.setBoard_file(multi.getParameter("board_file"));
		
		// 단, 파일명을 가져올 때는 단순히 getParameter() 메소드로 처리 불가
		// 1) 파일명을 관리하는 객체에 접근하여 파일명 목록 중 첫번째 파일명 가져오기
		String fileElement = multi.getFileNames().nextElement().toString();
		
		// 2) 1번 과정에서 가져온 이름을 활용하여 원본 파일명과 실제 업로드 된 파일명 가져오기
		board.setBoard_file(multi.getOriginalFileName(fileElement)); // 원본 파일명
		board.setBoard_real_file(multi.getFilesystemName(fileElement)); // 실제 업로드 된 파일명
		
		System.out.println(board); // toString 생략
		
		
		// BoardWriteProService 인스턴스 생성 후 registBoard() 메소드를 호출하여 글쓰기 작업 요청
		// => 파라미터 : BoardBean 객체    리턴타입 : boolean(isWriteSuccess)
		// 글쓰기 비즈니스 로직 요청에 대한 처리 완료됐다고 가정
		boolean isWriteSuccess = false;
		
		if(!isWriteSuccess) { // 글쓰기 작업 실패 시
			
			// 자바스크립트 통해 "글쓰기 실패" 출력 후 이전 페이지로 이동
			
			// => 웹브라우저로 HTML 코드 등을 내보내기 위한 작업 수행
			//    (자바 클래스에서 출력 스트림 형태로 HTML 태그를 출력해야함
			// 1. 출력하는 HTML 문서에 대한 문서 타입(contentType) 설정
			// => 응답 데이터의 타입으로 HTML 태그가 사용됨을 클라이언트에게 알려주는 역할
			// => response 객체의 setContentType() 메소드를 호출하여 문서 타입 문자열 전달
			//    (jsp 파일 최상단 page 디렉티브 내의 contentType=XXX 항목의 XXX 부분 활용)
			response.setContentType("text/html; charset=UTF-8");
			
			// 2. 자바 코드를 사용하여 HTML 태그나 스크립트 등을 출력(전송)하려면
			//    java.io.PrintWriter 객체 필요 (출력스트림 사용 가능한 객체)
			// => response 객체의 getWriter() 메소드를 호출하여 얻어올 수 있다!
			PrintWriter out = response.getWriter();
			
			// 3. PrintWriter 객체의 print() 또는 println() 메소드를 호출하여
			//    파라미터로 HTML 태그나 자바스크립트 코드를 전달(문자열 형태로 전달)
			out.println("<script>");
			out.println("alert('글쓰기 실패')");
			out.println("history.back()");
			out.println("</script>");
			
			
		} else { // 글쓰기 작업 성공 시 (= isWriteSucces 가 true 일 경우)
			
			// 포워딩 정보 저장을 위한 ActionForward 객체 생성 후
			// 포워딩 경로를 BoardList.bo 로 설정하고, 포워딩 방식을 Redirect 방식으로 설정
			// => 별도의 request 객체 유지 불필요, 새로운 요청으로 주소 변경
			forward = new ActionForward();
			forward.setPath("BoardList.bo");
			forward.setRedirect(true);
		}
		
		return forward;
	}

}
