<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:include page="header.jsp" />

<a href="Main">メイン画面に戻る</a>
<br>
<h2>記録したい頂き物の情報を入力してください</h2>
 <form action="NewGift" method= "post">
   <p>いただいた品物 : <input type="text" name="what"></p>
   <p>いただい日付   : <input type="date" name="when" min="2020-01-01" max="2030-12-31"></p> 
   <p>贈り主        : <input type="text" name="who">さん</p>
   <p>贈られた理由   : <input type="text" name="why"></p>
   <p>おおよその値段  : <input type="text" name="howMuch">円ほど</p>
   <p>
   返礼が必要か   :
   <input type="radio" name="needReturn" value="必要">必要
   <input type="radio" name="needReturn" value="不要">必要ではない
   </p>
   <input type="submit" value="頂物の情報を記録">
 </form>
</body>
</html>