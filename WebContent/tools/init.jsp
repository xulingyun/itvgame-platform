<%@ page language="java" contentType="text/html; charset=gb2312"
    pageEncoding="gb2312"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
  	<meta http-equiv="Pragma" content="no-cache" />
  	<meta http-equiv="Cache-Control" content="no-cache" />
  	<meta http-equiv="Expires" content="0" />
<title>��ʼ��</title>
</head>
<body>
<h1>��ʼ�����ݿ�</h1>
<hr/>
<form action="<%=request.getContextPath()+"/action/tools/init" %>" method="post">
�˺ţ�<input type="text" name="userName"/><br/>
���룺<input type="password" name="pwd"/><br/>
��ʼ����<input type="submit" value="ȷ��"/>
</form>
</body>
</html>