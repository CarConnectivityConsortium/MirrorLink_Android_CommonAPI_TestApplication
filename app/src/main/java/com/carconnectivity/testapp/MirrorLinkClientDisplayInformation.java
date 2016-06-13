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
import com.carconnectivity.testapp.views.TextViewInt;
import com.mirrorlink.android.commonapi.Defs;
import com.mirrorlink.android.commonapi.IDisplayListener;
import com.mirrorlink.android.commonapi.IDisplayManager;
import com.mirrorlink.lib.MirrorLinkApplicationContext;
import com.mirrorlink.lib.ServiceReadyCallback;

public class MirrorLinkClientDisplayInformation extends BaseActivity {
	TextViewInt serverHorizontalResolution = null;
	TextViewInt serverVerticalResolution = null;
	TextViewInt clientHorizontalResolution = null;
	TextViewInt clientVerticalResolution = null;

	TextViewInt width = null;
	TextViewInt height = null;
	TextViewInt distance = null;
	SuccesView success = null;

	Button getClientDisplayInformation = null;

	SuccesView callbackFired = null;
	LastExecutedViewMultiline callbackFiredList = null;
	Button clearCallbackList = null;

	IDisplayManager displayManager = null;

	@Override
	protected void onResume()
	{
		displayManager = getMirrorLinkApplicationContext().registerDisplayManager(this, mDisplayListener);
		if (displayManager == null){
			finish();
		}
		super.onResume();

	}
	@Override
	protected void onPause()
	{
		getMirrorLinkApplicationContext().unregisterDisplayManager(this,mDisplayListener);
		super.onPause();
	}
	
	IDisplayListener mDisplayListener = new IDisplayListener.Stub() {


		@Override
		public void onDisplayConfigurationChanged(final Bundle displayConfiguration) throws RemoteException {
			runOnUiThread(new Runnable() {
				public void run() {
					fillData(displayConfiguration);
					callbackFired.setValue(true);
					callbackFiredList.setNow();
				}
			});
		}

		@Override
		public void onPixelFormatChanged(Bundle pixelFormat) throws RemoteException {
			
		}
	};

	@Override
	void updateValues() {
		Bundle config = null;
		if (displayManager != null)
		{
			try {
				config = displayManager.getDisplayConfiguration();
			} catch (RemoteException e) {
				e.printStackTrace();
				success.setValue(false);
				return;
			}
		}
		if (config == null) {
			Toast.makeText(this, "Unable to get display configuration.", Toast.LENGTH_LONG).show();
			success.setValue(false);
			return;
		}
		fillData(config);
		success.setValue(true);
	}

	void fillData(Bundle config) {
		serverHorizontalResolution.setValue(config.getInt(Defs.DisplayConfiguration.SERVER_PIXEL_WIDTH));
		serverVerticalResolution.setValue(config.getInt(Defs.DisplayConfiguration.SERVER_PIXEL_HEIGHT));

		clientHorizontalResolution.setValue(config.getInt(Defs.DisplayConfiguration.CLIENT_PIXEL_WIDTH));
		clientVerticalResolution.setValue(config.getInt(Defs.DisplayConfiguration.CLIENT_PIXEL_HEIGHT));

		width.setValue(config.getInt(Defs.DisplayConfiguration.MM_WIDTH));
		height.setValue(config.getInt(Defs.DisplayConfiguration.MM_HEIGHT));
		distance.setValue(config.getInt(Defs.DisplayConfiguration.DISTANCE));
	}

	@Override
	ScrollView buildUI() {
		ScrollView scrollView = new ScrollView(this);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		serverHorizontalResolution = new TextViewInt(this, "Server horizontal resolution [px]", 0);
		layout.addView(serverHorizontalResolution);
		serverVerticalResolution = new TextViewInt(this, "Server vertical resolution [px]", 0);
		layout.addView(serverVerticalResolution);
		
		clientHorizontalResolution = new TextViewInt(this, "Client horizontal resolution [px]", 0);
		layout.addView(clientHorizontalResolution);
		clientVerticalResolution = new TextViewInt(this, "Client vertical resolution [px]", 0);
		layout.addView(clientVerticalResolution);
		
		width = new TextViewInt(this, "Client width [mm]", 0);
		layout.addView(width);
		height = new TextViewInt(this, "Client height [mm]", 0);
		layout.addView(height);
		distance = new TextViewInt(this, "Distance [mm]", 0);
		layout.addView(distance);
		success = new SuccesView(this, "Get Client Display Config succeded", false);
		layout.addView(success);

		getClientDisplayInformation = new Button(this);
		layout.addView(getClientDisplayInformation);
		getClientDisplayInformation.setText("Get Client Display Configuration");
		getClientDisplayInformation.setOnClickListener(new OnClickListener() {
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
