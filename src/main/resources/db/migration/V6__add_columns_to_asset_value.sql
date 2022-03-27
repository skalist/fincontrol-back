alter table asset_value add column broker_account_id varchar(36) references broker_account(id);
alter table asset_value add column assetsCount integer;
