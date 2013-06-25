<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta name="page-view-size" content="640*530"/>
	<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
  	<meta http-equiv="Pragma" content="no-cache" />
  	<meta http-equiv="Cache-Control" content="no-cache" />
  	<meta http-equiv="Expires" content="0" />
<title>首页</title>
</head>
<%
String returnUrl = request.getParameter("returnUrl");
String accountId = request.getParameter("accountId");
String userId = request.getParameter("userId");
String accountName = request.getParameter("accountName");
String userToken = request.getParameter("userToken");
String productId = request.getParameter("productId");

//String returnUrl = "http://localhost:8080/itvgamesh/product/worldwar/";
//String accountId = "10000";
//String userId = "igsuper00";
//String accountName = "xiaochen";
//String userToken = "userToken";
//String productId = "10";

String backUrl = URLEncoder.encode("index.jsp?returnUrl="+URLEncoder.encode(returnUrl, "GBK")
		+"&accountId="+accountId
		+"&userId="+userId
		+"&accountName="+accountName
		+"&userToken="+userToken
		+"&productId="+productId, "GBK");

String introUrl = "intro.jsp?returnUrl="+backUrl
		+"&accountId="+accountId
		+"&userId="+userId
		+"&accountName="+accountName
		+"&userToken="+userToken
		+"&productId="+productId;

String entryUrl = "entry.jsp?returnUrl="+backUrl
		+"&accountId="+accountId
		+"&userId="+userId
		+"&accountName="+accountName
		+"&userToken="+userToken
		+"&productId="+productId;

String ucUrl = "userCenter.jsp?returnUrl="+backUrl
		+"&accountId="+accountId
		+"&userId="+userId
		+"&accountName="+accountName
		+"&productId="+productId;

String rankUrl = "ranking.jsp?returnUrl="+backUrl
		+"&accountId="+accountId
		+"&userId="+userId
		+"&accountName="+accountName
		+"&productId="+productId;

%>

<script type="text/javascript">
function back(){
	window.location.href='<%=returnUrl%>';
}

function PageScroll(evt){
  evt = evt ? evt : window.event;
  var keyCode = evt.which ? evt.which : evt.keyCode;
  if(keyCode==8 || keyCode==126 || keyCode==48){
	  back();
	  return;
  }
}

function init() {
	var f = document.getElementById("focusMe");
	if (f) f.focus();
	document.onkeypress = PageScroll;
}
</script>
<body bgcolor="#000000"	leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="init();">
<div style="position:absolute;left:0px;top:0px;width:634px;height:520px;background-image:url('../../content/product/worldwar/home.jpg');background-repeat:no-repeat;">
	
	<div style="left:30px;top:468px;position:absolute">
		<a href="<%=entryUrl%>"><img width="102" height="38" src="../../content/common/empty.gif" border="0"/></a>
	</div>
	
	<div style="left:150px;top:468px;position:absolute">
		<a href="<%=introUrl%>"><img width="102" height="38"  src="../../content/common/empty.gif" border="0"/></a>
	</div>
	<div style="left:270px;top:468px;position:absolute">
		<a href="<%=rankUrl%>"><img width="102" height="38"  src="../../content/common/empty.gif" border="0"/></a>
	</div>
	<div style="left:390px;top:468px;position:absolute">
		<a href="<%=ucUrl%>"><img width="102" height="38"  src="../../content/common/empty.gif" border="0"/></a>
	</div>
	<div style="left:510px;top:468px;position:absolute">
		<a href="<%=returnUrl%>"><img width="102" height="38"  src="../../content/common/empty.gif" border="0"/></a>
	</div>
</div>
</body>
</html>