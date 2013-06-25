<%@ page language="java" contentType="text/html; charset=gb2312"
    pageEncoding="gb2312"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
    <script type="text/javascript" language="javascript">
        //启用遥控器键值捕获
        if (navigator.userAgent.indexOf("EnReach") > -1) {
            var reach = new EnReach();
            reach.EnableRCNumKey(0);
        }

        function KeyAction() {
            var keyCode = window.event.which ? window.event.which : window.event.keyCode;
            showTest(keyCode);
        }

        function showTest(value) {
            var testdiv = document.getElementById('test1');
            if (testdiv)
                testdiv.innerHTML = testdiv.innerHTML + "|" + value;
        }

        //document.onkeydown=KeyAction;
        document.onkeypress = KeyAction;

        function ShowVersion() {
            var versionDiv = document.getElementById('version1');
            var navigatorInfo = "";
            navigatorInfo += "<font color=red>appCodeName: </font>" + navigator.appCodeName + " ";
            navigatorInfo += "<font color=red>appName: </font>" + navigator.appName + " ";
            navigatorInfo += "<font color=red>platform: </font>" + navigator.platform + "<br>";
            navigatorInfo += "<font color=red>appVersion: </font>" + navigator.appVersion + " <br>"
            navigatorInfo += "<font color=red>userAgent: </font>" + navigator.userAgent + "<br>";


            versionDiv.innerHTML = navigatorInfo;
            showSTBInfo();
            if (typeof (iPanel) != "undefined" && navigator.userAgent.indexOf("EIS") < 0) {
                var temp = iPanel.ioctlRead("infoSWModule");
                var t = temp.split(";");
                var w = t[0].split(".");
                var version = w[0].split(":");
                showTest("<font color=red>STB version: </font>" + version[1]);
                SetVersion(version[1]);
            }
            else {
                SetVersion(navigator.userAgent);
            }
        }
        function SetVersion(val) {
            var html = "浏览器信息:";
            var versionDiv2 = document.getElementById('version2');
            if (typeof (val) == "undefined") {
                versionDiv2.innerHTML = html + "Default";
            }
            else {
                val = val.toLowerCase();
                if (val.indexOf("eis") >= 0)
                    versionDiv2.innerHTML = html + "CoshipOrHw";
                else if (val.indexOf("opera") == 0)
                    versionDiv2.innerHTML = html + "UT2.0";
                else if (val.indexOf('enreach') >= 0)
                    versionDiv2.innerHTML = html + "UT1.0";
                else if (val.indexOf("v1") == 0)
                    versionDiv2.innerHTML = html + "ZTEV1";
                else if (val.indexOf("v2") == 0)
                    versionDiv2.innerHTML = html + "ZTEV2";
                else if (val.indexOf("v3") == 0)
                    versionDiv2.innerHTML = html + "ZTEV3";
                else if (val.indexOf("v4") == 0)
                    versionDiv2.innerHTML = html + "ZTEV4";
                else
                    versionDiv2.innerHTML = html + "Unknow";
            }
        }
        function showSTBInfo() {
            var Result = "";
            var ResultDiv = document.getElementById('STBInfo');
            Result += "<font color=red>|序列号:</font>" + iPanel.ioctlRead("ProductBarCode") + "<br>";
            Result += "<font color=red>|PPPoE用户:</font>" + iPanel.ioctlRead("PPPoEUser") + "<br>";
            Result += "<font color=red>|PPPOE密码:</font>" + iPanel.ioctlRead("PPPoEPwd") + "<br>";
            Result += "<font color=red>|内存:</font>" + iPanel.ioctlRead("DRAMSize") + "<br>";
            Result += "<font color=red>|闪存:</font>" + iPanel.ioctlRead("FlashSize") + "<br>";
            Result += "<font color=red>|CPU:</font>" + iPanel.ioctlRead("CPUModel") + " " + iPanel.ioctlRead("CPUFrequency") + "<br>";
            Result += "<font color=red>|OSVersion:</font>" + iPanel.ioctlRead("OSVersion") + "<br>";
            Result += "<font color=red>|BrowserVersion:</font>" + iPanel.ioctlRead("BrowserVersion") + "<br>";
            Result += "<font color=red>|FirmwareVersion:</font>" + iPanel.ioctlRead("FirmwareVersion") + "<br>";
            Result += "<font color=red>|HWVersion:</font>" + iPanel.ioctlRead("HWVersion") + "<br>";
            Result += "<font color=red>|DSPVersion:</font>" + iPanel.ioctlRead("DSPVersion") + "<br>";
            Result += "<font color=red>|infoSWModule:</font>" + iPanel.ioctlRead("infoSWModule") + "<br>";

            ResultDiv.innerHTML = Result;
        }
    </script>
    <style type="text/css">
        body
        {
            background-repeat: no-repeat;
            margin: 0px;
            background-color: Black;
        }
        #DivMain
        {
            position: absolute;
            left: 20px;
            top: 30px;
            width: 640px;
            height: 524px;
            z-index: 1;
            text-align: center;
        }
    </style>
</head>
<body onload="ShowVersion()" style="background-color: #FFFFFF;">
    <div id="DivMain" name="DivMain">
        <div id="version2" name="version2">
            浏览器信息：Unknow</div>
        <hr>
        <div id="version1" name="version1">
            1</div>
        <hr>
        机顶盒信息
        <hr>
        <div id="STBInfo" name="STBInfo">
        </div>
        <hr>
        <div id="test1" name="test1" style="color: Blue;">
            键值:</div>
        <hr>
        <a href="http://114.80.197.24/Sh">返回</a> | <a href="javascript:location.reload();">刷新</a>
        | <a href="#">空测试链接</a>
    </div>
</body>
</html>