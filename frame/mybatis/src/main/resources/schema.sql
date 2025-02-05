CREATE TABLE t_user (
    `uid` BIGINT auto_increment NOT NULL,
    `uname` VARCHAR(32) NOT NULL,
	CONSTRAINT user_pk PRIMARY KEY (uid)
);