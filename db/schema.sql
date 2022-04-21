create table config
(
    `name`  varchar(255),
    `value` varchar(255)
);

create table user
(
    id         varchar(255) not null primary key,
    is_enabled bit null,
    password   varchar(255) not null,
    user_type  int null,
    username   varchar(255) not null
);