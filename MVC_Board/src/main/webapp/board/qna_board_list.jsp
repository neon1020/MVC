
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MVC 게시판</title>
<style type="text/css">
	#listForm {
		width: 1024px;
		max-height: 610px;
		margin: auto;
	}
	
	h2 {
		text-align: center;
	}
	
	table {
		margin: auto;
		width: 1024px;
	}
	
	#tr_top {
		background: orange;
		text-align: center;
	}
	
	#pageList {
		margin: auto;
		width: 1024px;
		text-align: center;
	}
	
	#emptyArea {
		margin: auto;
		width: 1024px;
		text-align: center;
	}
	
	#buttonArea {
		margin: auto;
		width: 1024px;
		text-align: right;
	}
	
	
</style>
</head>
<body>
	<!-- 게시판 리스트 -->
	<section id="listForm">
		<h2>게시판 글 목록</h2>
		<table>
				<tr id="tr_top">
					<td width="100px">번호</td>
					<td>제목</td>
					<td width="150px">작성자</td>
					<td width="150px">날짜</td>
					<td width="100px">조회수</td>
				</tr>
		</table>
	</section>
	<section id="buttonArea">
		<input type="button" value="글쓰기" onclick="location.href='BoardWriteForm.bo'" />
	</section>
	<section id="pageList">
			<input type="button" value="이전">
			1 2 3
			<input type="button" value="다음">
	</section>
</body>
</html>
