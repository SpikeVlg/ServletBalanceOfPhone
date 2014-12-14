-- enable foreign key supports
PRAGMA foreign_keys = ON;

CREATE TABLE if not exists users (
	id INTEGER PRIMARY KEY AUTOINCREMENT
	, login text UNIQUE NOT NULL
	, hash_password text NOT NULL
);

CREATE TABLE if not exists phone_balance (
	id INTEGER PRIMARY KEY AUTOINCREMENT
	-- create foreign key constrain for column user_id
	, user_id INTEGER UNIQUE NOT NULL REFERENCES users(id)
	, balance NUMBER(20, 2) NOT NULL
);

insert or replace into users(id, login, hash_password) values (1, '79995554433', 'ivan_pass');
insert or replace into users(id, login, hash_password) values (2, '79995554422', 'nik_pass');
insert or replace into users(id, login, hash_password) values (3, '79995554411', 'petr_pass');

insert or replace into phone_balance(user_id, balance) values (1, 200.00);
insert or replace into phone_balance(user_id, balance) values (2, 3000.00);
insert or replace into phone_balance(user_id, balance) values (3, 0.00);