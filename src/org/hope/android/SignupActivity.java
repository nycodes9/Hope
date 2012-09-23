package org.hope.android;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils.StringSplitter;
import android.text.style.CharacterStyle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class SignupActivity extends FragmentActivity {

	public static final String TAG = "SignupActivity";
	
	Spinner signupTypeSP;
	
	EditText signupEmailET;
	ImageButton signupEmailIB;
	
	EditText signupLocET;
	ImageButton signupLocIB;
	
	EditText signupPhoneET;
	ImageButton signupPhoneIB;
	
	Button signupCreateBtn;
	
	Context mContext;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layoutv2);
        
        mContext = this;
        
        signupEmailET = (EditText) findViewById(R.id.signup_ac_emailET);
        
        signupEmailIB = (ImageButton) findViewById(R.id.signup_ac_emailIB);
        signupEmailIB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
				Account[] accounts = AccountManager.get(mContext).getAccounts();
				
				final ArrayList<String> emails = new ArrayList<String>();
				for (Account account : accounts) {
				    if (emailPattern.matcher(account.name).matches()) {
				    	emails.add(account.name);
				    }
				}
				
				final CharSequence[] items = emails.toArray(new CharSequence[emails.size()]);
				
				new AlertDialog.Builder(mContext)
				.setTitle("Pick an email acount")
				.setItems(items, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				    	signupEmailET.setText(items[item]);
				    }
				})
				.create()
				.show();
			}
		});
        
        
        
        signupPhoneET = (EditText) findViewById(R.id.signup_ac_phoneET);
        
        signupPhoneIB = (ImageButton) findViewById(R.id.signup_ac_phoneIB);
        signupPhoneIB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String phoneDigits = ((TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE))
						.getLine1Number();
			
				signupPhoneET.setText(phoneDigits != null ? phoneDigits : "");
				  
			}
		});
        
        
        signupCreateBtn = (Button) findViewById(R.id.signup_ac_createBtn);
        signupCreateBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, DonateActivity.class));
			}
		});
        
    }

}
