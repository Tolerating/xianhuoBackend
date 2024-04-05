create database if not exists xianhuo;
DROP TABLE IF EXISTS users;
create table users
(
    id            bigint primary key auto_increment,
    name          varchar(100) comment '用户昵称',
    phone         varchar(50) comment '电话',
    password      varchar(50) not null comment '密码',
    school        varchar(50) comment '学校名字,通过学校定位得到',
    avatar        text           default 'default.jpg' comment '用户头像',
    email         varchar(50) not null comment '邮箱地址',
    profit        decimal(20, 2) default 0.00 comment '我的收益',
    identity_card varchar(50) comment '身份证',
    stu_number    varchar(50) comment '学生学号',
    grade         varchar(50) comment '班级',
    faculty       varchar(100) comment '学院',
    major         varchar(100) comment '专业',
    birthday      timestamp comment '生日',
    location      varchar(50) comment '学校所在定位',
    score         int comment '信誉分',
    in_chat       int            default 0 comment '是否在聊天页',
    created_at    timestamp      default now() comment '创建时间',
    deleted_at    timestamp comment '删除时间'
);

DROP TABLE IF EXISTS category;
create table category
(
    id          bigint primary key auto_increment,
    name        varchar(50) not null comment '分类名称',
    status      boolean     not null default 1 comment '分类状态，1生效，0不生效',
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
    status      boolean     not null default 1 comment '出售方式状态，1生效，0无效',
    create_time timestamp            default now() comment '创建时间',
    update_time timestamp comment '更新时间'

);
insert sell_mode(name,status)
values ('物品出售',1),
       ('物品出租',0);

--  发货方式表
drop table if exists dispatch_mode;
create table dispatch_mode
(
    id          bigint primary key auto_increment comment '发货方式id',
    name        varchar(50) not null comment '发货方式名字',
    status      boolean     not null default 1 comment '发货方式状态，1生效，0无效',
    create_time timestamp            default now() comment '创建时间',
    update_time timestamp comment '更新时间'
);
insert dispatch_mode(name,status)
values ('快递',0),
       ('自提',1);


-- 商品要求
drop table if exists product_require;
create table product_require
(
    id          bigint primary key auto_increment comment 'id',
    name        varchar(50) not null comment '商品要求名字',
    status      boolean     not null default 1 comment '商品要求状态，1生效，0不生效',
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
    sell_mode_id       bigint  not null,
    dispatch_id        bigint  not null,
    product_require_id bigint  not null,
    status             boolean not null default 1 comment '状态',
    create_time        timestamp        default now() comment '创建时间',
    update_time        timestamp comment '更新时间'
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
    id                 bigint primary key auto_increment comment '商品id',
    category_id        bigint         not null comment '分类id',
    detail             text           not null comment '商品详情',
    images             text           not null comment '商品图片，以逗号分隔',
    current_price      decimal(20, 2) not null comment '商品价格，保留两位小数',
    time_unit          varchar(20) comment '物品出租时间计量单位',
    origin_price       decimal(20, 2)          default 0 comment '商品原价',
    sell_mode_id       bigint         not null comment '出售方式id',
    dispatch_mode_id   bigint         not null comment '发货方式id',
    user_id            bigint         not null comment '发布者id',
    product_require_id varchar(10) comment '商品要求id,以逗号分隔',
    status             int            not null default 1 comment '商品状态，1表示在售，0表示售出，-1表示下架',
    location           varchar(50)    not null comment '商品所在学校定位',
    freight            decimal(20, 2)          default 0 comment '运费',
    address            varchar(50) comment '完整地址',
    create_time        timestamp               default now() comment '创建时间',
    update_time        timestamp comment '更新时间',
    delete_time        timestamp comment '删除时间'

);

-- 收藏夹表
drop table if exists favourite;
create table favourite
(
    id          bigint primary key auto_increment comment '收藏id',
    u_id        bigint not null comment '用户id',
    p_id        bigint not null comment '商品id',
    create_time timestamp default now() comment '创建时间',
    status      int       default 0 comment '状态，0表示未删除，1表示删除'
);

drop table if exists uni_id;
create table uni_id
(
    id        bigint primary key auto_increment comment 'id',
    xh_id     bigint       not null comment '闲货系统用户id',
    uni_id    varchar(200) not null comment 'uni统一登录的用户id',
    uni_token varchar(500) not null comment 'uni统一登录的token'
);
-- 用户聊天关系表
DROP TABLE IF EXISTS chat_user_link;
create table chat_user_link
(
    link_id     bigint primary key auto_increment comment '聊天主表id',
    from_user   bigint not null comment '发送方id',
    to_user     bigint not null comment '接受者id',
    create_time timestamp default now() comment '创建时间'
);

-- 聊天列表表
DROP TABLE IF EXISTS chat_list;
create table chat_list
(
    `list_id`     bigint primary key auto_increment comment '聊天列表主键',
    `link_id`     bigint NOT NULL COMMENT '聊天主表id',
    `from_user`   bigint NOT NULL COMMENT '发送者',
    `to_user`     bigint NOT NULL COMMENT '接收者',
    `from_window` int    NULL DEFAULT NULL COMMENT '发送方是否在窗口',
    `to_window`   int    NULL DEFAULT NULL COMMENT '接收方是否在窗口',
    `unread`      int    NULL DEFAULT NULL COMMENT '未读数',
    `status`      int    NULL DEFAULT NULL COMMENT '是否删除,0未删除，1删除'
);

-- 聊天内容详情表
DROP TABLE IF EXISTS chat_message;
create table chat_message
(
    `message_id` bigint primary key auto_increment COMMENT '聊天内容id',
    `link_id`    bigint       NOT NULL COMMENT '聊天主表id',
    `from_user`  bigint       NOT NULL COMMENT '发送者',
    `to_user`    bigint       NOT NULL COMMENT '接收者',
    `content`    varchar(255) NOT NULL COMMENT '聊天内容',
    `send_time`  timestamp default now() COMMENT '发送时间',
    `type`       int       default 0 COMMENT '消息类型，0表示文本，1表示图片',
    `is_latest`  int       DEFAULT NULL COMMENT '是否为最后一条信息'
);

-- 需求信息表
drop table if exists require_info;
create table require_info
(
    id          bigint primary key auto_increment comment '需求id',
    category_id bigint      not null comment '分类id',
    detail      text        not null comment '需求详情',
    user_id     bigint      not null comment '发布者id',
    status      int         not null default 1 comment '商品状态，1表示需求未解决，-1表示下架',
    location    varchar(50) not null comment '商品所在学校定位',
    school      varchar(50) comment '学校名称',
    create_time timestamp            default now() comment '创建时间'

);
-- 订单表
drop table if exists order_info;
create table order_info
(
    id                bigint primary key auto_increment comment '订单表id',
    order_id          varchar(200) not null comment '订单编号',
    after_service_id bigint comment '售后id',
    platform_flag    integer default 0 comment '能否平台介入标志【商家拒绝过一次即置为1】，0表示不能，1表示行',
    product_detail    text comment '商品描述',
    product_images    text comment '商品图片',
    product_address   varchar(100) comment '商品地址',
    product_category  bigint comment '商品分类',
    buy_id            bigint       not null comment '咸货系统支付者id',
    sell_id           bigint       not null comment '咸货系统出售者id',
    alipay_id         varchar(100) comment '支付宝订单号',
    product_id        bigint       not null comment '商品id',
    type              int            default 1 comment '支付类型，1表示支付宝，2表示微信',
    total             decimal(20, 2) default 0.0 comment '支付金额',
    buyer_sys_id      bigint comment '支付平台购买者用户id',
    buyer_sys_account varchar(100) comment '支付平台购买用户账号',
    status            int            default -1 comment '商品状态，1表示已支付，-1表示未支付,2表示售后处理中，0表示已退款',
    buyer_status      int            default 0 comment '购买者收货状态，1表示确认收货，0表示未确认',
    buyer_receive_time timestamp comment '买家收货时间',
    seller_status     int            default 0 comment '出售者发货状态，1表示已发货，0表示未发货',
    seller_send_time timestamp comment '卖家发货时间',
    create_time       timestamp      default now() comment '创建时间',
    pay_time          timestamp comment '支付时间'

);

-- 管理员表
drop table if exists admin_user;
create table admin_user
(
    id          bigint primary key auto_increment comment '管理员id',
    account     varchar(100) not null comment '管理员账号',
    password    varchar(100) not null comment '管理员密码',
    authority   int       default 0 comment '1表示超级管理员，0表示校园管理员',
    school      varchar(100) comment '学校定位，用于校园管理员',
    create_time timestamp default now() comment '创建时间'
);
insert into admin_user(account,password,authority)
values ('admin','admin',1);

-- 投诉表
drop table if exists complain;
create table complain
(
    id                  bigint primary key auto_increment comment '投诉表主键',
    complainant_id      bigint not null comment '投诉人id',
    complainant_cause   varchar(200) comment '投诉原因',
    complainant_subject bigint not null comment '被投诉的商品或帖子',
    seller_id           bigint not null comment '被投诉人id',
    type                int    not null default 1 comment '投诉类别，1表示商品，0表示求购帖子',
    deal_user           bigint comment '处理人id',
    deal_method         varchar(100) comment '处理方式描述',
    create_time         timestamp       default now() comment '投诉时间',
    deal_time           timestamp comment '处理时间'

) comment '投诉表';


-- 官方通知表
drop table if exists notice;
create table notice
(
    id          bigint primary key auto_increment comment '主键',
    title       varchar(50)  not null comment '通知标题',
    content     varchar(200) not null comment '通知内容',
    receiver_id bigint       not null comment '通知接收者id',
    attach_type integer default 0 comment '类型，0表示帖子，1表示商品',
    attach bigint not null comment '商品id或帖子id',
    publisher bigint not null comment '发布人id',
    create_time timestamp default now() comment '创建时间'
) comment '官方通知';


-- 售后表
drop table if exists after_service;
create table after_service
(
    id                  bigint primary key auto_increment comment '主键',
    buyer_id            bigint not null comment '买家id',
    cause               varchar(200) comment '退货原因',
    product_detail      text comment '商品描述',
    product_price       decimal(20,2) comment '商品金额',
    images              text comment '图片,逗号分隔',
    product_id          bigint not null comment '商品id',
    order_id            bigint not null comment '订单表商家订单id',
    seller_id           bigint not null comment '卖家id',
    seller_status       int       default 0 comment '商家处理状态，0为等待商家处理，1为商家同意，2表示商家不同意，',
    seller_refuse_cause varchar(200) comment '商家拒绝原因',
    seller_deal_time    timestamp comment '商家处理时间',
    platform_status     int       default -1 comment '平台处理状态，-1表示平台未介入，0表示平台介入，1表示处理完成',
    platform_result     varchar(200) comment '平台处理结果',
    platform_user       bigint comment '处理人id',
    status              int       default 0 comment '售后表状态，0表示售后处理中，-1表示售后失败，1表示售后成功，11表示待发货，12表示待收货',
    buyer_send          int default 0 comment '买家发货状态，0表示未发货，1表示已发货',
    buyer_send_time     timestamp comment '买家发货时间',
    seller_receive      int default 0 comment '卖家收货状态，0表示未收货，1表示已收货',
    seller_receive_time timestamp comment '卖家收货时间',
    platform_deal_time  timestamp comment '平台处理时间',
    create_time         timestamp default now() comment '创建时间'
) comment '售后表';
