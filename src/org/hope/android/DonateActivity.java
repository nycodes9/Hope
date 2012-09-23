package org.hope.android;

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

public class DonateActivity extends FragmentActivity {

	Context mContext;
	
	Button donateBtn;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_donate_layout);
        
        mContext = this;
        
        donateBtn = (Button) findViewById(R.id.donor_donateBtn);
        donateBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
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
            	/**
                 * logout code
                 */
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
