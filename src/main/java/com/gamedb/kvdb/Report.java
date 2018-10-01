package com.gamedb.kvdb;

import io.sentry.Sentry;

public class Report {

	public static void start() {
		
		Sentry.init("https://4582e2643292402ea595c57b8ee55901@sentry.io/1291761");
		
	}
	
	public static void report(Exception e) {
		Sentry.capture(e);
	}
	
}
