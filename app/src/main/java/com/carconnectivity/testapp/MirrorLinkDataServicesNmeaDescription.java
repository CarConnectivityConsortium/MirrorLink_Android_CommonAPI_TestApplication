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
import com.mirrorlink.lib.ServiceReadyCallback;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class MirrorLinkDataServicesNmeaDescription extends Dialog{
	Context mContext = null;
	MirrorLinkApplicationContext mAppContext = null;
	Integer mDataMinorVersion = 0;
	Integer mDataMajorVersion = 0;
	Integer mDataServiceId = 0;
	
	RadioButton nema_description_gga = null;
	RadioButton nema_description_gll = null;
	RadioButton nema_description_gsv = null;
	RadioButton nema_description_rmc = null;
	RadioButton nema_description_gsa = null;
	RadioButton nema_description_vtg = null;
	RadioButton nema_description_gst = null;
	
	IDataServicesManager dataServicesManager = null;

	protected MirrorLinkDataServicesNmeaDescription(Context context, MirrorLinkApplicationContext appContext, int dataMinorVersion, int dataMajorVersion, int dataServiceId) 
	{
		super(context);
		this.setTitle("NMEA Description");
		mAppContext = appContext;
		mContext = context;
		mDataMinorVersion = dataMinorVersion;
		mDataMajorVersion = dataMajorVersion;
		mDataServiceId = dataServiceId;
		
	}
	
	protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nmea_description);

        Button nmeaGet = (Button) findViewById(R.id.nmea_description_get);
        nmeaGet.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					dataServicesManager.getObject(mDataServiceId, Defs.GPSService.NMEADESCRIPTION_OBJECT_UID);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
        Button nmeaSubscribe = (Button) findViewById(R.id.nmea_description_subscribe);
        nmeaSubscribe.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					dataServicesManager.subscribeObject(mDataServiceId, Defs.GPSService.NMEADESCRIPTION_OBJECT_UID);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				
			}
		});
        
        Button nmeaUnsubscribe = (Button) findViewById(R.id.nmea_description_unsubscribe);
        nmeaUnsubscribe.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					dataServicesManager.unsubscribeObject(mDataServiceId, Defs.GPSService.NMEADESCRIPTION_OBJECT_UID);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
        
        
        nema_description_gga = (RadioButton) findViewById(R.id.nema_description_gga);
    	nema_description_gll = (RadioButton) findViewById(R.id.nema_description_gll);
    	nema_description_gsv = (RadioButton) findViewById(R.id.nema_description_gsv);
    	nema_description_rmc = (RadioButton) findViewById(R.id.nema_description_rmc);
    	nema_description_gsa = (RadioButton) findViewById(R.id.nema_description_gsa);
    	nema_description_vtg = (RadioButton) findViewById(R.id.nema_description_vtg);
    	nema_description_gst = (RadioButton) findViewById(R.id.nema_description_gst);
        
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
					if (serviceId == mDataServiceId && objectId == Defs.GPSService.NMEADESCRIPTION_OBJECT_UID)
					{
						if (nmeaData !=null)
						{
							Bundle data = nmeaData.getBundle(Defs.GPSService.NMEADESCRIPTION_SUPPORTED_FIELD_UID);
							
							if (data == null )
							{
								Toast.makeText(mContext, "Received nmea description object is invalid", Toast.LENGTH_SHORT).show();
								return;
							}
							
							int nmea_desc = data.getInt(Defs.DataObjectKeys.VALUE);

							nema_description_gga.setChecked((nmea_desc & (1 << 0)) == (1 << 0));
							nema_description_gll.setChecked((nmea_desc & (1 << 1)) == (1 << 1));
							nema_description_gsa.setChecked((nmea_desc & (1 << 2)) == (1 << 2));
							nema_description_gsv.setChecked((nmea_desc & (1 << 3)) == (1 << 3));
							nema_description_rmc.setChecked((nmea_desc & (1 << 4)) == (1 << 4));
							nema_description_vtg.setChecked((nmea_desc & (1 << 5)) == (1 << 5));
							nema_description_gst.setChecked((nmea_desc & (1 << 6)) == (1 << 6));

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
		super.onStart();
		ApplicationContext.getContext(new ServiceReadyCallback() {
			@Override
			public void connected(MirrorLinkApplicationContext mirrorLinkApplicationContext) {
				dataServicesManager = mirrorLinkApplicationContext.registerDataServicesManager(this, mDataServicesListener);
				if (dataServicesManager == null)
				{
					cancel();
				}
			}
		});
	}
	@Override
	protected void onStop()
	{
		mAppContext.unregisterDataServicesManager(this, mDataServicesListener);
		super.onStop();
	}

}
