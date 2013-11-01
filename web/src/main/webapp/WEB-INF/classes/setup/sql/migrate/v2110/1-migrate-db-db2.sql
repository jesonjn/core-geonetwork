-- Creates New tables required for this version

CREATE TABLE HarvesterSettings
  (
    id        int            not null,
    parentId  int,
    name      varchar(64)    not null,
    value     CLOB(1G),
    primary key(id)
  );

ALTER TABLE HarvesterSettings ADD FOREIGN KEY (parentId) REFERENCES HarvesterSettings (id);

CREATE TABLE ADDRESS
(
	id			  int 			not null,
	address       varchar(128),
	city          varchar(128),
	state         varchar(32),
	zip           varchar(16),
	country       varchar(128),
	primary key(id)
);

CREATE TABLE USERADDRESS
(
	userid 	int not null,
	addressid int not null,
	primary key(userid,addressid)
);

CREATE TABLE EMAIL
(
	user_id			  int 			not null,
	email         varchar(128),
	primary key(userid)
);
