package cn.ohyeah.itvgame.point.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SubscribeAction extends HttpServlet{

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		//int result = (Integer) req.getAttribute("result");
		int result = Integer.parseInt(String.valueOf(req.getParameter("result")));
		//String message = String.valueOf(req.getAttribute("message"));
		PrintWriter  out = resp.getWriter();
		out.print("<html><head></head><body> htmlÄÚÈÝ </body>"); 
		out.println("<html>");
		out.println("<title>");
		out.println("</title>");
		out.println("<body>");
		out.println("<div id=\"result\" name="+result+" />");

		out.println("</body>");
		out.println("</html>"); 
	}
}
