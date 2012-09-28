package org.hope.android;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
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

	public static final String TAG = "DonateActivity";

	Context mContext;
	
	Spinner donateFoodTypeSP;
	String donateFoodTypeSPVal = "";
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
	SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
	
	Button donateExpDateBtn;			Date expirationDate = new Date();
	Button donatePickUpBtn;			Date pickupDate = new Date();
	Button donatePickUpAfterBtn;		Date pickupAfterDate = new Date();
	Button donatePickUpBeforeBtn;	Date pickupBeforeDate = new Date();
	
	EditText donateFoodWtET;
	EditText donateFoodNameET;
	
	Button donateBtn;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_donate_layout);
        
        mContext = this;
        
        donateFoodNameET = (EditText) findViewById(R.id.donor_donate_foodNameET);
        donateFoodWtET = (EditText) findViewById(R.id.donor_donate_wtET);
        donateExpDateBtn = (Button) findViewById(R.id.donor_donate_expDateBtn);
        donatePickUpBtn = (Button) findViewById(R.id.donor_donate_pickupDateBtn);
        donatePickUpBeforeBtn = (Button) findViewById(R.id.donor_donate_pickupBeforeTimeBtn);
        donatePickUpAfterBtn = (Button) findViewById(R.id.donor_donate_pickupAfterTimeBtn);
        donateFoodTypeSP = (Spinner) findViewById(R.id.donor_donate_foodTypeSP);
        
        donateExpDateBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				final Calendar c = Calendar.getInstance();
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH);
				int day = c.get(Calendar.DAY_OF_MONTH);
				
				new DatePickerDialog(mContext, new OnDateSetListener() {
					
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						
						Calendar cal = Calendar.getInstance();
						cal.set(monthOfYear, monthOfYear, dayOfMonth, 24, 59);
						
						expirationDate = cal.getTime();
						donateExpDateBtn.setText(dateFormat.format(expirationDate));
					}
				}, year, month, day).show();
			}
		});
        
        donatePickUpBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				final Calendar c = Calendar.getInstance();
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH);
				int day = c.get(Calendar.DAY_OF_MONTH);
				
				new DatePickerDialog(mContext, new OnDateSetListener() {
					
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						
						Calendar cal = Calendar.getInstance();
						cal.set(monthOfYear, monthOfYear, dayOfMonth, 24, 59);
						
						pickupDate = cal.getTime();
						donatePickUpBtn.setText(dateFormat.format(pickupDate));
					}
				}, year, month, day).show();
			}
		});
        
        donatePickUpBeforeBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				final Calendar c = Calendar.getInstance();
				c.setTime(pickupDate);
				
				new TimePickerDialog(mContext, new OnTimeSetListener() {
					
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						c.set(Calendar.HOUR_OF_DAY, hourOfDay);
						c.set(Calendar.MINUTE, minute);
						
						pickupBeforeDate = c.getTime();
						
						donatePickUpBeforeBtn.setText(timeFormat.format(pickupBeforeDate));
					}
				}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
				
			}
		});
        
        donatePickUpAfterBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				final Calendar c = Calendar.getInstance();
				c.setTime(pickupDate);
				
				new TimePickerDialog(mContext, new OnTimeSetListener() {
					
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						c.set(Calendar.HOUR_OF_DAY, hourOfDay);
						c.set(Calendar.MINUTE, minute);
						
						pickupAfterDate = c.getTime();
						donatePickUpAfterBtn.setText(timeFormat.format(pickupAfterDate));
					}
				}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
				
			}
		});
        
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
				
				int foodWeight = 0;
				if (!donateFoodWtET.getText().toString().contains("-") && donateFoodWtET.getText().toString().trim().length() != 0) { 
					foodWeight = Integer.parseInt(donateFoodWtET.getText().toString().trim());
				}
				
				
				//subtracting by 1900 as there is a weird calculation bug. Will fix later.
				/*Date expireDate = new Date(donateExpDateBtn.getYear() - 1900, 
						donateExpDateBtn.getMonth(), 
						donateExpDateBtn.getDayOfMonth());
				Date pickupTimeBefore = new Date(donatePickUpBtn.getYear() - 1900,
						donatePickUpBtn.getMonth(),
						donatePickUpBtn.getDayOfMonth(),
						donatePickUpBeforeBtn.getCurrentHour(),
						donatePickUpBeforeBtn.getCurrentMinute());
				Date pickupTimeAfter = new Date(donatePickUpBtn.getYear() - 1900,
						donatePickUpBtn.getMonth(),
						donatePickUpBtn.getDayOfMonth(),
						donatePickUpAfterBtn.getCurrentHour(),
						donatePickUpAfterBtn.getCurrentMinute());*/
				
				ParseUser currentUser = ParseUser.getCurrentUser();
		        
				//if we get null, then something has gone wrong because
				//the user SHOULD be signed in.
				if (currentUser == null) {
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
				donation.put("expire_date", expirationDate);
				donation.put("food_weight", foodWeight);
				
				//donation.put("charity_name", "");
				//donation.put("charity_address", "");
				//donation.put("charity_phone", "");
				
				donation.put("donor_name", currentUser.getUsername());
				donation.put("donor_address", currentUser.getString("address"));
				donation.put("donor_phone", phoneNumber);
				donation.put("donor_email", currentUser.getString("email"));
				donation.put("available_start_time", pickupAfterDate);
				donation.put("available_end_time", pickupBeforeDate);
				
				
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
    
	public static class TimePickerFragment extends DialogFragment implements OnTimeSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			Log.i(TAG, "Timepicker View : " + view);
		}
	}
	
	public static class DatePickerFragment extends DialogFragment implements OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			Log.i(TAG, "DatePicker View : " + view);
		}
	}
}
