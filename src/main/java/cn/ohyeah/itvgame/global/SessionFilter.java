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
 * Session������
 * @author maqian
 */
public class SessionFilter implements Filter {

	/**
	 * ��������ʼ��
	 */
	public void init(FilterConfig cfg) throws ServletException {
	}

	/**
	 * ִ�й��˲���
	 */
	public void doFilter(ServletRequest req, ServletResponse rsp, FilterChain chain) 
		throws IOException, ServletException {

		try{
			RequestContext rc = RequestContext.get();
			if (rc.session()!=null) {
				chain.doFilter(req, rsp);
			}
			else {
				rc.requestAdd("promptText", "�����ʵ�ҳ���ѹ��ڣ��뷵�����·���");
				rc.requestAdd("returnUrl", Configuration.formatEpgUrl(rc));
				rc.forward(Configuration.getErrorPage());
			}
		}finally{
		}
	}

	/**
	 * �������ͷ���Դ
	 */
	public void destroy() {
	}

}