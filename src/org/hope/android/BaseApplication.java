package org.hope.android;

import org.hope.android.utils.API_KEYS;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;
import com.parse.PushService;

public class BaseApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		Parse.initialize(this, API_KEYS.PARSE_APP_ID, API_KEYS.PARSE_CLIENT_KEY); 
		PushService.subscribe(this, "", LoginActivity.class);
		
		
		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
		// Optionally enable public read access.
		// defaultACL.setPublicReadAccess(true);
		ParseACL.setDefaultACL(defaultACL, true);
	}

}
