if object_id('[Account]') is null create table [Account](
	accountId int not null identity(10000, 1),
	nickName varchar(32),
	pwdMd5 varchar(32),
	privilege int not null default 0,
	userId varchar(32) not null unique,
	scores int not null default 0,
	goldCoin int not null default 0,
	gameCoin int not null default 0,
	createTime datetime not null,
	updateTime datetime not null,
	state int not null default 0,
	primary key(accountId)
);
	
if object_id('[AccountPermission]') is null create table [AccountPermission](
	accountId int not null,
	daySubscribeLimit int not null default 0,
	monthSubscribeLimit int not null default 0,
	lastSubscribeTime datetime,
	daySubscribeAmount int not null default 0,
	monthSubscribeAmount int not null default 0,
	totalSubscribeAmount int not null default 0,
	accessPermission int not null default 0,
	subscribePermission int not null default 0,
	primary key(accountId)
);

if not exists(select * from [Account] where userId='igsuper00')
	insert into Account(pwdMd5, privilege, userId, createTime, updateTime, state) 
	values('76ba8e2f5ea9b4783377a966b6d601d4', 5, 'igsuper00', getdate(), getdate(), 1);
if not exists(select * from [Account] where userId='igsuper01')
	insert into Account(pwdMd5, privilege, userId, createTime, updateTime, state) 
	values('76ba8e2f5ea9b4783377a966b6d601d4', 5, 'igsuper01', getdate(), getdate(), 1);
if not exists(select * from [Account] where userId='igsuper02')
	insert into Account(pwdMd5, privilege, userId, createTime, updateTime, state) 
	values('76ba8e2f5ea9b4783377a966b6d601d4', 5, 'igsuper02', getdate(), getdate(), 1);
if not exists(select * from [Account] where userId='igsuper03')
	insert into Account(pwdMd5, privilege, userId, createTime, updateTime, state) 
	values('76ba8e2f5ea9b4783377a966b6d601d4', 5, 'igsuper03', getdate(), getdate(), 1);
if not exists(select * from [Account] where userId='igsuper04')
	insert into Account(pwdMd5, privilege, userId, createTime, updateTime, state) 
	values('76ba8e2f5ea9b4783377a966b6d601d4', 5, 'igsuper04', getdate(), getdate(), 1);
if not exists(select * from [Account] where userId='igsuper05')
	insert into Account(pwdMd5, privilege, userId, createTime, updateTime, state) 
	values('76ba8e2f5ea9b4783377a966b6d601d4', 5, 'igsuper05', getdate(), getdate(), 1);
if not exists(select * from [Account] where userId='igsuper06')
	insert into Account(pwdMd5, privilege, userId, createTime, updateTime, state) 
	values('76ba8e2f5ea9b4783377a966b6d601d4', 5, 'igsuper06', getdate(), getdate(), 1);
if not exists(select * from [Account] where userId='igsuper07')
	insert into Account(pwdMd5, privilege, userId, createTime, updateTime, state) 
	values('76ba8e2f5ea9b4783377a966b6d601d4', 5, 'igsuper07', getdate(), getdate(), 1);
if not exists(select * from [Account] where userId='igsuper08')
	insert into Account(pwdMd5, privilege, userId, createTime, updateTime, state) 
	values('76ba8e2f5ea9b4783377a966b6d601d4', 5, 'igsuper08', getdate(), getdate(), 1);
if not exists(select * from [Account] where userId='igsuper09')
	insert into Account(pwdMd5, privilege, userId, createTime, updateTime, state) 
	values('76ba8e2f5ea9b4783377a966b6d601d4', 5, 'igsuper09', getdate(), getdate(), 1);

if object_id('[ProductClass]') is not null drop table [ProductClass];
create table [ProductClass](
	classId int not null identity(1, 1),
	className varchar(32) not null,
	primary key(classId)
);

if object_id('[ProductProvider]') is not null drop table [ProductProvider];
create table [ProductProvider](
	providerId int not null identity(1, 1),
	providerName varchar(64) not null,
	type varchar(12) not null,
	primary key(providerId)
);

if object_id('[Product]') is not null drop table [Product];
create table [Product](
	productId int not null identity(1, 1),
	productName varchar(32) not null,
	productClass int not null default 0,
	appName varchar(16) not null unique,
	appType varchar(12) not null,
	description varchar(255),
	supportDataManager bit not null default 0,
	location varchar(128),
	gameid int not null default 0,
	createTime datetime not null,
	updateTime datetime not null,
	providerId int not null default 0,
	state int not null default 0,
	primary key(productId)
);

if object_id('[ProductDetail]') is not null drop table [ProductDetail];
create table [ProductDetail](
	productId int not null,
	appName varchar(16) not null unique,
	productName varchar(32) not null,
	rechargeManager varchar(12) not null,
	subscribeImplementor varchar(12) not null,
	amountUnit varchar(16) not null,
	rechargeRatio int not null default 10,
	daySubscribeLimit int not null default 0,
	monthSubscribeLimit int not null default 0,
	tryNumber int not null default 0,
	purchaseType varchar(12) not null,
	monthFee int not null default 0,
	validPeriod int not null default 0,
	periodFee int not null default 0,
	validCount int not null default 0,
	countFee int not null default 0,
	validSeconds int not null default 0,
	secondsFee int not null default 0,
	primary key(productId)
);

if object_id('[PurchaseRelation]') is not null drop table [PurchaseRelation];
create table [PurchaseRelation](
	purchaseId int not null identity(10000, 1),
	productId int not null,
	subscribeImplementor varchar(16) not null,
	subscribeType varchar(12) not null,
	amount int not null,
	value int not null,
	subscribeId varchar(16) not null,
	description varchar(255),
	primary key(purchaseId)
);

if object_id('[Authorization]') is null create table [Authorization](
	accountId int not null,
	productId int not null,
	authorizationType int not null default 0,
	leftTryNumber int not null default 0,
	leftValidSeconds int not null default 0,
	leftValidCount int not null default 0,
	goldCoin int not null default 0,
	gameCoin int not null default 0,
	authorizationStartTime datetime,
	authorizationEndTime datetime,
	primary key(accountId, productId)
);

if object_id('[ProductPermission]') is null create table [ProductPermission](
	accountId int not null,
	productId int not null,
	daySubscribeLimit int not null default 0,
	monthSubscribeLimit int not null default 0,
	lastSubscribeTime datetime,
	daySubscribeAmount int not null default 0,
	monthSubscribeAmount int not null default 0,
	totalSubscribeAmount int not null default 0,
	accessPermission int not null default 0,
	subscribePermission int not null default 0,
	primary key(accountId, productId)
);

if object_id('[GameProp]') is not null drop table [GameProp];
create table [GameProp](
	propId int not null identity(1, 1),
	propName varchar(32) not null,
	price int not null,
	validPeriod int not null,
	productId int not null,
	feeCode int not null,
	description varchar(255) not null,
	primary key(propId)
);

if not exists (select name from sysindexes where name = 'IDX_GP_PID')
	create index IDX_GP_PID on [GameProp](productId);

if object_id('[AccountProp]') is null create table [AccountProp](
	accountId int not null,
	propId int not null,
	productId int not null,
	count int not null,
	expiryDate datetime,
	primary key(accountId, productId, propId)
);

if object_id('[GameRecord]') is null create table [GameRecord](
	accountId int not null,
	recordId int not null,
	scores int not null default 0,
	playDuration int not null default 0,
	time datetime not null,
	remark varchar(255),
	data image,
	primary key(accountId, recordId)
);

if object_id('[GameAttainment]') is null create table [GameAttainment](
	accountId int not null,
	userId varchar(32) not null,
	attainmentId int not null,
	scores int not null default 0,
	playDuration int not null default 0,
	time datetime not null,
	remark varchar(255),
	data image,
	primary key(accountId, attainmentId)
);

if not exists (select name from sysindexes where name = 'IDX_GA_SCO')
	create index IDX_GA_SCO on [GameAttainment](scores);
	
if not exists (select name from sysindexes where name = 'IDX_GA_TIM_SCO')
	create index IDX_GA_TIM_SCO on [GameAttainment](time, scores);

if object_id('[PurchaseRecord]') is null create table [PurchaseRecord](
	id bigint not null identity(1, 1),
	accountId int not null,
	userId varchar(32) not null,
	productId int not null,
	productName varchar(32) not null,
	propId int not null,
	propName varchar(32) not null,
	propCount int not null,
	amount int not null,
	remark varchar(255) not null,
	time datetime not null,
	ip varchar(15),
	primary key(id)
);
if not exists (select name from sysindexes where name = 'IDX_PR_TIM')
	create index IDX_PR_TIM on [PurchaseRecord](time);
if not exists (select name from sysindexes where name = 'IDX_PR_PID_TIM')
	create index IDX_PR_PID_TIM on [PurchaseRecord](propId, time);
if not exists (select name from sysindexes where name = 'IDX_PR_AID_PID_TIM')
	create index IDX_PR_AID_PID_TIM on [PurchaseRecord](accountId, productId, time);

if object_id('[SubscribeRecord]') is null create table [SubscribeRecord](
	id bigint not null identity(1, 1),
	accountId int not null,
	userId varchar(32) not null,
	productId int not null,
	productName varchar(32) not null,
	amount int not null,
	subscribeImplementor varchar(16) not null,
	subscribeType varchar(12) not null,
	accountSubscribeCommand int not null,	
	productSubscribeCommand int not null,
	payType int not null,
	subscribeId varchar(16) not null,
	remark varchar(255) not null,
	time datetime not null,
	ip varchar(15),
	primary key(id)
);
if not exists (select name from sysindexes where name = 'IDX_SR_TIM')
	create index IDX_SR_TIM on [SubscribeRecord](time);
if not exists (select name from sysindexes where name = 'IDX_SR_PID_TIM')
	create index IDX_SR_PID_TIM on [SubscribeRecord](productId, time);
if not exists (select name from sysindexes where name = 'IDX_SR_UID_PID_TIM')
	create index IDX_SR_UID_PID_TIM on [SubscribeRecord](userId, productId, time);