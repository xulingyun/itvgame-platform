package cn.ohyeah.itvgame.global;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.halcyon.dao.DBManager;
import cn.halcyon.utils.RequestContext;

/**
 * ȫ�ֹ�����
 * @author maqian
 */
public class PlatformFilter implements Filter {

	private static final Log log = LogFactory.getLog(PlatformFilter.class);
	private ServletContext context;
	
	/**
	 * ��������ʼ��
	 */
	public void init(FilterConfig cfg) throws ServletException {
		this.context = cfg.getServletContext();
	}

	/**
	 * ִ�й��˲���
	 */
	public void doFilter(ServletRequest req, ServletResponse rsp, FilterChain chain) 
		throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)rsp;
        RequestContext rc = RequestContext.begin(this.context, request, response);

		try{
			String requrl = rc.url();
			String queryStr = rc.request().getQueryString();
			if (queryStr != null) {
				requrl += "?"+queryStr;
			}
			log.debug("requestUrl: "+requrl);
			chain.doFilter(req, rsp);
		}
		catch (Exception e) {
			log.error("platform filter error: ", e);
			throw new ServletException(e);
		}finally{
			if(rc!=null) rc.end();
			DBManager.closeConnection();
		}
	}

	/**
	 * �������ͷ���Դ
	 */
	public void destroy() {
		DBManager.closeDataSource();
	}

}