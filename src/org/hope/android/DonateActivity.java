package org.hope.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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

}
