package cn.ohyeah.itvgame.platform.model;

import org.apache.commons.lang.StringUtils;

public class ProductDetail implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2344239724674712498L;
	/*
	 * *���¼Ʒ�˵�������²�Ʒ��Ϊ�Զ�����
	 * ��1�� ��ͨ���£���Ч�ڵ��£��˶���ʱ��Ч��ϵͳ���㶩��ʱ��
	 * ��2�� ����V2����Ч�ڵ��£��˶�������Ч��ϵͳ���㶩��ʱ��
	 * ��3�� ����V3����Ч��30�죬�˶�����30����Ч������ʱ�������30��
	 * 
	 * ���μƷ�˵�������μƷ������˶��������趨��Ч�ڣ���С��λΪ����
	 */
	public static final String PURCHASE_FREE = "free";				//���
	public static final String PURCHASE_MONTH = "month";			//��ͨ����
	public static final String PURCHASE_PERIOD = "period";			//���̶�ʱ���շ�
	public static final String PURCHASE_COUNT = "count";			//����Ϸ�����շ�
	public static final String PURCHASE_TIME = "time";				//����Ϸʱ���շ�
	public static final String PURCHASE_MONTH_TIME = "monthtime";		//���°������ַ�ʽ
	public static final String PURCHASE_MONTH_V2 = "monthv2";			//����V2
	public static final String PURCHASE_MONTH_V3 = "monthv3";			//����V3
	
	private int productId;
    private String appName;
	private String productName;
	private String rechargeManager;
	private String subscribeImplementor;
	private String amountUnit;
	private int rechargeRatio;
	private int daySubscribeLimit;
	private int monthSubscribeLimit;
	private int tryNumber;
	private String purchaseType;
	private int monthFee;
	private int validPeriod;
	private int periodFee;
	private int validCount;
	private int countFee;
	private int validSeconds;
	private int secondsFee;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPurchaseType() {
		return purchaseType;
	}
	public void setPurchaseType(String purchaseType) {
		this.purchaseType = purchaseType;
	}
	
	public boolean isPurchaseTypeMonth() {
		return PURCHASE_MONTH.equals(purchaseType);
	}
	public void setPurchaseTypeMonth() {
		purchaseType = PURCHASE_MONTH;
	}
	public boolean isPurchaseTypeMonthV2() {
		return PURCHASE_MONTH_V2.equals(purchaseType);
	}
	public void setPurchaseTypeMonthV2() {
		purchaseType = PURCHASE_MONTH_V2;
	}
	public boolean isPurchaseTypeMonthV3() {
		return PURCHASE_MONTH_V3.equals(purchaseType);
	}
	public void setPurchaseTypeMonthV3() {
		purchaseType = PURCHASE_MONTH_V3;
	}
	public boolean isPurchaseTypeCount() {
		return PURCHASE_COUNT.equals(purchaseType);
	}
	public void setPurchaseTypeCount() {
		purchaseType = PURCHASE_COUNT;
	}
	public boolean isPurchaseTypePeriod() {
		return PURCHASE_PERIOD.equals(purchaseType);
	}
	public void setPurchaseTypePeriod() {
		purchaseType = PURCHASE_PERIOD;
	}
	public boolean isPurchaseTypeTime() {
		return PURCHASE_TIME.equals(purchaseType);
	}
	public void setPurchaseTypeTime() {
		purchaseType = PURCHASE_TIME;
	}
	public boolean isPurchaseTypeFree() {
		return PURCHASE_FREE.equals(purchaseType);
	}
	public void setPurchaseTypeFree() {
		purchaseType = PURCHASE_FREE;
	}
	public boolean isPurchaseTypeMonthTime() {
		return PURCHASE_MONTH_TIME.equals(purchaseType);
	}
	public void setPurchaseTypeMonthTime() {
		purchaseType = PURCHASE_MONTH_TIME;
	}
	
	public int getPurchaseTypeFee(String purchaseType) {
		int fee = -1;
		if (PURCHASE_PERIOD.equals(purchaseType)) {
			fee = getPeriodFee();
		}
		else if (PURCHASE_COUNT.equals(purchaseType)) {
			fee = getCountFee(); 
		}
		else if (PURCHASE_TIME.equals(purchaseType)) {
			fee = getSecondsFee();
		}
		else {
			fee = -1;
		}
		return fee;
	}
	
	public int getPurchaseTypeValue(String purchaseType) {
		int value = -1;
		if (PURCHASE_PERIOD.equals(purchaseType)) {
			value = getValidPeriod();
		}
		else if (PURCHASE_COUNT.equals(purchaseType)) {
			value = getValidCount(); 
		}
		else if (PURCHASE_TIME.equals(purchaseType)) {
			value = getValidSeconds();
		}
		else {
			value = -1;
		}
		return value;
	}
	
	public int getTryNumber() {
		return tryNumber;
	}
	public void setTryNumber(int tryNumber) {
		this.tryNumber = tryNumber;
	}
	
	public int getMonthFee() {
		return monthFee;
	}
	public void setMonthFee(int monthFee) {
		this.monthFee = monthFee;
	}
	
	public int getCountFee() {
		return countFee;
	}
	public void setCountFee(int countFee) {
		this.countFee = countFee;
	}
	
	public int getValidSeconds() {
		return validSeconds;
	}
	public void setValidSeconds(int validSeconds) {
		this.validSeconds = validSeconds;
	}
	
	public int getValidPeriod() {
		return validPeriod;
	}
	public void setValidPeriod(int validPeriod) {
		this.validPeriod = validPeriod;
	}
	
	public int getPeriodFee() {
		return periodFee;
	}
	public void setPeriodFee(int periodFee) {
		this.periodFee = periodFee;
	}
	
	public int getValidCount() {
		return validCount;
	}
	public void setValidCount(int validCount) {
		this.validCount = validCount;
	}
	
	public int getSecondsFee() {
		return secondsFee;
	}
	public void setSecondsFee(int secondsFee) {
		this.secondsFee = secondsFee;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getProductId() {
		return productId;
	}
    @Deprecated	public void setRechargeManager(String rechargeManager) {
		this.rechargeManager = rechargeManager;
	}

    /**
     * @deprecated replaced by
     * {@link cn.ohyeah.itvgame.global.Configuration#getRechargeImplementor(String)}
     * @return
     */
    @Deprecated	public String getRechargeManager() {
		return rechargeManager;
	}

	@Deprecated	public boolean isRechargeManagerNull() {
		return StringUtils.isEmpty(rechargeManager)||Product.MANAGER_TYPE_PLATFORM.equals(rechargeManager);
	}

    /**
     * @deprecated replaced by
     * {@link cn.ohyeah.itvgame.global.Configuration#isRechargeManagerPlatform(String)}
     * @return
     */
	@Deprecated	public boolean isRechargeManagerPlatform() {
		return Product.MANAGER_TYPE_PLATFORM.equals(rechargeManager);
	}

    /**
     * @deprecated replaced by
     * {@link cn.ohyeah.itvgame.global.Configuration#isRechargeManagerGame(String)}
     * @return
     */
    @Deprecated	public boolean isRechargeManagerGame() {
		return Product.MANAGER_TYPE_GAME.equals(rechargeManager);
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductName() {
		return productName;
	}

    @Deprecated	public void setSubscribeImplementor(String subscribeImplementor) {
		this.subscribeImplementor = subscribeImplementor;
	}

    /**
     * @deprecated replaced by
     * {@link cn.ohyeah.itvgame.global.Configuration#getSubscribeImplementor()}
     * @return
     */
    @Deprecated	public String getSubscribeImplementor() {
		return subscribeImplementor;
	}
	public void setRechargeRatio(int rechargeRatio) {
		this.rechargeRatio = rechargeRatio;
	}
	public int getRechargeRatio() {
		return rechargeRatio;
	}
	public int getDaySubscribeLimit() {
		return daySubscribeLimit;
	}
	public void setDaySubscribeLimit(int daySubscribeLimit) {
		this.daySubscribeLimit = daySubscribeLimit;
	}
	public int getMonthSubscribeLimit() {
		return monthSubscribeLimit;
	}
	public void setMonthSubscribeLimit(int monthSubscribeLimit) {
		this.monthSubscribeLimit = monthSubscribeLimit;
	}
	public void setAmountUnit(String amountUnit) {
		this.amountUnit = amountUnit;
	}
	public String getAmountUnit() {
		return amountUnit;
	}
}
