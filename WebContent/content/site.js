var Box = {
    "PAGEUP": "",    //��ҳ
    "PAGEDOWN": "",    //��ҳ
    "BACK": "",    //����
    "ZERO": "",    //����0
    "ONE": "",    //����1
    "TWO": "",    //����2
    "THREE": "",    //����3
    "FOUR": "",    //����4
    "FIVE": "",    //����5
    "SIX": "",    //����6
    "SEVEN": "",    //����7
    "EIGHT": "",    //����8
    "NINE": "",   //����9
    "LEFT": "",    //��
    "UP": "",     //��
    "RIGHT": "",   //��
    "DOWN": ""    //��
}
/*
HW���Ӱ���Javascript�ű�
*/
var HW = {
    "ZERO": 0x0030, //����0
    "ONE": 49, //����1
    "TWO": 50, //����2
    "THREE": 51, //����3
    "FOUR": 52, //����4
    "FIVE": 53, //����5
    "SIX": 54, //����6
    "SEVEN": 55, //����7
    "EIGHT": 56, //����8
    "NINE": 57, //����9
    "PAGEUP": 33, //��ҳ
    "PAGEDOWN": 34, //��ҳ
    "BACK": 0x0008, //����
    "LEFT": 37, //��
    "UP": 38, //��
    "RIGHT": 39, //��
    "DOWN": 40 //��
}

/*
ZTE���Ӱ���Javascript�ű�
*/
var ZTE = {
    "ZERO": 48, //����0
    "ONE": 49, //����1
    "TWO": 50, //����2
    "THREE": 51, //����3
    "FOUR": 52, //����4
    "FIVE": 53, //����5
    "SIX": 54, //����6
    "SEVEN": 55, //����7
    "EIGHT": 56, //����8
    "NINE": 57, //����9
    "PAGEUP": 301, //��ҳ
    "PAGEDOWN": 302, //��ҳ
    "BACK": 126, //����
    "LEFT": 271, //��
    "UP": 269, //��
    "RIGHT": 272, //��
    "DOWN": 270 //��
}


/*
UT���Ӱ���Javascript�ű�
*/
var UT = {
    "ZERO": 48, //����0
    "ONE": 49, //����1
    "TWO": 50, //����2
    "THREE": 51, //����3
    "FOUR": 52, //����4
    "FIVE": 53, //����5
    "SIX": 54, //����6
    "SEVEN": 55, //����7
    "EIGHT": 56, //����8
    "NINE": 57, //����9
    "PAGEUP": 86, //��ҳ
    "PAGEDOWN": 85, //��ҳ
    "BACK": 8, //����
    "LEFT": 37, //��
    "UP": 38, //��
    "RIGHT": 39, //��
    "DOWN": 40 //��
}

//����onkeypress�¼�
document.onkeypress = KeyAction;

//Modified by gongjingyi at 2009/1/6
function getUrl(keyCode)//���ݽ�ֵ��ȡ��Ӧ��url
{
    //�鿴hw��Ӧ��ֵ
    for (var item in HW) {
        if (HW[item] == keyCode) {
            return Box[item];
        }
    }

    //�鿴zte��Ӧ��ֵ
    for (var item in ZTE) {
        if (ZTE[item] == keyCode) {
            return Box[item];
        }
    }

    //�鿴ut��Ӧ��ֵ
    for (var item in UT) {
        if (UT[item] == keyCode) {
            return Box[item];
        }
    }

    //���û�ж�Ӧ��ֵ���򷵻�undefined
    return "undefined";
}

function KeyAction() {
    var evt = evt ? evt : window.event;
    var keyCode = evt.which ? evt.which : evt.keyCode;
    var _url = getUrl(keyCode);
    if (_url != "undefined" && _url != undefined)
        gotoURL(_url);
}

var gotoFlag = false;
function gotoURL(URL)//��ת
{
    if (URL.toLowerCase().substr(0, 11) == "javascript:") {
        eval(URL.substr(11));
    } else {
    	if (!gotoFlag) {
    		gotoFlag = true;
	    	if (URL.toLowerCase() != "back") {
		        if (URL != "") { window.location.href = URL; }
		    }
		    else {
		        history.go(-1);
		    }
    	}
    }
}


//Ĭ�Ͻ���
//������Ҫ��ΪĬ�Ͻ����Ԫ��ID�� ע�⣺ ֻ�Կɵ���Ķ�����Ч
function onFocus(elementID) {

    var object = document.getElementById(elementID);
    if (object != null) {
        object.focus();
    }

}

//������Ҫ��ΪĬ�Ͻ����Ԫ��ID�������һ��Ԫ�ز�����,����ڶ���Ԫ��ΪĬ�Ͻ���. ע�⣺ ֻ�Կɵ���Ķ�����Ч
function onFocus(elementID, secondeElementID) {

    var object = document.getElementById(elementID);
    if (object != null) {
        object.focus();
    }
    else {
        var object2 = document.getElementById(secondeElementID);
        if (object2 != null) {
            object2.focus();
        }
    }

}

/*
��ö���
*/
function $(_param) { return document.getElementById(_param) ? document.getElementById(_param) : document.form1[_param]; }