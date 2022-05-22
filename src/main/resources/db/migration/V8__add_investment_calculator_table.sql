create table investment_calculator
(
    id                            varchar(36)    not null primary key,
    user_id                       varchar(36)    not null,
    start_age                     integer        not null,
    retired_age                   integer        not null,
    investment_return_percent     numeric(6, 3)  not null,
    inflation_percent             numeric(6, 3)  not null,
    expected_salary_now_per_month numeric(15, 3) not null,
    current_accounts_cost         numeric(15, 3) not null
);
