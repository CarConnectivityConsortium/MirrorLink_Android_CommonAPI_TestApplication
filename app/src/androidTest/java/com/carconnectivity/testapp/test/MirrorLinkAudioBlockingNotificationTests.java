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

import java.util.ArrayList;
import java.util.List;

import android.widget.Button;

import com.carconnectivity.testapp.MirrorLinkAudioBlockingNotification;
import com.carconnectivity.testapp.test.base.BaseFunctionalTests;
import com.carconnectivity.testapp.views.BooloeanInputView;
import com.carconnectivity.testapp.views.LastExecutedViewMultiline;
import com.carconnectivity.testapp.views.SuccesView;
import com.carconnectivity.testapp.views.TextViewString;

public class MirrorLinkAudioBlockingNotificationTests extends BaseFunctionalTests {
	
	TextViewString audioBlockingReasonString = null;
	SuccesView callbackFired= null;
	LastExecutedViewMultiline callbackFiredList = null;
	BooloeanInputView resultResponse = null;
	Button clearCallbackList = null;
	
	
	
	public MirrorLinkAudioBlockingNotificationTests() {
		super();	
		testedActivity = MirrorLinkAudioBlockingNotification.class;
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();

		audioBlockingReasonString = (TextViewString) fields.get("audioBlockingReasonString").get(activity);
		callbackFired= (SuccesView) fields.get("callbackFired").get(activity);
		callbackFiredList = (LastExecutedViewMultiline) fields.get("callbackFiredList").get(activity);
		resultResponse = (BooloeanInputView) fields.get("resultResponse").get(activity);
		clearCallbackList = (Button) fields.get("clearCallbackList").get(activity);
		
	}
	
	
	public void testBLocking() throws InterruptedException
	{
		assertFalse(callbackFired.getValue());
		assertEquals(audioBlockingReasonString.getValue(),"n/a");
		assertEquals(callbackFiredList.getValue(),"N/A");
		
		
		// Fire calback
		List<Object> args = new ArrayList<Object>();
		args.add((int) 3);
		rc.send("mContextManger", "setAudioBlocking", args);
		getInstrumentation().waitForIdleSync();
		Thread.sleep(500);
		
		
		assertTrue(callbackFired.getValue());
		assertEquals(audioBlockingReasonString.getValue(),"3");
		assertNotSame(callbackFiredList.getValue(),"N/A");
		
		String oldCallbackList = callbackFiredList.getValue();
		
		// Fire calback
		args = new ArrayList<Object>();
		args.add(50);
		rc.send("mContextManger", "setAudioBlocking", args);
		getInstrumentation().waitForIdleSync();
		Thread.sleep(500);
		
		
		assertTrue(callbackFired.getValue());
		assertEquals(audioBlockingReasonString.getValue(),"50");
		assertNotSame(callbackFiredList.getValue(),"N/A");
		assertNotSame(callbackFiredList.getValue(),oldCallbackList);
		
		
		Thread.sleep(500);
		getInstrumentation().waitForIdleSync();
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				clearCallbackList.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		Thread.sleep(500);
		
		assertFalse(callbackFired.getValue());
		assertEquals("n/a",audioBlockingReasonString.getValue());
		assertEquals("N/A",callbackFiredList.getValue());	
	}
	
} 