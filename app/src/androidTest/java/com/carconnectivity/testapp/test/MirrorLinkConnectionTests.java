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

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.Button;

import com.carconnectivity.testapp.MirrorLinkConnection;
import com.carconnectivity.testapp.test.base.BaseFunctionalTests;
import com.carconnectivity.testapp.views.LastExecutedViewMultiline;
import com.carconnectivity.testapp.views.SuccesView;
import com.carconnectivity.testapp.views.TextViewString;

public class MirrorLinkConnectionTests extends BaseFunctionalTests {
	SuccesView connectionActive = null;
	Button getConncetionStatus = null;
	
	SuccesView callbackFired = null;
	LastExecutedViewMultiline callbackFiredList = null;
	Button clearCallbackList =null;
	
	public MirrorLinkConnectionTests() {
		super();
		testedActivity = MirrorLinkConnection.class;
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
	
		connectionActive = (SuccesView) fields.get("connectionActive").get(activity);
		getConncetionStatus =  (Button) fields.get("getConncetionStatus").get(activity);
		callbackFired =  (SuccesView) fields.get("callbackFired").get(activity);
		callbackFiredList = (LastExecutedViewMultiline) fields.get("callbackFiredList").get(activity);
		clearCallbackList = (Button) fields.get("clearCallbackList").get(activity);
	}
	
	
	public void testPrecondition()
	{
		assertNotNull(connectionActive);
		assertNotNull(getConncetionStatus);
		assertNotNull(callbackFired);
		assertNotNull(callbackFiredList);;
		assertNotNull(clearCallbackList);
	}
	
	public void testValues() throws InterruptedException
	{
	    List<Object> args = new ArrayList<Object>();
	    args.add(true);
	    args.add(false);
	    rc.send("mConnectionManager","setMirrorLinkSessionEstablished",args);
	    
	    Thread.sleep(500);
		getInstrumentation().waitForIdleSync();
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				getConncetionStatus.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		Thread.sleep(500);
		
		assertTrue(connectionActive.getValue());
	    
	    args = new ArrayList<Object>();
	    args.add(false);
	    args.add(false);
	    rc.send("mConnectionManager","setMirrorLinkSessionEstablished",args);
	    
	    Thread.sleep(500);
		getInstrumentation().waitForIdleSync();
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				getConncetionStatus.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		Thread.sleep(500);
		
		assertFalse(connectionActive.getValue());
	}
	
	public void testCallbacks() throws InterruptedException
	{
		String callbackString = callbackFiredList.getValue();
		
		assertEquals("N/A",callbackString);
		assertFalse(callbackFired.getValue());
		
	    List<Object> args = new ArrayList<Object>();
	    args.add(true);
	    args.add(true);
	    rc.send("mConnectionManager","setMirrorLinkSessionEstablished",args);
	    Thread.sleep(500);
	    
		assertTrue(connectionActive.getValue());
	    
	    args = new ArrayList<Object>();
	    args.add(false);
	    args.add(true);
	    rc.send("mConnectionManager","setMirrorLinkSessionEstablished",args);
	    
	    Thread.sleep(500);
		
		assertFalse(connectionActive.getValue());
		
		assertNotSame(callbackFiredList.getValue(),callbackString);
		assertTrue(callbackFired.getValue());
		
		
	}
	
	
} 