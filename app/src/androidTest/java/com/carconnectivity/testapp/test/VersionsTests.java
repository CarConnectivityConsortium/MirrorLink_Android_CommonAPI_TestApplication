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

import android.widget.Button;

import com.carconnectivity.testapp.Versions;
import com.carconnectivity.testapp.test.base.BaseFunctionalTests;
import com.carconnectivity.testapp.views.LastExecutedView;
import com.carconnectivity.testapp.views.SuccesView;
import com.carconnectivity.testapp.views.TextViewInt;

public class VersionsTests extends BaseFunctionalTests {

	int WAIT_TIME = 500;
	
	
	TextViewInt apiVersion =null;
	LastExecutedView apiVersionLastExecuted = null;
	
	TextViewInt mirrorLinkMajorVersion =null;
	TextViewInt mirrorLinkMinorVersion =null;
	SuccesView mlVersionSuccess = null;
	LastExecutedView mirrorLinkVersionLastExecuted = null;
	
	Button bGetCommonApiVersion = null;
	Button bGetMirrorLinkVersion = null;
	
	public void setUp() throws Exception {
		testedActivity = Versions.class;
		super.setUp();
		
		
		Field apiVersionField = testedActivity.getDeclaredField("apiVersion");
		apiVersionField.setAccessible(true);
		apiVersion = (TextViewInt) apiVersionField.get(activity);
		
		Field apiVersionLastExecutedField = testedActivity.getDeclaredField("apiVersionLastExecuted");
		apiVersionLastExecutedField.setAccessible(true);
		apiVersionLastExecuted = (LastExecutedView) apiVersionLastExecutedField.get(activity);
		
		Field mirrorLinkMajorVersionField = testedActivity.getDeclaredField("mirrorLinkMajorVersion");
		mirrorLinkMajorVersionField.setAccessible(true);
		mirrorLinkMajorVersion = (TextViewInt) mirrorLinkMajorVersionField.get(activity);
		
		Field mirrorLinkMinorVersionField = testedActivity.getDeclaredField("mirrorLinkMinorVersion");
		mirrorLinkMinorVersionField.setAccessible(true);
		mirrorLinkMinorVersion = (TextViewInt) mirrorLinkMinorVersionField.get(activity);
		
		Field mlVersionSuccessField = testedActivity.getDeclaredField("mlVersionSuccess");
		mlVersionSuccessField.setAccessible(true);
		mlVersionSuccess = (SuccesView) mlVersionSuccessField.get(activity);
		
		Field mirrorLinkVersionLastExecutedField = testedActivity.getDeclaredField("mirrorLinkVersionLastExecuted");
		mirrorLinkVersionLastExecutedField.setAccessible(true);
		mirrorLinkVersionLastExecuted = (LastExecutedView) mirrorLinkVersionLastExecutedField.get(activity);
		
		Field bGetCommonApiVersionField = testedActivity.getDeclaredField("bGetCommonApiVersion");
		bGetCommonApiVersionField.setAccessible(true);
		bGetCommonApiVersion = (Button) bGetCommonApiVersionField.get(activity);
		
		Field bGetMirrorLinkVersionField = testedActivity.getDeclaredField("bGetMirrorLinkVersion");
		bGetMirrorLinkVersionField.setAccessible(true);
		bGetMirrorLinkVersion = (Button) bGetMirrorLinkVersionField.get(activity);	
		
		assertNotNull(apiVersion);
		assertNotNull(apiVersionLastExecuted);
		
		assertNotNull(mirrorLinkMajorVersion);
		assertNotNull(mirrorLinkMinorVersion);
		assertNotNull(mlVersionSuccess);
		assertNotNull(mirrorLinkVersionLastExecuted);
		
		assertNotNull(bGetCommonApiVersion);
		assertNotNull(bGetMirrorLinkVersion);
	}
	
	public void testPreconditionTest()
	{
		assertNotNull(apiVersion);
		assertNotNull(apiVersionLastExecuted);
		
		assertNotNull(mirrorLinkMajorVersion);
		assertNotNull(mirrorLinkMinorVersion);
		assertNotNull(mlVersionSuccess);
		assertNotNull(mirrorLinkVersionLastExecuted);
		
		assertNotNull(bGetCommonApiVersion);
		assertNotNull(bGetMirrorLinkVersion);
	}
	

	public void testApiVersionTest() throws Exception
	{
		int apiVerstionValue = apiVersion.getValue();
		String  lastExecuted = apiVersionLastExecuted.getValue();
		
		
		Thread.sleep(WAIT_TIME);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				bGetCommonApiVersion.performClick();
			}
		});
		Thread.sleep(WAIT_TIME);
		String lastExecutedNew  = apiVersionLastExecuted.getValue();
		
		assertNotSame(lastExecutedNew, lastExecuted);
		
		
		boolean s =  rc.send("this", 
				"setCommonAPIServiceApiLevel" ,
				"[" +
				"{\"type\":\"int\", \"value\":\"2\"}" +
				"]");
		
		assertTrue(s);
		Thread.sleep(WAIT_TIME);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				bGetCommonApiVersion.performClick();
			}
		});
		Thread.sleep(WAIT_TIME);
		apiVerstionValue = apiVersion.getValue();
		assertEquals(2, apiVerstionValue);
		
		s =  rc.send("this", 
				"setCommonAPIServiceApiLevel" ,
				"[" +
				"{\"type\":\"int\", \"value\":\"66\"}" +
				"]");
		
		assertTrue(s);
		Thread.sleep(WAIT_TIME);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				bGetCommonApiVersion.performClick();
			}
		});
		Thread.sleep(WAIT_TIME);
		apiVerstionValue = apiVersion.getValue();
		assertEquals(66, apiVerstionValue);
		
	}
	public void testMlMajorVersionTest() throws InterruptedException
	{
		String lastExecuted = mirrorLinkVersionLastExecuted.getValue();
		
		
		Thread.sleep(WAIT_TIME);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				bGetMirrorLinkVersion.performClick();
			}
		});
		Thread.sleep(WAIT_TIME);
		String lastExecutedNew  = mirrorLinkVersionLastExecuted.getValue();
		
		assertNotSame(lastExecutedNew, lastExecuted);
		
		
		boolean s =  rc.send("mDeviceInfoManager", 
				"setMirrorLinkSessionVersionMajor" ,
				"[" +
				"{\"type\":\"int\", \"value\":\"4\"}" +
				"]");
		
		assertTrue(s);
		Thread.sleep(WAIT_TIME);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				bGetMirrorLinkVersion.performClick();
			}
		});
		Thread.sleep(WAIT_TIME);

		assertEquals(4, mirrorLinkMajorVersion.getValue());
		
		s =  rc.send("mDeviceInfoManager", 
				"setMirrorLinkSessionVersionMajor" ,
				"[" +
				"{\"type\":\"int\", \"value\":\"66\"}" +
				"]");
		
		
		assertTrue(s);
		Thread.sleep(WAIT_TIME);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				bGetMirrorLinkVersion.performClick();
			}
		});
		Thread.sleep(WAIT_TIME);
		assertEquals(66, mirrorLinkMajorVersion.getValue());
	}
	
	
	public void testMlMinorVersionTest() throws InterruptedException
	{
		String lastExecuted = mirrorLinkVersionLastExecuted.getValue();
		
		
		Thread.sleep(WAIT_TIME);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				bGetMirrorLinkVersion.performClick();
			}
		});
		Thread.sleep(WAIT_TIME);
		String lastExecutedNew  = mirrorLinkVersionLastExecuted.getValue();
		
		assertNotSame(lastExecutedNew, lastExecuted);
		
		
		boolean s =  rc.send("mDeviceInfoManager", 
				"setMirrorLinkSessionVersionMinor" ,
				"[" +
				"{\"type\":\"int\", \"value\":\"4\"}" +
				"]");
		
		assertTrue(s);
		Thread.sleep(WAIT_TIME);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				bGetMirrorLinkVersion.performClick();
			}
		});
		Thread.sleep(WAIT_TIME);

		assertEquals(4, mirrorLinkMinorVersion.getValue());
		
		s =  rc.send("mDeviceInfoManager", 
				"setMirrorLinkSessionVersionMinor" ,
				"[" +
				"{\"type\":\"int\", \"value\":\"66\"}" +
				"]");
		
		
		assertTrue(s);
		Thread.sleep(WAIT_TIME);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				bGetMirrorLinkVersion.performClick();
			}
		});
		Thread.sleep(WAIT_TIME);
		assertEquals(66, mirrorLinkMinorVersion.getValue());
	}


} 