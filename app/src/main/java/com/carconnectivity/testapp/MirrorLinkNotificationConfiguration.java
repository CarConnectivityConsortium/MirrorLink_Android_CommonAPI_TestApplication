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


import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.carconnectivity.testapp.views.LastExecutedViewMultiline;
import com.carconnectivity.testapp.views.SuccesView;
import com.carconnectivity.testapp.views.TextViewInt;
import com.mirrorlink.android.commonapi.Defs;
import com.mirrorlink.android.commonapi.INotificationListener;
import com.mirrorlink.android.commonapi.INotificationManager;

public class MirrorLinkNotificationConfiguration extends BaseActivity {
	
	SuccesView notificationUISupported = null;
	
	TextViewInt maxActions = null;
	TextViewInt maxActionsNameLen = null;
	TextViewInt maxActionsTitleLen = null;
	TextViewInt maxActionsBodyLen = null;
	
	Button getNotificationConfiguration = null;
	
	SuccesView callbackFired = null;
	LastExecutedViewMultiline callbackFiredList = null;
	Button clearCallbackList =null;
	
	private INotificationManager notificationManager = null;
	
	@Override
	protected void onResume()
	{
		notificationManager = getMirrorLinkApplicationContext().registerNotificationManager(this,mNotificationListener);
		try {
			if (notificationManager != null)
			{
				Bundle notiConfig = notificationManager.getNotificationConfiguration();
				fillData(notiConfig);
			}
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		if (notificationManager == null)
		{
			Toast.makeText(this, "Unable to get notification manager.", Toast.LENGTH_LONG).show();
			finish();
		}
		super.onResume();
	}
	@Override
	protected void onPause()
	{
		getMirrorLinkApplicationContext().unregisterNotificationManager(this, mNotificationListener);
		super.onPause();
	}
	
	

	INotificationListener mNotificationListener= new INotificationListener.Stub()
	{

		@Override
		public void onNotificationEnabledChanged(final boolean notiEnabled) throws RemoteException {

			
		}

		@Override
		public void onNotificationConfigurationChanged(final Bundle notificationConfiguration) throws RemoteException {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					fillData(notificationConfiguration);
					callbackFiredList.setNow();
					callbackFired.setValue(true);
				}
			});
			
		}

		@Override
		public void onNotificationActionReceived(int notificationId, int actionId) throws RemoteException {
		}

	};
	
	
	void fillData(Bundle data)
	{
		notificationUISupported.setValue(data.getBoolean(Defs.NotificationConfiguration.NOTIFICATION_SUPPORTED));
		maxActions.setValue(data.getInt(Defs.NotificationConfiguration.MAX_ACTIONS));
		maxActionsNameLen.setValue(data.getInt(Defs.NotificationConfiguration.MAX_ACTION_NAME_LENGTH));
		maxActionsTitleLen.setValue(data.getInt(Defs.NotificationConfiguration.MAX_TITLE_LENGTH));
		maxActionsBodyLen.setValue(data.getInt(Defs.NotificationConfiguration.MAX_BODY_LENGTH));
	}
	
	
	ScrollView buildUI()
	{
		ScrollView scrollView = new ScrollView(this);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		notificationUISupported = new SuccesView(this, "Notifiations UI Support", false);
		layout.addView(notificationUISupported);
		
		maxActions = new TextViewInt(this, "Max actions", 0);
		layout.addView(maxActions);
		maxActionsNameLen  = new TextViewInt(this, "Max action name length", 0);
		layout.addView(maxActionsNameLen);
		maxActionsTitleLen = new TextViewInt(this, "Max action title lenght", 0);
		layout.addView(maxActionsTitleLen);
		maxActionsBodyLen = new TextViewInt(this, "Max action body length", 0);
		layout.addView(maxActionsBodyLen);
		
		getNotificationConfiguration = new Button(this);
		layout.addView(getNotificationConfiguration);
		getNotificationConfiguration.setText("Get notification configuration");
		getNotificationConfiguration.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (notificationManager != null)
				{
					try {
						Bundle notiConfig = null;
						notiConfig = notificationManager.getNotificationConfiguration();
						fillData(notiConfig);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					
				}
				
			}
		});
		
		
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
