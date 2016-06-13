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

import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.carconnectivity.testapp.views.HeaderView;
import com.carconnectivity.testapp.views.LastExecutedViewMultiline;
import com.carconnectivity.testapp.views.SuccesView;
import com.carconnectivity.testapp.views.TextViewString;
import com.mirrorlink.android.commonapi.Defs;
import com.mirrorlink.android.commonapi.IEventMappingListener;
import com.mirrorlink.android.commonapi.IEventMappingManager;

public class MirrorLinkEventMappingInformation extends BaseActivity {
	TextViewString keyEventValue = null;
	TextViewString localEvent = null;

	SuccesView success = null;
	Button getClientMappingConfiguration = null;

	SuccesView callbackFired = null;
	LastExecutedViewMultiline callbackFiredList = null;
	Button clearCallbackList = null;

	ListView mEventsList = null;
	
	private IEventMappingManager mEventMappingManager = null;	
	
	@Override
	protected void onResume()
	{
		mEventMappingManager = getMirrorLinkApplicationContext().registerEventMappingManager(this, mEventMappingListener);
		if (mEventMappingManager == null)
		{
			finish();
		}
		super.onResume();
	}
	@Override
	protected void onPause()
	{
		getMirrorLinkApplicationContext().unregisterEventMappingManager(this, mEventMappingListener);
		super.onPause();
	}
	

	IEventMappingListener mEventMappingListener = new IEventMappingListener.Stub() {
		@Override
		public void onEventMappingChanged(Bundle eventMapping) throws RemoteException {
			runOnUiThread(new Runnable() {
				public void run() {
					updateValues();
					callbackFired.setValue(true);
					callbackFiredList.setNow();
				}
			});
		}

		@Override
		public void onEventConfigurationChanged(final Bundle eventConfiguration) throws RemoteException {
		}
	};

	@Override
	void updateValues() {
		List<Bundle> config = null;
		if (mEventMappingManager != null) {
			try {
				config = mEventMappingManager.getEventMappings();
			} catch (RemoteException e) {
				e.printStackTrace();
				success.setValue(false);
				return;
			}
		}
		if (config == null) {
			Toast.makeText(this, "Unable to receive event mappings.", Toast.LENGTH_LONG).show();
			success.setValue(false);
			return;
		}
		fillData(config);
		success.setValue(true);
	}

	void fillData(List<Bundle> eventMappingList) {
		List<HashMap<String, String>> mEventsListMap = new ArrayList<HashMap<String, String>>();

		for (Bundle b : eventMappingList) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("local", "Local: 0x"+ Integer.toHexString(b.getInt(Defs.EventMapping.LOCAL_EVENT)));
			map.put("remote", "Remote: 0x"+ Integer.toHexString(b.getInt(Defs.EventMapping.REMOTE_EVENT)));
			mEventsListMap.add(map);
		}

		SimpleAdapter adapter = new SimpleAdapter(this, mEventsListMap, android.R.layout.simple_list_item_2, new String[] { "local", "remote" }, new int[] {
				android.R.id.text1, android.R.id.text2 });
		mEventsList.setAdapter(adapter);

		// Workaround for listView in scroll view
		// http://nex-otaku-en.blogspot.com/2010/12/android-put-listview-in-scrollview.html

		int totalHeight = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			View listItem = adapter.getView(i, null, mEventsList);
			listItem.measure(0, MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = mEventsList.getLayoutParams();
		params.height = totalHeight + (mEventsList.getDividerHeight() * (mEventsList.getCount() - 1));
		mEventsList.setLayoutParams(params);
	}

	@Override
	ScrollView buildUI() {
		ScrollView scrollView = new ScrollView(this);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		mEventsList = new ListView(this);
		layout.addView(mEventsList);

		success = new SuccesView(this, "Success", false);
		layout.addView(success);

		getClientMappingConfiguration = new Button(this);
		layout.addView(getClientMappingConfiguration);
		getClientMappingConfiguration.setText("Get Client Event Mapping");
		getClientMappingConfiguration.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateValues();
			}
		});

		layout.addView(new HeaderView(this, "callbacks"));
		callbackFired = new SuccesView(this, "Callback fired", false);
		layout.addView(callbackFired);
		callbackFiredList = new LastExecutedViewMultiline(this);
		layout.addView(callbackFiredList);
		clearCallbackList = new Button(this);
		layout.addView(clearCallbackList);
		clearCallbackList.setText("Clear callback logs");
		clearCallbackList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callbackFiredList.clear();
				callbackFired.setValue(false);
			}
		});

		scrollView.addView(layout);
		return scrollView;
	}

}
