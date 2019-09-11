create table if not exists t_user(
    id int not null auto_increment,
    name varchar(255),
    create_time timestamp,
    primary key (id)
);
create table if not exists t_order(
    id bigint not null auto_increment,
    user int not null,
    foreign key (user) references t_user(id),
    primary key (id)
);
create table if not exists t_commodity(
    id bigint not null auto_increment,
    name varchar(255) not null,
    primary key (id)
);
create table if not exists t_order_commodity(
    t_order bigint not null,
    t_commodity bigint not null,
    count int not null default 0,
    foreign key (t_order) references t_order(id),
    foreign key (t_commodity) references t_commodity(id),
    primary key (t_order, t_commodity)
);