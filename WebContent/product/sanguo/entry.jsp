<%@ page language="java" contentType="text/html; charset=GB18030" 
	pageEncoding="GB18030"%>
<%@page import="cn.halcyon.utils.RequestContext"%>
<%@page import="cn.ohyeah.itvgame.global.Configuration"%>
<%@page import="cn.ohyeah.itvgame.global.BeanManager"%>
<%@page import="cn.ohyeah.itvgame.platform.model.Product"%>
<%@page import="cn.ohyeah.itvgame.platform.service.ProductService"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <HEAD>
  <TITLE>������Ϸ</TITLE>
  	<meta name="page-view-size" content="640*530"/>
  	<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
  	<meta http-equiv="Pragma" content="no-cache" />
  	<meta http-equiv="Cache-Control" content="no-cache" />
  	<meta http-equiv="Expires" content="0" />
 </HEAD>
 
 <%!
	ProductService productServ = (ProductService)BeanManager.getBean("productService");
 %>

 <%
	String accountId = request.getParameter("accountId");
	String userId = request.getParameter("userId");
	String accountName = request.getParameter("accountName");
	String userToken = request.getParameter("userToken");
	
	String productId = request.getParameter("productId");
	Product product = productServ.read(Integer.parseInt(productId));
	String appName = product.getAppName();
	String appPathPre = request.getContextPath()+"/data/apps/"+appName+"/"+appName;
	System.out.println("path:"+appPathPre);
	
	java.util.Date time = new java.util.Date();
	String protocolLocation = Configuration.formProtocolLocation(RequestContext.get());
 %>

<script language="javascript1.3">
function PageScroll(evt) {
	evt = evt ? evt : window.event;
	var keyCode = evt.which ? evt.which : evt.keyCode;
	if(keyCode != null)  /* ���� */
	{
		window.location="<%=request.getParameter("returnUrl") %>";
	}
	return;
}
setTimeout("document.onkeypress = PageScroll",16000);
</script>
 <body  leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" bgColor="#000000">
	<img src="../../content/common/wait.jpg" name="loading" id="loading" style="position:absolute; left:0px; top:0px; width:644px; height:534px; z-index:1;"/>
	<div style="position:absolute; left:0px; top:0px; width:640px; height:526px; z-index:1">
		<object id="j2meapp" classid="clsid:72E6F181-D1B0-4C22-B0D7-4A0740EEAEF5" width="644" height="534" >
			<param name="jad" value="../../data/apps/NewSanguo/NewSanguo.jad" />
			<param name="jar" value="../../data/apps/NewSanguo/NewSanguo.jar" />
			<param name="-Xkeypass" value="true" />
			<param name="systemTimeMillis" value="<%=time.getTime() %>" />
			 <param name="accountId" value="<%=accountId %>" />
			<param name="userId" value="<%=userId %>" />
			<param name="accountName" value="<%=accountName %>" />
			<param name="userToken" value="<%=userToken %>" />
			<param name="productId" value="<%=productId %>" />
			<param name="appName" value="<%=product.getAppName() %>" />
			<param name="server" value="<%=protocolLocation %>" /> 
		</object>
	</div>
 </body>
</html>