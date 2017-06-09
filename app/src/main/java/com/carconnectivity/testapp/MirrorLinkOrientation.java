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


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
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
import com.carconnectivity.testapp.views.TextViewString;
import com.mirrorlink.android.commonapi.IDeviceInfoManager;
import com.mirrorlink.android.commonapi.IDisplayListener;
import com.mirrorlink.android.commonapi.IDisplayManager;

public class MirrorLinkOrientation extends BaseActivity {
	TextViewString orientation = null;
	int orientationVal= -1;
	
	int mLastOrientation = -1;
	
	SuccesView succesView = null;
	
	Button setFramebufferOrientation = null;
	
	SuccesView callbackFired = null;
	LastExecutedViewMultiline callbackFiredList = null;
	Button clearCallbackList =null;

	private IDisplayManager mDisplayManager = null;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		if (savedInstanceState!=null)
		{
			mLastOrientation = savedInstanceState.getInt("mLastOrientation");
			orientationVal = savedInstanceState.getInt("orientationVal");
			orientation.setValue(savedInstanceState.getString("orientation"));
			succesView.setValue(savedInstanceState.getBoolean("succesView"));
						
			
			
			callbackFired.setValue(savedInstanceState.getBoolean("callbackFired"));
			callbackFiredList.setRawValue(savedInstanceState.getLongArray("callbackFiredList"));
			
		    if (mLastOrientation != getResources().getConfiguration().orientation ) 
		    {
		    	callbackFiredList.setNow();
		    	callbackFired.setValue(true);
		    	mLastOrientation = getResources().getConfiguration().orientation;
		    }
		}

		
	}
	
	@Override        
    protected void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putInt("mLastOrientation", mLastOrientation);
		savedInstanceState.putInt("orientationVal", orientationVal);
		savedInstanceState.putString("orientation", orientation.getValue());	
		savedInstanceState.putBoolean("succesView", succesView.getValue());
		savedInstanceState.putBoolean("callbackFired", callbackFired.getValue());
		savedInstanceState.putLongArray("callbackFiredList", callbackFiredList.getRawValues());
		super.onSaveInstanceState(savedInstanceState);      
    }
	
	@Override
	protected void onResume()
	{
		mDisplayManager = getMirrorLinkApplicationContext().registerDisplayManager(this, mDisplayListener);
		if (mDisplayManager == null)
		{
			Toast.makeText(this, "Unable to get display manager.", Toast.LENGTH_LONG).show();
			finish();
		}
		super.onResume();
	}
	@Override
	protected void onPause()
	{
		getMirrorLinkApplicationContext().unregisterDisplayManager(this, mDisplayListener);
		super.onPause();
	}
	
	
	
	IDisplayListener mDisplayListener = new IDisplayListener.Stub() {
		@Override
		public void onDisplayConfigurationChanged(final Bundle displayConfiguration) throws RemoteException {
		}

		@Override
		public void onPixelFormatChanged(Bundle pixelFormat) throws RemoteException {
			
		}
	};
	
	@Override
	ScrollView buildUI()
	{
		ScrollView scrollView = new ScrollView(this);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		orientation = new TextViewString(this,"Supported orientation", "Select orientation support");
		orientation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MirrorLinkOrientation.this);
				
				
				final CharSequence[] items = { "Landscape only", "Portrait only", "Landscape and Portrait" };

				builder.setTitle("Select actions");
			    builder.setSingleChoiceItems(items, 0,
		                new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog, int item) {
		                    	orientation.setValue(items[item].toString());
		                    	orientationVal = item+1;
		                    	dialog.cancel();
		                    }
		                });
				AlertDialog dialog = builder.create();	
				dialog.show();
			}	
		});
		layout.addView(orientation);
		
		setFramebufferOrientation = new Button(this);
		setFramebufferOrientation.setText("Set framebuffer orientation support");
		setFramebufferOrientation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				succesView.setValue(false);
				if (orientationVal == 1)
				{
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					succesView.setValue(true);
				}
				else if (orientationVal == 2)
				{
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					succesView.setValue(true);
				}
				else if (orientationVal == 3)
				{
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
					succesView.setValue(true);
				}
			}
		});
		layout.addView(setFramebufferOrientation);
		
		succesView = new SuccesView(this,"Set orientation support success", false);
		layout.addView(succesView);
		
		
		
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
