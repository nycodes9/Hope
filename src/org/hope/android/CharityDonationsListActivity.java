package org.hope.android;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.hope.android.utils.DonationsDS;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class CharityDonationsListActivity extends FragmentActivity {

	private static final String TAG = "CharityDonationsListActivity";

	private enum SORT_CATEGORY  {
		EXP_DATE, QTY, PICKUP, TYPE, LOCATION;
	}
	
	Context mContext;
	
	Spinner sortSP;
	SORT_CATEGORY currentSortChoice = SORT_CATEGORY.EXP_DATE;
	
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
        
        sortSP = (Spinner) findViewById(R.id.charity_donationsSortSP);
        sortSP.setOnItemSelectedListener(new OnItemSelectedListener() {
        	
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				switch (pos) {
				case 0:
					currentSortChoice = SORT_CATEGORY.EXP_DATE;
					break;
				case 1:
					currentSortChoice = SORT_CATEGORY.QTY;
					break;
				case 2:
					currentSortChoice = SORT_CATEGORY.PICKUP;
					break;
				case 3:
					currentSortChoice = SORT_CATEGORY.TYPE;
					break;
				case 4:
					currentSortChoice = SORT_CATEGORY.LOCATION;
					break;

				default:
					currentSortChoice = SORT_CATEGORY.EXP_DATE;
					break;
				}
				
				sortDonation(currentSortChoice);
				
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
        
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
				ParseQuery query = new ParseQuery("contributions");
				query.setLimit(25);
				
				try 
				{
					List<ParseObject> list  = query.find();
					
					for (int i = 0; i < list.size(); i++) 
					{
						ParseObject po = list.get(i);
						DonationsDS entry = new DonationsDS();
						
						//set flag to disable button
						if (po.getString("charity_address") != null && po.getString("charity_address").length() == 0) 
						{
							
						}
						
						entry.donationID = po.getObjectId().toString();
						entry.donorEmail = po.getString("donor_email");
						entry.donorPhoneNum = po.getString("donor_phone");
						entry.type = po.getString("food_type");
						entry.expDate = po.getDate("expire_date");
						entry.pickupAfterTime = po.getDate("available_start_time");
						entry.pickupBeforeTime = po.getDate("available_end_time");
						entry.pickupDate = po.getDate("available_end_time");
						entry.pickupInfoExtraStr = po.getString("food_name");
						entry.pickupLocAndroidAddress = po.getString("donor_address");
						entry.wtQty = po.getNumber("food_weight").floatValue();
						donationList.add(entry);
						
					}
					/*
					Context finishedToastContext = getApplicationContext();
					CharSequence finishedToastText = "Finished retrieving data";
					int duration = Toast.LENGTH_LONG;
					Toast toast = Toast.makeText(finishedToastContext, finishedToastText , duration);
					toast.show();
					*/
					return true;
				}
				catch (ParseException e) 
				{
					/*Context errorToastContext = getApplicationContext();
					CharSequence errorToastText = "ERROR:" + e.getMessage();
					int duration = Toast.LENGTH_LONG;
					Toast toast = Toast.makeText(errorToastContext, errorToastText , duration);
					toast.show();
					*/
					
					return false;
				}
				

				
				
				
				

				/*
				//************** Dummy Data fro Test **************
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
				*/
			}

			@Override
			protected void onPostExecute(Boolean success) {
				super.onPostExecute(success);
				donationsAdapter.notifyDataSetChanged();
			}
			
		}.execute();
    }
    
    public void sortDonation(final SORT_CATEGORY by) {

		Collections.sort(donationList, new Comparator<DonationsDS>() {

			public int compare(DonationsDS lhs, DonationsDS rhs) {

				int returnDummy = 0;

				switch (by) {
				case EXP_DATE:
					returnDummy = lhs.expDate.compareTo(rhs.expDate);
					break;

				case QTY:
					returnDummy = lhs.wtQty > rhs.wtQty ? 0 : 1;
					break;
					
				case PICKUP:
					returnDummy = lhs.pickupDate.compareTo(rhs.expDate);
					break;
					
				case TYPE:
					returnDummy = lhs.type.compareTo(rhs.type);
					break;
					
				case LOCATION:
					returnDummy = lhs.pickupLocAndroidAddress.toString().compareTo(lhs.pickupLocAndroidAddress.toString()); 
					break;
				}

				return returnDummy;

			}
			
		});
		
		donationsAdapter.notifyDataSetChanged();
		donationsListView.invalidateViews();

	}
    
    class DonationsDataAdapter extends BaseAdapter {

		public int getCount() {
			return donationList == null ? 0 : donationList.size();
		}

		public DonationsDS getItem(int position) {
			return donationList == null ? null : donationList.get(position);
		}

		public long getItemId(int position) {
			return donationList == null ? Long.MIN_VALUE : position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder;
			
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.donation_item_layout, parent, false);
				holder = new ViewHolder();
				holder.foodNameTV = (TextView)convertView.findViewById(R.id.donation_pickupInfoTV);
				holder.foodTypeTV = (TextView)convertView.findViewById(R.id.donation_foodTypeTV);
				holder.expDateTV = (TextView)convertView.findViewById(R.id.donation_expTV);
				holder.wtQtyTV = (TextView)convertView.findViewById(R.id.donation_wtTV);
				holder.pickupDateTV = (TextView)convertView.findViewById(R.id.donation_pickupDateTV);
				holder.pickupBtwnTV = (TextView)convertView.findViewById(R.id.donation_pickupTimeTV);
				holder.reviewBtn = (Button)convertView.findViewById(R.id.donation_reviewBtn);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder)convertView.getTag();
			}
			
			holder.foodTypeTV.setText(donationList.get(position).type.toLowerCase());
			holder.expDateTV.setText(sdatef.format(donationList.get(position).expDate));
			holder.wtQtyTV.setText(Float.toString(donationList.get(position).wtQty));
			holder.pickupDateTV.setText(sdatef.format(donationList.get(position).pickupDate));
			
			String timeBtwn = stimef.format(donationList.get(position).pickupAfterTime) + " and "
					+ stimef.format(donationList.get(position).pickupBeforeTime) ;
			
			holder.pickupBtwnTV.setText(timeBtwn);
			holder.foodNameTV.setText(donationList.get(position).pickupInfoExtraStr);
			
			holder.reviewBtn.setOnClickListener(new OnClickListener() {
				
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
    	
    	TextView foodNameTV;
    	TextView foodTypeTV;
    	TextView expDateTV;
    	TextView wtQtyTV;
    	TextView pickupDateTV;
    	TextView pickupBtwnTV;
    	CheckBox addToPickupListCB; 
    	Button reviewBtn;
    	
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
