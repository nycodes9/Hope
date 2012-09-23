package org.hope.android;

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

public class DonateActivity extends FragmentActivity {

	Context mContext;
	
	
	Spinner donateFoodTypeSP;
	String donateFoodTypeSPVal = "";
	
	DatePicker donateExpDateDP;
	DatePicker donatePickUpAfterDP;
	DatePicker donatePickUpBeforeDP;
	
	EditText donateFoodWtET;
	EditText donateAddInfoET;
	
	
	Button donateBtn;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_donate_layout);
        
        mContext = this;
        
        donateAddInfoET = (EditText) findViewById(R.id.donor_donate_pickupAddlnInfoET);
        donateFoodWtET = (EditText) findViewById(R.id.donor_donate_wtET);
        donateExpDateDP = (DatePicker) findViewById(R.id.donor_donate_expDP);
        donatePickUpBeforeDP = (DatePicker) findViewById(R.id.donor_donate_pickupBeforeTP);
        donatePickUpAfterDP = (DatePicker) findViewById(R.id.donor_donate_pickupAfterTP);
        donateFoodTypeSP = (Spinner) findViewById(R.id.donor_donate_foodTypeSP);
        
        donateBtn = (Button) findViewById(R.id.donor_donateBtn);
        donateBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				//prepare data for sending
				ParseObject donation = new ParseObject("contributions");
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
