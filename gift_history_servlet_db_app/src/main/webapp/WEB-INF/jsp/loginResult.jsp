<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="header.jsp" />

<c:choose>
 <c:when test="${empty loginUser}">
  <p>ログインに失敗しました</p>
  <a href="index.jsp">TOPへ</a>
 </c:when>
 <c:otherwise>
  <p>ログインに成功しました</p>
  <p>ようこそ ${loginUser.name}さん</p>
  <a href="Main">頂き物記録ページに移動する</a>
 </c:otherwise>
</c:choose>

<jsp:include page="footer.jsp" />