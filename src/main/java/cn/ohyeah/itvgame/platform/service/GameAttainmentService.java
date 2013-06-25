package cn.ohyeah.itvgame.platform.service;

import java.util.List;

import cn.halcyon.dao.DBException;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.platform.dao.IGameAttainmentDao;
import cn.ohyeah.itvgame.platform.model.GameAttainment;
import cn.ohyeah.itvgame.platform.model.Product;
import cn.ohyeah.itvgame.platform.viewmodel.GameAttainmentDesc;
import cn.ohyeah.itvgame.platform.viewmodel.GameRanking;

/**
 * 游戏成就服务接口
 * @author maqian
 * @version 1.0
 */
public class GameAttainmentService {
	private static final IGameAttainmentDao attainmentDao;
	private static final ProductService productServ;
	
	static {
		attainmentDao = (IGameAttainmentDao)BeanManager.getDao("gameAttainmentDao");
		productServ = (ProductService)BeanManager.getBean("productService");
	}
	
	private String getTableName(Product product) {
		return product.getAppName()+"GA";
	}
	
	private String getOrderString(String orderCmd) {
		if ("asc".equalsIgnoreCase(orderCmd)) {
			return "asc";
		}
		else {
			return "desc";
		}
	}
	
	/**
	 * 保存游戏成就
	 * @param attainment
	 */
	public void save(GameAttainment attainment, int productId) {
		Product product = productServ.validateExistProduct(productId);
		String tableName = getTableName(product);
		try {
			attainmentDao.save(tableName, attainment);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * 读取游戏成就
	 * @param accountId
	 * @param productId
	 * @param attainmentId
	 * @return
	 */
	public GameAttainment read(int accountId, int productId, int attainmentId, String orderCmd,
			java.util.Date start, java.util.Date end){
		Product product = productServ.validateExistProduct(productId);
		String tableName = getTableName(product);
		String cmd = getOrderString(orderCmd);
		try {
			if (start != null && end != null) {
				return attainmentDao.read(tableName, accountId, attainmentId, cmd, start, end);
			}
			else {
				return attainmentDao.read(tableName, accountId, attainmentId, cmd);
			}
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public GameAttainment read(int accountId, int productId, int attainmentId,
			java.util.Date start, java.util.Date end){
		return read(accountId, productId, attainmentId, "desc", start, end);
	}
	
	public GameAttainment read(int accountId, int productId, int attainmentId){
		return read(accountId, productId, attainmentId, "desc", null, null);
	}
	
	
	/**
	 * 保存或更新游戏成就
	 * @param attainment
	 */
	public void saveOrUpdate(GameAttainment attainment, int productId) {
		Product product = productServ.validateExistProduct(productId);
		String tableName = getTableName(product);
		try {
			attainmentDao.saveOrUpdate(tableName, attainment);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * 更新游戏成就
	 * @param attainment
	 */
	public void update(GameAttainment attainment, int productId) {
		Product product = productServ.validateExistProduct(productId);
		String tableName = getTableName(product);
		try {
			attainmentDao.update(tableName, attainment);
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * 查询成就描述列表
	 * @param accountId
	 * @param productId
	 * @return
	 */
	public List<GameAttainmentDesc> queryAttainmentDescList(int accountId, int productId, 
			String orderCmd, java.util.Date start, java.util.Date end) {
		Product product = productServ.validateExistProduct(productId);
		String tableName = getTableName(product);
		String cmd = getOrderString(orderCmd);
		try {
			if (start != null && end != null) {
				return attainmentDao.queryDescList(tableName, accountId, cmd, start, end);
			}
			else {
				return attainmentDao.queryDescList(tableName, accountId, cmd);
			}
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public List<GameAttainmentDesc> queryAttainmentDescList(int accountId, int productId) {
		return queryAttainmentDescList(accountId, productId, "desc", null, null);
	}
	
	public List<GameAttainmentDesc> queryAttainmentDescList(int accountId, int productId, 
			java.util.Date start, java.util.Date end) {
		return queryAttainmentDescList(accountId, productId, "desc", start, end);
	}
	
	/**
	 * 查询排名列表
	 * @param productId
	 * @return
	 */
	public List<GameRanking> queryRankingList(int productId, String orderCmd, 
			java.util.Date start, java.util.Date end, int offset, int length) {
		Product product = productServ.validateExistProduct(productId);
		String tableName = getTableName(product);
		String cmd = getOrderString(orderCmd);
		try {
			if (start != null && end != null) {
				return attainmentDao.queryRankingList(tableName, cmd, start, end, offset, length);
			}
			else {
				return attainmentDao.queryRankingList(tableName, cmd, offset, length);
			}
		}
		catch (DBException e) {
			throw new ServiceException(e);
		}
	}
	
	public List<GameRanking> queryRankingList(int productId, int offset, int length) {
		return queryRankingList(productId, "desc", null, null, offset, length);
	}

	public List<GameRanking> queryRankingList(int productId,
			java.util.Date start, java.util.Date end, int offset, int length) {
		return queryRankingList(productId, "desc", start, end, offset, length);
	}
}
