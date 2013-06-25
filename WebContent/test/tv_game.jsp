<%@ page language="java" contentType="text/html; charset=GB18030" 
	pageEncoding="GB18030"%>
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

<%
	
	String appPathPre = request.getContextPath()+"/data/apps/tv_game/tv_game";
	
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
		
		</object>
	</div>
 </body>
</html>