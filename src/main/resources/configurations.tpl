<?xml version="1.0" encoding="UTF-8"?>
<configurations>
    <configuration id="global">
        <property name="supportCache">true</property>
        <property name="sql.dialect">mysql</property>
        <property name="connection.xml">/mysql.config.xml</property>
        <property name="telcomOperator">{{{telcom}}}</property>
        <property name="serviceProvider">{{{sp}}}</property>
       <users>
			<user>
				<name>igtest01</name>
				<passwd>76ba8e2f5ea9b4783377a966b6d601d4</passwd>
				<role>test</role>
			</user>
			<user>
				<name>igadmin01</name>
				<passwd>76ba8e2f5ea9b4783377a966b6d601d4</passwd>
				<role>admin</role>
			</user>
		</users>
	</configuration>
	
	<configuration id="ohyeah">
		<property name="protocolLocation">
			<![CDATA[/protocol/processor]]>
		</property>
		<property name="amountUnit">元宝</property>
		<property name="cashToAmountRatio">10</property>
		<property name="rechargeRatio">10</property>
		<property name="daySubscribeLimit">50</property>
		<property name="monthSubscribeLimit">100</property>
		<property name="supportSubscribeLimit">false</property>
		<restricts>
			<timeRestrict>
				<productId>5</productId>
				<subscribeEnableDate>2011/10/01</subscribeEnableDate>
			</timeRestrict>
			<timeRestrict>
				<productId>6</productId>
				<subscribeEnableDate>2011/10/01</subscribeEnableDate>
			</timeRestrict>
		</restricts>
	</configuration>
	
	<configuration id="telcomCommon">
		<property name="amountUnit">元</property>
		<property name="cashToAmountRatio">1</property>
		<property name="supportPoints">false</property>
		<property name="pointsUnit">积分</property>
		<property name="cashToPointsRatio">100</property>
	</configuration>
	
	<configuration id="telcomgd">
		<property name="amountUnit">元</property>
		<property name="cashToAmountRatio">1</property>
		<property name="supportPoints">true</property>
		<property name="pointsUnit">积分</property>
		<property name="cashToPointsRatio">100</property>
	</configuration>
	
	<configuration id="telcomjs">
		<property name="amountUnit">元</property>
		<property name="cashToAmountRatio">1</property>
		<property name="supportPoints">false</property>
		<property name="pointsUnit">积分</property>
		<property name="cashToPointsRatio">100</property>
	</configuration>
	
	<configuration id="telcomsh">
		<property name="spid">02101689</property>
		<property name="epg"><![CDATA[Authentication.CTCGetConfig('EPGDomain')]]></property>
		<property name="ssoMode">redirect</property>
		<property name="ssoUrl">
			<![CDATA[http://124.75.29.164:7001/iptv3a/VASGetUserinfoAction.do?Action=UserTokenRequest&SPID=%s&ReturnURL=%s&ReturnInfo=%s]]>
		</property>
		<property name="authorizeMode">webservice</property>
		<property name="authorizeUrl">
			<![CDATA[http://124.75.29.164:7001/iptv3a/services/VasServiceSoapImpl]]>
		</property>
		<property name="amountUnit">元</property>
		<property name="cashToAmountRatio">1</property>
		<property name="supportPoints">false</property>
		<property name="pointsUnit">积分</property>
		<property name="cashToPointsRatio">100</property>
		<property name="queryPointsUrl">http://222.68.195.70:8080/remarkDispatcher/PointQueryInterfacePort</property>
		<property name="queryPointsCheckCodeKey">aaa</property>
		<property name="queryUserInfoUrl">http://124.75.29.171:7001/iptvInfo/services/UserInfoService</property>
		<property name="subscribeMode">redirect</property>
		<property name="subscribeUrl">
			<![CDATA[http://124.75.29.164:7001/iptv3a/user/subscribe/subscribe.do?Action=1&UserID=%s&ProductID=%s&UserToken=%s&SPID=%s&ReturnURL=%s]]>
		</property>
		<property name="unsubscribeMode">redirect</property>
		<property name="unsubscribeUrl">
			<![CDATA[http://124.75.29.164:7001/iptv3a/user/subscribe/subscribe.do?Action=2&UserID=%s&ProductID=%s&UserToken=%s&SPID=%s&ReturnURL=%s]]>
		</property>
		
		<ids>
			<id subscribeId="1000702403" type="period" period="0" amount="2"/>
		</ids>
		
		<patterns>
			<pattern name="action"><![CDATA[<form.*?action\s?=\s?"?([^\s"]*)"?.*?>]]></pattern>
			<pattern name="param"><![CDATA[<input.*?type\s?=\s?"?hidden"?.*?name\s?=\s?"?([^\s"]*)"?\s+value\s?=\s?"?([^\s"]*)"?.*?>]]></pattern>
			<pattern name="backUrl"><![CDATA[<a.*?onclick\s?=\s?"?location\.href='([^']*)'"?.*?>]]></pattern>
			<pattern name="result"><![CDATA[[?&]result=([^&]*)]]></pattern>
		</patterns>
	</configuration>
	
	<configuration id="the9">
		<property name="amountUnit">代币</property>
		<property name="cashToAmountRatio">1</property>
		<property name="supportPoints">false</property>
		<property name="subscribeMode">webservice</property>
		<property name="subscribeUrl">
			<![CDATA[http://114.80.197.24/Platform3Service/UserService.svc]]>
		</property>
	</configuration>
	
	<configuration id="chinagames">
		<property name="amountUnit">元</property>
		<property name="cashToAmountRatio">1</property>
		<property name="supportPoints">true</property>
		<property name="subscribeMode">redirect</property>
		<property name="subscribeUrl">
			<![CDATA[http://222.68.195.20:8080/zy_portal/subscribe/subscribe?Action=1&UserID=%s&ProductID=%s&UserToken=%s&SPID=%s&ReturnURL=%s]]>
		</property>
		<property name="unsubscribeMode">redirect</property>
		<property name="unsubscribeUrl">
			<![CDATA[http://222.68.195.20:8080/zy_portal/subscribe/subscribe?Action=2&UserID=%s&ProductID=%s&UserToken=%s&SPID=%s&ReturnURL=%s]]>
		</property>
	</configuration>
	
	<configuration id="winside">
		<property name="amountUnit">元宝</property>
		<property name="cashToAmountRatio">10</property>
		<property name="supportPoints">false</property>
		<property name="subscribeMode">webservice</property>
		<property name="consumeCoinsUrl">
			<![CDATA[consume_coins?userid=%s&username=%s&product=%s&contents=%s&amount=%s&coins=%s&checkcode=%s]]>
		</property>
		<property name="queryCoinsUrl">
			<![CDATA[query_coins?userid=%s]]>
		</property>
		<property name="rechargeUrl">
			<![CDATA[order_coins?userid=%s&username=%s&money=%s&spid=%s&product=%s&userToken=%s&checkCode=%s]]>
		</property>
	</configuration>
	
	<configuration id="winsidegd">
		<property name="amountUnit">元宝</property>
		<property name="cashToAmountRatio">10</property>
		<property name="supportPoints">true</property>
		<property name="subscribeMode">webservice</property>
		<property name="rechargeUrl">
			<![CDATA[order_coins?userid=%s&username=%s&spid=%s&stbType=%s&product=%s&money=%s&gameid=%s&enterURL=%s&zyUserToken=%s&checkCode=%s&payType=%s]]>
		</property>
	</configuration>
	
	<configuration id="chinagamesgd">
		<property name="favorUrl">
			<![CDATA[IPTV_Advance/FavorGameServlet?userID=%s&user=%s&gameID=%s&spid=%s&code=%s&timeStmp=%s]]>
		</property>
	</configuration>

    <configuration id="dijoy">
        <property name="amountUnit">元</property>
        <property name="cashToAmountRatio">1</property>
        <property name="supportPoints">false</property>
        <property name="feecodes">1/2/3/4</property>
        <property name="amounts">1/2/5/10</property>
        <property name="subscribeMode">webservice</property>
        <property name="paymentUrl">
            <![CDATA[http://220.248.44.50:3280/common/php/business/apppay.php]]>
        </property>
    </configuration>
</configurations>
