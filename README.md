ServletBalanceOfPhone
===========

A simple servlet application.

# Instructions
## Creating DB
Applications using [SQLite3](http://www.sqlite.org/) (tested with version 3.8.7.4).

Default path is root folder - ```./ServletBalanceOfPhone/phone_of_balance.db```

For create database use script (scripts/create_sqlite_db.sql). Example:

```
sqlite3 phone_of_balance.db  < scripts/create_sqlite_db.sql
```

## Run application
Two ways exists:

1. Run servlet local with command ```gradle bootRun``` and we get servlet by url
[http://localhost:8080/balance_of_phone]((http://localhost:8080/balance_of_phone)).
2. Run ```gradle war``` and deploy war-file in servlet container.

## Testing

For testing we can use chrome plagin [Postman](http://www.getpostman.com/).

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
Tested on next envirenment:

1. JDK 1.8.25
2. Gradle 2.2
3. SQLite 3.8.7.4