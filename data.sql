create table users
(
    id            bigint primary key auto_increment,
    name          varchar(100) not null comment '用户昵称',
    phone         varchar(50)  not null comment '电话',
    password      varchar(50)  not null comment '密码',
    school_id     int          not null comment '学校id',
    avatar        text default 'default.jpg' comment '用户头像',
    email         varchar(50)  comment '邮箱地址',
    identity_card varchar(50)  comment '身份证',
    stu_number    varchar(50) comment '学生学号',
    grade         varchar(50) comment '班级',
    faculty       varchar(100) comment '学院',
    major         varchar(100) comment '专业',
    birthday      timestamp comment '生日',
    score         int          comment '信誉分',
    created_at timestamp default now() comment '创建时间',
    deleted_at timestamp comment '删除时间'
);


