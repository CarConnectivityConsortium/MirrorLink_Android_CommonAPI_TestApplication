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

import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.carconnectivity.testapp.views.HeaderView;
import com.carconnectivity.testapp.views.SuccesView;
import com.mirrorlink.android.commonapi.ICertificationManager;
import com.mirrorlink.android.commonapi.IConnectionManager;
import com.mirrorlink.android.commonapi.IContextManager;
import com.mirrorlink.android.commonapi.IDataServicesManager;
import com.mirrorlink.android.commonapi.IDeviceInfoManager;
import com.mirrorlink.android.commonapi.IDeviceStatusManager;
import com.mirrorlink.android.commonapi.IDisplayManager;
import com.mirrorlink.android.commonapi.IEventMappingManager;
import com.mirrorlink.android.commonapi.INotificationManager;
import com.mirrorlink.lib.MirrorLinkApplicationContext;
import com.mirrorlink.lib.ServiceReadyCallback;

public class MirrorLinkApiModules extends BaseActivity {
	SuccesView mCommonAPIModule = null;
	SuccesView mDeviceModule = null;
	SuccesView mCertification = null;
	SuccesView mConnectionModule = null;
	SuccesView mDisplayModule = null;
	SuccesView mEventModule = null;
	SuccesView mKeyboardModule = null;
	SuccesView mKeyEventModule = null;
	SuccesView mContextModule = null;
	SuccesView mStatusModule = null;
	SuccesView mServicesModule = null;
	SuccesView mNotificationsModule = null;
	SuccesView mWebModule = null;
	
	Button updateValues = null;

	IDeviceInfoManager mDeviceInfoManager = null;
	ICertificationManager mCertificationManager =  null;
	IConnectionManager mConnectionManager =  null;
	IDisplayManager mDisplayManager = null;
	IEventMappingManager mEventMappingManager = null;
	IContextManager mContextManager = null;
	IDeviceStatusManager mDeviceStatusManager = null;
	IDataServicesManager mDataServiceManager = null;
	INotificationManager mNotificationManager = null;
	
	@Override
	protected void onResume()
	{
		mConnectionManager = getMirrorLinkApplicationContext().registerConnectionManager(this, null);
		mContextManager = getMirrorLinkApplicationContext().registerContextManager(this, null);
		mDeviceInfoManager = getMirrorLinkApplicationContext().registerDeviceInfoManager(this, null);
		mDisplayManager = getMirrorLinkApplicationContext().registerDisplayManager(this, null);
		mCertificationManager = getMirrorLinkApplicationContext().registerCertificationManager(this, null);
		mDeviceStatusManager = getMirrorLinkApplicationContext().registerDeviceStatusManager(this, null);
		mDataServiceManager = getMirrorLinkApplicationContext().registerDataServicesManager(this, null);
		mNotificationManager = getMirrorLinkApplicationContext().registerNotificationManager(this, null);
		mEventMappingManager = getMirrorLinkApplicationContext().registerEventMappingManager(this, null);
		super.onResume();
	}
	protected void onPause()
	{
		getMirrorLinkApplicationContext().unregisterConnectionManager(this, null);
		getMirrorLinkApplicationContext().unregisterContextManager(this, null);
		getMirrorLinkApplicationContext().unregisterDeviceInfoManager(this, null);
		getMirrorLinkApplicationContext().unregisterDisplayManager(this, null);
		getMirrorLinkApplicationContext().unregisterCertificationManager(this, null);
		getMirrorLinkApplicationContext().unregisterDeviceStatusManager(this, null);	
		getMirrorLinkApplicationContext().unregisterDataServicesManager(this, null);
		getMirrorLinkApplicationContext().unregisterNotificationManager(this, null);
		getMirrorLinkApplicationContext().unregisterEventMappingManager(this, null);

		super.onPause();
	}

	@Override
	void updateValues() {
		ApplicationContext.getContext(new ServiceReadyCallback() {
			@Override
			public void connected(MirrorLinkApplicationContext mirrorLinkApplicationContext) {
				mCommonAPIModule.setValue(true);

				if (mDeviceInfoManager != null) {
					mDeviceModule.setValue(true);
				}
				else{
					mDeviceModule.setValue(false);
				}
				if (mCertificationManager != null) {
					mCertification.setValue(true);
				}
				else{
					mCertification.setValue(false);
				}
				if (mConnectionManager != null) {
					mConnectionModule.setValue(true);
				}
				else{
					mConnectionModule.setValue(false);
				}
				if (mDisplayManager != null) {
					mDisplayModule.setValue(true);
				}
				else{
					mDisplayModule.setValue(false);
				}
				if (mEventMappingManager != null) {
					mEventModule.setValue(true);
				}
				else{
					mEventModule.setValue(false);
				}

				if (mContextManager != null) {
					mContextModule.setValue(true);
				}
				else{
					mContextModule.setValue(false);
				}
				if (mDeviceStatusManager != null) {
					mStatusModule.setValue(true);
				}
				else{
					mStatusModule.setValue(false);
				}
				if (mDataServiceManager != null) {
					mServicesModule.setValue(true);
				}
				else{
					mServicesModule.setValue(false);
				}
				if (mNotificationManager != null) {
					mNotificationsModule.setValue(true);
				}
				else{
					mNotificationsModule.setValue(false);
				}
			}
		});
	}

	ScrollView buildUI() {
		ScrollView scrollView = new ScrollView(this);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		layout.addView(new HeaderView(this, "Common API modules support"));

		mCommonAPIModule = new SuccesView(this, "Common API Module", false);
		layout.addView(mCommonAPIModule);

		mDeviceModule = new SuccesView(this, "Device Info Module", false);
		layout.addView(mDeviceModule);

		mCertification = new SuccesView(this, "Certification Module", false);
		layout.addView(mCertification);

		mConnectionModule = new SuccesView(this, "Connection Module", false);
		layout.addView(mConnectionModule);

		mDisplayModule = new SuccesView(this, "Display Module", false);
		layout.addView(mDisplayModule);

		mEventModule = new SuccesView(this, "Event Module", false);
		layout.addView(mEventModule);

		mContextModule = new SuccesView(this, "Context Module", false);
		layout.addView(mContextModule);

		mStatusModule = new SuccesView(this, "Device Status Module", false);
		layout.addView(mStatusModule);

		mServicesModule = new SuccesView(this, "Data Services Module", false);
		layout.addView(mServicesModule);

		mNotificationsModule = new SuccesView(this, "Notification Module", false);
		layout.addView(mNotificationsModule);
		
		scrollView.addView(layout);
		return scrollView;
	}

}
