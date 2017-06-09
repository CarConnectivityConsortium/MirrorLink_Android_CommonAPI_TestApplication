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
package com.carconnectivity.testapp.test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.test.suitebuilder.annotation.Suppress;
import android.util.Log;
import android.widget.Button;

import com.carconnectivity.testapp.BaseActivity;
import com.carconnectivity.testapp.MirrorLinkApiModules;
import com.carconnectivity.testapp.test.base.BaseFunctionalTests;
import com.carconnectivity.testapp.views.SuccesView;


public class MirrorLinkApiModulesTests extends BaseFunctionalTests {
	
	
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
	
	
	public MirrorLinkApiModulesTests() throws Exception {
		super();
		testedActivity = MirrorLinkApiModules.class;
	
	}
	public void setUp() throws Exception {
		super.setUp();
	
		updateFields();
	}
	
	void updateFields() throws IllegalArgumentException, IllegalAccessException
	{
		fields = new HashMap<String, Field>();
		for(Field f: testedActivity.getDeclaredFields())
		{
			f.setAccessible(true);
			fields.put(f.getName(), f);
		}

		updateValues = (Button)  fields.get("updateValues").get(activity);
		mCommonAPIModule = (SuccesView) fields.get("mCommonAPIModule").get(activity);
		mDeviceModule = (SuccesView) fields.get("mDeviceModule").get(activity);
		mCertification =  (SuccesView) fields.get("mCertification").get(activity);
		mConnectionModule =  (SuccesView) fields.get("mConnectionModule").get(activity);
		mDisplayModule =  (SuccesView) fields.get("mDisplayModule").get(activity);
		mEventModule =  (SuccesView) fields.get("mEventModule").get(activity);
		mKeyboardModule = (SuccesView) fields.get("mKeyboardModule").get(activity);
		mKeyEventModule =  (SuccesView) fields.get("mKeyEventModule").get(activity);
		mContextModule =  (SuccesView) fields.get("mContextModule").get(activity);
		mStatusModule =  (SuccesView) fields.get("mStatusModule").get(activity);
		mServicesModule =  (SuccesView) fields.get("mServicesModule").get(activity);
		mNotificationsModule =  (SuccesView) fields.get("mNotificationsModule").get(activity);
		mWebModule =  (SuccesView) fields.get("mWebModule").get(activity);
	}
	

	void showTestedActivity() throws IllegalArgumentException, IllegalAccessException
	{
		activity = startActivity();
		Log.v("BaseFunctionalTests", "setUp");
		getInstrumentation().waitForIdleSync();
		updateFields();
	}
	
	void closeTestedActivity()
	{
		if (activity != null)
		{
			activity.finish();
		}
		
	}
	
	
	
	public void testPrecondition() throws Exception
	{
		assertNotNull(mCommonAPIModule);
		assertNotNull(mDeviceModule);
		assertNotNull(mCertification);
		assertNotNull(mConnectionModule);
		assertNotNull(mDisplayModule );
		assertNotNull(mEventModule );
		assertNotNull(mContextModule);
		assertNotNull(mStatusModule);
		assertNotNull(mServicesModule );
		assertNotNull(mNotificationsModule);

	}
	
	
	public void testDeviceInfoManager() throws Exception
	{
		Thread.sleep(500);
		enableManager("DeviceInfoManager", false);
		
		assertFalse(mDeviceModule.getValue());
		assertNull(((BaseActivity) activity).getMirrorLinkApplicationContext().getDeviceInfoManager());

		enableManager("DeviceInfoManager", true);
		
		assertTrue(mDeviceModule.getValue());
		assertNotNull(((BaseActivity) activity).getMirrorLinkApplicationContext().getDeviceInfoManager());
	}
	


//	public void testActivity() 
//	{
//		getInstrumentation().waitForIdleSync();
//		assertNotNull(activity);
//		assertFalse(activity.isFinishing());
//		getInstrumentation().waitForIdleSync();
//	}
	
	
	public void testCertificationManager() throws Exception
	{
		enableManager("CertificationManager", false);
		assertFalse(mCertification.getValue());
		assertNull(((BaseActivity) activity).getMirrorLinkApplicationContext().getCertificationManager());

		enableManager("CertificationManager", true);

		assertTrue(mCertification.getValue());
		assertNotNull(((BaseActivity) activity).getMirrorLinkApplicationContext().getCertificationManager());
	}
	
	public void testConnectionManager() throws Exception
	{
		Thread.sleep(500);
		enableManager("ConnectionManager", false);
		

		assertFalse(mConnectionModule.getValue());
		assertNull(((BaseActivity) activity).getMirrorLinkApplicationContext().getConnectionManager());
		enableManager("ConnectionManager", true);
		assertTrue(mConnectionModule.getValue());
		assertNotNull(((BaseActivity) activity).getMirrorLinkApplicationContext().getConnectionManager());
	}

	
	public void testEventMappingManager() throws Exception
	{
		Thread.sleep(500);
		enableManager("EventMappingManager", false);
		assertFalse(mEventModule.getValue());
		assertNull(((BaseActivity) activity).getMirrorLinkApplicationContext().getEventMappingManager());
		enableManager("EventMappingManager", true);
		assertTrue(mEventModule.getValue());
		assertNotNull(((BaseActivity) activity).getMirrorLinkApplicationContext().getEventMappingManager());
	}
	
	public void testDisplayModule() throws Exception
	{
		enableManager("DisplayManager", false);
		assertFalse(mDisplayModule.getValue());
		assertNull(((BaseActivity) activity).getMirrorLinkApplicationContext().getDisplayManager());
		enableManager("DisplayManager", true);
		assertTrue(mDisplayModule.getValue());
		assertNotNull(((BaseActivity) activity).getMirrorLinkApplicationContext().getDisplayManager());
	}
	public void testContextManager() throws Exception
	{
		
		enableManager("ContextManager", false);
		assertFalse(mContextModule.getValue());
		assertNull(((BaseActivity) activity).getMirrorLinkApplicationContext().getContextManager());
		enableManager("ContextManager", true);
		assertTrue(mContextModule.getValue());
		assertNotNull(((BaseActivity) activity).getMirrorLinkApplicationContext().getContextManager());
	}
	
	public void testDeviceStatusManager() throws Exception
	{
		
		enableManager("DeviceStatusManager", false);
		assertFalse(mStatusModule.getValue());
		assertNull(((BaseActivity) activity).getMirrorLinkApplicationContext().getDeviceStatusManager());
		enableManager("DeviceStatusManager", true);
		assertTrue(mStatusModule.getValue());
		assertNotNull(((BaseActivity) activity).getMirrorLinkApplicationContext().getDeviceStatusManager());
	}
	
	public void testDataServicesManager() throws Exception
	{
		
		enableManager("DataServicesManager", false);        
		assertFalse(mServicesModule.getValue());
		assertNull(((BaseActivity) activity).getMirrorLinkApplicationContext().getDataServicesManager());
		enableManager("DataServicesManager", true);
		assertTrue(mServicesModule.getValue());
		assertNotNull(((BaseActivity) activity).getMirrorLinkApplicationContext().getDataServicesManager());
	}
	
	public void testNotificationManager() throws Exception
	{
		
		enableManager("NotificationManager", false);
		assertFalse(mNotificationsModule.getValue());
		assertNull(((BaseActivity) activity).getMirrorLinkApplicationContext().getNotificationManager());
		enableManager("NotificationManager", true);
		assertTrue(mNotificationsModule.getValue());
		assertNotNull(((BaseActivity) activity).getMirrorLinkApplicationContext().getNotificationManager());
	}
	
	
	public void enableManager(String managerName, boolean enable) throws Exception
	{
		getInstrumentation().waitForIdleSync();
		List<Object> args = new ArrayList<Object>();
		Bundle b = new Bundle();
		b.putBoolean(managerName, enable);
		args.add(b);
		rc.send("this", "setManagersAvailability", args);
		
		getInstrumentation().waitForIdleSync();
		
		closeTestedActivity();
		showTestedActivity();
	}
	

} 