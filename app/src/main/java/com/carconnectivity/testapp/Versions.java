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
import com.carconnectivity.testapp.views.LastExecutedView;
import com.carconnectivity.testapp.views.SuccesView;
import com.carconnectivity.testapp.views.TextViewInt;
import com.mirrorlink.android.commonapi.IDeviceInfoListener;
import com.mirrorlink.android.commonapi.IDeviceInfoManager;

public class Versions extends BaseActivity {
	TextViewInt apiVersion =null;
	LastExecutedView apiVersionLastExecuted = null;
	
	TextViewInt mirrorLinkMajorVersion =null;
	TextViewInt mirrorLinkMinorVersion =null;
	SuccesView mlVersionSuccess = null;
	LastExecutedView mirrorLinkVersionLastExecuted = null;
	
	Button bGetCommonApiVersion = null;
	Button bGetMirrorLinkVersion = null;

	IDeviceInfoManager deviceInfoManager = null;

	@Override
	protected void onResume()
	{

		deviceInfoManager = getMirrorLinkApplicationContext().registerDeviceInfoManager(this, mDeviceInfoListener);
		if (deviceInfoManager == null)
		{
			Toast.makeText(this, "Unable to get device info manager.", Toast.LENGTH_LONG).show();
			finish();
		}
		getApiVersion();
		getMLVersion();
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
					mirrorLinkMajorVersion.setValue(clientInformation.getInt(com.mirrorlink.android.commonapi.Defs.ClientInformation.VERSION_MAJOR));
					mirrorLinkMinorVersion.setValue(clientInformation.getInt(com.mirrorlink.android.commonapi.Defs.ClientInformation.VERSION_MINOR));
				}
			});
		}
	};

	
	
	
	void getApiVersion()
	{
		int version = 0;
		try {
			if (getMirrorLinkApplicationContext().getService() != null)
			{
				version = getMirrorLinkApplicationContext().getService().getCommonAPIServiceApiLevel();
			}
			
		} catch (RemoteException e) {
			e.printStackTrace();
			return;
		}
		apiVersion.setValue(version);
		apiVersionLastExecuted.setNow();
	}
	void getMLVersion()
	{
		try {
			if (deviceInfoManager != null)
			{
				mirrorLinkMajorVersion.setValue(deviceInfoManager.getMirrorLinkSessionVersionMajor());
				mirrorLinkMinorVersion.setValue(deviceInfoManager.getMirrorLinkSessionVersionMinor());
				mirrorLinkVersionLastExecuted.setNow();
				mlVersionSuccess.setValue(true);
			}

		} catch (RemoteException e) {
			e.printStackTrace();
			mlVersionSuccess.setValue(false);
			return;
		}
	}
	
	
	ScrollView buildUI()
	{
		ScrollView scrollView = new ScrollView(this);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		HeaderView apiVersionHeader = new HeaderView(this,"Common API version");
		layout.addView(apiVersionHeader);
		
		apiVersion = new TextViewInt(this,"Common API Version",0);
		layout.addView(apiVersion);
		
		
		apiVersionLastExecuted = new LastExecutedView(this);
		layout.addView(apiVersionLastExecuted);
		
		
		bGetCommonApiVersion = new Button(this);
		bGetCommonApiVersion.setText("Get Common API Version");
		
		bGetCommonApiVersion.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				getApiVersion();
			}
		});
		layout.addView(bGetCommonApiVersion);
		
		HeaderView mlVersionHeader = new HeaderView(this,"MirrorLink version");
		layout.addView(mlVersionHeader);
		
		mirrorLinkMajorVersion =new TextViewInt(this,"MirrorLink Major Version",0);
		layout.addView(mirrorLinkMajorVersion);
		mirrorLinkMinorVersion =new TextViewInt(this,"MirrorLink Minor Version",0);
		layout.addView(mirrorLinkMinorVersion);
		
		mirrorLinkVersionLastExecuted = new LastExecutedView(this);
		layout.addView(mirrorLinkVersionLastExecuted);
		
		mlVersionSuccess = new SuccesView(this, "Get mirrorlink version succeded", false);
		layout.addView(mlVersionSuccess);
		
		
		bGetMirrorLinkVersion = new Button(this);
		bGetMirrorLinkVersion.setText("Get MirrorLink Version");
		
		bGetMirrorLinkVersion.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				getMLVersion();
			}
		});
		layout.addView(bGetMirrorLinkVersion);
		
		scrollView.addView(layout);
		return scrollView;
	}

}
