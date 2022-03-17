create table industry
(
    id      varchar(36)  not null primary key,
    user_id varchar(36)  not null,
    name    varchar(255) not null
);

create table asset
(
    id          varchar(36)  not null primary key,
    user_id     varchar(36)  not null,
    name        varchar(255) not null,
    type        varchar(255) not null,
    currency    varchar(255) not null,
    code        varchar(255),
    description varchar(1024),
    industry_id varchar(36) references industry (id)
);

create table asset_value
(
    id           varchar(36)    not null primary key,
    user_id      varchar(36)    not null,
    asset_id     varchar(36)    not null references asset (id),
    event        varchar(255)   not null,
    date_created date           not null,
    cost         numeric(12, 3) not null
);
