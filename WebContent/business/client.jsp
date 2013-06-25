<%@ page language="java" contentType="text/html; charset=GB18030" 
	pageEncoding="GB18030"%>
<%@page import="cn.halcyon.utils.RequestContext"%>
<%@page import="cn.ohyeah.itvgame.global.Configuration"%>
<%@page import="cn.ohyeah.itvgame.global.BeanManager"%>
<%@page import="cn.ohyeah.itvgame.platform.model.ProductDetail"%>
<%@page import="cn.ohyeah.itvgame.platform.model.Product"%>
<%@page import="cn.ohyeah.itvgame.platform.service.ProductService"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <HEAD>
  <TITLE>加载游戏</TITLE>
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
	Integer accountId = (Integer)request.getAttribute("accountId");
	String userId = (String)request.getAttribute("userId");
	String accountName = (String)request.getAttribute("accountName");
	String userToken = (String)request.getAttribute("userToken");
	Integer productId = (Integer)request.getAttribute("productId");
	
	/*盛翼参数*/
	String cpId = "1";
	String cpPassWord = "BD55778DFE0";
	String userIdType = "0";
	
	Product product = productServ.read(productId);
	String appName = product.getAppName();
	String appPathPre = request.getContextPath()+"/data/apps/"+appName+"/"+appName;
	System.out.println("path:"+appPathPre);
	System.out.println("gameid:"+product.getGameid());
	
	java.util.Date time = new java.util.Date();
	String protocolLocation = Configuration.formProtocolLocation(RequestContext.get());
	System.out.println("加载游戏");
%>
<script language="javascript1.3">
function PageScroll(evt)
{
	evt = evt ? evt : window.event;
	var keyCode = evt.which ? evt.which : evt.keyCode;
	
	if(keyCode != null)  /* 返回 */
	{
		window.location="<%=(String)request.getAttribute("returnUrl") %>";
	}
	return;
}
setTimeout("document.onkeypress = PageScroll",8000);
</script>
 <body style="BACKGROUND-REPEAT: no-repeat;" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" bgColor="#000000">
	<img src="<%=request.getContextPath()+"/content/common/wait.jpg" %>" name="loading" id="loading" style="position:absolute; left:0px; top:0px; width:640px; height:530px; z-index:1;"/>
	<div style="position:absolute; left:0px; top:0px; width:640px; height:526px; z-index:1">
		<object id="j2meapp" classid="clsid:72E6F181-D1B0-4C22-B0D7-4A0740EEAEF5" width="644" height="534" >
			<param name="jad" value="<%=appPathPre+".jad" %>" />
			<param name="jar" value="<%=appPathPre+".jar" %>" />
			<param name="-Xkeypass" value="true" />
			<param name="systemTimeMillis" value="<%=time.getTime() %>" />
			<%--  <param name="accountId" value="<%=accountId %>" />
			<param name="userId" value="<%=userId %>" />
			<param name="accountName" value="<%=accountName %>" />
			<param name="userToken" value="<%=userToken %>" />
			<param name="productId" value="<%=productId %>" />
			<param name="appName" value="<%=product.getAppName() %>" />
			<param name="gameid" value="<%=product.getGameid() %>" />
			<param name="server" value="<%=protocolLocation %>" />
			<param name="price" value="<%=2/5/10/20 %>"/>  --%>
		</object>
	</div>
 </body>
</html>