create table expense_type(
    id varchar(36) not null primary key,
    name varchar(255)
);

create table income(
    id varchar(36) not null primary key,
    value numeric(12, 2) not null,
    description varchar(255)
);

create table expense(
    id varchar(36) not null primary key,
    value numeric(12, 2) not null,
    type varchar(36) not null,
    description varchar(255)
)
