create table broker_account
(
    id      varchar(36)  not null primary key,
    user_id varchar(36)  not null,
    name    varchar(255) not null
);
