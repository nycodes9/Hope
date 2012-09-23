package org.hope.android;

import java.util.List;

import org.hope.android.utils.API_KEYS;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseObject;
import com.parse.PushService;
import com.parse.ParseBroadcastReceiver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends FragmentActivity {

	EditText userEmail;
	EditText userPwd;
	Button loginBtn;
	Button signupBtn;
	Context mContext;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        mContext = this;
        
        //initialize Parse
        Parse.initialize(this, API_KEYS.PARSE_APP_ID, API_KEYS.PARSE_CLIENT_KEY); 
        PushService.subscribe(this, "", LoginActivity.class);
        
        //instantiate variables
        userEmail = (EditText) findViewById(R.id.login_emailET);
		userPwd = (EditText) findViewById(R.id.login_pwdET);
		
        
        //Do an initial check to see if the user is already signed in
        //if he is, just redirect to the 'logged in' page
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) 
        {
        	if (currentUser.getBoolean("isDonor")) 
        	{
        		startActivityFromLogin(DonateActivity.class);
        	}
        	else 
        	{
        		startActivityFromLogin(CharityDonationsListActivity.class);
        	}
        }
        
        
        signupBtn = (Button) findViewById(R.id.login_signupBtn);
        signupBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivityFromLogin(SignupActivity.class);
			}
		});
        
        loginBtn = (Button) findViewById(R.id.login_loginBtn);
        loginBtn.setOnClickListener(new OnClickListener() 
        {
        	@Override
        	public void onClick(View v) 
        	{
        		
        		
        		if (userEmail.getText().toString().trim().length() > 0 &&
        				userPwd.getText().toString().length() > 0) 
        		{
        			//Try to authenticate in background with Parse
            		ParseUser.logInInBackground(userEmail.getText().toString(), userPwd.getText().toString(), new LogInCallback() 
            		{
            			public void done(ParseUser user, ParseException e) 
            			{
            				//if something is returned, then authentication was successful
            				if (user != null) 
            				{
            					ParseQuery query = ParseUser.getQuery();
            					query.whereEqualTo("isDonor", true).whereEqualTo("username", user.getUsername());
            					query.findInBackground(new FindCallback() 
            					{
            						public void done (List<ParseObject> objects,  ParseException e) 
            						{
            							//if query didn't fail and it gave results
            							//then the user WAS a donor so redirect to
            							if (e == null && objects.size() > 0) 
            							{
            								startActivityFromLogin(DonateActivity.class);
            							}
            							//if the query didn't return anything, then the user
            							//was a business
            							else if (e == null && objects.size() == 0) 
            							{
            								startActivityFromLogin(CharityDonationsListActivity.class);
            								((Activity) mContext).finish();
            							}
            							//something went wrong with the query.
            							else 
            							{
            								
            							}
            						}
            					});
            				}
            				//if not, display error message to user
            				else 
            				{
            					
            				}
            			}
            		} );
        		}
        		
        		
        		
        	}
        });
    }
    
    //created so that they can't just come back to the login page
    //without explicitely logging out.
    public void startActivityFromLogin(Class<?> cls) 
    {
    	startActivity(new Intent(mContext, cls));
    	((Activity) mContext).finish();
    }
}
