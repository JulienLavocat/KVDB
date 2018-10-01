package com.gamedb.kvdb;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) throws Exception {

		try {
			Report.start();
			
			Files.start();
			
			Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					
					synchronized (Tables.db) {
						Files.commit();
					}
					
				}
				
			}, 2, 2, TimeUnit.SECONDS);
			
			new Router();
		} catch(Exception e) {
			Report.report(e);
			e.printStackTrace();
		}
	}

}
