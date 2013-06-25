<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
    
<%@page import="cn.ohyeah.itvgame.platform.service.PurchaseRecordService"%>
<%@page import="cn.ohyeah.itvgame.platform.service.RechargeService"%> 
<%@page import="cn.ohyeah.itvgame.platform.viewmodel.PurchaseDesc"%>
<%@page import="org.apache.commons.lang.StringUtils"%> 
<%@page import="java.util.*"%>
<%@page import="cn.ohyeah.itvgame.global.BeanManager"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  <title>消费记录</title>
	<meta name="page-view-size" content="640*530"/>
	<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
  	<meta http-equiv="Pragma" content="no-cache" />
  	<meta http-equiv="Cache-Control" content="no-cache" />
  	<meta http-equiv="Expires" content="0" />
  </head>
 <%!
 	private static final PurchaseRecordService prs = (PurchaseRecordService)BeanManager.getBean("purchaseRecordService");
 	private static final RechargeService rs = (RechargeService)BeanManager.getBean("rechargeService");
 	private static final int MAX_PAGE_ITEM_COUNT = 10;
 %>
 <%
 	int productId = Integer.parseInt(request.getParameter("productId"));
	int accountId = Integer.parseInt(request.getParameter("accountId"));
 	int pageNo = 0;
	if (request.getParameter("pageNo") != null) {
		pageNo = Integer.parseInt(request.getParameter("pageNo"));
	}
	request.setAttribute("pageNo", pageNo);
	
	String pageCountStr = request.getParameter("pageCount");
   	long pageCount = 1;
   	if(StringUtils.isEmpty(pageCountStr)){
   	    long count = prs.queryPurchaseRecordCount(accountId, productId);//总记录数
   		pageCount = count>0?(count+MAX_PAGE_ITEM_COUNT-1)/MAX_PAGE_ITEM_COUNT:1;
   	} else {
   		pageCount = Long.parseLong(pageCountStr);
   	}
	request.setAttribute("pageCount", pageCount);
   	
 	List<PurchaseDesc> purchaserecord = prs.queryPurchaseList(accountId, productId, pageNo*MAX_PAGE_ITEM_COUNT, MAX_PAGE_ITEM_COUNT);
	request.setAttribute("purchaseRecord", purchaserecord);
	
	String amountUnit = rs.getExpendAmountUnit(productId);
 %>
<script type="text/javascript">
function back(){
	window.location.href='<%=request.getParameter("returnUrl")%>';
}

function PageScroll(evt){
 evt = evt ? evt : window.event;
 var keyCode = evt.which ? evt.which : evt.keyCode;
 
 if(keyCode==8 || keyCode==126 || keyCode==48){
	back();
  	return;
 }
 
 if(keyCode==33 || keyCode==301 || keyCode==86) {
 	prevPage();
  	return;
 }
 
 if (keyCode==34 || keyCode==302 || keyCode==85) {
	nextPage();
  	return;
 }
}

function init() {
	var f = document.getElementById("focusMe");
	if (f) f.focus();
	document.onkeypress = PageScroll;
}
</script>
<body bgcolor="#000000"	leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="init();" >
<div style="position:absolute;left:0px;top:0px;width:634px;height:520px;background-image:url('../../content/product/worldwar/purchase.jpg');background-repeat:no-repeat;">
	<!--<span style="left:480px;top:126px;position:absolute;width:143px;height:29px;font-size:18px;color:#01dfe4;text-align:center">${param.userId }</span>
	<span style="left:480px;top:183px;position:absolute;width:143px;height:29px;font-size:18px;color:#01dfe4;text-align:center">${param.goldCoin }</span>
	
	--><table>
	<c:choose>
		<c:when test="${empty purchaseRecord}">
			<tr height="40"><td>
	 		<div style="color:#01dfe4;left: 85px; top: 152px; width: 160px; height: 22px; position: absolute">无消费记录</div>
	 		</td></tr>
		</c:when>
		<c:otherwise>
			<c:forEach items="${purchaseRecord}" var="item" varStatus="vs">
				<tr height="40">
				<td>
					<div style="color:#01dfe4; left:85px; top: ${vs.index*30+152}px; width: 160px; height: 22px; position: absolute">
					${fn:substring(item.time, 0, 16) }
					</div>
				</td>
				<td>
					<div style="color:#01dfe4; left:320px; top: ${vs.index*30+152}px; width: 118px; height: 22px; position: absolute">
					${item.propName}
					</div>
				</td>
				<td>
					<div style="color:#01dfe4; left:488px; top: ${vs.index*30+152}px; height: 22px; position: absolute">
					${item.amount }<%=amountUnit %>
					</div>
				</td>
				</tr>
			</c:forEach>
		</c:otherwise>
	</c:choose>

	</table>
	<span style="width:79;height:26px;left:77px;top:468px;position:absolute;font-size:26px;color:#000000">${pageNo+1 }/${pageCount }</span>
	<c:choose>
		<c:when test="${pageNo==0}">
		<div style="left:186px;top:465px;width:111px;height:37px;position:absolute;">
		<a href="#" disabled="disabled">
			<img width="102" height="38"  src="../../content/common/empty.gif" border="0"/>
		</a>
		</div>
		</c:when>
		<c:otherwise>
			<c:url value="${purchase.jsp}" var="prevUrl">
				<c:param name="productId" value="${param.productId }"></c:param>
				<c:param name="accountId" value="${param.accountId }"></c:param>
				<c:param name="userId" value="${param.userId }"></c:param>
				<c:param name="goldCoin" value="${param.goldCoin }"></c:param>
				<c:param name="pageNo" value="${pageNo-1 }"></c:param>
				<c:param name="pageCount" value="${pageCount }"></c:param>
				<c:param name="returnUrl" value="${param.returnUrl }"></c:param>
			</c:url>
			<div style="left:186px;top:465px;width:111px;height:37px;position:absolute;">
			<a href="${prevUrl}" >
				<img width="102" height="38"  src="../../content/common/empty.gif" border="0"/>
			</a>
			</div>
		</c:otherwise>
	</c:choose>
	
	<c:choose>
		<c:when test="${pageNo==pageCount-1}">
		<div style="left:312px;top:465px;width:111px;height:37px;position:absolute;">
		<a href="#" disabled="disabled">
			<img width="102" height="38"  src="../../content/common/empty.gif" border="0"/>
		</a>
		</div>
		</c:when>
		<c:otherwise>
			<c:url value="purchase.jsp" var="nextUrl">
				<c:param name="productId" value="${param.productId }"></c:param>
				<c:param name="accountId" value="${param.accountId }"></c:param>
				<c:param name="userId" value="${param.userId }"></c:param>
				<c:param name="goldCoin" value="${param.goldCoin }"></c:param>
				<c:param name="pageNo" value="${pageNo+1 }"></c:param>
				<c:param name="pageCount" value="${pageCount }"></c:param>
				<c:param name="returnUrl" value="${param.returnUrl }"></c:param>
			</c:url>
			<div style="left:312px;top:465px;width:111px;height:37px;position:absolute;">
			<a href="${nextUrl}" >
				<img width="102" height="38"  src="../../content/common/empty.gif" border="0"/>
			</a>
			</div>
		</c:otherwise>
	</c:choose>
	
	<!--<div style="left:349px;top:425px;width:87px;height:37px;position:absolute;">
	<a href="${param.returnUrl }"><img src="../../content/common/empty.gif" border="0"/></a>
	</div>
--></div>
<script type="text/javascript">
	var pageNo = ${pageNo};
	var pageCount = ${pageCount};
	var gotoFlag = false;
	function prevPage() {
		if (pageNo > 0) {
			if (!gotoFlag) {
				gotoFlag = true;
				window.location.href='${prevUrl}';
			}
		}
	}
	
	function nextPage() {
		if (pageNo < pageCount-1) {
			if (!gotoFlag) {
				gotoFlag = true;
				window.location.href='${nextUrl}';
			}
		}
	}
</script>
</body>
</html>
