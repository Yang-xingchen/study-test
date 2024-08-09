DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (
	u_id BIGINT auto_increment NOT NULL,
	u_name varchar(32) NOT NULL,
	u_age INT NULL,
	CONSTRAINT t_user_pk PRIMARY KEY (u_id)
);

INSERT t_user (u_id, u_name, u_age)
VALUES
    (1, 'user1', 20),
    (2, 'user2', 17),
    (3, 'user3', 23);

--
DROP TABLE IF EXISTS t_type;
CREATE TABLE t_type (
	t_id BIGINT auto_increment NOT NULL,
	t_name varchar(32) NOT NULL,
	t_parent BIGINT NULL,
	t_level INT NOT NULL,
	CONSTRAINT t_type_pk PRIMARY KEY (t_id)
);

INSERT t_type (t_id, t_name, t_parent, t_level)
VALUES
    (1, '日用品', NULL, 0),
    (2, '纸巾', 1, 1),
    (3, '笔', 1, 1),
    (4, '电子产品', NULL, 0),
    (5, '手机', 4, 1),
    (6, '电脑', 4, 1);

--
DROP TABLE IF EXISTS t_good;
CREATE TABLE t_good (
	g_id BIGINT auto_increment NOT NULL,
	g_name varchar(32) NOT NULL,
	g_type BIGINT NOT NULL,
	g_money BIGINT NOT NULL,
	CONSTRAINT t_good_pk PRIMARY KEY (g_id)
);

INSERT t_good (g_id, g_name, g_type, g_money)
VALUES
    (1, 'X纸', 2, 200),
    (2, 'Y纸', 2, 300),
    (3, 'X手机', 5, 100000),
    (4, 'Y手机', 5, 350000),
    (5, 'Z手机', 5, 500000);

--
DROP TABLE IF EXISTS t_order;
CREATE TABLE t_order (
	o_id BIGINT NOT NULL,
	o_status INT NOT NULL,
	o_create_time DATETIME NOT NULL,
	o_uid BIGINT NOT NULL,
	CONSTRAINT t_order_pk PRIMARY KEY (o_id)
);

INSERT t_order (o_id, o_status, o_create_time, o_uid)
VALUES
    (1, 0, '2024-8-1 12:00:00', 1),
    (2, 1, '2024-8-1 12:00:00', 1),
    (3, 1, '2024-8-3 11:00:00', 2),
    (4, 1, '2024-8-5 15:00:00', 2),
    (5, 2, '2024-8-5 10:00:00', 1),
    (6, 2, '2024-8-7 14:00:00', 3);

--
DROP TABLE IF EXISTS t_order_item;
CREATE TABLE t_order_item (
	oi_id BIGINT NOT NULL,
	oi_oid BIGINT NOT NULL,
	oi_gid BIGINT NOT NULL,
	oi_count INT NOT NULL,
	CONSTRAINT t_order_item_pk PRIMARY KEY (oi_id)
);

INSERT t_order_item (oi_id, oi_oid, oi_gid, oi_count)
VALUES
    (1001, 1, 1, 5),
    (1002, 1, 2, 3),
    (1003, 1, 4, 1),
    (2001, 2, 3, 1),
    (3001, 3, 2, 7),
    (4001, 4, 5, 1),
    (4002, 4, 2, 2),
    (5001, 5, 2, 2),
    (6001, 6, 5, 2);