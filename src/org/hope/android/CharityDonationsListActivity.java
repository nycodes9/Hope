package org.hope.android;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.hope.android.utils.DonationsDS;
import org.hope.android.utils.DonationsDS.FOOD_TYPES;

import com.parse.ParseUser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CharityDonationsListActivity extends FragmentActivity {

	private static final String TAG = "CharityDonationsListActivity";

	Context mContext;
	
	ListView donationsListView;
	View emptyView;
	ArrayList<DonationsDS> donationList;
	DonationsDataAdapter donationsAdapter;
	
	SimpleDateFormat sdatef = new SimpleDateFormat("EEE, MMM d, ''yy");
	SimpleDateFormat stimef = new SimpleDateFormat("hh:mm aaa");
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charity_donations_list_layout);
        
        mContext = this;
        
        donationsAdapter = new DonationsDataAdapter();
        
        emptyView = findViewById(R.id.charity_donations_emptyPB);
        donationsListView = (ListView) findViewById(R.id.charity_donationsLV);
        donationsListView.setEmptyView(emptyView);
        donationsListView.setAdapter(donationsAdapter);
        
        sync();
       
    }
    
    public void sync() {
    
    	new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				donationList = new ArrayList<DonationsDS>();
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				
				
				
				/**
				 * TODO 1.Call <h6>Parse Query here to populate donations 
				 * 2. create DonationsDS objects and add these to array list
				 * 3. Create all possible Comparators to help sort by functions (or handle from Parse Queries)
				 * 4. Prepare search functionality features 
				 * 5. if Parse fetch exception return Boolean.FALSE else Boolean.TRUE   
				 */
				
				/************** Dummy Data fro Test **************/
				for (int i = 0 ; i < 5 ; i++){
					DonationsDS temp = new DonationsDS();
					temp.donationID = i;
					temp.donorEmail = new String("donor" + i + "@hope.org");
					temp.donorPhoneNum = "123456";
					
					temp.type = FOOD_TYPES.COOKED;
					temp.expDate = new Date();
					temp.wtQty = i;
					temp.pickupDate = new Date();
					temp.pickupAfterTime = new Date();
					temp.pickupBeforeTime = new Date();
					temp.pickupInfoExtraStr = new String("Strung info : " + i);
					
					temp.pickupLocAndroidAddress = new Address(Locale.CHINA);
					
					donationList.add(temp);
				}
				return Boolean.TRUE;
				/***************************************************/
			}

			@Override
			protected void onPostExecute(Boolean success) {
				super.onPostExecute(success);
				donationsAdapter.notifyDataSetChanged();
			}
			
		}.execute();
    }
    
    class DonationsDataAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return donationList == null ? 0 : donationList.size();
		}

		@Override
		public DonationsDS getItem(int position) {
			return donationList == null ? null : donationList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return donationList == null ? Long.MIN_VALUE : donationList.get(position).donationID;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder;
			
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.donation_item_layout, parent, false);
				holder = new ViewHolder();
				holder.donorEmailTV = (TextView)convertView.findViewById(R.id.donation_donorEmailTV);
				holder.foodTypeTV = (TextView)convertView.findViewById(R.id.donation_foodTypeTV);
				holder.expDateTV = (TextView)convertView.findViewById(R.id.donation_expTV);
				holder.wtQtyTV = (TextView)convertView.findViewById(R.id.donation_wtTV);
				holder.pickupDateTV = (TextView)convertView.findViewById(R.id.donation_pickupDateTV);
				holder.pickupBtwnTV = (TextView)convertView.findViewById(R.id.donation_pickupTimeTV);
				holder.extraInfoTV = (TextView)convertView.findViewById(R.id.donation_pickupInfoTV);
				holder.pickupBtnTV = (Button)convertView.findViewById(R.id.donation_pickupBtn);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder)convertView.getTag();
			}
			
			holder.donorEmailTV.setText(donationList.get(position).donorEmail);
			holder.foodTypeTV.setText(donationList.get(position).type.name().toLowerCase());
			holder.expDateTV.setText(sdatef.format(donationList.get(position).expDate));
			holder.wtQtyTV.setText(Float.toString(donationList.get(position).wtQty));
			holder.pickupDateTV.setText(sdatef.format(donationList.get(position).pickupDate));
			
			String timeBtwn = stimef.format(donationList.get(position).pickupAfterTime) + " and "
					+ stimef.format(donationList.get(position).pickupBeforeTime) ;
			
			holder.pickupBtwnTV.setText(timeBtwn);
			holder.extraInfoTV.setText(donationList.get(position).pickupInfoExtraStr);
			
			holder.pickupBtnTV.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					/**
					 * TODO 1. Open new activity with map co-ords to review and confirm
					 * 2. Once confirmed option to a. send email b. sms c. call d. sms (ATT sms api)
					 * 3. Raise notification (may be mutually exclusive)
					 */
				}
			});
			
			return convertView;
		}

    }
    
    static class ViewHolder {
    	
    	TextView donorEmailTV;
    	TextView foodTypeTV;
    	TextView expDateTV;
    	TextView wtQtyTV;
    	TextView pickupDateTV;
    	TextView pickupBtwnTV;
    	TextView extraInfoTV;
    	Button pickupBtnTV;
    	
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
