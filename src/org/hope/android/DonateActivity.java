package org.hope.android;

import java.util.Date;

import com.parse.ParseObject;
import com.parse.ParseUser;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

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
        
        donateBtn = (Button) findViewById(R.id.donor_donateBtn);
        donateBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				
				String foodName = donateFoodNameET.getText().toString();
				String additionalInfo = donateAddInfoET.getText().toString();
				
				int foodWeight = 0;
				if (!donateFoodWtET.getText().toString().contains("-")) 
				{
					foodWeight = Integer.parseInt(donateFoodWtET.getText().toString().trim());
				}
				//Date date = donateExpDateDP.
				
				
				//prepare data for sending
				ParseObject donation = new ParseObject("contributions");
				donation.put("food_name", "");
				donation.put("food_type", "");
				donation.put("expire_date", "");
				donation.put("food_weight", "");
				
				donation.put("charity_name", "");
				donation.put("charity_address", "");
				donation.put("charity_phone", "");
				
				donation.put("donor_name", "");
				donation.put("donor_address", "");
				donation.put("donor_phone", "");
				donation.put("available_start_time", "");
				donation.put("available_end_time", "");
				donation.put("additional_info", "");
				
				donation.saveInBackground();
				//donation.put
				
				
				
				
				startActivity(new Intent(mContext, CharityDonationsListActivity.class));
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
               return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
