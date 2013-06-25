<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@page import="java.net.URLEncoder"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta name="page-view-size" content="640*530"/>
	<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
  	<meta http-equiv="Pragma" content="no-cache" />
  	<meta http-equiv="Cache-Control" content="no-cache" />
  	<meta http-equiv="Expires" content="0" />
<title>帮助</title>
</head>
<%
	String returnUrl = request.getParameter("returnUrl");
%>
<script type="text/javascript">
var sgImgBase = "../../content/product/sanguo";
var curCategory = 0;
var curItem = 0;
var helpItems = [
	["/help-1-1.jpg"],
	["/help-2-1.jpg", "/help-2-2.jpg", "/help-2-3.jpg"],
	["/help-3-1.jpg"]
	];
	
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

function showItem() {
	document.getElementById("helpContent").src = sgImgBase+helpItems[curCategory][curItem];
}

function changeCategory(category) {
	curCategory = category;
	curItem = 0;
	showItem();
	changePage();
}

function showPageText() {
	var pageText = (curItem+1)+"/"+helpItems[curCategory].length;
	document.getElementById("pageText").innerHTML = pageText;
}

function changePage() {
	var leftBtn = document.getElementById("leftBtn");
	if (curItem == 0) {
		if (leftBtn.parentNode.style.disabled != true) {
			leftBtn.parentNode.style.disabled = true;
			leftBtn.src = sgImgBase+"/btn-left-disabled.png";
		}
	}
	else {
		if (leftBtn.parentNode.style.disabled != false) {
			leftBtn.parentNode.style.disabled = false;
			leftBtn.src = sgImgBase+"/btn-left.png";
		}
	}
	
	var rightBtn = document.getElementById("rightBtn");
	if (curItem == helpItems[curCategory].length-1) {
		if (rightBtn.parentNode.style.disabled != true) {
			rightBtn.parentNode.style.disabled = true;
			rightBtn.src = sgImgBase+"/btn-right-disabled.png";
		}
	}
	else {
		if (rightBtn.parentNode.style.disabled != false) {
			rightBtn.parentNode.style.disabled = false;
			rightBtn.src = sgImgBase+"/btn-right.png";
		}
	}
	showPageText();
}

function nextItem(event) {
	if (curItem < helpItems[curCategory].length-1) {
		curItem++;
	}
	showItem();
	changePage();
	event.preventDefault();
}

function prevItem(event) {
	if (curItem > 0) {
		curItem--;
	}
	showItem();
	changePage();
	event.preventDefault();
}

function init() {
	var f = document.getElementById("focusMe");
	if (f) f.focus();
	document.onkeypress = PageScroll;
	curCategory = 0;
	curItem = 0;
	changeCategory(0);
}
</script>
<body bgcolor="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="init();">
<div style="position:absolute;left:0px;top:0px;width:634px;height:520px;background-image:url('../../content/product/sanguo/help.jpg');background-repeat:no-repeat;">

<div style="left:33px;top:89px;position:absolute">
	<a onfocus="changeCategory(0);"  href="#"><img width="87" height="75" src="../../content/common/empty.gif" border="0"/></a>
</div>

<div style="left:33px;top:183px;position:absolute">
	<a onfocus="changeCategory(1);" href="#"><img width="87" height="75" src="../../content/common/empty.gif" border="0"/></a>
</div>

<div style="left:33px;top:277px;position:absolute">
	<a onfocus="changeCategory(2);" href="#"><img width="87" height="75" src="../../content/common/empty.gif" border="0"/></a>
</div>

<div style="left:133px;top:91px;position:absolute">
	<img id="helpContent" width="482" height=377" border="0"/>
</div>

<div style="left:479px;top:481px;position:absolute">
	<a href="#" onclick="prevItem(event);"><img id="leftBtn" width="31" height="34" border="0"/></a>
</div>

<span id="pageText" style="left:505px;top:481px;position:absolute;width:87px;height:34px;font-size:22px;text-align:center"></span>

<div style="left:580px;top:481px;position:absolute">
	<a href="#" onclick="nextItem(event);"><img id="rightBtn" width="31" height="34" border="0"/></a>
</div>

</div>
</body>
</html>