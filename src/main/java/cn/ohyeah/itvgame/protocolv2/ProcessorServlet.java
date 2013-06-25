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
 * Э�鴦��servlet
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
        //�Ӱ汾2��ʼ, ����length�ֶ�
        if (head.getVersion() >= 2) {
            rsp.setInt(rsp.length()-8, rsp.readerIndex()+4);
        }
    }

    private void processRequest(ProcessorContext context, HttpServletRequest req) throws IOException {
        InputStream is = null;
        try {
            is = req.getInputStream();
            /*������������*/
            byte[] reqBytes = IOUtils.toByteArray(is);
            ByteBuffer reqBuf = new ByteBuffer(reqBytes);

            /*��������*/
            processor.processRequest(context, reqBuf);
        }
        finally {
            closeInputStream(is);
        }
    }

    private void processResponse(ProcessorContext context, HttpServletResponse rsp) throws IOException {
        OutputStream os = null;
        try {
            /*������Ӧ����*/
            ByteBuffer rspBuf = new ByteBuffer(128);
            processor.processResponse(context, rspBuf);

            /*������Ӧ����*/
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
			/*��������*/
            processRequest(context, req);
            /*������Ӧ*/
            processResponse(context, rsp);
		}
		catch (RequestProcessException e) {
			log.error("Э�鴦�����", e);
            /*������Ӧ*/
            processResponse(context, rsp);
		}
	}
}
