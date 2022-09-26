<%@page import="vo.BoardBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	BoardBean article = (BoardBean)request.getAttribute("article");
	String nowPage = (String)request.getAttribute("page");
%>    

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MVC 게시판</title>
<style type="text/css">
	#replyForm {
		width: 500px;
		height: 610px;
		border: 1p solid red;
		margin: auto;
	}
	
	h2 {
		text-align: center;
	}
	
	table {
		margin: auto;
		width: 450px;
	}
	
	.td_left {
		width: 150px;
		background: orange;
	}
	
	.td_right {
		width: 300px;
		background: skyblue;
	}
	
	#commandCell {
		text-align: center;
	}
	
	#board_content {
		resize: none;
	}
</style>
</head>
<body>
	<!-- 게시판 답변하기 -->
	<section id="replyForm">
		<h2>글 답변하기</h2>
		<form action="BoardReplyPro.bo" method="post" name="replyForm">
			<!-- 답변 글 작성에 필요한 게시물 정보 중 입력받지 않는 정보도 함께 전달 -->
			<input type="hidden" name="page" value="<%=nowPage %>" />
			<input type="hidden" name="board_num" value="<%=article.getBoard_num() %>" />
			<input type="hidden" name="board_re_ref" value="<%=article.getBoard_re_ref() %>" />
			<input type="hidden" name="board_re_lev" value="<%=article.getBoard_re_lev() %>" />
			<input type="hidden" name="board_re_seq" value="<%=article.getBoard_re_seq() %>" />
			<table>
				<tr>
					<td class="td_left"><label for="board_name">글쓴이</label></td>
					<td class="td_right"><input type="text" name="board_name" required="required" /></td>
				</tr>
				<tr>
					<td class="td_left"><label for="board_pass">비밀번호</label></td>
					<td class="td_right"><input type="password" name="board_pass" required="required" /></td>
				</tr>
				<tr>
					<td class="td_left"><label for="board_subject">제목</label></td>
					<td class="td_right"><input type="text" name="board_subject" required="required" /></td>
				</tr>
				<tr>
					<td class="td_left"><label for="board_content">내용</label></td>
					<td class="td_right">
						<textarea id="board_content" name="board_content" cols="40" rows="15" required="required">
						------------- 원본 글 내용 -------------
							<%=article.getBoard_content() %>
						</textarea>
					</td>
				</tr>
			</table>
			<section id="commandCell">
				<input type="submit" value="답변글등록">&nbsp;&nbsp;
				<input type="reset" value="다시쓰기">
			</section>
		</form>
	</section>
</body>
</html>


