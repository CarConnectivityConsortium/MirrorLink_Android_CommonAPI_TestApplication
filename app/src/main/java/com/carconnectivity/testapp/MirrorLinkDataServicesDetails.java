/*
 * Copyright Car Connectivity Consortium
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * You may decide to give the Car Connectivity Consortium input, suggestions
 * or feedback of a technical nature which may be implemented on the
 * Car Connectivity Consortium products (“Feedback”).
 *
 * You agrees that any such Feedback is given on non-confidential
 * basis and Licensee hereby waives any confidentiality restrictions
 * for such Feedback. In addition, Licensee grants to the Car Connectivity Consortium
 * and its affiliates a worldwide, non-exclusive, perpetual, irrevocable,
 * sub-licensable, royalty-free right and license under Licensee’s copyrights to copy,
 * reproduce, modify, create derivative works and directly or indirectly
 * distribute, make available and communicate to public the Feedback
 * in or in connection to any CCC products, software and/or services.
 */
package com.carconnectivity.testapp;


import java.util.List;

import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.carconnectivity.testapp.views.HeaderView;
import com.carconnectivity.testapp.views.LastExecutedViewMultiline;
import com.carconnectivity.testapp.views.SuccesView;
import com.carconnectivity.testapp.views.TextViewString;
import com.mirrorlink.android.commonapi.Defs;
import com.mirrorlink.android.commonapi.IDataServicesListener;
import com.mirrorlink.android.commonapi.IDataServicesManager;
import com.mirrorlink.android.commonapi.IDeviceStatusManager;

public class MirrorLinkDataServicesDetails extends BaseActivity {
	String mDataServiceName = "";
	Integer mDataMinorVersion = 0;
	Integer mDataMajorVersion = 0;
	Integer mDataServiceId = 0;

	SuccesView registerSucceed = null;
	TextViewString registerSuccessServiceID = null;

	Button subscribeButton = null;	
	Button unsubscribeButton = null;	
	SuccesView callbackFired = null;
	LastExecutedViewMultiline callbackFiredList = null;
	Button clearCallbackList =null;
	

	TextViewString mSubscribeCallbackServiceId = null;
	TextViewString mSubscribeCallbackObjectId = null;
	SuccesView     mSubscribeCallbackSuccess = null;
	TextViewString mSubscribeCallbackSubscriptionType = null;
	TextViewString mSubscribeCallbackInterval = null;
	SuccesView     mSubscribeCallbackFired = null;
	LastExecutedViewMultiline mSubscribeCallbackFiredList = null;
	Button mSubscribeCallbackFiredClear =null;
	
	private IDataServicesManager dataServicesManager = null;

	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		Bundle data = getIntent().getExtras();
		mDataServiceName = data.getString("SERVICE_NAME");
		
		mDataMinorVersion = data.getInt("VERSION_MINOR");
		mDataMajorVersion =  data.getInt("VERSION_MAJOR");
		mDataServiceId =  data.getInt("SERVICE_ID");
		
		
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume()
	{
		dataServicesManager = getMirrorLinkApplicationContext().registerDataServicesManager(this,mDataServicesListener);
		super.onResume();
		if (dataServicesManager== null)
		{
			finish();
		}
		

	}
	@Override
	protected void onPause()
	{
		getMirrorLinkApplicationContext().unregisterDataServicesManager(this, mDataServicesListener);
		super.onPause();
	}
	

	
	IDataServicesListener mDataServicesListener = new IDataServicesListener.Stub() {
		@Override
		public void onSubscribeResponse(final int serviceId, final int objectId, final boolean success, final int subscriptionType, final int interval) throws RemoteException {
			runOnUiThread(new Runnable() {
				public void run() {
					mSubscribeCallbackServiceId.setValue("0x"+String.format("%08x", serviceId).toUpperCase());
					mSubscribeCallbackObjectId.setValue("0x"+String.format("%08x", objectId).toUpperCase()); 
					mSubscribeCallbackSuccess.setValue(success);
					
					if (subscriptionType == Defs.SubscriptionType.REGULAR_INTERVAL)
					{
						mSubscribeCallbackSubscriptionType.setValue("REGULAR_INTERVAL");
					}
					else if (subscriptionType == Defs.SubscriptionType.ON_CHANGE)
					{
						mSubscribeCallbackSubscriptionType.setValue("ON_CHANGE");
					}
					else if (subscriptionType == Defs.SubscriptionType.AUTOMATIC)
					{
						mSubscribeCallbackSubscriptionType.setValue("AUTOMATIC");
					}
					
					mSubscribeCallbackInterval.setValue(Integer.toString(interval));
					mSubscribeCallbackFired.setValue(true);
					mSubscribeCallbackFiredList.setNow();
				}
			});
			
			
			
		}
		
		@Override
		public void onSetDataObjectResponse(int serviceId, int objectId, boolean success) throws RemoteException {

		}
		
		@Override
		public void onRegisterForService(final int serviceId, final boolean success) throws RemoteException {
			runOnUiThread(new Runnable() {
				public void run() {
					callbackFired.setValue(true);
					callbackFiredList.setNow();

					registerSucceed.setValue(success);
					registerSuccessServiceID.setValue("0x"+String.format("%08x", serviceId).toUpperCase());

				}
			});
		}
		
		@Override
		public void onGetDataObjectResponse(int serviceId, int objectId, boolean success, Bundle object) throws RemoteException {
		}

		@Override
		public void onAvailableServicesChanged(List<Bundle> services) throws RemoteException {
		}
		
	
	};
	

	ScrollView buildUI()
	{
		ScrollView scrollView = new ScrollView(this);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		HeaderView appCertEntitiesHeader = new HeaderView(this,mDataServiceName);
		layout.addView(appCertEntitiesHeader);

		
		
		subscribeButton = new Button(this);
		subscribeButton.setText("Register to service callback");
		subscribeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
	
				try {
					dataServicesManager.registerToService(mDataServiceId,mDataMajorVersion,mDataMinorVersion);
				} catch (RemoteException e) {
					Toast.makeText(getApplicationContext(), "Can't register to callback on service id " + mDataServiceId , Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}
		});
		layout.addView(subscribeButton);
		
		unsubscribeButton = new Button(this);
		unsubscribeButton.setText("Unregister to service callback");
		unsubscribeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					dataServicesManager.unregisterFromService(mDataServiceId);
				} catch (RemoteException e) {
					Toast.makeText(getApplicationContext(), "Can't unregister to callback on service id " + mDataServiceId , Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}
		});
		layout.addView(unsubscribeButton);
		
		
		layout.addView(new HeaderView(this, "MirrorLink registration callbacks"));

		registerSucceed = new SuccesView(this, "Register succeed", false);
		layout.addView(registerSucceed);

		registerSuccessServiceID = new TextViewString(this, "Registered service Id","n/a");
		layout.addView(registerSuccessServiceID);


		callbackFired = new SuccesView(this, "Callback fired", false);
		layout.addView(callbackFired);
		callbackFiredList = new LastExecutedViewMultiline(this);
		layout.addView(callbackFiredList);
		
		
		clearCallbackList =new Button(this);
		clearCallbackList.setText("Clear callback logs");
		clearCallbackList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callbackFiredList.clear();
				callbackFired.setValue(false);
				registerSucceed.setValue(false);
				registerSuccessServiceID.setValue("n/a");
			}
		});
		layout.addView(clearCallbackList);
		
		
		
		layout.addView(new HeaderView(this,"Services objects:"));
		if (mDataServiceName.equals(Defs.GPSService.SERVICE_NAME))
		{
			layout.addView(new HeaderView(this,"GPS NMEA:"));
			
			Button nmeaButton = new Button(this);
			nmeaButton.setText("GPS NMEA");
			nmeaButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MirrorLinkDataServicesNmea dialog = new MirrorLinkDataServicesNmea(MirrorLinkDataServicesDetails.this, 
																						getMirrorLinkApplicationContext(),
																						mDataMinorVersion,
																						mDataMajorVersion,
																						mDataServiceId);
					dialog.show();
					
				}
			});
			layout.addView(nmeaButton);
			Button nmeaDescriptionButton = new Button(this);
			nmeaDescriptionButton.setText("GPS NMEA Description");
			nmeaDescriptionButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MirrorLinkDataServicesNmeaDescription dialog = new MirrorLinkDataServicesNmeaDescription(MirrorLinkDataServicesDetails.this, 
																											 getMirrorLinkApplicationContext(),
																											 mDataMinorVersion,
																											 mDataMajorVersion,
																											 mDataServiceId);
					dialog.show();
					
				}
			});
			layout.addView(nmeaDescriptionButton);		
		}
		else if(mDataServiceName.equals(Defs.LocationService.SERVICE_NAME))
		{
			Button locationObject = new Button(this);
			locationObject.setText("Location Object");
			locationObject.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MirrorLinkDataServicesLocationObject dialog = new MirrorLinkDataServicesLocationObject(MirrorLinkDataServicesDetails.this, 
																											 getMirrorLinkApplicationContext(),
																											 mDataMinorVersion,
																											 mDataMajorVersion,
																											 mDataServiceId);
					dialog.show();
					
				}
			});
			layout.addView(locationObject);		
		}
		
		layout.addView(new HeaderView(this,"Subscription callback information"));
		mSubscribeCallbackServiceId = new TextViewString(this,"Service ID","n/a");
		layout.addView(mSubscribeCallbackServiceId);
		mSubscribeCallbackObjectId = new TextViewString(this,"Object ID","n/a");
		layout.addView(mSubscribeCallbackObjectId);
		mSubscribeCallbackSuccess = new SuccesView(this, "Success", false);
		layout.addView(mSubscribeCallbackSuccess);
		mSubscribeCallbackSubscriptionType = new TextViewString(this,"Subscription type","n/a");
		layout.addView(mSubscribeCallbackSubscriptionType);
		mSubscribeCallbackInterval = new TextViewString(this,"Interval","n/a");
		layout.addView(mSubscribeCallbackInterval);
		
		
		mSubscribeCallbackFired = new SuccesView(this, "Callback fired", false);
		layout.addView(mSubscribeCallbackFired);
		mSubscribeCallbackFiredList = new LastExecutedViewMultiline(this);
		layout.addView(mSubscribeCallbackFiredList);
		mSubscribeCallbackFiredClear = new Button(this);
		mSubscribeCallbackFiredClear.setText("Clear callback logs");
		mSubscribeCallbackFiredClear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSubscribeCallbackServiceId.setValue("n/a");
				mSubscribeCallbackObjectId.setValue("n/a");
				mSubscribeCallbackSuccess.setValue(false);
				mSubscribeCallbackSubscriptionType.setValue("n/a");
				mSubscribeCallbackInterval.setValue("n/a");
				
				
				mSubscribeCallbackFiredList.clear();
				mSubscribeCallbackFired.setValue(false);
			}
		});
		layout.addView(mSubscribeCallbackFiredClear);
		

		scrollView.addView(layout);
		return scrollView;
	}

}
