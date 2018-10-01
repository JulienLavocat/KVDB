## KVDB
KVDB is a database, accessible via a REST interface or websockets. Data are are stored on disk, with key-value mapped datas.
Thoses key-value pairs are stored inside of tables.
## Endpoints

    GET /tables/:tableName/
	    Show table informations (content).
	    
	GET /tables/:tableName/get/:key
		Show the value associated with key in table.

	GET /tables/:tableName/put/?key={key}&value={value}
		Put the key-value pair in table. Returns if the value already exists.

	GET /tables/:tableName/exist/:key
		Return true if the requested key exist.
	
	GET /tables/:tableName/delete/:key
		Delete the key-value pair if it exist, return true if it deleted the key.
	
	GET /tables/:tableName/clear
		Clear all the content of the table.

	GET /tables/:tableName/size
		Return the number of key-value pair in table.
## Security
In KVDB, security is based on tokens. For each tables, a Json Web Token (JWT) is generated. This token must be provided in each request to the database as a query-parameter called "token" like this :

    GET /tables/:tableName/clear?token={token}
		
You can get a token (for testing purposes) using the following endpoint :

    GET /token?user={userid}&table={tableName}
User ID must be registered in the users.json file before any usage !


