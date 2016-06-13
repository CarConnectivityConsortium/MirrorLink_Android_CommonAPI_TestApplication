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

import com.carconnectivity.testapp.views.LastExecutedView;
import com.carconnectivity.testapp.views.SuccesView;
import com.mirrorlink.android.commonapi.IDeviceInfoListener;
import com.mirrorlink.android.commonapi.IDeviceInfoManager;
import com.mirrorlink.lib.MirrorLinkApplicationContext;
import com.mirrorlink.lib.ServiceReadyCallback;

public class MirrorLinkVrtKeyboard extends BaseActivity {

	SuccesView vkbAvailable = null;
	SuccesView vkbTouchSupport = null;
	SuccesView vkbKnobSupport = null;
	SuccesView vkbDriveMode = null;

	LastExecutedView callbackFiredTime = null;
	Button getVirtualKeyboardSupport = null;

	IDeviceInfoManager deviceInfoManager = null;
	
	@Override
	protected void onResume()
	{
		ApplicationContext.getContext(new ServiceReadyCallback() {
			@Override
			public void connected(MirrorLinkApplicationContext mirrorLinkApplicationContext) {
				deviceInfoManager = mirrorLinkApplicationContext.registerDeviceInfoManager(this, mDeviceInfoListener);

				if (deviceInfoManager == null) {
					finish();
				}
			}
		});
		super.onResume();
	}
	@Override
	protected void onPause()
	{
		getMirrorLinkApplicationContext().unregisterDeviceInfoManager(this, mDeviceInfoListener);
		super.onPause();
	}
	
	
	
	IDeviceInfoListener mDeviceInfoListener = new IDeviceInfoListener.Stub() {
		@Override
		public void onDeviceInfoChanged(final Bundle clientInformation) throws RemoteException {

		}
	};


	
	void updateValues()
	{
		try {
			if (deviceInfoManager != null)
			{
				Bundle vkbSupport = deviceInfoManager.getServerVirtualKeyboardSupport();
				
				if(vkbSupport != null)
				{
					vkbAvailable.setValue(vkbSupport.getBoolean("AVAILABLE"));
					vkbTouchSupport.setValue(vkbSupport.getBoolean("TOUCH_SUPPORT"));
					vkbKnobSupport.setValue(vkbSupport.getBoolean("KNOB_SUPPORT"));
					vkbDriveMode.setValue(vkbSupport.getBoolean("DRIVE_MODE"));
					callbackFiredTime.setNow();
				}
				else
				{
					Toast.makeText(this, "Unable to get virtual keyboard support data." , Toast.LENGTH_LONG).show();
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}

			
		
	}

	@Override
	ScrollView buildUI()
	{
		ScrollView scrollView = new ScrollView(this);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		vkbAvailable = new SuccesView(this,"Availability of a virtual keyboard", false);
		layout.addView(vkbAvailable);
		vkbTouchSupport = new SuccesView(this,"Touch events support", false);
		layout.addView(vkbTouchSupport);
		vkbKnobSupport = new SuccesView(this,"Knob events support", false);
		layout.addView(vkbKnobSupport);
		vkbDriveMode = new SuccesView(this,"Drive mode compatibile", false);
		layout.addView(vkbDriveMode);

		
		callbackFiredTime = new LastExecutedView(this);
		layout.addView(callbackFiredTime);
		
		getVirtualKeyboardSupport = new Button(this);
		layout.addView(getVirtualKeyboardSupport);
		
		getVirtualKeyboardSupport.setText("Get Virtual Keyboard Support");
		getVirtualKeyboardSupport.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateValues();
			}
		});
		

		scrollView.addView(layout);
		return scrollView;
	}
	

}
