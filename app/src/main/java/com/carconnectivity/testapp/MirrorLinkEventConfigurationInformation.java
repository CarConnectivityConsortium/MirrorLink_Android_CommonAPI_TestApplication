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

import com.carconnectivity.testapp.views.HeaderView;
import com.carconnectivity.testapp.views.LastExecutedViewMultiline;
import com.carconnectivity.testapp.views.SuccesView;
import com.carconnectivity.testapp.views.TextViewString;
import com.mirrorlink.android.commonapi.Defs;
import com.mirrorlink.android.commonapi.IEventMappingListener;
import com.mirrorlink.android.commonapi.IEventMappingManager;

public class MirrorLinkEventConfigurationInformation extends BaseActivity {
	TextViewString knobSupportMask = null;
	TextViewString deviceKeySupportMask = null;
	TextViewString multimediaKeySupportMask = null;
	TextViewString functionKeySupportMask = null;
	SuccesView ituKeySupport = null;
	TextViewString touchEventSupport = null;
	TextViewString pressureMask = null;
	
	SuccesView success = null;
	
	Button getClientEventConfiguration = null;
	
	SuccesView callbackFired= null;
	LastExecutedViewMultiline callbackFiredList = null;
	Button clearCallbackList = null;
	
	private IEventMappingManager mEventMappingManager = null;
	
	@Override
	protected void onResume()
	{
		mEventMappingManager = getMirrorLinkApplicationContext().registerEventMappingManager(this, mEventMappingListener);

		if (mEventMappingManager== null)
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
		}
		
		@Override
		public void onEventConfigurationChanged(final Bundle eventConfiguration) throws RemoteException {
			runOnUiThread(new Runnable() {
			     public void run() {
			    	 fillData(eventConfiguration);
			    	 callbackFired.setValue(true);
			    	 callbackFiredList.setNow();
			    }
			});
		}
	};
	
	@Override
	void updateValues()
	{
		Bundle config = null;
		if (mEventMappingManager != null)
		{
			try {
				config = mEventMappingManager .getEventConfiguration();
			} catch (RemoteException e) {
				e.printStackTrace();
				success.setValue(false);
				return;
			}
		}
		if (config==null)
		{
			Toast.makeText(this, "Unable to get client event configuration.", Toast.LENGTH_LONG).show();
			success.setValue(false);
			return;
		}
		fillData(config);
		success.setValue(true);

	}
	
	void fillData(Bundle eventConfig)
	{
		knobSupportMask.setValue("0x" + Long.toHexString(eventConfig.getInt(Defs.EventConfiguration.KNOB_KEY_SUPPORT)));
		deviceKeySupportMask.setValue("0x" + Long.toHexString(eventConfig.getInt(Defs.EventConfiguration.DEVICE_KEY_SUPPORT)));
		multimediaKeySupportMask.setValue("0x" + Long.toHexString(eventConfig.getInt(Defs.EventConfiguration.MULTIMEDIA_KEY_SUPPORT)));
		functionKeySupportMask.setValue("0x" + Long.toHexString(eventConfig.getInt(Defs.EventConfiguration.FUNC_KEY_SUPPORT)));
		ituKeySupport.setValue(eventConfig.getBoolean(Defs.EventConfiguration.ITU_KEY_SUPPORT));
		touchEventSupport.setValue("0x" +Integer.toHexString(eventConfig.getInt(Defs.EventConfiguration.TOUCH_SUPPORT)));
		pressureMask.setValue("0x" +Integer.toHexString(eventConfig.getInt(Defs.EventConfiguration.PRESSURE_MASK)));
	}
	
	@Override
	ScrollView buildUI()
	{
		ScrollView scrollView = new ScrollView(this);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		knobSupportMask = new TextViewString(this, "Knob support bitmask", "n/a");
		layout.addView(knobSupportMask);
		deviceKeySupportMask = new TextViewString(this, "Device key support bitmask", "n/a");
		layout.addView(deviceKeySupportMask);
		multimediaKeySupportMask = new TextViewString(this, "Multimedia key support bitmask", "n/a");
		layout.addView(multimediaKeySupportMask);
		functionKeySupportMask = new TextViewString(this, "Function key support bitmask", "n/a");
		layout.addView(functionKeySupportMask);
		ituKeySupport = new SuccesView(this, "ITU key support ", false);
		layout.addView(ituKeySupport);
		touchEventSupport = new TextViewString(this, "Touch event support bitmask",  "n/a");
		layout.addView(touchEventSupport);
		pressureMask = new TextViewString(this, "Pressure bitmask", "n/a");
		layout.addView(pressureMask);
		
		success =  new SuccesView(this, "Success", false);
		layout.addView(success);
		
		getClientEventConfiguration = new Button(this);
		layout.addView(getClientEventConfiguration);
		getClientEventConfiguration.setText("Get Client Event Configuration");
		getClientEventConfiguration.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateValues();
			}
		});
		
		
		layout.addView(new HeaderView(this,"callbacks"));
		callbackFired= new SuccesView(this,"Callback fired",false);
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
