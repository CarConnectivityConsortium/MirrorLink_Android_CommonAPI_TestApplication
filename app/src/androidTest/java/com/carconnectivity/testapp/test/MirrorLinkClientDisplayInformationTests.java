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
import java.util.List;

import android.os.Bundle;
import android.widget.Button;

import com.carconnectivity.testapp.MirrorLinkClientDisplayInformation;
import com.carconnectivity.testapp.test.base.BaseFunctionalTests;
import com.carconnectivity.testapp.views.LastExecutedViewMultiline;
import com.carconnectivity.testapp.views.SuccesView;
import com.carconnectivity.testapp.views.TextViewInt;

public class MirrorLinkClientDisplayInformationTests extends BaseFunctionalTests {
	
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


	public MirrorLinkClientDisplayInformationTests() {
		super();
		testedActivity =  MirrorLinkClientDisplayInformation.class;
	}
	
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		serverHorizontalResolution = (TextViewInt)  fields.get("serverHorizontalResolution").get(activity);
		serverVerticalResolution = (TextViewInt)  fields.get("serverVerticalResolution").get(activity);
		clientHorizontalResolution =  (TextViewInt)  fields.get("clientHorizontalResolution").get(activity);
		clientVerticalResolution =  (TextViewInt)  fields.get("clientVerticalResolution").get(activity);
		width =  (TextViewInt)  fields.get("width").get(activity);
		height =  (TextViewInt)  fields.get("height").get(activity);
		distance =  (TextViewInt)  fields.get("distance").get(activity);
		success = (SuccesView)  fields.get("success").get(activity);
		getClientDisplayInformation = (Button)  fields.get("getClientDisplayInformation").get(activity);
		callbackFired =  (SuccesView)  fields.get("callbackFired").get(activity);
		callbackFiredList =  (LastExecutedViewMultiline)  fields.get("callbackFiredList").get(activity);
		clearCallbackList =  (Button)  fields.get("clearCallbackList").get(activity);

	}
	public void testPrecondition() 
	{
		assertNotNull(serverHorizontalResolution);
		assertNotNull(serverVerticalResolution);
		assertNotNull(clientHorizontalResolution);
		assertNotNull(clientVerticalResolution);
		assertNotNull(width);
		assertNotNull(height);
		assertNotNull(distance);
		assertNotNull(success);
		assertNotNull(getClientDisplayInformation);
		assertNotNull(callbackFired);
		assertNotNull(callbackFiredList);
		assertNotNull(clearCallbackList);
	}
	

	public void testValues() throws Exception 
	{
		Thread.sleep(1000);
		List<Object> args = new ArrayList<Object>();
	    Bundle b = new Bundle();
	    b.putInt("SERVER_PIXEL_WIDTH",1);
	    b.putInt("SERVER_PIXEL_HEIGHT",2);
	    b.putInt("CLIENT_PIXEL_WIDTH",3);
	    b.putInt("CLIENT_PIXEL_HEIGHT",4);
	    b.putInt("MM_WIDTH",5);
	    b.putInt("MM_HEIGHT",6);
	    b.putInt("DISTANCE",7);
	    args.add(b);
	    args.add(false);
	    rc.send("mDisplayManager","setDisplayConfiguration",args);
	    
	    Thread.sleep(500);
		getInstrumentation().waitForIdleSync();
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				getClientDisplayInformation.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		Thread.sleep(500);
		
		assertEquals(1, serverHorizontalResolution.getValue());
		assertEquals(2, serverVerticalResolution.getValue());
		assertEquals(3, clientHorizontalResolution.getValue());
		assertEquals(4, clientVerticalResolution.getValue());
		assertEquals(5, width.getValue());
		assertEquals(6, height.getValue());
		assertEquals(7, distance.getValue());
		assertEquals(true, success.getValue());
		assertEquals(false, callbackFired.getValue());
		
		
		 Thread.sleep(500);
		
	    args = new ArrayList<Object>();
	    b = new Bundle();
	    b.putInt("SERVER_PIXEL_WIDTH",101);
	    b.putInt("SERVER_PIXEL_HEIGHT",102);
	    b.putInt("CLIENT_PIXEL_WIDTH",103);
	    b.putInt("CLIENT_PIXEL_HEIGHT",104);
	    b.putInt("MM_WIDTH",105);
	    b.putInt("MM_HEIGHT",106);
	    b.putInt("DISTANCE",107);
	    args.add(b);
	    args.add(false);
	    rc.send("mDisplayManager","setDisplayConfiguration",args);
	    
	    
		    
	    Thread.sleep(500);
		assertEquals(1, serverHorizontalResolution.getValue());
		assertEquals(2, serverVerticalResolution.getValue());
		assertEquals(3, clientHorizontalResolution.getValue());
		assertEquals(4, clientVerticalResolution.getValue());
		assertEquals(5, width.getValue());
		assertEquals(6, height.getValue());
		assertEquals(7, distance.getValue());
		assertEquals(true, success.getValue());
		assertEquals(false, callbackFired.getValue());
		
		
		Thread.sleep(500);
		getInstrumentation().waitForIdleSync();
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				getClientDisplayInformation.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		Thread.sleep(500);
	    
	    
		
		assertEquals(101, serverHorizontalResolution.getValue());
		assertEquals(102, serverVerticalResolution.getValue());
		assertEquals(103, clientHorizontalResolution.getValue());
		assertEquals(104, clientVerticalResolution.getValue());
		assertEquals(105, width.getValue());
		assertEquals(106, height.getValue());
		assertEquals(107, distance.getValue());
		
		assertEquals(true, success.getValue());
		assertEquals(false, callbackFired.getValue());
		
	}
	
	
	public void testCallback() throws Exception 
	{
		Thread.sleep(1000);
		
		String callbackString = callbackFiredList.getValue();
		
		assertEquals("N/A", callbackString);
		
	    List<Object> args = new ArrayList<Object>();
	    Bundle b = new Bundle();
	    b.putInt("SERVER_PIXEL_WIDTH",101);
	    b.putInt("SERVER_PIXEL_HEIGHT",102);
	    b.putInt("CLIENT_PIXEL_WIDTH",103);
	    b.putInt("CLIENT_PIXEL_HEIGHT",104);
	    b.putInt("MM_WIDTH",105);
	    b.putInt("MM_HEIGHT",106);
	    b.putInt("DISTANCE",107);
	    args.add(b);
	    args.add(true);
	    rc.send("mDisplayManager","setDisplayConfiguration",args);
	    
	    Thread.sleep(1000);
		
		assertEquals(101, serverHorizontalResolution.getValue());
		assertEquals(102, serverVerticalResolution.getValue());
		assertEquals(103, clientHorizontalResolution.getValue());
		assertEquals(104, clientVerticalResolution.getValue());
		assertEquals(105, width.getValue());
		assertEquals(106, height.getValue());
		assertEquals(107, distance.getValue());
		
		assertEquals(true, success.getValue());
		assertEquals(true, callbackFired.getValue());
		
		assertNotSame(callbackFiredList.getValue(), callbackString);
	}

} 