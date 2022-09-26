package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vo.ActionForward;

public interface Action {
	
	// XXXAction 클래스에서 수행할 작업을 공통 메소드인 execute() 메소드로 정의
	// => XXXAction 클래스에서 execute() 메소드를 구현하도록 강제하기 위해 추상메소드로 정의
	// => 해당 클래스에서 상속에 대한 제약을 덜어주기 위해 인터페이스로 정의(다중 상속(구현)이 가능)
	// => 파라미터 : HttpServletRequest(request), HttpServletResponse(response)
	// => 리턴타입 : ActionForward
	// => 발생하는 모든 예외를 호출한 곳으로 위임(던지기) = throws
	
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
