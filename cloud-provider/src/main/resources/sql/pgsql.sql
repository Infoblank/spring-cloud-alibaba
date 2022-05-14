create table rm_pm_user
(
    id           int8         default nextval('sys_user_id_seq'::regclass) not null,
    login_name   varchar(64)                                               not null,
    name         varchar(64)                                               not null,
    password     varchar(255)                                              not null,
    create_time  timestamp(6) default now(),
    modify_time  timestamp(6) default now(),
    login_state  int2,
    deleted_flag int2,
    login_time   timestamp(6) default now()
);

comment on column rm_pm_user.login_name is '登录名';

comment on column rm_pm_user.name is '姓名';

comment on column rm_pm_user.password is '密码';

comment on column rm_pm_user.create_time is '创建时间';

comment on column rm_pm_user.modify_time is '修改时间';

comment on column rm_pm_user.login_state is '登录状态';

comment on column rm_pm_user.deleted_flag is '删除标记';

comment on column rm_pm_user.login_time is '最后登录时间';