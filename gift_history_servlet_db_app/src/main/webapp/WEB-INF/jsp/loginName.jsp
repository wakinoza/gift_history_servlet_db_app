<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<p>
<c:out value="${loginUser.name}" />さん、ログイン中
<a href="Logout">ログアウトする</a>
</p>
<br>