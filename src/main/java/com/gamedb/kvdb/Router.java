package com.gamedb.kvdb;

import com.gamedb.kvdb.http.ClearAnswer;
import com.gamedb.kvdb.http.DatabaseNotFoundAnswer;
import com.gamedb.kvdb.http.DeleteAnswer;
import com.gamedb.kvdb.http.ExistAnswer;
import com.gamedb.kvdb.http.GetAnswer;
import com.gamedb.kvdb.http.GetMapAnswer;
import com.gamedb.kvdb.http.PutAnswer;
import com.gamedb.kvdb.http.SizeAnswer;

import io.javalin.Javalin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class Router {

	public static final String KEY = "KVDBSecuruti";
	
	public Router() {

		Javalin app = Javalin.start(80);

		app.get("/tables/", ctx -> {
			ctx.json(Tables.db.getAllNames());
		});
		
		app.get("/tables/:table", ctx -> {
			String user = auth(ctx.queryParam("token"), ctx.param("table"));
			if(user == null)
				return;
			
			GetMapAnswer a = Tables.getMap(user, ctx.param("table"));
			if(a.table == null)
				ctx.json(new DatabaseNotFoundAnswer());
			else
				ctx.json(a);
		});
		
		/*
		 * Get value for specified key and table
		 */
		app.get("/tables/:table/get/:key", ctx -> {
			String user = auth(ctx.queryParam("token"), ctx.param("table"));
			if(user == null)
				return;
			
			GetAnswer a = Tables.get(user, ctx.param("table"), ctx.param("key"));
			
			if(a.table == null)
				ctx.json(new DatabaseNotFoundAnswer());
			else
				ctx.json(a);

		});

		/*
		 * Put a key and value in table
		 */
		app.get("/tables/:table/put", ctx -> {
			String user = auth(ctx.queryParam("token"), ctx.param("table"));
			if(user == null)
				return;
			
			PutAnswer a = Tables.put(user, ctx.param("table"), ctx.queryParam("key"), ctx.queryParam("value"));
			if(a.table == null)
				ctx.json(new DatabaseNotFoundAnswer());
			else
				ctx.json(a);

		});

		/*
		 * Return true if specified value exist in db
		 */
		app.get("/tables/:table/exist/:key", ctx -> {
			String user = auth(ctx.queryParam("token"), ctx.param("table"));
			if(user == null)
				return;
			
			ExistAnswer a = Tables.exist(user, ctx.param("table"), ctx.param("key"));
			if(a.table == null)
				ctx.json(new DatabaseNotFoundAnswer());
			else
				ctx.json(a);

		});
		
		/*
		 * Delete the specified key from table
		 */
		app.get("/tables/:table/delete/:key", ctx -> {
			String user = auth(ctx.queryParam("token"), ctx.param("table"));
			if(user == null)
				return;
			
			DeleteAnswer a = Tables.delete(user, ctx.param("table"), ctx.param("key"));
			if(a.table == null)
				ctx.json(new DatabaseNotFoundAnswer());
			else
				ctx.json(a);
		});
		
		/*
		 * Drop the specified table
		 */
		app.get("/tables/:table/clear", ctx -> {
			String user = auth(ctx.queryParam("token"), ctx.param("table"));
			if(user == null)
				return;
			
			ClearAnswer a = Tables.clear(user, ctx.param("table"));
			if(a.table == null)
				ctx.json(new DatabaseNotFoundAnswer());
			else
				ctx.json(a);
		});
		
		app.get("/tables/:table/size", ctx -> {
			String user = auth(ctx.queryParam("token"), ctx.param("table"));
			if(user == null)
				return;
			
			SizeAnswer a = Tables.size(user, ctx.param("table"));
			if(a.table == null)
				ctx.json(new DatabaseNotFoundAnswer());
			else
				ctx.json(a);
		});
		
		app.get("/token", ctx -> {
			
			String jwt = Jwts.builder()
					.signWith(SignatureAlgorithm.HS256, KEY)
					.claim("user", ctx.queryParam("user"))
					.setAudience(ctx.queryParam("table"))
					//.setId(UUID.randomUUID().toString()) maybe consider using this to revoke token later on ?
					.compact();
			ctx.json(jwt);
			
		});
		
	}
	
	/*
	 * Used to check that the users can access the requested table
	 */
	public String auth(String token, String table) {
		
		try {
			Claims claims = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
			if(claims.getAudience().equals(table))
				return (String) claims.get("user");
			else
				return null;
		} catch(Exception e) {
			return null;
		}
		
	}
	
}
