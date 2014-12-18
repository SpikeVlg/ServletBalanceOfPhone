CREATE TABLE users (
	id INT PRIMARY KEY auto_increment
	, login VARCHAR(255) UNIQUE NOT NULL
	, hash_password VARCHAR(255) NOT NULL
);

CREATE TABLE phone_balance (
	id INT PRIMARY KEY auto_increment
	-- create foreign key constrain for column user_id
	, user_id INT UNIQUE NOT NULL REFERENCES users(id)
	, balance NUMBER(20, 2) NOT NULL
);


insert into users(id, login, hash_password) values (1, '79995554433', 'ivan_pass');
insert into users(id, login, hash_password) values (2, '79995554422', 'nik_pass');
insert into users(id, login, hash_password) values (3, '79995554411', 'petr_pass');

insert into phone_balance(user_id, balance) values (1, 200.00);
insert into phone_balance(user_id, balance) values (2, 3000.00);
insert into phone_balance(user_id, balance) values (3, 0.00);