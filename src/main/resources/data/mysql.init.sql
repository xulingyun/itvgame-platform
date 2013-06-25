create table if not exists Account(
	accountId int not null auto_increment,
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
)engine=InnoDB auto_increment=10000;

create table if not exists AccountPermission(
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

insert into Account(pwdMd5, privilege, userId, createTime, updateTime, state) 
	values('76ba8e2f5ea9b4783377a966b6d601d4', 5, 'igsuper00', now(), now(), 1)
	on duplicate key update userId=values(userId);
insert into Account(pwdMd5, privilege, userId, createTime, updateTime, state) 
	values('76ba8e2f5ea9b4783377a966b6d601d4', 5, 'igsuper01', now(), now(), 1)
	on duplicate key update userId=values(userId);
insert into Account(pwdMd5, privilege, userId, createTime, updateTime, state) 
	values('76ba8e2f5ea9b4783377a966b6d601d4', 5, 'igsuper02', now(), now(), 1)
	on duplicate key update userId=values(userId);
insert into Account(pwdMd5, privilege, userId, createTime, updateTime, state) 
	values('76ba8e2f5ea9b4783377a966b6d601d4', 5, 'igsuper03', now(), now(), 1)
	on duplicate key update userId=values(userId);
insert into Account(pwdMd5, privilege, userId, createTime, updateTime, state) 
	values('76ba8e2f5ea9b4783377a966b6d601d4', 5, 'igsuper04', now(), now(), 1)
	on duplicate key update userId=values(userId);
insert into Account(pwdMd5, privilege, userId, createTime, updateTime, state) 
	values('76ba8e2f5ea9b4783377a966b6d601d4', 5, 'igsuper05', now(), now(), 1)
	on duplicate key update userId=values(userId);
insert into Account(pwdMd5, privilege, userId, createTime, updateTime, state) 
	values('76ba8e2f5ea9b4783377a966b6d601d4', 5, 'igsuper06', now(), now(), 1)
	on duplicate key update userId=values(userId);
insert into Account(pwdMd5, privilege, userId, createTime, updateTime, state) 
	values('76ba8e2f5ea9b4783377a966b6d601d4', 5, 'igsuper07', now(), now(), 1)
	on duplicate key update userId=values(userId);
insert into Account(pwdMd5, privilege, userId, createTime, updateTime, state) 
	values('76ba8e2f5ea9b4783377a966b6d601d4', 5, 'igsuper08', now(), now(), 1)
	on duplicate key update userId=values(userId);
insert into Account(pwdMd5, privilege, userId, createTime, updateTime, state) 
	values('76ba8e2f5ea9b4783377a966b6d601d4', 5, 'igsuper09', now(), now(), 1)
	on duplicate key update userId=values(userId);

drop table if exists ProductClass;
create table if not exists ProductClass(
	classId int not null auto_increment,
	className varchar(32) not null,
	primary key(classId)
);

drop table if exists ProductProvider;
create table if not exists ProductProvider(
	providerId int not null auto_increment,
	providerName varchar(64) not null,
	type varchar(12) not null,
	primary key(providerId)
);

drop table if exists Product;
create table if not exists Product(
	productId int not null auto_increment,
	productName varchar(32) not null,
	productClass int not null default 0,
	appName varchar(16) not null unique,
	appType varchar(12) not null,
	description varchar(255),
	supportDataManager tinyint(1) not null default 0,
	location varchar(128),
	gameid int not null default 0,
	createTime datetime not null,
	updateTime datetime not null,
	providerId int not null default 0,
	state int not null default 0,
	primary key(productId)
);

drop table if exists ProductDetail;
create table if not exists ProductDetail(
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

drop table if exists PurchaseRelation;
create table if not exists PurchaseRelation(
	purchaseId int not null auto_increment,
	productId int not null,
	subscribeImplementor varchar(16) not null,
	subscribeType varchar(12) not null,
	amount int not null,
	value int not null,
	subscribeId varchar(16) not null,
	description varchar(255),
	primary key(purchaseId)
)auto_increment=10000;

create table if not exists Authorization(
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
)engine=InnoDB;

create table if not exists ProductPermission(
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

drop table if exists GameProp;
create table if not exists GameProp(
	propId int not null auto_increment,
	propName varchar(32) not null,
	price int not null,
	validPeriod int not null default 0,
	productId int not null,
	feeCode int not null,
	description varchar(255) not null,
	primary key(propId),
	index(productId)
);

create table if not exists AccountProp(
	accountId int not null,
	propId int not null,
	productId int not null,
	count int not null,
	expiryDate datetime,
	primary key(accountId, productId, propId)
)engine=InnoDB;

create table if not exists GameRecord(
	accountId int not null,
	recordId int not null,
	scores int not null default 0,
	playDuration int not null default 0,
	time datetime not null,
	remark varchar(255),
	data blob,
	primary key(accountId, recordId)
);

create table if not exists GameAttainment(
	accountId int not null,
	userId varchar(32) not null,
	attainmentId int not null,
	scores int not null default 0,
	playDuration int not null default 0,
	time datetime not null,
	remark varchar(255),
	data blob,
	primary key(accountId, attainmentId),
	index(scores),
	index(time, scores)
);

create table if not exists PurchaseRecord(
	id bigint not null auto_increment,
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
	primary key(id),
	index(time),
	index(propId, time),
	index(accountId, productId, time)
)engine=InnoDB;

create table if not exists SubscribeRecord(
	id bigint not null auto_increment,
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
	primary key(id),
	index(time),
	index(productId, time),
	index(userId, productId, time)
)engine=InnoDB;
