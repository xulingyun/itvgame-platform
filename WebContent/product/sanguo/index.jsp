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
<title>三国引导页</title>
</head>
<%
String returnUrl = request.getParameter("returnUrl");
String accountId = request.getParameter("accountId");
String userId = request.getParameter("userId");
String accountName = request.getParameter("accountName");
String userToken = request.getParameter("userToken");
String productId = request.getParameter("productId");

String backUrl = URLEncoder.encode("index.jsp?returnUrl="+URLEncoder.encode(returnUrl, "GBK")
		+"&accountId="+accountId
		+"&userId="+userId
		+"&accountName="+accountName
		+"&userToken="+userToken
		+"&productId="+productId, "GBK");

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
<div style="position:absolute;left:0px;top:0px;width:634px;height:520px;background-image:url('../../content/product/sanguo/home.jpg');background-repeat:no-repeat;">
	
	<img style="left:23px;top:35px;position:absolute" src="../../content/product/sanguo/notice.png" border="0"/>
	
	<div style="left:14px;top:479px;position:absolute">
		<a href="<%=entryUrl%>"><img width="133" height="38" src="../../content/common/empty.gif" border="0"/></a>
	</div>
	<div style="left:155px;top:479px;position:absolute">
		<a href="help.jsp?returnUrl=<%=backUrl%>"><img width="121" height="38" src="../../content/common/empty.gif" border="0"/></a>
	</div>
	<div style="left:282px;top:479px;position:absolute">
		<a href="<%=rankUrl%>"><img width="121" height="38" src="../../content/common/empty.gif" border="0"/></a>
	</div>
	<div style="left:409px;top:479px;position:absolute">
		<a href="intro.jsp?returnUrl=<%=backUrl%>"><img width="121" height="38" src="../../content/common/empty.gif" border="0"/></a>
	</div>
	<div style="left:536px;top:479px;position:absolute">
		<a href="<%=returnUrl%>"><img width="96" height="38" src="../../content/common/empty.gif" border="0"/></a>
	</div>
	<!-- 
	<div style="left:491px;top:345px;position:absolute">
		<a href="<%=ucUrl%>"><img width="111" height="109" src="../../content/common/empty.gif" border="0"/></a>
	</div>
	 -->
</div>
</body>
</html>