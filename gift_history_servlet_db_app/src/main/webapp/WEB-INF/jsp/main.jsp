<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="header.jsp" />

<form action="NewGift">
  <button type="submit" style="background:orange; border:none; border-radius:5px; cursor:pointer; padding:5px;">頂き物の新規記録</button>
</form>
<br>

<div style="margin: 5px; background-color:#1760a0; color:white; padding-left: 10px;">
   <h2　style="margin: 0;">頂き物一覧</h2>
</div>
<c:choose>
  <c:when test="${empty giftItemList}">
    <div style="margin: 5px; border-bottom: 1px solid #ccc; padding-bottom: 5px;">
      <p>頂き物の記録はありません</p>
    </div>
  </c:when>
  
  <c:otherwise>
    <c:forEach var="gift" items="${giftItemList}">
       <div style="margin: 5px; border-bottom: 1px solid #ccc; padding-bottom: 5px;　display: flex; align-items: center;">
         <span style="margin-right: 15px;">
         <c:choose>
           <c:when test="${gift.needReturn == '必要'}">
             ${gift.when} に ${gift.who}さんより 「${gift.what}」 【返礼】 ${gift.needReturn} : ${gift.hasGaveReturn}
           </c:when>
           <c:otherwise>
             ${gift.when} に ${gift.who}さんより 「${gift.what}」 【返礼】 ${gift.needReturn}
           </c:otherwise>  
         </c:choose>
         </span>
         <form action="Main" method="post" style="display:inline-block;">
           <input type="hidden" name="id" value="${gift.id}">
           <button type="submit" style="background:blue; border:none; border-radius:5px; color:white; cursor:pointer; padding:5px;">詳細確認</button>
         </form>  
       </div>
     </c:forEach>
  </c:otherwise>
</c:choose>

<jsp:include page="footer.jsp" />