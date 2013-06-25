package cn.ohyeah.itvgame.protocol;

/**
 * 协议常量
 * @author maqian
 * @version 1.0
 *
 */
public interface Constant {
	/*协议版本号*/
	public static final byte PROTOCOL_VERSION = 1;
	
	/*协议标志*/
	public static final byte PROTOCOL_TAG_SYS_SERV = 1;		/*系统服务协议*/
	public static final byte PROTOCOL_TAG_RECORD = 2;		/*游戏记录协议*/
	public static final byte PROTOCOL_TAG_ATTAINMENT = 3;	/*游戏成就（排行）协议*/
	public static final byte PROTOCOL_TAG_PROP = 4;			/*道具协议*/
	public static final byte PROTOCOL_TAG_SUBSCRIBE = 5;		/*订购*/
	public static final byte PROTOCOL_TAG_PURCHASE = 6;		/*消费协议*/
	public static final byte PROTOCOL_TAG_ACCOUNT = 7;		/*用户账户协议*/
	
	
	/*协议命令*/
	public static final byte SYS_SERV_CMD_SYN_TIME = 1;				/*时间同步*/
	public static final byte SYS_SERV_CMD_ADD_FAVORITEGD = 2;		/*广东，添加到收藏夹*/
	
	public static final byte RECORD_CMD_SAVE = 1;					/*保存记录*/
	public static final byte RECORD_CMD_READ = 2;					/*读取记录*/
	public static final byte RECORD_CMD_QUERY_DESC_LIST = 3;			/*查询记录描述列表*/
	public static final byte RECORD_CMD_UPDATE = 4;					/*更新记录*/
	
	public static final byte ATTAINMENT_CMD_SAVE = 1;					/*保存成就*/
	public static final byte ATTAINMENT_CMD_READ = 2;					/*读取成就*/
	public static final byte ATTAINMENT_CMD_UPDATE = 3;					/*更新成就*/
	public static final byte ATTAINMENT_CMD_QUERY_DESC_LIST = 4;			/*查询成就描述列表*/
	public static final byte ATTAINMENT_CMD_QUERY_RANKING_LIST = 5;		/*查询排名列表*/
	
	public static final byte PROP_CMD_QUERY_PROP_LIST = 1;				/*查询道具列表*/
	public static final byte PROP_CMD_QUERY_OWN_PROP_LIST = 2;			/*查询拥有道具列表*/ 
	public static final byte PROP_CMD_USE_PROPS = 3;						/*使用道具*/
	public static final byte PROP_CMD_SYN_PROPS = 4;						/*同步道具*/
	
	public static final byte SUBSCRIBE_CMD_SUBSCRIBE = 1;				/*订购*/
	public static final byte SUBSCRIBE_CMD_QUERY_SUBSCRIBE_RECORD = 2;	/*查询订购记录*/
	public static final byte SUBSCRIBE_CMD_RECHARGE = 3;					/*充值*/
	public static final byte SUBSCRIBE_CMD_QUERY_BALANCE = 4;			/*查询余额*/
	public static final byte SUBSCRIBE_CMD_SUBSCRIBE_PRODUCT = 5;		/*订购产品*/
	public static final byte SUBSCRIBE_CMD_RECHARGE_WINSIDEGD = 6;		/*winsidegd充值*/
	public static final byte SUBSCRIBE_CMD_GOTO_ORDER_PAGE_WINSIDEFJ = 7;		/*winsidefj跳转到充值界面*/
	public static final byte SUBSCRIBE_CMD_RECHARGE_DIJOY = 8;		/*winsidegd充值*/
	
	public static final byte PURCHASE_CMD_PURCHASE_PROP = 1;				/*购买道具*/
	public static final byte PURCHASE_CMD_EXPEND = 2;					/*花费金币*/
	public static final byte PURCHASE_CMD_QUERY_PURCHASE_RECORD = 3;		/*查询消费记录*/
	public static final byte PURCHASE_CMD_EXPEND_DIJOY = 4;		        /*dijoy花费金币*/
	
	public static final byte ACCOUNT_CMD_QUERY_AUTHORIZATION = 1;		/*查询鉴权信息*/
	public static final byte ACCOUNT_CMD_QUERY_SUB_PROPS = 2;			/*查询订购相关属性*/
	public static final byte ACCOUNT_CMD_USER_LOGIN = 3;					/*用户登录*/
	
	
	/*错误码(Error Code)*/
	public static final byte EC_INVALID_TAG = -1;			/*协议标识无效*/
	public static final byte EC_INVALID_CMD = -2;			/*协议命令无效*/
	public static final byte EC_RECORD_NOT_EXIST = -3;		/*游戏记录不存在*/
	public static final byte EC_ATTAINMENT_NOT_EXIST = -4;	/*游戏成就不存在*/
	
}
