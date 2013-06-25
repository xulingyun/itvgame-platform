package cn.ohyeah.itvgame.global;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.ohyeah.itvgame.business.service.IPointsService;
import cn.ohyeah.itvgame.business.service.IPurchase;
import cn.ohyeah.itvgame.business.service.IRecharge;
import cn.ohyeah.itvgame.business.service.ISubscribe;
import cn.ohyeah.itvgame.business.service.impl.AuthorizeImpl;
import cn.ohyeah.itvgame.business.service.impl.PermitImpl;

public class BeanManager {
	private static Map<String, Object> beans = new HashMap<String, Object>();
	
	static {
		initBeans();
	}

	private static void initBeans() {
		parseDocument("/beans.xml");
	}
	
	@SuppressWarnings("unchecked")
	private static void parseDocument(String cfgPath) {
		SAXReader reader = new SAXReader(); 
		InputStream is = Configuration.class.getResourceAsStream(cfgPath);
		String name = null;
		String value = null;
		try {
			Document document = reader.read(is);
			Element root = document.getRootElement(); 
			Iterator<Element> it = (Iterator<Element>)root.elementIterator();
			while (it.hasNext()) {
				Element e = (Element) it.next(); 
				if ("bean".equalsIgnoreCase(e.getName())) {
					name = e.attributeValue("name");
					value = e.attributeValue("class");
					beans.put(name, Class.forName(value).newInstance());
				}
				else if ("dao".equalsIgnoreCase(e.getName())) {
					name = e.attributeValue("name");
					value = e.attributeValue("class");
					if (name.startsWith(Configuration.getSqlDialect())) {
						beans.put(name, Class.forName(value).newInstance());
					}
				}
			}
		} catch (Throwable e) {
			throw new RuntimeException("初始化bean出错，name="+name+", value="+value, e);
		}
		finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	public static Object getBean(String name) {
		Object obj = beans.get(name);
		if (obj == null) {
			throw new RuntimeException("未获取到Bean, name="+name);
		}
		return obj;
	}
	
	public static Object getDao(String name) {
		return getBean(Configuration.getSqlDialect()+"."+name);
	}
	
	public static IPurchase getPurchaseImplFacade() {
		return (IPurchase)getBean("purchaseImplFacade");
	}
	
	public static ISubscribe getSubscribeImpl(String implementor) {
		return (ISubscribe)getBean(implementor+".subscribeImpl");
	}
	
	public static ISubscribe getSubscribeImplFacade() {
		return (ISubscribe)getBean("subscribeImplFacade");
	}
	
	public static IRecharge getRechargeImpl(String implementor) {
		return (IRecharge)getBean(implementor+".rechargeImpl");
	}
	
	public static IRecharge getRechargeImplFacade() {
		return (IRecharge)getBean("rechargeImplFacade");
	}
	
	public static IPointsService getPointsServiceImplFacade() {
		return (IPointsService)getBean("pointsServiceImplFacade");
	}
	
	public static IPointsService getPointsServiceImpl() {
		return (IPointsService)getBean(Configuration.getPointsServiceImplementor()+".pointsServiceImpl");
	}
	
	public static AuthorizeImpl getAuthorizeImpl() {
		return (AuthorizeImpl)getBean("authorizeImpl");
	}
	
	public static PermitImpl getPermitImpl() {
		return (PermitImpl)getBean("permitImpl");
	}
	
	public static void setBean(String name, Object value) {
		beans.put(name, value);
	}
}
