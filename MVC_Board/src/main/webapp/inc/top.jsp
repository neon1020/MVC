<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@  taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 세션 아이디("sId") 없을 경우 Login(MemberLoginForm.me) 과 Join(MemberJoinForm.me) 링크 표시 -->
<!-- 세션 아이디 있을 경우 세션아이디 와 Logout(MemberLogout.me) 링크 표시 -->
<div id="member_area">
	<c:choose>
		<c:when test="${empty sessionScope.sId }">
			<a href="MemberLoginForm.me">Login</a> | <a href="MemberJoinForm.me">Join</a>
		</c:when>
		<c:otherwise>
			${sessionScope.sId } 님 | <a href="MemberLogout.me">Logout</a>
		</c:otherwise>
	</c:choose>
</div>