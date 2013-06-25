package cn.halcyon.utils;

import java.io.*;
import java.text.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.lang.math.NumberUtils;

import cn.halcyon.mvc.ActionException;


/**
 * 请求上下文
 * @author Winter Lau
 * @date 2010-1-13 下午04:18:00
 */
public class RequestContext {
	
	private final static String UTF_8 = "UTF-8";
	private final static ThreadLocal<RequestContext> contexts = new ThreadLocal<RequestContext>();
	private static String webroot = null;
	
	private String baseUrl;
	private ServletContext context;
	private HttpSession session;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Map<String, Cookie> cookies;
	
	static {
		webroot = getWebrootPath();
		
		ConvertUtils.register(new SqlDateConverter(null), java.sql.Date.class);
		ConvertUtils.register(new Converter(){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
			SimpleDateFormat sdf_time = new SimpleDateFormat("yyyy-M-d H:m");
			@SuppressWarnings("rawtypes")
			public Object convert(Class type, Object value) {
				if(value == null) return null;
		        if (value instanceof Date) return (value);
		        try {
		            return sdf_time.parse(value.toString());
		        } catch (ParseException e) {
		            try {
						return sdf.parse(value.toString());
					} catch (ParseException e1) {
						return null;
					}
		        }
			}}, java.util.Date.class);
	}
	
	private final static String getWebrootPath() {
		String root = RequestContext.class.getResource("/").getFile();
		try {
			root = new File(root).getParentFile().getParentFile().getCanonicalPath();
			root += File.separator;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return root;
	}
	
	/**
	 * 初始化请求上下文
	 * @param ctx
	 * @param req
	 * @param res
	 */
	public static RequestContext begin(ServletContext ctx, HttpServletRequest req, HttpServletResponse res) {
		RequestContext rc = new RequestContext();
		rc.baseUrl = RequestUtils.getBaseUrl(req);
		rc.context = ctx;
		rc.request = req;
		rc.response = res;
		rc.response.setCharacterEncoding(UTF_8);
		rc.session = req.getSession(false);
		rc.cookies = new HashMap<String, Cookie>();
		Cookie[] cookies = req.getCookies();
		if(cookies != null)
			for(Cookie ck : cookies) {
				rc.cookies.put(ck.getName(), ck);
			}
		contexts.set(rc);
		return rc;
	}

	/**
	 * 返回Web应用的路径
	 * @return
	 */
	public static String root() { return webroot; }
	
	/**
	 * 获取当前请求的上下文
	 * @return
	 */
	public static RequestContext get(){
		return contexts.get();
	}
	
	public void end() {
		this.baseUrl = null;
		this.context = null;
		this.request = null;
		this.response = null;
		this.session = null;
		this.cookies = null;
		contexts.remove();
	}
	
	public Locale locale(){ return request.getLocale(); }

	public void closeCache(){
        header("Pragma","No-cache");
        header("Cache-Control","no-cache");
        header("Expires", 0L);
	}
	
	public long id() {
		return param("id", 0L);
	}
	
	public String ip(){
		return RequestUtils.getRemoteAddr(request);
	}
	
	public Object requestAttr(String name) {
		return request.getAttribute(name);
	}
	
	public void requestAdd(String name, Object value) {
		request.setAttribute(name, value);
	}
	
	@SuppressWarnings("unchecked")
	public Enumeration<String> params() {
		return request.getParameterNames();
	}
	
	public String param(String name, String...def_value) {
		String v = request.getParameter(name);
		return (v!=null)?v:((def_value.length>0)?def_value[0]:null);
	}
	
	public long param(String name, long def_value) {
		return NumberUtils.toLong(param(name), def_value);
	}

	public int param(String name, int def_value) {
		return NumberUtils.toInt(param(name), def_value);
	}

	public byte param(String name, byte def_value) {
		return (byte)NumberUtils.toInt(param(name), def_value);
	}

	public String[] params(String name) {
		return request.getParameterValues(name);
	}

	public long[] lparams(String name){
		String[] values = params(name);
		if(values==null) return null;
		return (long[])ConvertUtils.convert(values, long.class);
	}
	
	public String uri(){
		return request.getRequestURI();
	}
	
	public String url() {
		return request.getRequestURL().toString();
	}
	
	public String contextPath(){
		return request.getContextPath();
	}
	
	public void redirect(String uri) throws IOException {
		response.sendRedirect(uri);
	}
	
	public void forward(String uri) throws ServletException, IOException {
		RequestDispatcher rd = context.getRequestDispatcher(uri);
		rd.forward(request, response);
	}

	public void include(String uri) throws ServletException, IOException {
		RequestDispatcher rd = context.getRequestDispatcher(uri);
		rd.include(request, response);
	}
	
	/**
	 * 输出信息到浏览器
	 * @param msg
	 * @throws IOException
	 */
	public void print(Object msg) throws IOException {
		if(!UTF_8.equalsIgnoreCase(response.getCharacterEncoding()))
			response.setCharacterEncoding(UTF_8);
		response.getWriter().print(msg);
	}

	public void output_json(String[] key, Object[] value) throws IOException {
		StringBuilder json = new StringBuilder("{");
		for(int i=0;i<key.length;i++){
			if(i>0)
				json.append(',');
			boolean isNum = value[i] instanceof Number ;
			json.append("\"");
			json.append(key[i]);
			json.append("\":");
			if(!isNum) json.append("\"");
			json.append(value[i]);
			if(!isNum) json.append("\"");
		}
		json.append("}");
		print(json.toString());
	}

	public void output_json(String key, Object value) throws IOException {
		output_json(new String[]{key}, new Object[]{value});
	}
	public void error(int code, String...msg) throws IOException {
		if(msg.length>0)
			response.sendError(code, msg[0]);
		else
			response.sendError(code);
	}
	
	public void forbidden() throws IOException { 
		error(HttpServletResponse.SC_FORBIDDEN); 
	}

	public void not_found() throws IOException { 
		error(HttpServletResponse.SC_NOT_FOUND); 
	}

	public ServletContext context() { return context; }
	public HttpSession session() { return session; }
	public HttpSession session(boolean create) { 
		return (session==null && create)?(session=request.getSession()):session; 
	}
	public Object sessionAttr(String attr) {
		HttpSession ssn = session();
		return (ssn!=null)?ssn.getAttribute(attr):null;
	}
	public void sessionAdd(String name, Object value) {
		HttpSession ssn = session(true);
		ssn.setAttribute(name, value);
	}
	public HttpServletRequest request() { return request; }
	public HttpServletResponse response() { return response; }
	public Cookie cookie(String name) { return cookies.get(name); }
	public void cookie(String name, String value, int max_age, boolean all_sub_domain) {
		RequestUtils.setCookie(request, response, name, value, max_age, all_sub_domain);
	}
	public void deleteCookie(String name,boolean all_domain) { RequestUtils.deleteCookie(request, response, name, all_domain); }
	public String header(String name) { return request.getHeader(name); }
	public void header(String name, String value) { response.setHeader(name, value); }
	public void header(String name, int value) { response.setIntHeader(name, value); }
	public void header(String name, long value) { response.setDateHeader(name, value); }

	/**
	 * 将HTTP请求参数映射到bean对象中
	 * @param req
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	public <T> T form(Class<T> beanClass) {
		try{
			T bean = beanClass.newInstance();
			BeanUtils.populate(bean, request.getParameterMap());
			return bean;
		}catch(Exception e) {
			throw new ActionException(e.getMessage());
		}
	}

	public String baseUrl() {
		return baseUrl;
	}
}