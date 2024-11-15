create table useTestFlyway
(
    id           int auto_increment primary key,
    account_id   varchar(100) null comment '用户id',
    name         varchar(50)  null comment '用户昵称',
    token        char(36)     null comment '用户token',
    gmt_create   bigint       null,
    gmt_modified bigint       null comment '时间戳'
    );