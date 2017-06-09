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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.carconnectivity.testapp.views.HeaderView;
import com.carconnectivity.testapp.views.LastExecutedViewMultiline;
import com.carconnectivity.testapp.views.SuccesView;
import com.mirrorlink.android.commonapi.Defs;
import com.mirrorlink.android.commonapi.IDataServicesListener;
import com.mirrorlink.android.commonapi.IDataServicesManager;
import com.mirrorlink.lib.MirrorLinkApplicationContext;
import com.mirrorlink.lib.ServiceReadyCallback;

public class MirrorLinkDataServices extends BaseActivity {
	ListView dataServicesListView= null;
	Button getDataServicesButton = null;	
	
	SuccesView callbackFired = null;
	LastExecutedViewMultiline callbackFiredList = null;
	Button clearCallbackList =null;

	IDataServicesManager dataServicesManager = null;

	@Override
	protected void onResume()
	{
		super.onResume();
        dataServicesManager = getMirrorLinkApplicationContext().registerDataServicesManager(this,mDataServicesListener);
        if (dataServicesManager == null)
        {
			Toast.makeText(this, "Unable to get data services manager.", Toast.LENGTH_LONG).show();
            finish();
        }
        updateDataServices();
	}
	@Override
	protected void onPause()
	{
		getMirrorLinkApplicationContext().unregisterDataServicesManager(this, mDataServicesListener);
		super.onPause();
	}
	

	IDataServicesListener mDataServicesListener = new IDataServicesListener.Stub() {
		@Override
		public void onSubscribeResponse(int serviceId, int objectId, boolean success, int subscriptionType, int interval) throws RemoteException {

		}
		
		@Override
		public void onSetDataObjectResponse(int serviceId, int objectId, boolean success) throws RemoteException {
			
		}
		
		@Override
		public void onRegisterForService(int serviceId, boolean success) throws RemoteException {
			
		}
		
		@Override
		public void onGetDataObjectResponse(int serviceId, int objectId, boolean success, Bundle object) throws RemoteException {
			
		}
		
		@Override
		public void onAvailableServicesChanged(final List<Bundle> dataServices) throws RemoteException {
			runOnUiThread(new Runnable() {
				public void run() {
					fillData(dataServices);
			        callbackFiredList.setNow();
			        callbackFired.setValue(true);
				}
			});
		}
	};
	
	void fillData(List<Bundle> dataServices)
	{
		List<HashMap<String, String>> dataServicesList = new ArrayList<HashMap<String, String>>();
		 
		
		for(int i = 0; i < dataServices.size(); i++){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("text", dataServices.get(i).getString(Defs.ServiceInformation.SERVICE_NAME).trim());
			map.put("text2", 
					"ServiceID: 0x"+ String.format("%x",dataServices.get(i).getInt(Defs.ServiceInformation.SERVICE_ID)).toUpperCase() + 
					", Version: " + dataServices.get(i).getInt(Defs.ServiceInformation.VERSION_MAJOR) + "." +
					dataServices.get(i).getInt(Defs.ServiceInformation.VERSION_MINOR));
			
			dataServicesList.add(map);
		}

		SimpleAdapter adapter = new SimpleAdapter(this,dataServicesList, android.R.layout.simple_list_item_2, new String[] {"text","text2"}, new int[] {android.R.id.text1, android.R.id.text2} );
		dataServicesListView.setAdapter(adapter);
		
		// Workaround for listView in scroll view
		// http://nex-otaku-en.blogspot.com/2010/12/android-put-listview-in-scrollview.html
		
		int totalHeight = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, dataServicesListView);
            listItem.measure(0, MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
		
		ViewGroup.LayoutParams params = dataServicesListView.getLayoutParams();
        params.height = totalHeight + (dataServicesListView.getDividerHeight() * (dataServicesListView.getCount() - 1));
        dataServicesListView.setLayoutParams(params);
	}
	void updateDataServices()
	{
		if(dataServicesManager != null)
		{
			List<Bundle> dataServices = new ArrayList<Bundle>();
			try {
				dataServices = dataServicesManager.getAvailableServices();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
			fillData(dataServices);
		}
	}
	
	
	ScrollView buildUI()
	{
		ScrollView scrollView = new ScrollView(this);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		HeaderView appCertEntitiesHeader = new HeaderView(this,"List of available data services:");
		layout.addView(appCertEntitiesHeader);
		dataServicesListView = new ListView(this);
		layout.addView(dataServicesListView);
		
		
		dataServicesListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1, int pos, long arg3) {
				Intent intent = new Intent(getApplicationContext(), MirrorLinkDataServicesDetails.class);
				List<Bundle> dataServices = new ArrayList<Bundle>();;
				if(dataServicesManager != null)
				{
					try {
						dataServices = dataServicesManager.getAvailableServices();
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					
					fillData(dataServices);
				}
				Bundle service = dataServices.get(pos);
				
				intent.putExtra("SERVICE_NAME", service.getString("SERVICE_NAME"));
				intent.putExtra("VERSION_MINOR", service.getInt("VERSION_MINOR"));
				intent.putExtra("VERSION_MAJOR", service.getInt("VERSION_MAJOR"));
				intent.putExtra("SERVICE_ID", service.getInt("SERVICE_ID"));
				
			    startActivity(intent);
			}
		});
		
		getDataServicesButton = new Button(this);
		getDataServicesButton.setText("Get Available Services");
		getDataServicesButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateDataServices();
			}
		});
		
		layout.addView(getDataServicesButton);
		
		
		layout.addView(new HeaderView(this,"MirrorLink Available services callbacks"));
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
			}
		});
		layout.addView(clearCallbackList);

		scrollView.addView(layout);
		return scrollView;
	}

}
