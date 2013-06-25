<%@ page language="java" contentType="text/html; charset=gb2312"
    pageEncoding="gb2312"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
  	<meta http-equiv="Pragma" content="no-cache" />
  	<meta http-equiv="Cache-Control" content="no-cache" />
  	<meta http-equiv="Expires" content="0" />
<title>初始化</title>
</head>
<body>
<h1>初始化数据库</h1>
<hr/>
<form action="<%=request.getContextPath()+"/action/tools/init" %>" method="post">
账号：<input type="text" name="userName"/><br/>
密码：<input type="password" name="pwd"/><br/>
初始化：<input type="submit" value="确定"/>
</form>
</body>
</html>