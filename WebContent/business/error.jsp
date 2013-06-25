<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta name="page-view-size" content="640*530"/>
  	<meta http-equiv="Content-Type" content="text/html; charset=gbk">
  	<meta http-equiv="Pragma" content="no-cache" />
  	<meta http-equiv="Cache-Control" content="no-cache" />
  	<meta http-equiv="Expires" content="0" />
<title>错误提示</title>

<STYLE type="text/css">
.info{
	FONT-SIZE: 24px; 
	COLOR: #f9ff4e; 
	FONT-FAMILY: "黑体"; 
	TEXT-DECORATION: none;
}
</STYLE>
</head>
<body style="BACKGROUND-REPEAT: no-repeat;" background="<%=(String)request.getContextPath()+"/content/common/error.jpg" %>"
	bgColor="#abbfe8" leftMargin="0"  topMargin="0"  marginheight="0" marginwidth="0">
	
	<table width="630" height="517" border="0" cellpadding="0" cellspacing="0">
	<tr>
	  <td width="38" height="119">&nbsp;</td>
	  <td width="565">&nbsp;</td>
	  <td width="27">&nbsp;</td>
	</tr>
	<tr>
	  <td height="345">&nbsp;</td>
	  <td valign="top">
	    <table width="563" height="156" border="0" cellpadding="0" cellspacing="0">
	      <tr>
	        <td width="563" height="75">&nbsp;</td>
	      </tr>
	      <tr>
	        <td height="117" align="center" valign="top" class="info"><%=(String)request.getAttribute("promptText") %></td>
	        </tr>
	        <tr>
	          <td height="153">&nbsp;</td>
	        </tr>
	    </table>
	  </td>
	  <td>&nbsp;</td>
	</tr>
</table>
	
	<div style="left:475px;top:410px;position:absolute">
	<a href="<%=request.getAttribute("returnUrl") %>">
	<img src="<%=request.getContextPath()+"/content/common/btn-back.png" %>" border=0>
	</a>
	</div>

</body>
</html>