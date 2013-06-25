package cn.ohyeah.itvgame.global;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import cn.halcyon.utils.RequestContext;


/**
 * Session过滤器
 * @author maqian
 */
public class SessionFilter implements Filter {

	/**
	 * 过滤器初始化
	 */
	public void init(FilterConfig cfg) throws ServletException {
	}

	/**
	 * 执行过滤操作
	 */
	public void doFilter(ServletRequest req, ServletResponse rsp, FilterChain chain) 
		throws IOException, ServletException {

		try{
			RequestContext rc = RequestContext.get();
			if (rc.session()!=null) {
				chain.doFilter(req, rsp);
			}
			else {
				rc.requestAdd("promptText", "您访问的页面已过期，请返回重新访问");
				rc.requestAdd("returnUrl", Configuration.formatEpgUrl(rc));
				rc.forward(Configuration.getErrorPage());
			}
		}finally{
		}
	}

	/**
	 * 过滤器释放资源
	 */
	public void destroy() {
	}

}