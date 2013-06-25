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
<title>试玩提示</title>

<STYLE type="text/css">
.text{
	FONT-SIZE: 24px; 
	COLOR: #b6ff7b; 
	FONT-FAMILY: "黑体"; 
	TEXT-DECORATION: none;
}

.info{
	FONT-SIZE: 24px; 
	COLOR: #f9ff4e; 
	FONT-FAMILY: "黑体"; 
	TEXT-DECORATION: none;
}
</STYLE>
</head>

<script type="text/javascript">
	function init() {
	    var f = document.getElementById("focusMe");
	    if (f) f.focus();
	}
	function doTry()
	{
		this.form1.submit();
	}
</script>
<body style="BACKGROUND-REPEAT: no-repeat;" background="<%=(String)request.getContextPath()+"/content/common/info.jpg" %>"
	bgColor="#abbfe8" leftMargin="0"  topMargin="0"  marginheight="0" marginwidth="0">
	<form id="form1" action="<%=(String)request.getAttribute("tryUrl") %>" method="post">
	<table>
		<tr><td width="631" height="80">&nbsp;</td>
		</tr>
		<tr><td height="330"><table width="630" height="314">
			<tr><td width="60" height="308">&nbsp;</td>
				<td width="503"><table width="504" height="311">
				<tr><td width="496" height="15">&nbsp;</td>
				</tr>
				<tr><td height="211"><table width="499" height="189" style="table-layout:fixed">
				<tr valign="top">
				  <td width="169" height="60" class="text">您将试玩游戏：</td>
				  <td class="info"><%=request.getAttribute("productName") %></td>
				</tr>
				<tr valign="top">
				  <td height="60" class="text">剩余试玩次数：</td>
				  <td class="info"><%=request.getAttribute("leftTryNum") %>次</td>
				</tr>
				<tr>
				  <td height="60" class="text">&nbsp;</td>
				  <td class="info">&nbsp;</td>
				</tr>
				</table></td></tr>
				<tr>
				  <td height="30">&nbsp;</td>
				</tr>
				<tr><td height="43"><table><tr>
					<td width="83">&nbsp;</td>
					<td width="106"><a id="focusMe" name="focusMe" href="javascript:doTry();">
				<IMG src="<%=(String)request.getContextPath()+"/content/common/btn-ok.png" %>" border=0></a></td>
					<td width="93">&nbsp;</td>
					<td width="106"><a href="<%=(String)request.getAttribute("cancelUrl") %>">
				<img src="<%=(String)request.getContextPath()+"/content/common/btn-back.png" %>" border=0></a></td>
					<td width="87">&nbsp;</td>
					</tr></table></td>
				</tr>
			  </table></td>
			<td width="51">&nbsp;</td>
			</tr>
		</table></td></tr>
		<tr><td height="109">&nbsp;</td>
		</tr>
	</table>
	<input type="hidden" name="accountId" value="<%=(String)request.getAttribute("accountId") %>">
	<input type="hidden" name="productId" value="<%=(String)request.getAttribute("productId") %>">
	<input type="hidden" name="returnUrl" value="<%=(String)request.getAttribute("returnUrl") %>">
	</form>	
</body>
</html>