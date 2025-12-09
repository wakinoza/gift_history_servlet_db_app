<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:include page="header.jsp" />
<%@page isErrorPage="true" %>
<br>
<a href="Main">メイン画面に戻る</a>
<br>
<p>指定の処理は実行できませんでした</p>
<p><%= exception %></p>
</body>
</html>