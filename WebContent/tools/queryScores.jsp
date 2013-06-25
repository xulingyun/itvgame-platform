<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@page import="com.sanss.wtjf.service.interfac.PointQueryInterfaceServiceLocator"%>
<%@page import="com.sanss.wtjf.service.interfac.PointQueryInterfaceService"%>
<%@page import="com.sanss.wtjf.service.interfac.PointQueryInterfaceDelegate"%>
<%@page import="com.sanss.wtjf.service.interfac.PointQryRequest"%>
<%@page import="com.sanss.wtjf.service.interfac.PointQryResponse"%>
<%@page import="com.sanss.iptvinfo.ws.userinfo.UserInfoServiceServiceLocator"%>
<%@page import="com.sanss.iptvinfo.ws.userinfo.UserInfoService"%>
<%@page import="com.sanss.iptvinfo.ws.userinfo.UserInfoResult"%>
<%@page import="com.sanss.iptvinfo.ws.userinfo.UserInfoElement"%>
<%@page import="java.util.Enumeration"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>Insert title here</title>
</head>
<script type="text/javascript">
function back(){
	window.location.href='<%=request.getContextPath()%>';
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
<%
	String userIds[][] = {
		{"65410917", "100"},
		{"51981781", "100"},
		{"02154077511", "80"},
		{"53740902", "78"},
		{"56936329", "77"},
		{"65107482", "67"},
		{"54766731", "62"},
		{"65364502", "62"},
		{"53733707", "62"},
		{"49092383", "60"},
		{"02157465231", "60"},
		{"66252231", "50"},
		{"51084469", "48"},
		{"48992735", "43"},
		{"55247090", "40"},
		{"18916199756", "40"},
		{"66297406", "39"},
		{"66322265", "36"},
		{"72263870", "35"},
		{"65761322", "31"},
		{"18939942982", "30"},
		{"49055704", "30"},
		{"66292193", "30"},
		{"56167922", "30"},
		{"48097199", "26"},
		{"55315135", "25"},
		{"54087331", "20"},
		{"19101003", "20"},
		{"66268365", "20"},
		{"02133841517", "20"},
		{"19101257", "20"},
		{"53350087", "20"},
		{"18930032723", "18"},
		{"65347921", "15"},
		{"56114280", "12"},
		{"56577135", "10"},
		{"53900871", "10"},
		{"66606380", "10"},
		{"55018339", "10"},
		{"02164938981", "10"},
		{"53966624", "10"},
		{"55820634", "10"},
		{"54686319", "10"},
		{"50868510", "10"},
		{"18930044209", "10"},
		{"65166028", "10"},
		{"72055306", "7"},
		{"65943328", "6"},
		{"55249291", "6"},
		{"02137190206", "5"},
		{"56374032", "5"},
		{"51105029", "5"},
		{"18930940829", "5"},
		{"72030120", "5"},
		{"48901126", "5"},
		{"48109910", "5"},
		{"56794373", "4"},
		{"72622747", "4"},
		{"48951007", "2"},
		{"50617412", "2"},
		{"56522113", "2"},
		{"66576886", "2"},
		{"51286261", "2"},
		{"48023571", "2"},
		{"55252375", "2"},
		{"19101135", "2"},
		{"72429020", "2"},
		{"65092569", "2"},
		{"48998852", "2"},
		{"56553746", "2"},
		{"02157188675", "2"},
		{"65789835", "2"}
	};

	String userId = (String)session.getAttribute("userId");
	if (userId == null || "".equals(userId)) {
		userId="66378006";
	}
	String adslName = null;
	UserInfoServiceServiceLocator uilocater = new UserInfoServiceServiceLocator();
	java.net.URL uiUrl = new java.net.URL("http://124.75.29.171:7001/iptvInfo/services/UserInfoService");
	UserInfoService uiServ = uilocater.getUserInfoService(uiUrl);
	
	/*UserInfoResult uiRst = uiServ.getUserInfo(userId);
	UserInfoElement[] uiItems = null;
	if ("0".equals(uiRst.getResult())) {
		uiItems = uiRst.getElements();
	}*/
	/*
	for (UserInfoElement item : uiItems) {
		if ("adslname".equalsIgnoreCase(item.getKey())) {
			adslName = item.getValue();
			break;
		}
	}
	*/
	/*
	PointQueryInterfaceServiceLocator locater = new PointQueryInterfaceServiceLocator();
	PointQueryInterfaceDelegate service;
	java.net.URL remoteUrl = new java.net.URL("http://222.68.195.70:8080/remarkDispatcher/PointQueryInterfacePort");
	service = locater.getPointQueryInterfacePort(remoteUrl);
	PointQryRequest req = new PointQryRequest();
	req.setAdslname("");
	req.setCheckcode("");
	PointQryResponse rsp = service.qryPoints(req);
	*/
%>
<body onload="init();">
<H1>用户信息</H1><hr/>
<table>
<%
for (int i = 0; i < userIds.length; ++i) {
	UserInfoResult uiRst = uiServ.getUserInfo(userIds[i][0]);
	UserInfoElement[] uiItems = null;
	String result = uiRst.getResult();
	String desc = uiRst.getDesc();
	if ("0".equals(result)) {
		uiItems = uiRst.getElements();
		String adslname = null;
		String address = null;
		for (UserInfoElement item : uiItems) {
			if ("adslname".equalsIgnoreCase(item.getKey())) {
				adslname = item.getValue();
			}
			if ("address".equalsIgnoreCase(item.getKey())) {
				address = item.getValue();
			}
		}
%>
		<tr><td><%= userIds[i][0]%></td><td><%= userIds[i][1]%></td><td><%= adslname%></td><td><%= address%></td></tr>
	<% 
	}
	else {
	%>
		<tr><td><%= userIds[i][0]%></td><td><%= userIds[i][1]%></td><td><%= result%></td><td><%= desc%></td></tr>
<% 
	}
}
%>
	

</table>
</body>
</html>