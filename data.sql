create table if not exists users
(
    id            bigint primary key auto_increment,
    name          varchar(100) comment '用户昵称',
    phone         varchar(50)  comment '电话',
    password      varchar(50)  not null comment '密码',
    school     varchar(50)     comment '学校名字',
    avatar        text      default 'default.jpg' comment '用户头像',
    email         varchar(50) not null comment '邮箱地址',
    identity_card varchar(50) comment '身份证',
    stu_number    varchar(50) comment '学生学号',
    grade         varchar(50) comment '班级',
    faculty       varchar(100) comment '学院',
    major         varchar(100) comment '专业',
    birthday      timestamp comment '生日',
    location      varchar(50) comment '学校所在定位',
    score         int comment '信誉分',
    created_at    timestamp default now() comment '创建时间',
    deleted_at    timestamp comment '删除时间'
);

DROP TABLE IF EXISTS category;
create table category
(
    id   bigint primary key auto_increment,
    name varchar(50) not null comment '分类名称',
    status      boolean     not null default 1 comment '分类状态',
    create_time timestamp            default now() comment '创建时间',
    update_time timestamp comment '更新时间'
);

insert category(name)
values ('书籍资料'),
       ('学习用品'),
       ('数码产品'),
       ('服装配饰'),
       ('运动健身'),
       ('美妆个护'),
       ('交通工具'),
       ('零食专区'),
       ('游戏虚拟'),
       ('卡券门票'),
       ('宿舍神器'),
       ('技能服务'),
       ('文玩艺术'),
       ('其它新奇');

--  出售方式表
DROP TABLE IF EXISTS sell_mode;
create table sell_mode
(
    id          bigint primary key auto_increment comment '出售方式id',
    name        varchar(50) not null comment '出售方式名称',
    status      boolean     not null default 1 comment '出售方式状态',
    create_time timestamp            default now() comment '创建时间',
    update_time timestamp comment '更新时间'

);
insert sell_mode(name)
values ('物品出售'),
       ('物品出租');

--  发货方式表
drop table if exists dispatch_mode;
create table dispatch_mode
(
    id     bigint primary key auto_increment comment '发货方式id',
    name   varchar(50) not null comment '发货方式名字',
    status boolean     not null default 1 comment '发货方式状态',
    create_time timestamp            default now() comment '创建时间',
    update_time timestamp comment '更新时间'
);
insert dispatch_mode(name)
values ('快递'),
       ('自提');


-- 商品要求
drop table if exists product_require;
create table product_require
(
    id          bigint primary key auto_increment comment 'id',
    name        varchar(50) not null comment '商品要求名字',
    status      boolean     not null default 1 comment '商品要求状态',
    create_time timestamp            default now() comment '创建时间',
    update_time timestamp comment '更新时间'
);
insert product_require(name)
values ('不砍价'),
       ('不退货'),
       ('仅限本校');


-- 售卖方式、发货方式、商品要求对应表
drop table if exists sell_mode_dispatch_require;
create table sell_mode_dispatch_require
(
    id                 bigint primary key auto_increment,
    sell_mode_id       bigint not null,
    dispatch_id        bigint not null,
    product_require_id bigint not null,
    status      boolean     not null default 1 comment '状态',
    create_time timestamp            default now() comment '创建时间',
    update_time timestamp comment '更新时间'
);
insert sell_mode_dispatch_require(sell_mode_id, dispatch_id, product_require_id)
values (1, 1, 1),
       (1, 1, 2),
       (1, 2, 1),
       (1, 2, 2),
       (1, 2, 3),
       (2, 2, 1),
       (2, 2, 3);

--  商品列表
drop table if exists product;
create table product
(
    id               bigint primary key auto_increment comment '商品id',
    category_id      bigint         not null comment '分类id',
    detail           text           not null comment '商品详情',
    images           text           not null comment '商品图片，以逗号分隔',
    current_price    decimal(20, 2) not null comment '商品价格，保留两位小数',
    time_unit        varchar(20) comment '物品出租时间计量单位',
    origin_price     decimal(20, 2) comment '商品原价',
    sell_mode_id     bigint         not null comment '出售方式id',
    dispatch_mode_id bigint         not null comment '发货方式id',
    user_id          bigint         not null comment '发布者id',
    product_require_id varchar(10) comment '商品要求id,以逗号分隔',
    status           int        not null default 1 comment '商品状态，1表示在售，0表示售出，-1表示下架',
    location         varchar(50)    not null comment '商品所在学校定位',
    freight          decimal(20, 2) comment '运费',
    create_time      timestamp               default now() comment '创建时间',
    update_time      timestamp comment '更新时间',
    delete_time timestamp comment '删除时间'

);

