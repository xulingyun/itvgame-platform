package cn.ohyeah.itvgame.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ohyeah.itvgame.business.IpInfo;
import cn.ohyeah.itvgame.protocol.DefaultProcessor;
import cn.ohyeah.itvgame.protocol.IProcessor;

/**
 * 协议处理servlet
 * @author maqian
 * @version 1.0
 */
public class ProcessorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(ProcessorServlet.class);
	private static final IProcessor processor = new DefaultProcessor();
	
	public ProcessorServlet() {
		super();
	}
	
	private boolean closeInputStream(InputStream dis) {
		if (dis != null) {
			try {
				dis.close();
			} catch (IOException e) {
				log.error("Unabled to close input stream", e);
				return false;
			}
		}
		return true;
	}
	
	private boolean closeOutputStream(OutputStream dos) {
		if (dos != null) {
			try {
				dos.close();
			} catch (IOException e) {
				log.error("Unabled to close output stream", e);
				return false;
			}
		}
		return true;
	}
	
	protected void service(HttpServletRequest req, HttpServletResponse rsp)
			throws ServletException, IOException {
		InputStream is = null;
		ByteArrayInputStream bais = null;
		DataInputStream dis = null;
		OutputStream os = null;
		ByteArrayOutputStream baos = null;
		DataOutputStream dos = null;
		byte[] indata = null;
		byte[] outdata = null;
		ProcessorContext context = null;
		try {
			context = new ProcessorContext();
			context.setProp("ip", IpInfo.ip());

			/*接受请求数据*/
			is = req.getInputStream();
			baos = new ByteArrayOutputStream();
			indata = new byte[1024];
			int curReadLen = 0;
			while (curReadLen != -1) {
				curReadLen = is.read(indata, 0, indata.length);
				if (curReadLen > 0) {
					baos.write(indata, 0, curReadLen);
				}
			}
			
			if (is!=null && closeInputStream(is)) {
				is = null;
			}
			indata = null;
			indata = baos.toByteArray();
			if (baos!=null && closeOutputStream(baos)) {
				baos = null;
			}
			
			/*处理请求*/
			bais = new ByteArrayInputStream(indata);
			dis = new DataInputStream(bais);
			processor.processRequest(context, dis);
			indata = null;
			if (dis!=null && closeInputStream(dis)) {
				dis = null;
			}
			if (bais!=null && closeInputStream(bais)) {
				bais = null;
			}
			
			/*生成响应数据*/
			baos = new ByteArrayOutputStream();
			dos = new DataOutputStream(baos);
			processor.processResponse(context, dos);
			outdata = baos.toByteArray();
			if (dos!=null && closeOutputStream(dos)) {
				dos = null;
			}
			if (baos!=null && closeOutputStream(baos)) {
				baos = null;
			}
			
			/*返回响应数据*/
			rsp.setContentType("application/octet-stream");
			rsp.setContentLength(outdata.length);
			os = rsp.getOutputStream();
			os.write(outdata, 0, outdata.length);
			outdata = null;
			os.flush();
			if (os!=null && closeOutputStream(os)) {
				os = null;
			}
		}
		catch (Throwable e) {
			log.error("协议处理错误", e);
			indata = null;
			if (dis!=null && closeInputStream(dis)) {
				dis = null;
			}
			if (bais!=null && closeInputStream(bais)) {
				bais = null;
			}
			
			/*生成响应数据*/
			baos = new ByteArrayOutputStream();
			dos = new DataOutputStream(baos);
			processor.processResponse(context, dos);
			outdata = baos.toByteArray();
			if (dos!=null && closeOutputStream(dos)) {
				dos = null;
			}
			if (baos!=null && closeOutputStream(baos)) {
				baos = null;
			}
			
			/*返回响应数据*/
			rsp.setContentType("application/octest-stream");
			rsp.setContentLength(outdata.length);
			os = rsp.getOutputStream();
			os.write(outdata, 0, outdata.length);
			outdata = null;
			os.flush();
			if (os!=null && closeOutputStream(os)) {
				os = null;
			}
		}
		finally {
			closeInputStream(is);
			closeInputStream(dis);
			closeInputStream(bais);
			closeOutputStream(dos);
			closeOutputStream(baos);
			closeOutputStream(os);
		}
	}
}
