package org.hope.android.utils;

import java.util.Date;

import android.location.Address;
import android.provider.ContactsContract.CommonDataKinds.Email;

public class DonationsDS {

	public static enum FOOD_TYPES {
		RAW, COOKED, FROZEN;
	}
	
	
	public String donationID;
	public String donorEmail;
	public String donorPhoneNum;
	
	public String type;
	public Date expDate;
	public float wtQty;
	public Date pickupDate;
	public Date pickupAfterTime;
	public Date pickupBeforeTime;
	public String pickupInfoExtraStr;

	public boolean disableBtn;
	public String pickupLocAndroidAddress;
	
}
