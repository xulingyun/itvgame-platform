package cn.ohyeah.itvgame.business;

/**
 * 服务错误码
 * @author maqian
 *
 */
public class ErrorCode {
	
	public static final int EC_REQUIRE_REDIRECT = -1000;				/*需要重定向*/
	public static final int EC_INVALID_ACCOUNT = -1001;				/*帐号不存在*/
	public static final int EC_INVALID_PRODUCT = -1002;				/*产品不存在*/
	public static final int EC_INVALID_PROP = -1003;					/*道具不存在*/
	public static final int EC_INVALID_AUTHORIZATION = -1004;			/*鉴权信息不存在*/
	public static final int EC_INVALID_PURCHASE_TYPE = -1005;			/*计费类型不支持*/
	public static final int EC_INVALID_PURCHASE_ID = -1006;			/*计费ID不存在*/
	public static final int EC_INVALID_SUBSCRIBE_ID = -1007;			/*订购ID无效*/
	public static final int EC_INVALID_SUBSCRIBE_AMOUNT = -1008;		/*没有相应金额的计费ID*/
	public static final int EC_GOLD_COIN_NOT_ENOUGH = -1009;			/*金币(元宝)不足*/
	public static final int EC_GAME_COIN_NOT_ENOUGH = -1010;			/*游戏币不足*/
	public static final int EC_REACH_ACCOUNT_SUB_DAY_LIMIT = -1011;		/*达到用户当日订购限额*/
	public static final int EC_REACH_ACCOUNT_SUB_MONTH_LIMIT = -1012;		/*达到用户当月订购限额*/
	public static final int EC_REACH_PRODUCT_SUB_DAY_LIMIT = -1013;		/*达到产品当日订购限额*/
	public static final int EC_REACH_PRODUCT_SUB_MONTH_LIMIT = -1014;		/*达到产品当月订购限额*/
	public static final int EC_ACCOUNT_VISIT_PERMISSION_DENY = -1015;		/*账户没有访问权限*/
	public static final int EC_ACCOUNT_SUB_PERMISSION_DENY = -1016;		/*账户没有订购权限*/
	public static final int EC_PRODUCT_VISIT_PERMISSION_DENY = -1017;		/*产品没有访问权限*/
	public static final int EC_PRODUCT_SUB_PERMISSION_DENY = -1018;		/*产品没有订购权限*/
	public static final int EC_GAME_RECORD_NOT_SUPPORT = -1019;			/*不支持游戏记录*/
	public static final int EC_GAME_ATTAINMENT_NOT_SUPPORT = -1020;		/*不支持游戏排行*/
	public static final int EC_DATABASE_ERROR = -1021;				/*数据库操作错误*/
	public static final int EC_SESSION_TIMEOUT = -1022;				/*session过期*/
	public static final int EC_USER_CANCELED = -1023;					/*用户取消操作*/
	public static final int EC_SUBSCRIBE_FAILED = -1024;				/*订购失败*/
	public static final int EC_RECHARGE_FAILED = -1025;				/*充值错误*/
	public static final int EC_SERVICE_FAILED = -1026;				/*服务错误*/
	public static final int EC_ADD_FAVOR_FAILED = -1027;				/*添加收藏夹失败*/
	public static final int EC_GOTO_RECHARGE_PAGE = -1028;				/*跳转充值界面失败*/
	public static final int EC_SEND_HEARTBEAT = -1029;				/*发送心跳包失败*/
	
	public static String getErrorMessage(int errorCode) {
		String message = null;
		switch (errorCode) {
		case EC_REQUIRE_REDIRECT:				message = "页面需要重定向"; break;
		case EC_INVALID_ACCOUNT:				message = "账号不存在"; break;
		case EC_INVALID_PRODUCT:				message = "产品不存在"; break;
		case EC_INVALID_PROP:					message = "道具不存在"; break;
		case EC_INVALID_AUTHORIZATION:			message = "授权信息不存在"; break;
		case EC_INVALID_PURCHASE_TYPE:			message = "计费类型不支持"; break;
		case EC_INVALID_PURCHASE_ID:			message = "计费ID不存在"; break;
		case EC_INVALID_SUBSCRIBE_ID:			message = "订购ID不存在"; break;
		case EC_INVALID_SUBSCRIBE_AMOUNT:		message = "没有相应金额的计费ID";	 break;
		case EC_GOLD_COIN_NOT_ENOUGH:			message = "金币（元宝）不足"; break;
		case EC_GAME_COIN_NOT_ENOUGH:			message = "游戏币不足"; break;
		case EC_REACH_ACCOUNT_SUB_DAY_LIMIT:	message = "达到用户当日订购限额"; break;
		case EC_REACH_ACCOUNT_SUB_MONTH_LIMIT:	message = "达到用户当月订购限额"; break;
		case EC_REACH_PRODUCT_SUB_DAY_LIMIT:	message = "达到产品当日订购限额"; break;
		case EC_REACH_PRODUCT_SUB_MONTH_LIMIT:	message = "达到产品当月订购限额"; break;
		case EC_ACCOUNT_VISIT_PERMISSION_DENY:	message = "此账号没有访问权限"; break;
		case EC_ACCOUNT_SUB_PERMISSION_DENY:	message = "此账号没有订购权限"; break;
		case EC_PRODUCT_VISIT_PERMISSION_DENY:	message = "没有此产品的访问权限"; break;
		case EC_PRODUCT_SUB_PERMISSION_DENY:	message = "没有此产品的订购权限"; break;
		case EC_GAME_RECORD_NOT_SUPPORT:		message = "此产品不支持游戏记录"; break;
		case EC_GAME_ATTAINMENT_NOT_SUPPORT:	message = "此产品不支持游戏排行"; break;
		case EC_DATABASE_ERROR:					message = "数据库操作错误"; break;
		case EC_SESSION_TIMEOUT:				message = "用户会话已过期"; break;
		case EC_USER_CANCELED:					message = "用户取消操作"; break;
		case EC_SUBSCRIBE_FAILED:				message = "订购过程中出现未知异常"; break;
		case EC_RECHARGE_FAILED:				message = "充值过程中出现未知异常"; break;
		case EC_SERVICE_FAILED:					message = "系统服务错误"; break;
		default:	message = "未知错误"; break;
		}
		return message;
	}
}
