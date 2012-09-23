package org.hope.android;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class SignupActivity extends FragmentActivity {

	public static final String TAG = "SignupActivity";
	
	Spinner signupTypeSP;
	String signupTypeSPVal = "";
	
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
        signupLocET = (EditText) findViewById(R.id.signup_ac_locationET);
        signupEmailIB = (ImageButton) findViewById(R.id.signup_ac_emailIB);
        signupTypeSP = (Spinner) findViewById(R.id.signup_typeSP);
        signupPhoneET = (EditText) findViewById(R.id.signup_ac_phoneET);
        
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
        
        signupTypeSPVal = signupTypeSP.getItemAtPosition(0).toString();
        //signupTypeSPVal = "Donor";
        
        signupTypeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
        {
        	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
        	{
               signupTypeSPVal  = parent.getItemAtPosition(pos).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {}
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
				
				//make sure fields aren't blank
				if (signupEmailET.toString().trim().length() > 0 &&
						signupLocET.toString().trim().length() > 0 &&
						signupPhoneET.toString().trim().length() > 0) 
				{
					//create a new user with the credentials given
					ParseUser newUser = new ParseUser();
					newUser.setUsername(signupEmailET.getText().toString());
					newUser.setPassword("helloworld");
					newUser.setEmail(signupEmailET.getText().toString());
					newUser.put("address", signupLocET.getText().toString());
					newUser.put("User_Phone", signupPhoneET.getText().toString());
					
					if (signupTypeSPVal.equals("Donor")) 
					{
						newUser.put("isDonor", true);
					}
					else 
					{
						newUser.put("isDonor", false);
					}
					
					
					newUser.signUpInBackground(new SignUpCallback() 
					{
						public void done(ParseException e) 
						{
							//no exception means the signup succeeded.
							if (e == null) 
							{
								startActivity(new Intent(mContext, DonateActivity.class));
							}
							//something went wrong. print error message
							else 
							{
								
							}
						}
					});
					
					
				}
				//one of the fields are blank so output error message
				else 
				{
					AlertDialog.Builder alertDialog;
					alertDialog = new AlertDialog.Builder(mContext);
					alertDialog.setPositiveButton("Ok",
						    new DialogInterface.OnClickListener() {
						        public void onClick(DialogInterface dialog, int which) {
						           
						        }
						    });
					alertDialog.setTitle("Error");
					alertDialog.setMessage("something went wrong!");
					alertDialog.create().show();
				}
				
				
				 
				
				
				
				
				
			}
		});
        
    }

}
