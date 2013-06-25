<%@ page language="java" isErrorPage="true"  contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@page import="java.io.PrintWriter"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>Insert title here</title>
</head>
<body>
错误信息：<br/>
<%=exception.getMessage()%><br/>
错误原因：<br/>
<%exception.printStackTrace(new PrintWriter(out)); %>
</body>
</html>