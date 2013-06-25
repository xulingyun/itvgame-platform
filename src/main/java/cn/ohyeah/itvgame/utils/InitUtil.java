package cn.ohyeah.itvgame.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;

import cn.halcyon.dao.QueryHelper;
import cn.halcyon.utils.SqlFileUtil;
import cn.ohyeah.itvgame.global.BeanManager;
import cn.ohyeah.itvgame.global.Configuration;
import cn.ohyeah.itvgame.platform.model.GameProp;
import cn.ohyeah.itvgame.platform.model.PurchaseRelation;
import cn.ohyeah.itvgame.platform.model.Product;
import cn.ohyeah.itvgame.platform.model.ProductClass;
import cn.ohyeah.itvgame.platform.model.ProductDetail;
import cn.ohyeah.itvgame.platform.model.ProductProvider;
import cn.ohyeah.itvgame.platform.service.PurchaseRelationService;
import cn.ohyeah.itvgame.platform.service.ProductService;
import cn.ohyeah.itvgame.platform.service.GamePropService;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class InitUtil {
	private static final ProductService productServ;
	private static final GamePropService propServ;
	private static final PurchaseRelationService piServ;
	private List<String> recordSqls;
	private List<String> attainmentSqls;
	
	static {
		productServ = (ProductService)BeanManager.getBean("productService");
		propServ = (GamePropService)BeanManager.getBean("gamePropService");
		piServ = (PurchaseRelationService)BeanManager.getBean("purchaseRelationService");
	}
	
	synchronized public void init() throws IOException {
		try {
			recordSqls = new ArrayList<String>();
			attainmentSqls = new ArrayList<String>();
			createTable();
			insertData();
		}
		finally {
			recordSqls = null;
			attainmentSqls = null;
		}
	}
	
	private void insertProductClass(Workbook wb) {
		Sheet sheet = wb.getSheet("ProductClass");
		int rows = sheet.getRows();
		for (int r = 1; r < rows; ++r) {
			ProductClass pc = new ProductClass();
			int c = 1;
			Cell cell = sheet.getCell(c++, r);
			pc.setClassName(cell.getContents());
			productServ.saveProductClass(pc);
		}
	}
	
	private void insertProductProvider(Workbook wb) {
		Sheet sheet = wb.getSheet("ProductProvider");
		int rows = sheet.getRows();
		for (int r = 1; r < rows; ++r) {
			ProductProvider pp = new ProductProvider();
			int c = 1;
			Cell cell = sheet.getCell(c++, r);
			pp.setProviderName(cell.getContents());
			cell = sheet.getCell(c++, r);
			pp.setType(cell.getContents());
			productServ.saveProductProvider(pp);
		}
	}
	
	private void insertProductAndDetail(Workbook wb) {
		Sheet sheet = wb.getSheet("Product");
		int rows = sheet.getRows();
		java.util.Date now = new java.util.Date();
		for (int r = 1; r < rows; ++r) {
			Product product = new Product();
			product.setCreateTime(now);
			product.setUpdateTime(now);
			
			ProductDetail detail = new ProductDetail();
			int c = 0;
			Cell cell = sheet.getCell(c++, r);
			product.setProductId(Integer.parseInt(cell.getContents()));
			cell = sheet.getCell(c++, r);
			product.setProductName(cell.getContents());
			cell = sheet.getCell(c++, r);
			product.setProductClass(Integer.parseInt(cell.getContents()));
			cell = sheet.getCell(c++, r);
			product.setAppName(cell.getContents());
			cell = sheet.getCell(c++, r);
			product.setAppType(cell.getContents());
			cell = sheet.getCell(c++, r);
			product.setDescription(cell.getContents());
			cell = sheet.getCell(c++, r);
			product.setSupportDataManager(BooleanUtils.toBoolean(cell.getContents()));
			cell = sheet.getCell(c++, r);
			product.setLocation(cell.getContents());
			cell = sheet.getCell(c++, r);
			product.setGameid(Integer.parseInt(cell.getContents()));
			cell = sheet.getCell(c++, r);
			product.setProviderID(Integer.parseInt(cell.getContents()));
			cell = sheet.getCell(c++, r);
			product.setState(Integer.parseInt(cell.getContents()));
			productServ.save(product);
			
			if (product.isSupportDataManager()) {
				for (String sql : recordSqls) {
					String recSql = sql.replaceAll("(GameRecord)", product.getAppName()+"GR");
					recSql = recSql.replaceAll("(IDX_GR)", "$1"+product.getProductId());
					QueryHelper.update(recSql);
				}
				for (String sql : attainmentSqls) {
					String attSql = sql.replaceAll("(GameAttainment)", product.getAppName()+"GA");
					attSql = attSql.replaceAll("(IDX_GA)", "$1"+product.getProductId());
					QueryHelper.update(attSql);
				}
			}
			
			cell = sheet.getCell(c++, r);
			detail.setRechargeManager(cell.getContents());
			cell = sheet.getCell(c++, r);
			detail.setRechargeRatio(Integer.parseInt(cell.getContents()));
			cell = sheet.getCell(c++, r);
			detail.setDaySubscribeLimit(Integer.parseInt(cell.getContents()));
			cell = sheet.getCell(c++, r);
			detail.setMonthSubscribeLimit(Integer.parseInt(cell.getContents()));
			cell = sheet.getCell(c++, r);
			detail.setAmountUnit(cell.getContents());
			
			cell = sheet.getCell(c++, r);
			detail.setTryNumber(Integer.parseInt(cell.getContents()));
			cell = sheet.getCell(c++, r);
			detail.setSubscribeImplementor(cell.getContents());
			cell = sheet.getCell(c++, r);
			detail.setPurchaseType(cell.getContents());
			cell = sheet.getCell(c++, r);
			detail.setMonthFee(Integer.parseInt(cell.getContents()));
			
			/*读取按有效期计费信息*/
			cell = sheet.getCell(c++, r);
			detail.setValidPeriod(Integer.parseInt(cell.getContents()));
			cell = sheet.getCell(c++, r);
			detail.setPeriodFee(Integer.parseInt(cell.getContents()));
			
			/*读取按游戏时长计费信息*/
			cell = sheet.getCell(c++, r);
			detail.setValidSeconds(Integer.parseInt(cell.getContents()));
			cell = sheet.getCell(c++, r);
			detail.setSecondsFee(Integer.parseInt(cell.getContents()));
			
			/*读取按游戏次数计费信息*/
			cell = sheet.getCell(c++, r);
			detail.setValidCount(Integer.parseInt(cell.getContents()));
			cell = sheet.getCell(c++, r);
			detail.setCountFee(Integer.parseInt(cell.getContents()));

			detail.setProductId(product.getProductId());
            detail.setAppName(product.getAppName());
			detail.setProductName(product.getProductName());
			productServ.saveProductDetail(detail);
		}
	}
	
	private void insertIdMap(Workbook wb) {
		Sheet sheet = wb.getSheet("PurchaseRelation");
		int rows = sheet.getRows();
		for (int r = 1; r < rows; ++r) {
			PurchaseRelation id = new PurchaseRelation();
			int c = 1;
			Cell cell = sheet.getCell(c++, r);
			id.setProductId(Integer.parseInt(cell.getContents()));
			cell = sheet.getCell(c++, r);
			id.setSubscribeImplementor(cell.getContents());
			cell = sheet.getCell(c++, r);
			id.setSubscribeType(cell.getContents());
			cell = sheet.getCell(c++, r);
			id.setValue(Integer.parseInt(cell.getContents()));
			cell = sheet.getCell(c++, r);
			id.setAmount(Integer.parseInt(cell.getContents()));
			cell = sheet.getCell(c++, r);
			id.setSubscribeId(cell.getContents());
			cell = sheet.getCell(c++, r);
			id.setDescription(cell.getContents());
			piServ.save(id);
		}
	}
	
	private void insertProp(Workbook wb) {
		Sheet sheet = wb.getSheet("GameProp");
		int rows = sheet.getRows();
		for (int r = 1; r < rows; ++r) {
			GameProp prop = new GameProp();
			int c = 1;
			Cell cell = sheet.getCell(c++, r);
			prop.setPropName(cell.getContents());
			cell = sheet.getCell(c++, r);
			prop.setPrice(Integer.parseInt(cell.getContents()));
			cell = sheet.getCell(c++, r);
			prop.setValidPeriod(Integer.parseInt(cell.getContents()));
			cell = sheet.getCell(c++, r);
			prop.setProductId(Integer.parseInt(cell.getContents()));
			cell = sheet.getCell(c++, r);
			prop.setFeeCode(Integer.parseInt(cell.getContents()));
			cell = sheet.getCell(c++, r);
			prop.setDescription(cell.getContents());
			propServ.save(prop);
		}
	}
	
	private void insertData() {
		InputStream dataIs = null;
		Workbook wb = null;
		try {
			dataIs = SqlFileUtil.class.getResourceAsStream(Configuration.getSqlDataFile());
			wb = Workbook.getWorkbook(dataIs);
			insertProductClass(wb);
			insertProductProvider(wb);
			insertProductAndDetail(wb);
			insertProp(wb);
			insertIdMap(wb);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			if (wb != null) {
				wb.close();
			}
			if (dataIs != null) {
				try {
					dataIs.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	private void createTable() {
		try {
			List<String> sqls = SqlFileUtil.loadSql(Configuration.getSqlPlatInitFile());
			for (String sql : sqls) {
				if (sql.indexOf("GameRecord") > 0) {
					recordSqls.add(sql);
				}
				else if (sql.indexOf("GameAttainment") > 0) {
					attainmentSqls.add(sql);
				}
				else {
					QueryHelper.update(sql);
				}
			}
			sqls.clear();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
