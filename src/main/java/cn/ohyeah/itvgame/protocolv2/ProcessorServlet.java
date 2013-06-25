package cn.ohyeah.itvgame.protocolv2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.ohyeah.stb.utils.ByteBuffer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ohyeah.itvgame.business.IpInfo;

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
	
	private void closeInputStream(InputStream dis) {
		if (dis != null) {
			try {
				dis.close();
			} catch (IOException e) {
				log.error("Unabled to close input stream", e);
			}
		}
	}
	
	private void closeOutputStream(OutputStream dos) {
		if (dos != null) {
			try {
				dos.close();
			} catch (IOException e) {
				log.error("Unabled to close output stream", e);
			}
		}
	}

    private void encode(HeadWrapper head, ByteBuffer rsp) {
        //从版本2开始, 编码length字段
        if (head.getVersion() >= 2) {
            rsp.setInt(rsp.length()-8, rsp.readerIndex()+4);
        }
    }

    private void processRequest(ProcessorContext context, HttpServletRequest req) throws IOException {
        InputStream is = null;
        try {
            is = req.getInputStream();
            /*接受请求数据*/
            byte[] reqBytes = IOUtils.toByteArray(is);
            ByteBuffer reqBuf = new ByteBuffer(reqBytes);

            /*处理请求*/
            processor.processRequest(context, reqBuf);
        }
        finally {
            closeInputStream(is);
        }
    }

    private void processResponse(ProcessorContext context, HttpServletResponse rsp) throws IOException {
        OutputStream os = null;
        try {
            /*生成响应数据*/
            ByteBuffer rspBuf = new ByteBuffer(128);
            processor.processResponse(context, rspBuf);

            /*返回响应数据*/
            rsp.setContentType("application/octet-stream");
            rsp.setContentLength(rspBuf.length());
            encode(context.getHeadWrapper(), rspBuf);
            os = rsp.getOutputStream();
            rspBuf.spit(os);
            os.flush();
        }
        finally {
            closeOutputStream(os);
        }
    }
	
	protected void service(HttpServletRequest req, HttpServletResponse rsp)
			throws ServletException, IOException {
		ProcessorContext context = new ProcessorContext();
        context.setProp("ip", IpInfo.ip());
		try {
			/*处理请求*/
            processRequest(context, req);
            /*处理响应*/
            processResponse(context, rsp);
		}
		catch (RequestProcessException e) {
			log.error("协议处理错误", e);
            /*处理响应*/
            processResponse(context, rsp);
		}
	}
}
