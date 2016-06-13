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
 */package com.carconnectivity.testapp.test;

import java.lang.reflect.Field;

import android.widget.Button;

import com.carconnectivity.testapp.MirrorLinkVrtKeyboard;
import com.carconnectivity.testapp.test.base.BaseFunctionalTests;
import com.carconnectivity.testapp.views.LastExecutedView;
import com.carconnectivity.testapp.views.SuccesView;

public class MirrorLinkVirtualKeyboardTests extends BaseFunctionalTests {

	@Override
	public void setUp() throws Exception {
		testedActivity =  MirrorLinkVrtKeyboard.class;
		super.setUp();
		
	}

	public void testButtons() throws Exception
	{

		boolean s = false;
		Field getVirtualKeyboardSupport = testedActivity.getDeclaredField("getVirtualKeyboardSupport");
		getVirtualKeyboardSupport.setAccessible(true);
		final Button getVirtualKeyboardSupportButton = (Button) getVirtualKeyboardSupport.get(activity);
		
		Field vkbAvailableField = testedActivity.getDeclaredField("vkbAvailable");
		vkbAvailableField.setAccessible(true);
		SuccesView vkbAvailable = (SuccesView) vkbAvailableField.get(activity);
				
		Field vkbTouchSupportField = testedActivity.getDeclaredField("vkbTouchSupport");
		vkbTouchSupportField.setAccessible(true);
		SuccesView vkbTouchSupport = (SuccesView) vkbTouchSupportField.get(activity);
		
		
		Field vkbKnobSupportField  = testedActivity.getDeclaredField("vkbKnobSupport");
		vkbKnobSupportField.setAccessible(true);
		SuccesView vkbKnobSupport = (SuccesView) vkbKnobSupportField.get(activity);
		
		Field vkbDriveModeField  = testedActivity.getDeclaredField("vkbDriveMode");
		vkbDriveModeField.setAccessible(true);
		SuccesView vkbDriveMode = (SuccesView) vkbDriveModeField.get(activity);
		
		Field callbackFiredTimeField  = testedActivity.getDeclaredField("callbackFiredTime");
		callbackFiredTimeField.setAccessible(true);
		LastExecutedView callbackFiredTime = (LastExecutedView) callbackFiredTimeField.get(activity);
		
		assertNotNull(getVirtualKeyboardSupportButton);
		assertNotNull(vkbAvailable);
		assertNotNull(vkbTouchSupport);
		assertNotNull(vkbKnobSupport);
		assertNotNull(vkbDriveMode);
		assertNotNull(callbackFiredTime);
		
		
		s = rc.send("mDeviceInfoManager", 
				"setServerVirtualKeyboardSupport" ,
				"[" +
				"{\"type\":\"boolean\", \"value\":\"true\"}," +
				"{\"type\":\"boolean\", \"value\":\"true\"}," +
				"{\"type\":\"boolean\", \"value\":\"true\"}," +
				"{\"type\":\"boolean\", \"value\":\"true\"}" +
				"]");
		
		assertTrue(s);
		
		Thread.sleep(200);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				getVirtualKeyboardSupportButton.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		Thread.sleep(200);
		
		String oldValue = callbackFiredTime.getValue();
		
		assertTrue(vkbAvailable.getValue());
		assertTrue(vkbTouchSupport.getValue());
		assertTrue(vkbKnobSupport.getValue());
		assertTrue(vkbDriveMode.getValue());
		
		//////////////////////////////////////////////////////////////
		
		s = rc.send("mDeviceInfoManager", 
				"setServerVirtualKeyboardSupport" ,
				"[" +
				"{\"type\":\"boolean\", \"value\":\"false\"}," +
				"{\"type\":\"boolean\", \"value\":\"false\"}," +
				"{\"type\":\"boolean\", \"value\":\"false\"}," +
				"{\"type\":\"boolean\", \"value\":\"false\"}" +
				"]");
		
		assertTrue(s);
		
		Thread.sleep(500);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				getVirtualKeyboardSupportButton.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		Thread.sleep(1500);
		
		
		String newValue = callbackFiredTime.getValue();
		assertFalse(oldValue.equals(newValue));
		
		assertFalse(vkbAvailable.getValue());
		assertFalse(vkbTouchSupport.getValue());
		assertFalse(vkbKnobSupport.getValue());
		assertFalse(vkbDriveMode.getValue());
		

	}
	

	
} 