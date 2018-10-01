package com.gamedb.kvdb;

import java.util.UUID;

import io.javalin.Javalin;
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
			
			if(!auth(ctx.queryParam("token"), ctx.param("table")))
				return;
			
			ctx.json(Tables.getMap(ctx.param("table")));
		});
		
		/*
		 * Get value for specified key and table
		 */
		app.get("/tables/:table/get/:key", ctx -> {
			
			if(!auth(ctx.queryParam("token"), ctx.param("table")))
				return;
			
			ctx.json(Tables.get(ctx.param("table"), ctx.param("key")));

		});

		/*
		 * Put a key and value in table
		 */
		app.get("/tables/:table/put", ctx -> {

			if(!auth(ctx.queryParam("token"), ctx.param("table")))
				return;
			
			ctx.json(Tables.put(ctx.param("table"), ctx.queryParam("key"), ctx.queryParam("value")));

		});

		/*
		 * Return true if specified value exist in db
		 */
		app.get("/tables/:table/exist/:key", ctx -> {

			if(!auth(ctx.queryParam("token"), ctx.param("table")))
				return;
			
			ctx.json(Tables.exist(ctx.param("table"), ctx.param("key")));

		});
		
		/*
		 * Delete the specified key from table
		 */
		app.get("/tables/:table/delete/:key", ctx -> {
			
			if(!auth(ctx.queryParam("token"), ctx.param("table")))
				return;
			
			ctx.json(Tables.delete(ctx.param("table"), ctx.param("key")));
		});
		
		/*
		 * Drop the specified table
		 */
		app.get("/tables/:table/clear", ctx -> {

			if(!auth(ctx.queryParam("token"), ctx.param("table")))
				return;
			
			ctx.json(Tables.clear(ctx.param("table")));
			
		});
		
		app.get("/tables/:table/size", ctx -> {
			
			if(!auth(ctx.queryParam("token"), ctx.param("table")))
				return;
			
			ctx.json(Tables.size(ctx.param("table")));
		});
		
		app.get("/token", ctx -> {
			
			String jwt = Jwts.builder()
					.signWith(SignatureAlgorithm.HS256, KEY)
					.claim("from", "julien")
					.setAudience(ctx.queryParam("table"))
					//.setId(UUID.randomUUID().toString()) maybe consider using this to revoke token later on ?
					.compact();
			ctx.json(jwt);
			
		});
		
	}
	
	/*
	 * Used to check that the users can access the requested table
	 */
	public boolean auth(String token, String table) {
		
		try {
			String access = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody().getAudience();
			if(access.equals(table))
				return true;
			else
				return false;
		} catch(Exception e) {
			return false;
		}
		
	}
	
}
