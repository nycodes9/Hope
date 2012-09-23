package org.hope.android;

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
        
        
        
        
        signupBtn = (Button) findViewById(R.id.login_signupBtn);
        signupBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, SignupActivity.class));
			}
		});
    }

}
