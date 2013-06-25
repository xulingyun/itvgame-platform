var Box = {
    "PAGEUP": "",    //上页
    "PAGEDOWN": "",    //下页
    "BACK": "",    //返回
    "ZERO": "",    //数字0
    "ONE": "",    //数字1
    "TWO": "",    //数字2
    "THREE": "",    //数字3
    "FOUR": "",    //数字4
    "FIVE": "",    //数字5
    "SIX": "",    //数字6
    "SEVEN": "",    //数字7
    "EIGHT": "",    //数字8
    "NINE": "",   //数字9
    "LEFT": "",    //左
    "UP": "",     //上
    "RIGHT": "",   //右
    "DOWN": ""    //下
}
/*
HW盒子按键Javascript脚本
*/
var HW = {
    "ZERO": 0x0030, //数字0
    "ONE": 49, //数字1
    "TWO": 50, //数字2
    "THREE": 51, //数字3
    "FOUR": 52, //数字4
    "FIVE": 53, //数字5
    "SIX": 54, //数字6
    "SEVEN": 55, //数字7
    "EIGHT": 56, //数字8
    "NINE": 57, //数字9
    "PAGEUP": 33, //上页
    "PAGEDOWN": 34, //下页
    "BACK": 0x0008, //返回
    "LEFT": 37, //左
    "UP": 38, //上
    "RIGHT": 39, //右
    "DOWN": 40 //下
}

/*
ZTE盒子按键Javascript脚本
*/
var ZTE = {
    "ZERO": 48, //数字0
    "ONE": 49, //数字1
    "TWO": 50, //数字2
    "THREE": 51, //数字3
    "FOUR": 52, //数字4
    "FIVE": 53, //数字5
    "SIX": 54, //数字6
    "SEVEN": 55, //数字7
    "EIGHT": 56, //数字8
    "NINE": 57, //数字9
    "PAGEUP": 301, //上页
    "PAGEDOWN": 302, //下页
    "BACK": 126, //返回
    "LEFT": 271, //左
    "UP": 269, //上
    "RIGHT": 272, //右
    "DOWN": 270 //下
}


/*
UT盒子按键Javascript脚本
*/
var UT = {
    "ZERO": 48, //数字0
    "ONE": 49, //数字1
    "TWO": 50, //数字2
    "THREE": 51, //数字3
    "FOUR": 52, //数字4
    "FIVE": 53, //数字5
    "SIX": 54, //数字6
    "SEVEN": 55, //数字7
    "EIGHT": 56, //数字8
    "NINE": 57, //数字9
    "PAGEUP": 86, //上页
    "PAGEDOWN": 85, //下页
    "BACK": 8, //返回
    "LEFT": 37, //左
    "UP": 38, //上
    "RIGHT": 39, //右
    "DOWN": 40 //下
}

//触发onkeypress事件
document.onkeypress = KeyAction;

//Modified by gongjingyi at 2009/1/6
function getUrl(keyCode)//根据健值获取对应的url
{
    //查看hw对应键值
    for (var item in HW) {
        if (HW[item] == keyCode) {
            return Box[item];
        }
    }

    //查看zte对应键值
    for (var item in ZTE) {
        if (ZTE[item] == keyCode) {
            return Box[item];
        }
    }

    //查看ut对应键值
    for (var item in UT) {
        if (UT[item] == keyCode) {
            return Box[item];
        }
    }

    //如果没有对应键值，则返回undefined
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
function gotoURL(URL)//跳转
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


//默认焦点
//传入需要设为默认焦点的元素ID。 注意： 只对可点击的对象有效
function onFocus(elementID) {

    var object = document.getElementById(elementID);
    if (object != null) {
        object.focus();
    }

}

//传入需要设为默认焦点的元素ID。如果第一个元素不存在,则设第二个元素为默认焦点. 注意： 只对可点击的对象有效
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
获得对象
*/
function $(_param) { return document.getElementById(_param) ? document.getElementById(_param) : document.form1[_param]; }