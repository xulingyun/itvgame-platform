package cn.ohyeah.itvgame.platform.dao.impl;

import cn.halcyon.dao.QueryHelper;
import cn.ohyeah.itvgame.global.Configuration;
import cn.ohyeah.itvgame.platform.dao.IProductDetailDao;
import cn.ohyeah.itvgame.platform.model.ProductDetail;

public class MysqlProductDetailDaoImpl implements IProductDetailDao {

	@Override
	public ProductDetail read(int productId) {
		return QueryHelper.read_cache(ProductDetail.class, "productDetail", productId, 
				"select * from ProductDetail where productId=?", productId);
	}

	@Override
	public void save(ProductDetail detail) {
		QueryHelper.update("insert into ProductDetail(productId, appName, productName, rechargeManager, "
				+ "subscribeImplementor, amountUnit, rechargeRatio, daySubscribeLimit, monthSubscribeLimit, "
				+ "tryNumber, purchaseType, monthFee, validPeriod, periodFee, validCount, "
				+ "countFee, validSeconds, secondsFee) "
				+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				detail.getProductId(), detail.getAppName(), detail.getProductName(), detail.getRechargeManager(),
				detail.getSubscribeImplementor(), detail.getAmountUnit(), detail.getRechargeRatio(),
				detail.getDaySubscribeLimit(), detail.getMonthSubscribeLimit(), detail.getTryNumber(), 
				detail.getPurchaseType(), detail.getMonthFee(), detail.getValidPeriod(), 
				detail.getPeriodFee(), detail.getValidCount(), detail.getCountFee(), 
				detail.getValidSeconds(), detail.getSecondsFee());
		Configuration.setCache("productDetail", detail.getProductId(), detail);
	}

	@Override
	public void update(ProductDetail detail) {
		QueryHelper.update("update ProductDetail set productName=?, rechargeManager=?, "
				+ "subscribeImplementor=?, amountUnit=?, rechargeRatio=?, daySubscribeLimit=?, monthSubscribeLimit=?, "
				+ "tryNumber=?, purchaseType=?, monthFee=?, validPeriod=?, " 
				+ "periodFee=?, validCount=?, countFee=?, validSeconds=?, secondsFee=? " 
				+ "where productId=?",
				detail.getProductName(), detail.getRechargeManager(), detail.getTryNumber(), 
				detail.getSubscribeImplementor(), detail.getAmountUnit(), detail.getRechargeRatio(),
				detail.getDaySubscribeLimit(), detail.getMonthSubscribeLimit(), detail.getPurchaseType(), 
				detail.getMonthFee(), detail.getValidPeriod(), detail.getPeriodFee(),
				detail.getValidCount(), detail.getCountFee(), detail.getValidSeconds(),
				detail.getSecondsFee(), detail.getProductId());
	}

}
