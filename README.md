ServletBalanceOfPhone
===========

A simple servlet application "Balance of phone" on spring boot.

# Instructions
## Creating DB
Application use [SQLite3](http://www.sqlite.org/) (tested with version 3.8.7.4)
or [H2](http://www.h2database.com/html/main.html) (tested with version 1.4.183).

For switching between databases need to edit connection string in
file ```resources/application.properties```.

### SQLite
Default path is root folder - ```./ServletBalanceOfPhone/phone_of_balance.db```

For create database use script (scripts/create_sqlite_db.sql). Example:

```
sqlite3 phone_of_balance.db  < scripts/create_sqlite_db.sql
```

Warning: SQLite not supported hibernate.

### H2

For create database use script (scripts/create_h2_db.sql) in h2 database console.

## DAO

Application supports two DAO implementations:

1. Spring JDBC Template (PhoneServiceDAOJdbcTemplate)
2. Hibernate (PhoneServiceDAOHibernate) - Warning: **SQLite not supported hibernate**

For switching between DAO implementations need to edit file **MainController.java**

```java
@Autowired
public MainController(@Qualifier("hibernate") PhoneServiceDAO phoneServiceDAO, DataValidator dataValidator
		, PasswordEncoder passwordEncoder){
	....
}
```

On

```java
@Autowired
public MainController(@Qualifier("jdbctemplate") PhoneServiceDAO phoneServiceDAO, DataValidator dataValidator
		, PasswordEncoder passwordEncoder){
	....
}
```

## Run application
Two ways:

1. Run servlet local with command ```gradle bootRun``` and get servlet by url
[http://localhost:8080/balance_of_phone]((http://localhost:8080/balance_of_phone)).
2. Run ```gradle war``` and deploy war-file in servlet container.

## Testing

For testing can use chrome plugin [Postman](http://www.getpostman.com/).

For work need to set next headers:

|Header      |Value                        |
|------------|-----------------------------|
|Content-Type|application/xml;charset=UTF-8|
|Accept      |application/xml              |

Default url "/phone_of_balance".
If you run gradle task

### Request examples
#### Adding new user

```xml
<?xml version="1.0" encoding="utf-8"?>
<request>
	<request-type>new-agt</request-type>
	<login>79554443322</login>
	<password>someStrongPassword5%</password>
</request>
```

Get next result.

```xml
<?xml version="1.0" encoding="utf-8"?>
<response>
	<result-code>0</result-code>
</response>
```

#### Get user balance

```xml
<?xml version="1.0" encoding="utf-8"?>
<request>
	<request-type>agt-bal</request-type>
	<login>79554443322</login>
	<password>someStrongPassword5%</password>
</request>
```

Get next result.

```xml
<?xml version="1.0" encoding="utf-8"?>
<response>
	<result-code>0</result-code>
	<bal>100.00</bal>
</response>
```
### Response codes

|Code|Value|
|---:|-----|
|0|OK|
|1|User already exists|
|2|Invalid login|
|3|Weak password|
|4|Unknown action|
|5|Unknown error|
|6|Authentication error|
|7|User not found|

### Environments
Tested on next environment:

1. JDK 1.8.25
2. Gradle 2.2
3. SQLite 3.8.7.4
4. H2 1.4.183