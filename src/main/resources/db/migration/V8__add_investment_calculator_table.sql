create table investment_calculator
(
    id                        varchar(36)    not null primary key,
    user_id                   varchar(36)    not null,
    start_age                 integer        not null,
    retired_age               integer        not null,
    investment_return_percent numeric(4, 3)  not null,
    inflation_percent         numeric(4, 3)  not null,
    expected_salary_per_month numeric(12, 3) not null
);
