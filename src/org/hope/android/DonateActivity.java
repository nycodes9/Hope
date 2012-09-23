package org.hope.android;

import java.util.Date;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class DonateActivity extends FragmentActivity {

	Context mContext;
	
	
	Spinner donateFoodTypeSP;
	String donateFoodTypeSPVal = "";
	
	DatePicker donateExpDateDP;
	DatePicker donatePickUpDP;
	TimePicker donatePickUpAfterDP;
	TimePicker donatePickUpBeforeDP;
	
	EditText donateFoodWtET;
	EditText donateAddInfoET;
	EditText donateFoodNameET;
	
	Button donateBtn;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_donate_layout);
        
        mContext = this;
        
        donateFoodNameET = (EditText) findViewById(R.id.donor_donate_foodNameET);
        donateAddInfoET = (EditText) findViewById(R.id.donor_donate_pickupAddlnInfoET);
        donateFoodWtET = (EditText) findViewById(R.id.donor_donate_wtET);
        donateExpDateDP = (DatePicker) findViewById(R.id.donor_donate_expDP);
        donatePickUpDP = (DatePicker) findViewById(R.id.donor_donate_pickupDP);
        donatePickUpBeforeDP = (TimePicker) findViewById(R.id.donor_donate_pickupBeforeTP);
        donatePickUpAfterDP = (TimePicker) findViewById(R.id.donor_donate_pickupAfterTP);
        donateFoodTypeSP = (Spinner) findViewById(R.id.donor_donate_foodTypeSP);
        
        
        
        
		donateFoodTypeSPVal = donateFoodTypeSP.getItemAtPosition(0).toString();
		
		donateFoodTypeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				donateFoodTypeSPVal = parent.getItemAtPosition(pos).toString();
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
        
        
        
        donateBtn = (Button) findViewById(R.id.donor_donateBtn);
        donateBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) 
			{
				
				String foodName = donateFoodNameET.getText().toString();
				String additionalInfo = donateAddInfoET.getText().toString();
				
				int foodWeight = 0;
				if (!donateFoodWtET.getText().toString().contains("-") && donateFoodWtET.getText().toString().trim().length() != 0) 
				{
					foodWeight = Integer.parseInt(donateFoodWtET.getText().toString().trim());
				}
				
				
				//subtracting by 1900 as there is a weird calculation bug. Will fix later.
				Date expireDate = new Date(donateExpDateDP.getYear() - 1900, 
						donateExpDateDP.getMonth(), 
						donateExpDateDP.getDayOfMonth());
				Date pickupTimeBefore = new Date(donatePickUpDP.getYear() - 1900,
						donatePickUpDP.getMonth(),
						donatePickUpDP.getDayOfMonth(),
						donatePickUpBeforeDP.getCurrentHour(),
						donatePickUpBeforeDP.getCurrentMinute());
				Date pickupTimeAfter = new Date(donatePickUpDP.getYear() - 1900,
						donatePickUpDP.getMonth(),
						donatePickUpDP.getDayOfMonth(),
						donatePickUpAfterDP.getCurrentHour(),
						donatePickUpAfterDP.getCurrentMinute());
				
				
				
				ParseUser currentUser = ParseUser.getCurrentUser();
		        
				//if we get null, then something has gone wrong because
				//the user SHOULD be signed in.
				if (currentUser == null) 
		        {
		        	//print error message
					return;
		        }
				
				//a stub number is saved in the event the phone number can't be parsed correctly
				//this is just for testing
				long phoneNumber = 1234567890;
				
				try {
					phoneNumber = Long.parseLong(currentUser.getString("User_Phone"));
				}
				catch (NumberFormatException e) 
				{}
				
				//prepare data for sending
				ParseObject donation = new ParseObject("contributions");
				donation.put("food_name", foodName);
				donation.put("food_type", donateFoodTypeSPVal);
				donation.put("expire_date", expireDate);
				donation.put("food_weight", foodWeight);
				
				//donation.put("charity_name", "");
				//donation.put("charity_address", "");
				//donation.put("charity_phone", "");
				
				donation.put("donor_name", currentUser.getUsername());
				donation.put("donor_address", currentUser.getString("address"));
				donation.put("donor_phone", phoneNumber);
				donation.put("donor_email", currentUser.getString("email"));
				donation.put("available_start_time", pickupTimeBefore);
				donation.put("available_end_time", pickupTimeAfter);
				donation.put("additional_info", additionalInfo);
				
				
				//generate a quick toast message
				Context toastContext = getApplicationContext();
				CharSequence toastText = "Putting up your food up for donation...";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(toastContext, toastText, duration);
				toast.show();
				
				donation.saveInBackground(new SaveCallback() 
				{
					@Override
					public void done(ParseException e) 
					{
						Context finishedToastContext = getApplicationContext();
						int duration = Toast.LENGTH_SHORT;
						Toast toast;
						
						if (e == null) 
						{
							CharSequence finishedToastText = "Successfully put food up for donation!";
							toast = Toast.makeText(finishedToastContext, finishedToastText, duration);
							toast.show();
						}
						else 
						{
							CharSequence finishedToastText = "FAILED. Error:" + e.getMessage();
							duration = Toast.LENGTH_LONG;
							toast = Toast.makeText(finishedToastContext, finishedToastText, duration);
							toast.show();
						}
						
					}
					
				});
				
				
				
			}
		});
        
        
        
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_layout, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_settings:
               Log.i(this.getClass().getName(), "Logout presssed");
               
               //logging out of the current user and then setting currentUser to null
               ParseUser.logOut();
               ParseUser currentUser = ParseUser.getCurrentUser();
               startActivity(new Intent(mContext, LoginActivity.class));
               ((Activity) mContext).finish();
               
               return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
