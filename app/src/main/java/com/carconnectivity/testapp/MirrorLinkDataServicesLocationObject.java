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


import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.carconnectivity.testapp.views.DoubleInputView;
import com.carconnectivity.testapp.views.LastExecutedViewMultiline;
import com.carconnectivity.testapp.views.SuccesView;
import com.mirrorlink.android.commonapi.Defs;
import com.mirrorlink.android.commonapi.IDataServicesListener;
import com.mirrorlink.android.commonapi.IDataServicesManager;
import com.mirrorlink.lib.MirrorLinkApplicationContext;

public class MirrorLinkDataServicesLocationObject extends Dialog {
	int mDataServiceId = 0;
	
	DoubleInputView cLatitude = null;
	DoubleInputView cLongtitue =null;
	DoubleInputView cAltitude = null;
	DoubleInputView cAccuracy =null;
	DoubleInputView cAltAccuracy =null;
	DoubleInputView cHeading =null;
	DoubleInputView cSpeed = null;

	SuccesView callbackFired = null;
	LastExecutedViewMultiline callbackFiredList= null;
	Button clearCallbackList = null;
	
	Integer mDataMinorVersion = 0;
	Integer mDataMajorVersion = 0;
	
	
	Context mContext = null;
	MirrorLinkApplicationContext mAppContext = null;
	IDataServicesManager dataServicesManager = null;
	
	protected MirrorLinkDataServicesLocationObject(Context context, MirrorLinkApplicationContext appContext, int dataMinorVersion, int dataMajorVersion, int dataServiceId) {
		super(context);
		this.setTitle("Location");
		mAppContext = appContext;
		mContext = context;
		mDataMinorVersion = dataMinorVersion;
		mDataMajorVersion = dataMajorVersion;
		mDataServiceId = dataServiceId;
	}

	@Override
	protected void onStart()
	{
		buildUI();

		dataServicesManager = mAppContext.registerDataServicesManager(this, mDataServicesListener);
		super.onStart();
		if (dataServicesManager == null)
		{
			cancel();
		}
		try {
			dataServicesManager.registerToService(mDataServiceId,mDataMajorVersion,mDataMinorVersion);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}
	@Override
	protected void onStop()
	{
		try {
			dataServicesManager.unregisterFromService(mDataServiceId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		mAppContext.unregisterDataServicesManager(this, mDataServicesListener);
		super.onStop();
	}
	
	
	IDataServicesListener mDataServicesListener = new IDataServicesListener.Stub() {
		@Override
		public void onSubscribeResponse(final int serviceId, final int objectId, final boolean success, final int subscriptionType, final int interval) throws RemoteException {
		}
		
		@Override
		public void onSetDataObjectResponse(int serviceId, int objectId, boolean success) throws RemoteException {

		}
		
		@Override
		public void onRegisterForService(int serviceId, boolean success) throws RemoteException {

		}
		
		@Override
		public void onGetDataObjectResponse(final int serviceId, final int objectId, boolean success, final Bundle locationObject) throws RemoteException {
			((Activity)mContext).runOnUiThread(new Runnable() {
				public void run() {
					if (serviceId == mDataServiceId && objectId == Defs.LocationService.LOCATION_OBJECT_UID)
					{
						if (locationObject !=null)
						{
							Bundle coords = locationObject.getBundle(Defs.LocationService.COORD_FIELD_UID);
							
							if (coords != null)
							{
								Bundle LATITUDE_FIELD_UID = coords.getBundle(Defs.LocationService.LATITUDE_FIELD_UID);
								Bundle LONGITUDE_FIELD_UID = coords.getBundle(Defs.LocationService.LONGITUDE_FIELD_UID);
								Bundle ALTITUDE_FIELD_UID = coords.getBundle(Defs.LocationService.ALTITUDE_FIELD_UID);
								Bundle ACCURACY_FIELD_UID = coords.getBundle(Defs.LocationService.ACCURACY_FIELD_UID);
								Bundle ALTITUDEACCURACY_FIELD_UID = coords.getBundle(Defs.LocationService.ALTITUDEACCURACY_FIELD_UID);
								Bundle HEADING_FIELD_UID = coords.getBundle(Defs.LocationService.HEADING_FIELD_UID);
								Bundle SPEED_FIELD_UID = coords.getBundle(Defs.LocationService.SPEED_FIELD_UID);
								
								
								if (LATITUDE_FIELD_UID == null
									|| LONGITUDE_FIELD_UID  == null
									|| ALTITUDE_FIELD_UID  == null
									|| ACCURACY_FIELD_UID  == null
									|| ALTITUDEACCURACY_FIELD_UID  == null
									|| HEADING_FIELD_UID == null 
									|| SPEED_FIELD_UID == null)
								{
									Toast.makeText(mContext, "Received location object is invalid", Toast.LENGTH_SHORT).show();
									return;
								}

								cLatitude.setValue(LATITUDE_FIELD_UID.getDouble(Defs.DataObjectKeys.VALUE));
								cLongtitue.setValue(LONGITUDE_FIELD_UID.getDouble(Defs.DataObjectKeys.VALUE));
								cAltitude.setValue(ALTITUDE_FIELD_UID.getDouble(Defs.DataObjectKeys.VALUE));
								cAccuracy.setValue(ACCURACY_FIELD_UID.getDouble(Defs.DataObjectKeys.VALUE));
								cAltAccuracy.setValue(ALTITUDEACCURACY_FIELD_UID.getDouble(Defs.DataObjectKeys.VALUE));
								cHeading.setValue(HEADING_FIELD_UID.getDouble(Defs.DataObjectKeys.VALUE));
								cSpeed.setValue(SPEED_FIELD_UID.getDouble(Defs.DataObjectKeys.VALUE));	
								
															
								callbackFired.setValue(true);
								callbackFiredList.setNow();
							}

						}
					}
			
				}
			});
		}

		@Override
		public void onAvailableServicesChanged(List<Bundle> services) throws RemoteException {
			
		}
		
	
	};
	
	void buildUI() {
		ScrollView scrollView = new ScrollView(mContext);
		LinearLayout layout = new LinearLayout(mContext);
		layout.setOrientation(LinearLayout.VERTICAL);
		
	
		cLatitude = new DoubleInputView(mContext,"Latitude"); 
		layout.addView(cLatitude);
		
		cLongtitue = new DoubleInputView(mContext,"Longtitude"); 
		layout.addView(cLongtitue);
		
		cAltitude = new DoubleInputView(mContext,"Altitude"); 
		layout.addView(cAltitude);
		
		cAccuracy = new DoubleInputView(mContext,"Accuracy"); 
		layout.addView(cAccuracy);	
		
		cAltAccuracy = new DoubleInputView(mContext,"Altitude accuracy"); 
		layout.addView(cAltAccuracy);	
		
		cHeading = new DoubleInputView(mContext,"Heading"); 
		layout.addView(cHeading);
		
		cSpeed = new DoubleInputView(mContext,"Speed"); 
		layout.addView(cSpeed);	
		
		Button setObject = new Button(mContext);
		setObject.setText("Set location object");
		
		setObject.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle locationObject = new Bundle();
				
				Bundle coords = new Bundle();
				
				Bundle LATITUDE_FIELD_UID = new Bundle();
				LATITUDE_FIELD_UID.putDouble(Defs.DataObjectKeys.VALUE, cLatitude.getValue());
				coords.putBundle(Defs.LocationService.LATITUDE_FIELD_UID, LATITUDE_FIELD_UID);
				
				Bundle LONGITUDE_FIELD_UID = new Bundle();
				LONGITUDE_FIELD_UID.putDouble(Defs.DataObjectKeys.VALUE, cLongtitue.getValue());
				coords.putBundle(Defs.LocationService.LONGITUDE_FIELD_UID, LONGITUDE_FIELD_UID);
				
				Bundle ALTITUDE_FIELD_UID = new Bundle();
				LATITUDE_FIELD_UID.putDouble(Defs.DataObjectKeys.VALUE, cAltitude.getValue());
				coords.putBundle(Defs.LocationService.ALTITUDE_FIELD_UID, ALTITUDE_FIELD_UID);
				
				Bundle ACCURACY_FIELD_UID = new Bundle();
				LATITUDE_FIELD_UID.putDouble(Defs.DataObjectKeys.VALUE, cAccuracy.getValue());
				coords.putBundle(Defs.LocationService.ACCURACY_FIELD_UID, ACCURACY_FIELD_UID);
				
				Bundle ALTITUDEACCURACY_FIELD_UID = new Bundle();
				LATITUDE_FIELD_UID.putDouble(Defs.DataObjectKeys.VALUE, cAltAccuracy.getValue());
				coords.putBundle(Defs.LocationService.ALTITUDEACCURACY_FIELD_UID, ALTITUDEACCURACY_FIELD_UID);
				
				Bundle HEADING_FIELD_UID = new Bundle();
				LATITUDE_FIELD_UID.putDouble(Defs.DataObjectKeys.VALUE, cHeading.getValue());
				coords.putBundle(Defs.LocationService.HEADING_FIELD_UID, HEADING_FIELD_UID);
				
				Bundle SPEED_FIELD_UID = new Bundle();
				LATITUDE_FIELD_UID.putDouble(Defs.DataObjectKeys.VALUE, cSpeed.getValue());
				coords.putBundle(Defs.LocationService.SPEED_FIELD_UID, SPEED_FIELD_UID);
								
				locationObject.putBundle(Defs.LocationService.COORD_FIELD_UID, coords);
				
				Date d = new Date();
				locationObject.putLong(Defs.LocationService.TIMESTAMP_FIELD_UID, d.getTime());
	
				try {
					dataServicesManager.setObject(mDataServiceId, Defs.LocationService.LOCATION_OBJECT_UID, locationObject);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		layout.addView(setObject);	
		
		Button getObject = new Button(mContext);
		getObject.setText("Get location object");
		
		getObject.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					dataServicesManager.getObject(mDataServiceId, Defs.LocationService.LOCATION_OBJECT_UID);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			
		});
		layout.addView(getObject);	
		
		callbackFired = new SuccesView(mContext, "Callback fired", false);
		layout.addView(callbackFired);
		callbackFiredList = new LastExecutedViewMultiline(mContext);
		layout.addView(callbackFiredList);
		
		
		clearCallbackList =new Button(mContext);
		clearCallbackList.setText("Clear callback logs");
		clearCallbackList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callbackFiredList.clear();
				callbackFired.setValue(false);
			}
		});
		layout.addView(clearCallbackList);
		
		scrollView.addView(layout);
		

		setContentView(scrollView);
 
	}

}
