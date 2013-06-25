<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="cn.ohyeah.itvgame.platform.service.GameAttainmentService"%>
<%@page import="cn.ohyeah.itvgame.platform.viewmodel.GameRanking"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.lang.time.DateFormatUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.apache.commons.lang.time.DateUtils"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="cn.ohyeah.itvgame.platform.model.GameAttainment"%>
<%@page import="cn.ohyeah.itvgame.global.BeanManager"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta name="page-view-size" content="640*530"/>
	<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
  	<meta http-equiv="Pragma" content="no-cache" />
  	<meta http-equiv="Cache-Control" content="no-cache" />
  	<meta http-equiv="Expires" content="0" />
<title>排行</title>
</head>
<%!
	private static GameAttainmentService gas = (GameAttainmentService)BeanManager.getBean("gameAttainmentService");
	private int calcAttainmentId(java.util.Date t) {
		Calendar c = Calendar.getInstance();
		c.setTime(t);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)+1;
		return year*100+month;
	}
	
	private String formatUserId(String userId) {
		int userIdLen = userId.length();
		return userId.substring(0, 4)+"**"+userId.substring(userIdLen-2);
	}
%>
<%
	String returnUrl = request.getParameter("returnUrl");
	int accountId = Integer.parseInt(request.getParameter("accountId"));
	int productId = Integer.parseInt(request.getParameter("productId"));
	java.util.Date now = new java.util.Date();
	java.util.Date start = cn.ohyeah.itvgame.utils.DateUtil.getMonthStartTime(now);
	java.util.Date end = cn.ohyeah.itvgame.utils.DateUtil.getMonthEndTime(now);
	int attainmentId = calcAttainmentId(now);
	List<GameRanking> list = gas.queryRankingList(productId, start, end, 0, 10);
	GameAttainment ga = gas.read(accountId, productId, attainmentId, start, end);
	String userRank = "榜上无名";
	if (ga!=null) {
		userRank = "第"+ga.getRanking()+"名";
	}
%>
<script type="text/javascript">
function back(){
	window.location.href='<%=returnUrl %>';
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
<body bgcolor="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="init();">
<div style="position:absolute;left:0px;top:0px;width:634px;height:520px;background-image:url('../../content/product/worldwar/ranking.jpg');background-repeat:no-repeat;">
		<table>
		<%
			if (list!=null && list.size()>0) {
			for(int i = 0; i < list.size(); i++) {
				GameRanking gameRank = list.get(i);
		%>
		   <tr height="40">
		   	  <td>
		         <div style="left:100px;top:<%=i*30+155 %>px;height:22px;position:absolute;color:#ffffff;">
		         <font color="#FFFF00"><strong>
		           <%=gameRank.getRanking() %>
		         </strong></font>
		         </div>
		      </td>
		      <td>
		         <div style="left:260px;top:<%=i*30+155 %>px;height:22px;position:absolute;color:#ffffff;">
		           <%=formatUserId(gameRank.getUserId()) %>
		         </div>
		      </td>
		      <td>
		         <div style="left:500px;top:<%=i*30+155 %>px;height:22px;position:absolute;color:#ffffff;">
		           <%=gameRank.getScores() %>
		         </div>
		      </td>
		   </tr>
	  <%
	    	}
		}
	  %> 
		</table>
		
		<span style="left:192px;top:478px;position:absolute;width:98px;height:33px;font-size:20px;color:#FF00FF;text-align:center"><%=userRank %></span>
		<!--<div style="left:518px;top:454px;position:absolute">
			<a href="<%=returnUrl %>"><img width="89" height="38" src="../../content/common/empty.gif" border="0"/></a>
		</div>
--></div>
</body>
</html>