package cn.ohyeah.itvgame.point.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.global.Configuration;
import cn.ohyeah.itvgame.platform.dao.IAuthorizationDao;
import cn.ohyeah.itvgame.platform.model.Account;
import cn.ohyeah.itvgame.platform.model.Authorization;
import cn.ohyeah.itvgame.platform.service.AccountService;
import cn.ohyeah.itvgame.platform.service.ProductService;

public class PointService {
	private static final Log log = LogFactory.getLog(PointService.class);
	private static final ProductService productServ;
	private static final AccountService accServ;
	private static final IAuthorizationDao authDao;
	private static final String privateKey;
	private static final String pointId;
	private static final String pointId2;
	private static final int gameCoin;
	private static final int gameCoin2;
	
	static {
		productServ = (ProductService)BeanManager.getBean("productService");
		accServ = (AccountService)BeanManager.getBean("accountService");
		authDao = (IAuthorizationDao)BeanManager.getDao("authorizationDao");
		privateKey = Configuration.getProperty("telcomsh", "privateKey");
		pointId = Configuration.getProperty("telcomsh", "productID");
		pointId2 = Configuration.getProperty("telcomsh", "productID2");
		gameCoin = Integer.parseInt(Configuration.getProperty("telcomsh", "gameCoin"));
		gameCoin2 = Integer.parseInt(Configuration.getProperty("telcomsh", "gameCoin2"));
		
	}
	
	
	public Map<String, String> pointRecharge(Map<String, String> map){
		String pId = map.get("pId");
		String userId = map.get("userId");
		String transactionId = map.get("transactionId");
		String sign = map.get("sign");
		
		Account account = accServ.read(userId);
		Map<String, String> resultMap = new HashMap<String, String>();
		//Sign=md5(ProductID+ Result+ UserId+ transactionid+ privateKey)
		String s = DigestUtils.md5Hex(pId+userId+transactionId+privateKey);
		
		if(!pId.equals(pointId) && !pId.equals(pointId2)){
			resultMap.put("result", "-1");
			resultMap.put("ProductID", pId);
			resultMap.put("UserId", userId);
			resultMap.put("transactionId", transactionId);
			resultMap.put("privateKey", privateKey);
			resultMap.put("Sign", sign);
			return resultMap;
		}
		
		if(account == null){
			//accServ.addNewStbUser(userId);
			resultMap.put("result", "-2");
			resultMap.put("ProductID", pId);
			resultMap.put("UserId", userId);
			resultMap.put("transactionId", transactionId);
			resultMap.put("privateKey", privateKey);
			resultMap.put("Sign", sign);
			return resultMap;
		}
		
		if(!sign.equals(s)){
			resultMap.put("result", "-3");
			resultMap.put("ProductID", pId);
			resultMap.put("UserId", userId);
			resultMap.put("transactionId", transactionId);
			resultMap.put("privateKey", privateKey);
			resultMap.put("Sign", sign);
			return resultMap;
		}
		
		int productId = -1;
		int gold = 0;
		if(pId.equals(pointId)){
			productId = 12;
			gold = gameCoin;
		}else if(pId.equals(pointId2)){
			productId = 13;
			gold = gameCoin2;
		}
		/*读取用户鉴权,并更新用户游戏币*/
		Authorization auth = authDao.read(account.getAccountId(), productId);
		auth.incGoldCoin(gold);
		authDao.updateCoins(auth);
		log.info(">>积分畅游,兑换游戏币:"+gold+", 游戏产品Id:"+productId+", userId:"+userId+", transactionId:"+transactionId+", sign:"+sign+", privateKey:"+privateKey);
		resultMap.put("result", "0");
		resultMap.put("ProductID", pId);
		resultMap.put("UserId", userId);
		resultMap.put("transactionId", transactionId);
		resultMap.put("privateKey", privateKey);
		resultMap.put("Sign", sign);
		return resultMap;
	}
	
}
