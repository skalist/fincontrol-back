create table users
(
    id       varchar(36) not null primary key,
    username varchar(255),
    password varchar(255)
);

create table bank_operation_category
(
    id      varchar(36)  not null primary key,
    user_id varchar(36)  not null,
    name    varchar(255) not null
);

create table bank_account
(
    id      varchar(36)  not null primary key,
    user_id varchar(36)  not null,
    name    varchar(255) not null
);

create table bank_operation
(
    id                         varchar(36)    not null primary key,
    user_id                    varchar(36)    not null,
    bank_operation_category_id varchar(36)    not null references bank_operation_category (id),
    bank_account_id            varchar(36)    not null references bank_account (id),
    type                       varchar(10)    not null,
    date_created               date           not null,
    cost                       numeric(12, 3) not null
);
