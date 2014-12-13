-- enable foreign key supports
PRAGMA foreign_keys = ON;

CREATE TABLE if not exists users (
	id INTEGER PRIMARY KEY AUTOINCREMENT
	, login text UNIQUE NOT NULL
	, password text NOT NULL
);

CREATE TABLE if not exists phone_balance (
	id INTEGER PRIMARY KEY AUTOINCREMENT
	-- create foreign key constrain for column user_id
	, user_id INTEGER UNIQUE NOT NULL REFERENCES users(id)
	, balance NUMBER(20, 2) NOT NULL
);

insert or replace into users(login, password) values ('ivan', 'ivan_pass'); 
insert or replace into users(login, password) values ('petr', 'petr_pass'); 
insert or replace into users(login, password) values ('nik', 'nik_pass'); 


insert or replace into phone_balance(user_id, balance) values (1, 2.00);
insert or replace into phone_balance(user_id, balance) values (2, 2.00);