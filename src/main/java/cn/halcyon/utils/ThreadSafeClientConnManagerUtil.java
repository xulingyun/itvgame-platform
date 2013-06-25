package cn.halcyon.utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParamBean;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class ThreadSafeClientConnManagerUtil implements Runnable{
	private static final Log log = LogFactory.getLog(ThreadSafeClientConnManagerUtil.class);
	private static final ThreadSafeClientConnManagerUtil instance;
	private static final ThreadSafeClientConnManager connctionManager;
	/** 
     * 最大连接数 
     */  
    public static final int MAX_TOTAL_CONNECTIONS = 400;  
    /** 
     * 每个路由最大连接数 
     */  
    public static final int MAX_ROUTE_CONNECTIONS = 400;  
    /** 
     * 获取连接的最大等待时间 , 30秒
     */  
    public static final int WAIT_TIMEOUT = 30000;  
    /** 
     * 连接超时时间 , 30秒
     */  
    public static final int CONNECT_TIMEOUT = 30000;  
    /** 
     * 读取超时时间 , 30秒
     */  
    public static final int READ_TIMEOUT = 30000;
    
    public static final int EXPIRE_CHECK_PERIOD = 60000;
    
    private static boolean shutdown;
    
    static {
    	instance = new ThreadSafeClientConnManagerUtil();
    	connctionManager = new ThreadSafeClientConnManager();
    	connctionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
    	connctionManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
		new Thread(instance).start();
    }
    
    private ThreadSafeClientConnManagerUtil(){}
    
    public static ResponseHandler<String> stringHandler = new ResponseHandler<String>() {
        public String handleResponse(
                HttpResponse response) throws ClientProtocolException, IOException {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);
            } else {
                return null;
            }
        }
    };
    
    public static ResponseHandler<byte[]> byteArrayHandler = new ResponseHandler<byte[]>() {
        public byte[] handleResponse(
                HttpResponse response) throws ClientProtocolException, IOException {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
            	return EntityUtils.toByteArray(entity);
            } else {
                return null;
            }
        }
    };
    
    
	@Override
    protected void finalize() throws Throwable {
    	super.finalize();
    	shutdown = true;
    }
	
	public static ThreadSafeClientConnManager getConnectionManager() {
		return connctionManager;
	}
	
	public static <T> T execute(
			final HttpClient httpClient,
            final HttpUriRequest request,
            final ResponseHandler<? extends T> responseHandler)
                throws Exception {
		try {
			return httpClient.execute(request, responseHandler);
		}
		catch (Exception e) {
			log.error("Error occured when execute request", e);
    		if (request != null) {
    			request.abort();
    		}
			throw e;
		}
	}
	
	public static String executeForBodyString(
			final HttpClient httpClient,
            final HttpUriRequest request)
                throws Exception {
		return execute(httpClient, request, stringHandler);
	}
	
	public static byte[] executeForBodyByteArray(
			final HttpClient httpClient,
            final HttpUriRequest request)
                throws Exception {
		return execute(httpClient, request, byteArrayHandler);
	}
	
	public static DefaultHttpClient buildDefaultHttpClient() {
		DefaultHttpClient httpClient = new DefaultHttpClient(connctionManager);
		HttpParams basicParam = new BasicHttpParams();
		HttpConnectionParamBean cpb = new HttpConnectionParamBean(basicParam);
		cpb.setConnectionTimeout(CONNECT_TIMEOUT);
		cpb.setSoTimeout(READ_TIMEOUT);
		httpClient.setParams(basicParam);
		
		httpClient.setHttpRequestRetryHandler(new HttpRequestRetryHandler() {
			@Override
			public boolean retryRequest(IOException exception, int executionCount,
                    HttpContext context) {
				if(executionCount >= 5){
                    return false;
                }
                if(exception instanceof NoHttpResponseException){
                    return true;
                } else if (exception instanceof ClientProtocolException){
                    return true;
                } 
                return false;
			}
		});
		return httpClient;
	}

	public static ThreadSafeClientConnManagerUtil getInstance() {
		return instance;
	}

	@Override
	public void run() {
		long lastTimeMillis = System.currentTimeMillis();
		while (!shutdown) {
			try {
				long curTimeMillis = System.currentTimeMillis();
				if (curTimeMillis-lastTimeMillis >= EXPIRE_CHECK_PERIOD) {
					/*关闭过期的连接*/
					connctionManager.closeExpiredConnections();
					/*关闭30秒没有活动的连接*/
					connctionManager.closeIdleConnections(30, TimeUnit.SECONDS);
					lastTimeMillis = curTimeMillis;
					Thread.sleep(EXPIRE_CHECK_PERIOD);
				}
				else {
					Thread.sleep(EXPIRE_CHECK_PERIOD-(curTimeMillis-lastTimeMillis));
				}
			}
			catch (Throwable t) {
				log.error("Error occured when loop for httpclient connection expire check", t);
			}
		}
		connctionManager.shutdown();
	}

}
