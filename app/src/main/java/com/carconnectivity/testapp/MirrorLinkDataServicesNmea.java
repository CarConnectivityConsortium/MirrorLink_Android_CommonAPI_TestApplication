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

import com.mirrorlink.android.commonapi.Defs;
import com.mirrorlink.android.commonapi.IDataServicesListener;
import com.mirrorlink.android.commonapi.IDataServicesManager;
import com.mirrorlink.lib.MirrorLinkApplicationContext;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MirrorLinkDataServicesNmea extends Dialog{
	Context mContext = null;
	MirrorLinkApplicationContext mAppContext = null;
	Integer mDataMinorVersion = 0;
	Integer mDataMajorVersion = 0;
	Integer mDataServiceId = 0;
	
	TextView mNmeaData = null;

	IDataServicesManager dataServicesManager = null;
	
	protected MirrorLinkDataServicesNmea(Context context, MirrorLinkApplicationContext appContext, int dataMinorVersion, int dataMajorVersion, int dataServiceId) {
		super(context);
		this.setTitle("NMEA");
		mAppContext = appContext;
		mContext = context;
		mDataMinorVersion = dataMinorVersion;
		mDataMajorVersion = dataMajorVersion;
		mDataServiceId = dataServiceId;
		
	}
	
	protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.nmea);
        
        mNmeaData = (TextView) findViewById(R.id.nmea_data);
        
        
        Button nmeaGet = (Button) findViewById(R.id.nmea_get);
        nmeaGet.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					dataServicesManager.getObject(mDataServiceId, Defs.GPSService.NMEA_OBJECT_UID);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
        Button nmeaSubscribe = (Button) findViewById(R.id.nmea_subscribe);
        nmeaSubscribe.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					dataServicesManager.subscribeObject(mDataServiceId, Defs.GPSService.NMEA_OBJECT_UID);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				
			}
		});
        
        Button nmeaUnsubscribe = (Button) findViewById(R.id.nmea_unsubscribe);
        nmeaUnsubscribe.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					dataServicesManager.unsubscribeObject(mDataServiceId, Defs.GPSService.NMEA_OBJECT_UID);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
        
        
        
        Button nmeaClear = (Button) findViewById(R.id.nmea_clear);
        nmeaClear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mNmeaData.setText("");
				
			}
		});
        
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
		public void onGetDataObjectResponse(final int serviceId, final int objectId, boolean success, final Bundle nmeaData) throws RemoteException {
			((Activity)mContext).runOnUiThread(new Runnable() {
				public void run() {
					if (serviceId == mDataServiceId && objectId == Defs.GPSService.NMEA_OBJECT_UID)
					{
						if (nmeaData !=null)
						{
							
							
							
							Bundle data = nmeaData.getBundle(Defs.GPSService.NMEA_DATA_FIELD_UID);
							Bundle timestamp = nmeaData.getBundle(Defs.GPSService.NMEA_TIMESTAMP_FIELD_UID);
							
							if (data == null || timestamp == null)
							{
								Toast.makeText(mContext, "Received nmea object is invalid", Toast.LENGTH_SHORT).show();
								return;
							}
							
							byte[] nmea_data = data.getByteArray(Defs.DataObjectKeys.VALUE);
							
	
							mNmeaData.setText(new String(nmea_data));

						}
					}
			
				}
			});
		}

		@Override
		public void onAvailableServicesChanged(List<Bundle> services) throws RemoteException {
			
		}
		
	
	};
	
	@Override
	protected void onStart()
	{
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

}
