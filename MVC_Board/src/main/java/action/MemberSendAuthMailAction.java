package action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auth.GenerateUserAuthenticationCode;
import auth.SendMail;
import svc.MemberSendAuthMailService;
import vo.ActionForward;
import vo.AuthInfoBean;

public class MemberSendAuthMailAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("MemberSendAuthMailAction");
		
		ActionForward forward = null;
		
		String id = request.getParameter("id");
		String email = request.getParameter("email");
		
		// System.out.println(id + ", " + email);
		
		// GenerateUserAuthenticationCode 클래스 인스턴스 생성 및 Getter 호출하여 인증코드 리턴
		// => 파라미터로 생성할 코드의 길이 전달(ex. 50글자)
		GenerateUserAuthenticationCode code = new GenerateUserAuthenticationCode(50);
		String authCode = code.getAuthCode();
		// System.out.println("인증코드 : " + authCode);
		
		// AuthInfoBean 객체 생성 및 아이디, 인증코드 저장
		AuthInfoBean authInfo = new AuthInfoBean(id, authCode);
		
		// 인증 메일 발송에 사용될 정보(수신자 메일주소, 메일 제목, 메일 본문) 생성
		// 수신자 메일주소는 위의 email (가입자의 이메일) 그대로 사용
		String subject = "[아이티윌] 아이티윌 가입 인증 메일입니다.";
		String content = "<a href='http://localhost:8080/MVC_Board/MemberAuth.me?id=" + id +"&authCode=" + authCode + "'><b>인증하려면 클릭하세요.</b></a>";
		
		// 인증 메일 발송을 위해 SendMail 클래스 인스턴스 생성 및 sendMail() 메소드 호출하여 메일 발송
		// => 파라미터 : 수신자 메일주소, 메일 제목, 메일 본문
		SendMail sendMail = new SendMail();
		boolean isSendSuccess = sendMail.sendMail(email, subject, content);
		
		// 인증 정보 DB에 등록 작업 요청
		// MemberSendAuthMailService 클래스의 registAuthInfo() 메소드 호출
		// => 파라미터 : AuthInfoBean 객체     리턴타입 : boolean(isRegistSuccess)
		MemberSendAuthMailService service = new MemberSendAuthMailService();
		boolean isRegistSuccess = service.registAuthInfo(authInfo);
		
		forward = new ActionForward();
		forward.setRedirect(true);
		
		// 만약, 인증 메일 발송 실패 또는 인증 정보 등록 실패일 경우
		// => 새로운 인증 정보 발송을 위해 MemberSendAuthMail.me 요청(Redirect)
		//    (파라미터 : 아이디, 이메일)
		// 아니면, 회원 가입 완료 페이지("MemberJoinResult.me") 서블릿 요청(Redirect)
		if(!isSendSuccess || !isRegistSuccess) {
			forward.setPath("MemberJoinResult.me?id=" + id + "&email=" + email);
			
		} else {
			forward.setPath("MemberJoinResult.me");
		}
		
		return forward;
	}

}
