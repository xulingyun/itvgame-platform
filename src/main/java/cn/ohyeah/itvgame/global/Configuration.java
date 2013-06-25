package cn.ohyeah.itvgame.global;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.halcyon.cache.CacheManager;
import cn.halcyon.dao.DBManager;
import cn.halcyon.utils.RequestContext;
import cn.ohyeah.itvgame.business.IdInfo;
import cn.ohyeah.itvgame.platform.model.User;

public class Configuration {
    public static final String PLAT_RECHARGE_IMPL = "platform";
    public static final String GAME_RECHARGE_IMPL = "game";

	public static final String MODE_REDIRECT = "redirect";
	public static final String MODE_WEBSERVICE = "webservice";
	
	private static Map<String, Object> cfg = new HashMap<String, Object>();
	private static boolean supportCache;
	private static boolean supportSubscribeLimit;
	private static String telcomOperator;
	private static String serviceProvider;
	private static String subscribeImplementor;
    private static Properties subscribeImplementors;
	private static String pointsServiceImplementor;
    private static Properties pointsServiceImplementors;
    private static String rechargeImplementor;
    private static Properties rechargeImplementors;

	static {
		init();
	}
	
	private Configuration(){}
	
	@SuppressWarnings("unchecked")
	private static void parseIds(Element element, String id) {
		Iterator<Element> it = (Iterator<Element>)element.elementIterator();
		List<IdInfo> countIds = new ArrayList<IdInfo>();
		List<IdInfo> monthIds = new ArrayList<IdInfo>();
		while (it.hasNext()) {
			Element e = (Element) it.next();
			if ("id".equals(e.getName())) {
				String type = e.attributeValue("type");
				if ("period".equals(type)) {
					IdInfo idinfo = new IdInfo();
					idinfo.setSubscribeType("period");
					idinfo.setSubscribeId(e.attributeValue("subscribeId"));
					idinfo.setAmount(Integer.parseInt(e.attributeValue("amount")));
					idinfo.setPeriod(Integer.parseInt(e.attributeValue("period")));
					countIds.add(idinfo);
				}
				else if ("month".equals(type)) {
					IdInfo idinfo = new IdInfo();
					idinfo.setSubscribeType("month");
					idinfo.setSubscribeId(e.attributeValue("subscribeId"));
					idinfo.setAmount(Integer.parseInt(e.attributeValue("amount")));
					idinfo.setPeriod(Integer.parseInt(e.attributeValue("period")));
					monthIds.add(idinfo);
				}
				else {
					
				}
			}
		}
		cfg.put(id+".id.count", countIds);
		cfg.put(id+".id.month", monthIds);
	}
	
	@SuppressWarnings("unchecked")
	private static void parsePatterns(Element element, String id) {
		Iterator<Element> it = (Iterator<Element>)element.elementIterator();
		while (it.hasNext()) {
			Element e = (Element) it.next();
			if ("pattern".equals(e.getName())) {
				cfg.put(id+".pattern."+e.attributeValue("name"), e.getTextTrim());
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void parseUsers(Element element, String id) {
		Iterator<Element> it = (Iterator<Element>)element.elementIterator();
		while (it.hasNext()) {
			Element e = (Element)it.next();
			if ("user".equals(e.getName())) {
				Iterator<Element> it2 = (Iterator<Element>)e.elementIterator();
				User user = new User();
				while(it2.hasNext()) {
					Element e2 = (Element)it2.next();
					String eName2 = e2.getName();
					if ("name".equals(eName2)) {
						user.setName(e2.getTextTrim());
					}
					else if ("passwd".equals(eName2)) {
						user.setPwdMd5(e2.getTextTrim());
					}
					else if ("role".equals(eName2)) {
						user.setRole(e2.getTextTrim());
					}
				}
				cfg.put(id+".user."+user.getName(), user);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void parseRestrict(Element element, String id) {
		Iterator<Element> it = (Iterator<Element>)element.elementIterator();
		while (it.hasNext()) {
			Element e = (Element)it.next();
			String eName = e.getName();
			if ("timeRestrict".equals(eName)) {
				Iterator<Element> it2 = (Iterator<Element>)e.elementIterator();
				String productId = null;
				java.util.Date date = null;
				while(it2.hasNext()) {
					Element e2 = (Element)it2.next();
					String eName2 = e2.getName();
					if ("productId".equals(eName2)) {
						productId = e2.getTextTrim();
					}
					else if("subscribeEnableDate".equals(eName2)) {
						try {
							date = DateUtils.parseDate(e2.getTextTrim(), new String[]{"yyyy/MM/dd", "yyyy-MM-dd"});
						} catch (ParseException e1) {
							throw new RuntimeException(e1);
						}
					}
				}
				cfg.put(id+".timeRestrict."+productId, date);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void parseConfiguration(Element element) {
		String id = element.attributeValue("id");
		Iterator<Element> it = (Iterator<Element>)element.elementIterator();
		while (it.hasNext()) {
			Element e = (Element)it.next(); 
			String eName = e.getName();
			if ("property".equals(eName)) {
				cfg.put(id+"."+e.attributeValue("name"), e.getTextTrim());
			}
			else if ("ids".equals(eName)) {
				parseIds(e, id);
			}
			else if ("patterns".equals(eName)) {
				parsePatterns(e, id);
			}
			else if ("users".equals(eName)) {
				parseUsers(e, id);
			}
			else if ("restricts".equals(eName)) {
				parseRestrict(e, id);
			}
			else {
				
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void parseDocument(String cfgPath) {
		SAXReader reader = new SAXReader(); 
		InputStream is = Configuration.class.getResourceAsStream(cfgPath);
		try {
			Document document = reader.read(is);
			Element root = document.getRootElement(); 
			Iterator<Element> it = (Iterator<Element>)root.elementIterator();
			while (it.hasNext()) {
				Element e = (Element) it.next(); 
				parseConfiguration(e);
			}
		} catch (DocumentException e) {
			throw new RuntimeException(e);
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
	
	private static void init() {
		parseDocument("/configurations.xml");
		supportCache = BooleanUtils.toBoolean((String)cfg.get("global.supportCache"));
		supportSubscribeLimit = BooleanUtils.toBoolean((String)cfg.get("ohyeah.supportSubscribeLimit"));
		telcomOperator = (String)cfg.get("global.telcomOperator");
		serviceProvider = (String)cfg.get("global.serviceProvider");
        initSubscribeImplementor();
        initPointsServiceImplementor();
        initRechargeImplementor();
	}

    private static Properties loadProperties(String path) {
        Properties props = new Properties();
        InputStream is = Configuration.class.getResourceAsStream(path);
        try {
            props.load(is);
            return props;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void initSubscribeImplementor() {
        subscribeImplementors = loadProperties("/subscribeImplementors.properties");
        subscribeImplementor = subscribeImplementors.getProperty(
                getTelcomOperator()+"."+getServiceProvider(),
                subscribeImplementors.getProperty(getServiceProvider())
        );
        if (StringUtils.isEmpty(subscribeImplementor)) {
            throw new RuntimeException("can not find appropriate subscribeImplementor");
        }
    }

    private static void initPointsServiceImplementor() {
        pointsServiceImplementors = loadProperties("/pointsServiceImplementors.properties");
        pointsServiceImplementor = pointsServiceImplementors.getProperty(
                getTelcomOperator()+"."+getServiceProvider(),
                pointsServiceImplementors.getProperty(getTelcomOperator(),"notsupport")
        );
    }

    private static void initRechargeImplementor() {
        rechargeImplementors = loadProperties("/rechargeImplementors.properties");
        rechargeImplementor = rechargeImplementors.getProperty(
                getTelcomOperator()+"."+getServiceProvider(),
                rechargeImplementors.getProperty(getServiceProvider())
        );
        if (StringUtils.isEmpty(rechargeImplementor)) {
            throw new RuntimeException("can not find appropriate rechargeImplementor");
        }
    }
	
	public static String encodeUrl(String url) throws UnsupportedEncodingException {
		return java.net.URLEncoder.encode(url, "gbk");
	}
	
	public static String getProperty(String implementor, String property) {
		return (String)cfg.get(implementor+"."+property);
	}
	
	public static boolean isSupportCache() {
		return supportCache;
	}
	
	public static boolean isSupportSubscribeLimit() {
		return supportSubscribeLimit;
	}
	
	public static Connection getConnection() throws SQLException {
		return DBManager.getConnection();
	}
	
	public static void setCache(String cache, Serializable key, Serializable value) {
		if (supportCache) {
			CacheManager.set(cache, key, value);
		}
	}
	
	public static Object getCache(String cache, Serializable key) {
		if (supportCache) {
			return CacheManager.get(cache, key);
		}
		return null;
	}
	
	public static User getUser(String userName) {
		return (User)cfg.get("global.user."+userName);
	}
	
	public static String getSqlDialect() {
		return (String)cfg.get("global.sql.dialect");
	}
	
	public static String getSqlPlatInitFile() {
		return "/data/"+getSqlDialect()+".init.sql";
	}
	
	public static String getSqlDataFile() {
		return "/data/data.xls";
	}
	
	public static String getConnectionXml() {
		return (String)cfg.get("global.connection.xml");
	}
	
	public static String getTelcomOperator() {
		return telcomOperator;
	}
	
	public static String getServiceProvider() {
		return serviceProvider;
	}
	
	public static String getSubscribeImplementor() {
		return subscribeImplementor;
	}
	
	public static String getPointsServiceImplementor() {
		return pointsServiceImplementor;
	}

    public static String getRechargeImplementor(String appName) {
        return rechargeImplementors.getProperty(
                getTelcomOperator()+"."+getServiceProvider()+"."+appName,
                rechargeImplementor
        );
    }

    public static boolean isRechargeManagerPlatform(String appName) {
        return PLAT_RECHARGE_IMPL.equals(getRechargeImplementor(appName));
    }

    public static boolean isRechargeManagerGame(String appName) {
        return GAME_RECHARGE_IMPL.equals(getRechargeImplementor(appName));
    }

    /*
	public static String getSubscribeImplementor(String implementor) {
		if ("default".equalsIgnoreCase(implementor)) {
			return subscribeImplementor;
		}
		return implementor;
	}*/

	public static String getProductLoginUrl() {
		return "/action/product/login?productId=%s&returnUrl=%s";
	}
	
	public static String formatProductLoginUrl(RequestContext rc, 
			int productId, String returnUrl) throws UnsupportedEncodingException {
		String url = String.format(getProductLoginUrl(), 
				Integer.toString(productId), java.net.URLEncoder.encode(returnUrl, "GBK"));
		if (!url.startsWith("http://")) {
			url = rc.baseUrl()+url;
		}
		return url;
	}
	
	public static String getProductLogoutUrl() {
		return "/action/product/logout?accountId=%s&productId=%s&returnUrl=%s";
	}
	
	public static String formatProductLogoutUrl(RequestContext rc, int accountId,
			int productId, String returnUrl) throws UnsupportedEncodingException {
		String url = String.format(Configuration.getProductLogoutUrl(), 
				Integer.toString(accountId),
				Integer.toString(productId),
				Configuration.encodeUrl(rc.param("returnUrl")));
		if (!url.startsWith("http://")) {
			url = rc.baseUrl()+url;
		}
		return url;
	}
	
	public static String getGameHallUrl() {
		return "/business/hall.jsp";
	}
	
	public static String getProductAuthorizeUrl() {
		return "/action/business/authorize?accountId=%s&productId=%s&returnUrl=%s";
	}
	
	public static String formatProductAuthorizeUrl(RequestContext rc, int accountId, 
			int productId, String returnUrl) throws UnsupportedEncodingException {
		String url = String.format(getProductAuthorizeUrl(), 
				Integer.toString(accountId), 
				Integer.toString(productId), 
				java.net.URLEncoder.encode(returnUrl, "GBK"));
		if (!url.startsWith("http://")) {
			url = rc.baseUrl()+url;
		}
		return url;
	}
	
	public static String getProductSubscribeUrl() {
		return "/action/business/subscribe?accountId=%s&productId=%s&returnUrl=%s";
	}
	
	public static String formatProductSubscribeUrl(RequestContext rc, int accountId, 
			int productId, String returnUrl) throws UnsupportedEncodingException {
		String url = String.format(getProductSubscribeUrl(), 
				Integer.toString(accountId), 
				Integer.toString(productId), 
				java.net.URLEncoder.encode(returnUrl, "GBK"));
		if (!url.startsWith("http://")) {
			url = rc.baseUrl()+url;
		}
		return url;
	}
	
	public static String formatProductSubActionUrl(RequestContext rc) {
		return rc.baseUrl()+"/action/business/subAction";
	}
	
	public static String getProductTryUrl() {
		return "/action/business/tryGame?accountId=%s&productId=%s&returnUrl=%s";
	}
	
	public static String formatProductTryUrl(RequestContext rc, int accountId, 
			int productId, String returnUrl) throws UnsupportedEncodingException {
		String url = String.format(getProductTryUrl(), 
				Integer.toString(accountId), 
				Integer.toString(productId), 
				java.net.URLEncoder.encode(returnUrl, "GBK"));
		if (!url.startsWith("http://")) {
			url = rc.baseUrl()+url;
		}
		return url;
	}
	
	public static String formatProductTryActionUrl(RequestContext rc) {
		return rc.baseUrl()+"/action/business/tryAction";
	}
	
	public static String getTryConfirmUrl() {
		return "/business/try.jsp";
	}

	public static String getSubscribeConfirmUrl() {
		return "/business/subscribe.jsp";
	}
	
	public static String getSubscribeResultUrl() {
		return "/business/subResult.jsp";
	}
	
	public static String getClientLoaderUrl() {
		return "/business/client.jsp";
	}
	
	public static String formatClientLoaderParams(int accountId, String userId, 
			int productId, String returnUrl) throws UnsupportedEncodingException {
		String params = "accountId=%s&userId=%s&productId=%s&returnUrl=%s";
		return String.format(params, Integer.toString(accountId), userId, 
				Integer.toString(productId), encodeUrl(returnUrl));
	}
	
	public static String getProtocolLocation() {
		return (String)cfg.get("ohyeah.protocolLocation");
	}
	
	public static String formProtocolLocation(RequestContext rc) {
		String url = getProtocolLocation();
		if (!url.startsWith("http://")) {
			url = rc.baseUrl()+url;
		}
		return url;
	}
	
	public static int getRechargeRatio() {
		return Integer.parseInt((String)cfg.get("ohyeah.rechargeRatio"));
	}
	
	public static int getDaySubscribeLimit() {
		return Integer.parseInt((String)cfg.get("ohyeah.daySubscribeLimit"));
	}
	
	public static int getMonthSubscribeLimit() {
		return Integer.parseInt((String)cfg.get("ohyeah.monthSubscribeLimit"));
	}
	
	public static String getSpid() {
		return (String)cfg.get(getTelcomOperator()+".spid");
	}
	
	public static String getErrorPage() {
		return "/business/error.jsp";
	}
	
	public static String formatEpgUrl(RequestContext rc) {
		return rc.contextPath()+"/business/gotoEpg.html";
	}
	
	public static String getEpgUrl() {
		return "/business/gotoEpg.html";
	}
	
	public static java.util.Date getSubscribeEnableDate(int productId) {
		return (java.util.Date)cfg.get("ohyeah.timeRestrict."+productId);
	}
	
	public static String getEpg() {
		return (String)cfg.get(getTelcomOperator()+".epg");
	}
	
	public static boolean isSsoModeRedirect() {
		return MODE_REDIRECT.equals(getSsoMode());
	}
	
	public static boolean isSsoModeWebservice() {
		return MODE_WEBSERVICE.equals(getSsoMode());
	}
	
	public static String getSsoMode() {
		return (String)cfg.get(getTelcomOperator()+".ssoMode");
	}
	
	public static String getSsoUrl() {
		return (String)cfg.get(getTelcomOperator()+".ssoUrl");
	}
	
	public static String getAmountUnit(String implementor) {
		return (String)cfg.get(implementor+".amountUnit");
	}
	
	public static String getTelcomOperatorAmountUnit() {
		return getAmountUnit(getTelcomOperator());
	}
	
	public static String getPointsUnit(String implementor) {
		return (String)cfg.get(implementor+".pointsUnit");
	}
	
	public static String getTelcomOperatorPointsUnit() {
		return getPointsUnit(getTelcomOperator());
	}
	
	public static boolean isSupportPointsService(String implementor) {
		return BooleanUtils.toBoolean((String)cfg.get(implementor+".supportPoints"));
	}
	
	public static String getQueryPointsUrl(String implementor) {
		return (String)cfg.get(implementor+".queryPointsUrl");
	}
	
	public static String getQueryUserInfoUrl(String implementor) {
		return (String)cfg.get(implementor+".queryUserInfoUrl");
	}
	
	public static int getCashToPointsRatio(String implementor) {
		return Integer.parseInt((String)cfg.get(implementor+".cashToPointsRatio"));
	}
	
	public static int getTelcomOperatorCashToPointsRatio() {
		return getCashToPointsRatio(getTelcomOperator());
	}
	
	public static int getCashToAmountRatio(String impl) {
		return Integer.parseInt((String)cfg.get(impl+".cashToAmountRatio"));
	}
	
	public static int getTelcomOperatorCashToAmountRatio() {
		return getCashToAmountRatio(getTelcomOperator());
	}
	
	public static boolean isSubscribeModeRedirect(String implementor) {
		return MODE_REDIRECT.equals(getSubscribeMode(implementor));
	}
	
	public static boolean isSubscribeModeWebservice(String implementor) {
		return MODE_WEBSERVICE.equals(getSubscribeMode(implementor));
	}
	
	public static String getSubscribeMode(String implementor) {
		return (String)cfg.get(implementor+".subscribeMode");
	}
	
	public static String getSubscribeUrl(String implementor) {
		return (String)cfg.get(implementor+".subscribeUrl");
	}
	
	public static String formatSubscribeUrl(RequestContext rc, String implementor, 
			String userId, String subscribeId, String userToken, String returnUrl) throws UnsupportedEncodingException {
		String url = String.format(getSubscribeUrl(implementor), 
				userId, subscribeId, userToken,
				getSpid(), java.net.URLEncoder.encode(returnUrl, "GBK"));
		if (!url.startsWith("http://")) {
			url = rc.baseUrl()+url;
		}
		return url;
	}
	
	public static boolean isUnsubscribeModeRedirect(String implementor) {
		return MODE_REDIRECT.equals(getUnsubscribeMode(implementor));
	}
	
	public static boolean isUnsubscribeModeWebservice(String implementor) {
		return MODE_WEBSERVICE.equals(getUnsubscribeMode(implementor));
	}
	
	public static String getUnsubscribeMode(String implementor) {
		return (String)cfg.get(implementor+".unsubscribeMode");
	}
	
	public static String getUnsubscribeUrl(String implementor) {
		return (String)cfg.get(implementor+".unsubscribeUrl");
	}
	
	public static boolean isAuthorizeModeRedirect(String implementor) {
		return MODE_REDIRECT.equals(getAuthorizeMode(implementor));
	}
	
	public static boolean isAuthorizeModeWebservice(String implementor) {
		return MODE_WEBSERVICE.equals(getAuthorizeMode(implementor));
	}
	
	public static String getAuthorizeMode(String implementor) {
		return (String)cfg.get(implementor+".authorizeMode");
	}
	
	public static String getAuthorizeUrl(String implementor) {
		return (String)cfg.get(implementor+".authorizeUrl");
	}
	
	public static String getPattern(String implementor, String name) {
		return (String)cfg.get(implementor+".pattern."+name);
	}
	
	public static String getActionPattern(String implementor) {
		return (String)cfg.get(implementor+".pattern.action");
	}
	
	public static String getParamPattern(String implementor) {
		return (String)cfg.get(implementor+".pattern.param");
	}
	
	public static String getBackUrlPattern(String implementor) {
		return (String)cfg.get(implementor+".pattern.backUrl");
	}
	
	public static String getResultPattern(String implementor) {
		return (String)cfg.get(implementor+".pattern.result");
	}
	
}