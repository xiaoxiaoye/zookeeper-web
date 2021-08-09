drop table if exists `zk_config_info`;
create table zk_config_info
(
    id int auto_increment
        primary key,
    name varchar(255) null,
    description text null,
    url varchar(255) null,
    acl varchar(255) null,
    create_time datetime null,
    update_time datetime null
);