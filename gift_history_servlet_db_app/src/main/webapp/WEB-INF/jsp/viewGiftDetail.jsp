<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="header.jsp" />
<jsp:include page="loginName.jsp" />

<div style="margin: 5px; background-color:#1760a0; color:white; padding-left: 10px;">
   <h2　style="margin: 0;">頂き物の詳細</h2>
</div>
<div style="margin: 5px; border-bottom: 1px solid #ccc; padding-bottom: 5px;">
<p>いただいた日付：<c:out value="${currentGiftItem.whenis}" /></p>
<p>いただいた品物：<c:out value="${currentGiftItem.what}" /></p>
<p>贈り主：<c:out value="${currentGiftItem.who}" />さん</p>
<p>贈られた理由：<c:out value="${currentGiftItem.why}" /></p>
<p>おおよとしての値段：<c:out value="${currentGiftItem.howMuch}" />円ほど</p>
<p>返礼が必要か：<c:out value="${currentGiftItem.needReturn}" /></p>

<c:if test="${currentGiftItem.needReturn == '必要'}">
<p>返礼をしたかどうか：<c:out value="${currentGiftItem.hasGaveReturn}" /></p>
</c:if>
<br>

<c:if test="${currentGiftItem.needReturn == '必要' && currentGiftItem.hasGaveReturn == '未返礼'}">
  <form action="ViewGiftDetail?action=returned" method="post" style="display:inline-block;">
    <input type="hidden" name="id" value="<c:out value='${currentGiftItem.id}' />">
    <button type="submit" style="background:blue; border:none; border-radius:5px; color:white; cursor:pointer; padding:5px;">返礼済み</button>
  </form>
</c:if>
<form action="ViewGiftDetail?action=remove" method="post" style="display:inline-block;">
  <input type="hidden" name="id" value="<c:out value='${currentGiftItem.id}' />">
  <button type="submit" style="background:orange; border:none; border-radius:5px; cursor:pointer; padding:5px;">記録を削除</button>
</form> 
</div>
<br>
<a href="Main">メイン画面に戻る</a>

<jsp:include page="footer.jsp" />