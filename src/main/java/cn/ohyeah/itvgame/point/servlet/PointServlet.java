package cn.ohyeah.itvgame.point.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.point.service.PointService;

/**
 * 电信积分畅游接口
 * @author jackey chen
 * @time 2013-03-05
 *
 */
public class PointServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final PointService pointService;
	private static final Map<String, String>  map = new HashMap<String, String>();

	static{
		pointService = (PointService)BeanManager.getBean("pointService");
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String pId = (String) req.getParameter("ProductID");
		String userId = (String) req.getParameter("UserId");
		String transactionId = (String) req.getParameter("transactionid");
		String sign = (String) req.getParameter("Sign");
		
		map.put("pId", pId);
		map.put("userId", userId);   
		map.put("transactionId", transactionId);
		map.put("sign", sign);
		
		Map<String, String> resultMap = pointService.pointRecharge(map);
		
		resp.setContentType("text/html;charset=GBK");
		//resp.setContentType("application/x-javascript;charset=UTF-8");
		
		JSONObject json = JSONObject.fromObject(resultMap); 
		System.out.println(json);
		resp.getWriter().write(json.toString()); 
		resp.getWriter().flush();
		resp.getWriter().close();
	}
}
