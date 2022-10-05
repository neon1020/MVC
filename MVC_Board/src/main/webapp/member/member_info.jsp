<%@page import="vo.MemberBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@  taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@  taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="<%=request.getContextPath() %>/css/top.css" rel="stylesheet" type="text/css">
</head>

<body>
	<header>
		<jsp:include page="/inc/top.jsp"></jsp:include>
	</header>
	
	<!-- 세션 아이디가 null 일 경우 메인페이지로 돌려보내기 -->
	<c:if test="${sessionScope.sId eq null }">
		<script type="text/javascript">
			alert("잘못된 접근입니다!");
			location.href = "./";
		</script>
	</c:if>
	<h1>회원 정보 조회</h1>
	<form action="MemberModify.me" method="post" name="joinForm">
		<table border="1">
		
		<%
		MemberBean member = (MemberBean)request.getAttribute("member");
		String[] str = member.getEmail().split("@");
		String email1 = str[0];
		String email2 = str[1];
		
		String gender = member.getGender();
		%>
		
			<tr>
				<td>이름</td>
				<td><input type="text" name="name" size="20" value="${member.name }" required="required"></td>
			</tr>
			<tr>
				<td>성별</td>
				<td>
					<input type="radio" name="gender" value="남" <%if(gender.equals("남")) { %> checked="checked" <%} %>>남&nbsp;&nbsp;
					<input type="radio" name="gender" value="여" <%if(gender.equals("여")) { %> checked="checked" <%} %>>여
				</td>
			</tr>
			<tr>
				<td>E-Mail</td>
				<td>
					<%-- <input type="text" name="email1" size="10" value="<%=email1 %>">@ --%>
					<%-- <input type="text" name="email2" size="10" value="<%=email2 %>"> --%>
					
					<!-- 별도의 변수 저장 없이 분리 결과를 즉시 배열로 접근 -->
					<%-- <input type="text" name="email1" value="${fn:split(member.email, '@')[0] }" required="required" size="10">@ --%>
					<%-- <input type="text" name="email2" value="${fn:split(member.email, '@')[1] }" required="required" size="10"> --%>
					
					<!-- EL 을 사용하여 문자열 분리 작업 및 배열 접근을 동일하게 수행하는 방법 -->
					<!-- JSTL functions 라이브러리 필요 -->
					<input type="text" name="email1" size="10" value="${fn:split(member.email, '@')[0] }" required="required">@
					<input type="text" name="email2" size="10" value="${fn:split(member.email, '@')[1] }" required="required">
				</td>
			</tr>
			<tr>
				<td>아이디</td>
				<td>
					<input type="text" name="id" size="20" value="${member.id }" required="required">
				</td>
			</tr>
			
			<tr>
				<td>기존 패스워드</td>
				<td>
					<input type="password" name="passwd" required="required" size="20">
				</td>
			</tr>
			<tr>
				<td>새 패스워드</td>
				<td>
					<input type="password" name="newPasswd" size="20" placeholder="8-20자리 영문자,숫자,특수문자 조합">
					<span id="checkPasswdResult"></span>
				</td>
			</tr>
			<tr>
				<td>새 패스워드 확인</td>
				<td>
					<input type="password" name="newPasswd2" size="20" placeholder="8-20자리 영문자,숫자,특수문자 조합">
					<span id="checkPasswdResult2"></span>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<input type="submit" value="회원정보수정">
					<input type="button" value="취소" onclick="history.back()">
				</td>
			</tr>
		</table>
	</form>
</body>
</html>