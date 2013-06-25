package cn.ohyeah.itvgame.business;

/**
 * ���������
 * @author maqian
 *
 */
public class ErrorCode {
	
	public static final int EC_REQUIRE_REDIRECT = -1000;				/*��Ҫ�ض���*/
	public static final int EC_INVALID_ACCOUNT = -1001;				/*�ʺŲ�����*/
	public static final int EC_INVALID_PRODUCT = -1002;				/*��Ʒ������*/
	public static final int EC_INVALID_PROP = -1003;					/*���߲�����*/
	public static final int EC_INVALID_AUTHORIZATION = -1004;			/*��Ȩ��Ϣ������*/
	public static final int EC_INVALID_PURCHASE_TYPE = -1005;			/*�Ʒ����Ͳ�֧��*/
	public static final int EC_INVALID_PURCHASE_ID = -1006;			/*�Ʒ�ID������*/
	public static final int EC_INVALID_SUBSCRIBE_ID = -1007;			/*����ID��Ч*/
	public static final int EC_INVALID_SUBSCRIBE_AMOUNT = -1008;		/*û����Ӧ���ļƷ�ID*/
	public static final int EC_GOLD_COIN_NOT_ENOUGH = -1009;			/*���(Ԫ��)����*/
	public static final int EC_GAME_COIN_NOT_ENOUGH = -1010;			/*��Ϸ�Ҳ���*/
	public static final int EC_REACH_ACCOUNT_SUB_DAY_LIMIT = -1011;		/*�ﵽ�û����ն����޶�*/
	public static final int EC_REACH_ACCOUNT_SUB_MONTH_LIMIT = -1012;		/*�ﵽ�û����¶����޶�*/
	public static final int EC_REACH_PRODUCT_SUB_DAY_LIMIT = -1013;		/*�ﵽ��Ʒ���ն����޶�*/
	public static final int EC_REACH_PRODUCT_SUB_MONTH_LIMIT = -1014;		/*�ﵽ��Ʒ���¶����޶�*/
	public static final int EC_ACCOUNT_VISIT_PERMISSION_DENY = -1015;		/*�˻�û�з���Ȩ��*/
	public static final int EC_ACCOUNT_SUB_PERMISSION_DENY = -1016;		/*�˻�û�ж���Ȩ��*/
	public static final int EC_PRODUCT_VISIT_PERMISSION_DENY = -1017;		/*��Ʒû�з���Ȩ��*/
	public static final int EC_PRODUCT_SUB_PERMISSION_DENY = -1018;		/*��Ʒû�ж���Ȩ��*/
	public static final int EC_GAME_RECORD_NOT_SUPPORT = -1019;			/*��֧����Ϸ��¼*/
	public static final int EC_GAME_ATTAINMENT_NOT_SUPPORT = -1020;		/*��֧����Ϸ����*/
	public static final int EC_DATABASE_ERROR = -1021;				/*���ݿ��������*/
	public static final int EC_SESSION_TIMEOUT = -1022;				/*session����*/
	public static final int EC_USER_CANCELED = -1023;					/*�û�ȡ������*/
	public static final int EC_SUBSCRIBE_FAILED = -1024;				/*����ʧ��*/
	public static final int EC_RECHARGE_FAILED = -1025;				/*��ֵ����*/
	public static final int EC_SERVICE_FAILED = -1026;				/*�������*/
	public static final int EC_ADD_FAVOR_FAILED = -1027;				/*����ղؼ�ʧ��*/
	public static final int EC_GOTO_RECHARGE_PAGE = -1028;				/*��ת��ֵ����ʧ��*/
	public static final int EC_SEND_HEARTBEAT = -1029;				/*����������ʧ��*/
	
	public static String getErrorMessage(int errorCode) {
		String message = null;
		switch (errorCode) {
		case EC_REQUIRE_REDIRECT:				message = "ҳ����Ҫ�ض���"; break;
		case EC_INVALID_ACCOUNT:				message = "�˺Ų�����"; break;
		case EC_INVALID_PRODUCT:				message = "��Ʒ������"; break;
		case EC_INVALID_PROP:					message = "���߲�����"; break;
		case EC_INVALID_AUTHORIZATION:			message = "��Ȩ��Ϣ������"; break;
		case EC_INVALID_PURCHASE_TYPE:			message = "�Ʒ����Ͳ�֧��"; break;
		case EC_INVALID_PURCHASE_ID:			message = "�Ʒ�ID������"; break;
		case EC_INVALID_SUBSCRIBE_ID:			message = "����ID������"; break;
		case EC_INVALID_SUBSCRIBE_AMOUNT:		message = "û����Ӧ���ļƷ�ID";	 break;
		case EC_GOLD_COIN_NOT_ENOUGH:			message = "��ң�Ԫ��������"; break;
		case EC_GAME_COIN_NOT_ENOUGH:			message = "��Ϸ�Ҳ���"; break;
		case EC_REACH_ACCOUNT_SUB_DAY_LIMIT:	message = "�ﵽ�û����ն����޶�"; break;
		case EC_REACH_ACCOUNT_SUB_MONTH_LIMIT:	message = "�ﵽ�û����¶����޶�"; break;
		case EC_REACH_PRODUCT_SUB_DAY_LIMIT:	message = "�ﵽ��Ʒ���ն����޶�"; break;
		case EC_REACH_PRODUCT_SUB_MONTH_LIMIT:	message = "�ﵽ��Ʒ���¶����޶�"; break;
		case EC_ACCOUNT_VISIT_PERMISSION_DENY:	message = "���˺�û�з���Ȩ��"; break;
		case EC_ACCOUNT_SUB_PERMISSION_DENY:	message = "���˺�û�ж���Ȩ��"; break;
		case EC_PRODUCT_VISIT_PERMISSION_DENY:	message = "û�д˲�Ʒ�ķ���Ȩ��"; break;
		case EC_PRODUCT_SUB_PERMISSION_DENY:	message = "û�д˲�Ʒ�Ķ���Ȩ��"; break;
		case EC_GAME_RECORD_NOT_SUPPORT:		message = "�˲�Ʒ��֧����Ϸ��¼"; break;
		case EC_GAME_ATTAINMENT_NOT_SUPPORT:	message = "�˲�Ʒ��֧����Ϸ����"; break;
		case EC_DATABASE_ERROR:					message = "���ݿ��������"; break;
		case EC_SESSION_TIMEOUT:				message = "�û��Ự�ѹ���"; break;
		case EC_USER_CANCELED:					message = "�û�ȡ������"; break;
		case EC_SUBSCRIBE_FAILED:				message = "���������г���δ֪�쳣"; break;
		case EC_RECHARGE_FAILED:				message = "��ֵ�����г���δ֪�쳣"; break;
		case EC_SERVICE_FAILED:					message = "ϵͳ�������"; break;
		default:	message = "δ֪����"; break;
		}
		return message;
	}
}
