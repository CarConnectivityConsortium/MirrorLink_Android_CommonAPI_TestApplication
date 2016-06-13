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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.carconnectivity.testapp.views.LastExecutedView;
import com.carconnectivity.testapp.views.LastExecutedViewMultiline;
import com.carconnectivity.testapp.views.SuccesView;
import com.carconnectivity.testapp.views.TextViewString;
import com.mirrorlink.android.commonapi.IDeviceInfoListener;
import com.mirrorlink.android.commonapi.IDeviceInfoManager;

public class MirrorLinkClientInformation extends BaseActivity {
	private final static String LOG_TAG = MirrorLinkClientInformation.class.getCanonicalName();
	TextViewString clientIdentifier = null;
	TextViewString friendlyname = null;
	TextViewString manufacter = null;
	TextViewString modelName = null;
	TextViewString modelNumber = null;

	SuccesView clientInformationSucceed = null;
	LastExecutedView clientInformationLastExecuted = null;
	Button getClientInfo = null;

	SuccesView clientInformationCallbackFired = null;
	LastExecutedViewMultiline clientInformationCallbackFiredList = null;
	Button clearCallbackFiredIndicator = null;

	private IDeviceInfoManager deviceInfoManager = null;

	@Override
	protected void onResume()
	{
		deviceInfoManager = getMirrorLinkApplicationContext().registerDeviceInfoManager(this, mDeviceInfoListener);
		if (deviceInfoManager == null)
		{
			finish();
		}
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
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Log.v(LOG_TAG, "onDeviceInfoChanged");
					clientIdentifier.setValue(clientInformation.getString(com.mirrorlink.android.commonapi.Defs.ClientInformation.CLIENT_IDENTIFIER));
					friendlyname.setValue(clientInformation.getString(com.mirrorlink.android.commonapi.Defs.ClientInformation.CLIENT_FRIENDLY_NAME));
					manufacter.setValue(clientInformation.getString(com.mirrorlink.android.commonapi.Defs.ClientInformation.CLIENT_MANUFACTURER));
					modelName.setValue(clientInformation.getString(com.mirrorlink.android.commonapi.Defs.ClientInformation.CLIENT_MODEL_NAME));
					modelNumber.setValue(clientInformation.getString(com.mirrorlink.android.commonapi.Defs.ClientInformation.CLIENT_MODEL_NUMBER));
					clientInformationCallbackFired.setValue(true);
					clientInformationCallbackFiredList.setNow();
				}
			});
		}
	};

	@Override
	void updateValues() {
		Bundle clientInfo = null;

		if (deviceInfoManager != null)
		{
			
			try {
				clientInfo = deviceInfoManager.getMirrorLinkClientInformation();
			} catch (RemoteException e) {
				e.printStackTrace();
				clientInformationSucceed.setValue(false);
				return;
			}
	
			if (clientInfo == null) {
				clientInformationSucceed.setValue(false);
				return;
			}
	
			clientIdentifier.setValue(clientInfo.getString(com.mirrorlink.android.commonapi.Defs.ClientInformation.CLIENT_IDENTIFIER));
			friendlyname.setValue(clientInfo.getString(com.mirrorlink.android.commonapi.Defs.ClientInformation.CLIENT_FRIENDLY_NAME));
			manufacter.setValue(clientInfo.getString(com.mirrorlink.android.commonapi.Defs.ClientInformation.CLIENT_MANUFACTURER));
			modelName.setValue(clientInfo.getString(com.mirrorlink.android.commonapi.Defs.ClientInformation.CLIENT_MODEL_NAME));
			modelNumber.setValue(clientInfo.getString(com.mirrorlink.android.commonapi.Defs.ClientInformation.CLIENT_MODEL_NUMBER));
			clientInformationSucceed.setValue(true);
			clientInformationLastExecuted.setNow();
		}
		else
		{
			Toast.makeText(this, "Unable to get display info manager.", Toast.LENGTH_LONG).show();
			finish();
		}
	}

	@Override
	ScrollView buildUI() {
		ScrollView scrollView = new ScrollView(this);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		clientIdentifier = new TextViewString(this, "Client identifier", "n/a");
		layout.addView(clientIdentifier);

		friendlyname = new TextViewString(this, "Friendly name", "n/a");
		layout.addView(friendlyname);
		manufacter = new TextViewString(this, "Manufacter", "n/a");
		layout.addView(manufacter);
		modelName = new TextViewString(this, "Model name", "n/a");
		layout.addView(modelName);
		modelNumber = new TextViewString(this, "Model number", "n/a");
		layout.addView(modelNumber);

		clientInformationSucceed = new SuccesView(this, "GetClientInfo succeded", false);
		layout.addView(clientInformationSucceed);

		clientInformationLastExecuted = new LastExecutedView(this);
		layout.addView(clientInformationLastExecuted);

		getClientInfo = new Button(this);
		getClientInfo.setText("Get Client info");
		layout.addView(getClientInfo);

		getClientInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateValues();
			}
		});

		clientInformationCallbackFired = new SuccesView(this, "GetClientInfo callback fired", false);
		layout.addView(clientInformationCallbackFired);

		clientInformationCallbackFiredList = new LastExecutedViewMultiline(this);
		layout.addView(clientInformationCallbackFiredList);

		clearCallbackFiredIndicator = new Button(this);
		clearCallbackFiredIndicator.setText("Clear callback fired indicator");
		layout.addView(clearCallbackFiredIndicator);

		clearCallbackFiredIndicator.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clientInformationCallbackFired.setValue(false);
				clientInformationCallbackFiredList.clear();

			}
		});
		scrollView.addView(layout);
		return scrollView;
	}
}
