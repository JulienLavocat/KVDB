package com.gamedb.kvdb;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.mapdb.DB;

import com.gamedb.kvdb.http.PutAnswer;
import com.gamedb.kvdb.reporting.PutOperationReport;
import com.google.gson.Gson;

public class Main {

	public static void main(String[] args) throws Exception {

		try {
			Report.start();

			Files.start();

			Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {

					synchronized (Files.dbs) {
						for(DB db : Files.dbs.values()) {
							db.commit();
							//System.out.println("Commiting: " + db.getStore().toString());
						}
					}

				}

			}, 4, 2, TimeUnit.SECONDS);

			new Router();
		} catch(Exception e) {
			Report.report(e);
			e.printStackTrace();
		}
		
	}

}
