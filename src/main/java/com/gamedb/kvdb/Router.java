package com.gamedb.kvdb;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.ArrayList;

import org.eclipse.jetty.websocket.api.CloseStatus;

import com.gamedb.kvdb.http.ClearAnswer;
import com.gamedb.kvdb.http.DatabaseNotFoundAnswer;
import com.gamedb.kvdb.http.DeleteAnswer;
import com.gamedb.kvdb.http.ExistAnswer;
import com.gamedb.kvdb.http.GetAnswer;
import com.gamedb.kvdb.http.ShowAnswer;
import com.gamedb.kvdb.http.PutAnswer;
import com.gamedb.kvdb.http.SizeAnswer;
import com.google.gson.Gson;

import io.javalin.Javalin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.mapdb.DB;

public class Router {

	public static final String KEY = "KVDBSecuruti";
	public static Gson gson = new Gson();
	public static String index;

	public Router() throws IOException {

		Javalin app = Javalin.start(80);

		index = new String(java.nio.file.Files.readAllBytes(Paths.get("public/index.html")));
		
		app.get("/", ctx -> {
			ctx.html(index);
		});
		
		app.get("/tables/", ctx -> {
			
			HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
			
			for(Entry<UUID, DB> entry : Files.dbs.entrySet()) {
				
				ArrayList<String> tables = new ArrayList<String>();
				
				Iterable<String> it = entry.getValue().getAllNames();
				it.forEach(tables::add);
				
				map.put(entry.getKey().toString(), tables);
			}
			
			ctx.json(map);
		});

		app.get("/tables/:table", ctx -> {
			String user = auth(ctx.queryParam("token"), ctx.param("table"));
			if(user == null)
				return;

			ShowAnswer a = Tables.getMap(user, ctx.param("table"));
			if(a.table == null)
				ctx.json(new DatabaseNotFoundAnswer());
			else {
				ctx.json(a);
				Report.reportShowOperation(UUID.fromString(user), a);
			}
				
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
			else {
				ctx.json(a);
				Report.reportGetOperation(UUID.fromString(user), a);
			}
		});

		/*
		 * Put a key and value in table
		 */
		app.get("/tables/:table/put", ctx -> {
			String user = auth(ctx.queryParam("token"), ctx.param("table"));
			if(user == null)
				return;
			
			if(ctx.anyQueryParamNull("key", "value"))
				return;

			PutAnswer a = Tables.put(user, ctx.param("table"), ctx.queryParam("key"), ctx.queryParam("value"));
			if(a.table == null)
				ctx.json(new DatabaseNotFoundAnswer());
			else {
				Report.reportPutOperation(UUID.fromString(user), a);
				ctx.json(a);
			}
			
			
			
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
			else {
				Report.reportExistOperation(UUID.fromString(user), a);
				ctx.json(a);
			}
				

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
			else {
				Report.reportDeleteOperation(UUID.fromString(user), a);
				ctx.json(a);
			}
			
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
			else {
				Report.reportClearOperation(UUID.fromString(user), a);
				ctx.json(a);
			}
				
		});

		app.get("/tables/:table/size", ctx -> {
			String user = auth(ctx.queryParam("token"), ctx.param("table"));
			if(user == null)
				return;

			SizeAnswer a = Tables.size(user, ctx.param("table"));
			if(a.table == null)
				ctx.json(new DatabaseNotFoundAnswer());
			else {
				Report.reportSizeOperation(UUID.fromString(user), a);
				ctx.json(a);
			}
				
		});

		app.get("/token", ctx -> {
			ctx.json(getToken(ctx.queryParam("user"), ctx.queryParam("table")));
		});

		app.ws("/tables/:table", ws -> {

			ws.onConnect(session -> {

				if(session.queryString() == null || session.queryParam("token") == null) {
					session.close(new CloseStatus(4001, "Auth required."));
					session.disconnect();
				}

				String user = auth(session.queryParam("token"), session.param("table"));
				if(user == null) {
					session.close(new CloseStatus(4002, "Invalid authentication."));
					session.disconnect();
				}
				
				session.send("Authenticated on table : " + session.param("table"));
				session.send(user);
			});

			ws.onMessage((session, msg) -> {

				String[] split = msg.split("\\Q§*/:\\E"); //0 = method | 1 = userID
				String table = session.param("table");

				switch(split[0]) {

				case "show":
					ShowAnswer show = Tables.getMap(split[1],table);
					if(show.table == null)
						session.send(gson.toJson(new DatabaseNotFoundAnswer()));
					else
						session.send(gson.toJson(show));
					break;

				case "get":
					GetAnswer get = Tables.get(split[1],table, split[2]);
					if(get.table == null)
						session.send(gson.toJson(new DatabaseNotFoundAnswer()));
					else
						session.send(gson.toJson(get));
					break;

				case "put":
					PutAnswer put = Tables.put(split[1],table, split[2], split[3]);
					if(put.table == null)
						session.send(gson.toJson(new DatabaseNotFoundAnswer()));
					else
						session.send(gson.toJson(put));
					break;

				case "delete":
					DeleteAnswer del = Tables.delete(split[1],table, split[2]);
					if(del.table == null)
						session.send(gson.toJson(new DatabaseNotFoundAnswer()));
					else
						session.send(gson.toJson(del));
					break;

				case "clear":
					ClearAnswer clear = Tables.clear(split[1],table);
					if(clear.table == null)
						session.send(gson.toJson(new DatabaseNotFoundAnswer()));
					else
						session.send(gson.toJson(clear));
					break;

				case "exist":
					ExistAnswer exist = Tables.exist(split[1],table, split[2]);
					if(exist.table == null)
						session.send(gson.toJson(new DatabaseNotFoundAnswer()));
					else
						session.send(gson.toJson(exist));
					break;

				case "size":
					SizeAnswer size = Tables.size(split[1], table);
					if(size.table == null)
						session.send(gson.toJson(new DatabaseNotFoundAnswer()));
					else
						session.send(gson.toJson(size));
					break;

				}

			});

			ws.onClose((session, statusCode, reason) -> System.out.println("Closed"));
			ws.onError((session, throwable) -> System.out.println("Errored"));

		});

	}

	/*
	 * Used to check that the users can access the requested table
	 */
	public String auth(String token, String table) {

		try {
			Claims claims = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
			//System.out.println(claims.getAudience() + "   " + claims.get("user"));
			if(claims.getAudience().equals(table))
				return (String) claims.get("user");
			else
				return null;
		} catch(Exception e) {
			return null;
		}

	}

	public String getToken(String user, String table) {

		return Jwts.builder()
				.signWith(SignatureAlgorithm.HS256, KEY)
				.claim("user", user)
				.setAudience(table)
				//.setId(UUID.randomUUID().toString()) maybe consider using this to revoke token later on ?
				.compact();

	}

}
