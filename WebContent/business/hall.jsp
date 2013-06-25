<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<%@page import="java.util.List"%>
<%@page import="cn.ohyeah.itvgame.platform.model.Product"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="cn.ohyeah.itvgame.platform.service.ProductService"%>
<%@page import="cn.ohyeah.itvgame.global.BeanManager"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta name="page-view-size" content="640*530"/>
	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
  	<meta http-equiv="Pragma" content="no-cache" />
  	<meta http-equiv="Cache-Control" content="no-cache" />
  	<meta http-equiv="Expires" content="0" />
<title>游戏列表</title>
</head>
<%!
	private static String ulText = "<li><a href=\"javascript:entryProduct(%s);\">%s</a></li>";
	private static ProductService productServ = (ProductService)BeanManager.getBean("productService");
%>
<%
	String rootPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
	
	String entryUrl = request.getContextPath()+"/action/gateway/"+request.getParameter("entrance")+"Entry";
	String returnUrl = request.getParameter("returnUrl");
	String backUrl = URLEncoder.encode(request.getContextPath()+"/business/hall.jsp"
			+"?entrance="+request.getParameter("entrance")
			+"&returnUrl="+URLEncoder.encode(request.getParameter("returnUrl"),"gbk"), 
			"gbk");
	List<Product> plist = productServ.readAll();
%>

<script type="text/javascript">
function entryProduct(productId) {
	window.location="<%=entryUrl+"?returnUrl="+backUrl%>"+"&productId="+productId;
}
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

<body onload="init();">
<h2>欧耶游戏测试--<%=request.getParameter("entrance")+"平台"%></h2>
<hr/>
<ul>
<li><a href="http://192.168.16.70:8080/itvgame/action/gateway/chinagamesEntry?productId=9">参数测试</a></li> 
<li><a href="http://222.66.209.183/winside/iptv/tank/index.do">超级坦克测试</a></li> 
<li><a href="<%=rootPath+"/TVGame/XiYouGameEntry" %>">西游大乱斗测试</a></li> 
<li><a href="../../test/ohyeah.html">游戏登入测试</a></li>
<%
if (plist != null) {
	for (Product p : plist) {
		out.println(String.format(ulText, Integer.toString(p.getProductId()), p.getProductName()));
	}
}
%>
</ul>
</body>
</html>