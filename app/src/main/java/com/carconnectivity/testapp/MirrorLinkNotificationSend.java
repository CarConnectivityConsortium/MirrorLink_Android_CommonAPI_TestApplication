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
import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.carconnectivity.testapp.views.HeaderView;
import com.carconnectivity.testapp.views.IntInputView;
import com.carconnectivity.testapp.views.LastExecutedViewMultiline;
import com.carconnectivity.testapp.views.SuccesView;
import com.carconnectivity.testapp.views.TextInputView;
import com.carconnectivity.testapp.views.TextViewInt;
import com.carconnectivity.testapp.views.TextViewString;
import com.mirrorlink.android.commonapi.Defs;
import com.mirrorlink.android.commonapi.INotificationListener;
import com.mirrorlink.android.commonapi.INotificationManager;

public class MirrorLinkNotificationSend extends BaseActivity {

	TextViewInt mlNotificationId = null;
	TextInputView notificationTitle = null;
	TextInputView notificationBody =null;
	Button sendNotification = null;
	TextViewString actionListText = null;
	
	TextViewString iconUrlListText = null;

	
	TextViewInt vncNotificationId = null;
	Button sendVncNotification = null;
	
	IntInputView cancelNotificationId = null;
	Button cancelNotificationButton =null;
	
	TextViewInt notificationId = null;
	TextViewInt notificationActionId = null;
	
	SuccesView callbackFired = null;
	LastExecutedViewMultiline callbackFiredList = null;
	Button clearCallbackList =null;
	
	private INotificationManager notificationManager = null;

	@Override
	protected void onResume()
	{
		notificationManager = getMirrorLinkApplicationContext().registerNotificationManager(this, mNotificationListener);

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
	
	
	
	INotificationListener mNotificationListener = new INotificationListener.Stub()
	{

		@Override
		public void onNotificationEnabledChanged(final boolean notiEnabled) throws RemoteException {
			
		}

		@Override
		public void onNotificationConfigurationChanged(Bundle notificationConfiguration) throws RemoteException {
			
		}

		@Override
		public void onNotificationActionReceived(final int nId, final int aId) throws RemoteException {
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					notificationId.setValue(nId);
					notificationActionId.setValue(aId);
					callbackFired.setValue(true);
					callbackFiredList.setNow();
					
				}
			});
			
		}

	};

	ScrollView buildUI()
	{
		ScrollView scrollView = new ScrollView(this);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		
		layout.addView(new HeaderView(this,"MirrorLink Notification"));
		mlNotificationId = new TextViewInt(this, "Notification ID", 0);
		layout.addView(mlNotificationId);
		
		notificationTitle = new TextInputView(this, "Title","Enter Title");
		layout.addView(notificationTitle);
		notificationBody = new TextInputView(this, "Body", "Enter body");
		layout.addView(notificationBody);
		
		
		actionListText = new TextViewString(this,"Action list", "OK");
		actionListText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MirrorLinkNotificationSend.this);
				
				final CharSequence[] items = { "Okay", "Okay, Cancel", "Yes, No, Cancel", "Abort" };

				builder.setTitle("Select actions");
			    builder.setSingleChoiceItems(items, 1,
		                new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog, int item) {
		                    	actionListText.setValue(items[item].toString());
		                    	dialog.cancel();
		                    }
		                });
				AlertDialog dialog = builder.create();	
				dialog.show();
			}	
		});
		layout.addView(actionListText);
		
		
		iconUrlListText = new TextViewString(this,"Icon URL", "none");
		iconUrlListText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MirrorLinkNotificationSend.this);
				
				final CharSequence[] items = { "http://Icon1", "http://Icon2", "http://Icon3", "http://Icon4" };

				builder.setTitle("Select icon");
			    builder.setSingleChoiceItems(items, 1,
		                new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog, int item) {
		                    	iconUrlListText.setValue(items[item].toString());
		                    	dialog.cancel();
		                    }
		                });
				AlertDialog dialog = builder.create();	
				dialog.show();
			}	
		});
		layout.addView(iconUrlListText);
	
		sendNotification = new Button(this);
		sendNotification.setText("Send Notification");
		sendNotification.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					List<String> actionList = Arrays.asList(actionListText.getValue().split(","));
					Uri iconUrl = Uri.parse(iconUrlListText.getValue());
					
					List<Bundle> actions = new ArrayList<Bundle>(); 

					for (int i=0;i<actionList.size();i++)
					{
						Bundle action =  new Bundle();
					    action.putInt(Defs.Action.ACTION_ID, i+1);
					    action.putString(Defs.Action.ACTION_NAME, actionList.get(i));
					    action.putBoolean(Defs.Action.LAUNCH_APP, false);
					        
					    actions.add(action);
					}
					
			      

					
					int response = notificationManager.sendClientNotification(notificationTitle.getValue(), notificationBody.getValue(), iconUrl, actions);
					if (response == 0)
					{
						Toast.makeText(getBaseContext(), "Sending notification failed." , Toast.LENGTH_LONG).show();
					}
					mlNotificationId.setValue(response);
					cancelNotificationId.setValue(response);
				} catch (RemoteException e) {
					e.printStackTrace();
				}	
			}
		});
		layout.addView(sendNotification);
		
		
		
		layout.addView(new HeaderView(this,"MirrorLink VNC Notification"));
		vncNotificationId = new TextViewInt(this, "Notification ID", 0);
		layout.addView(vncNotificationId);
		
		sendVncNotification = new Button(this);
		sendVncNotification.setText("Send VNC Notification");
		sendVncNotification.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					int response = notificationManager.sendVncNotification();
					if (response == 0)
					{
						Toast.makeText(getBaseContext(), "Sending notification failed." , Toast.LENGTH_LONG).show();
					}
					vncNotificationId.setValue(response);
					cancelNotificationId.setValue(response);
				} catch (RemoteException e) {
					e.printStackTrace();
				}	
			}
		});
		layout.addView(sendVncNotification);
		
		layout.addView(new HeaderView(this,"Cancel Notification"));
		
		cancelNotificationId= new IntInputView(this, "Notification ID", 0);
		layout.addView(cancelNotificationId);
		
		
		cancelNotificationButton =new Button(this);
		cancelNotificationButton.setText("Cancel notification");
		cancelNotificationButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					boolean response = notificationManager.cancelNotification(cancelNotificationId.getValue());
					if (response == false)
					{
						Toast.makeText(getBaseContext(), "Canceling notification failed." , Toast.LENGTH_LONG).show();
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		layout.addView(cancelNotificationButton);
		
		
		layout.addView(new HeaderView(this,"Notification actions"));
		notificationId= new TextViewInt(this, "Notification ID", 0);
		layout.addView(notificationId);
		
		notificationActionId = new TextViewInt(this, "Action ID", 0);
		layout.addView(notificationActionId);
		
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
