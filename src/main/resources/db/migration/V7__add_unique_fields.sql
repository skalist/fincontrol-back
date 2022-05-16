alter table users add constraint users_unique_key unique (username);
alter table operation_category add constraint operation_category_unique_key unique (name, user_id);
alter table industry add constraint industry_unique_key unique (name, user_id);
alter table broker_account add constraint broker_account_unique_key unique (name, user_id);
alter table bank_account add constraint bank_account_unique_key unique (name, user_id);
alter table asset add constraint asset_unique_key unique (name, user_id);
