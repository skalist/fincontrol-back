create table users
(
    id       varchar(36) not null primary key,
    username varchar(255),
    password varchar(255)
);

create table expense_type
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
