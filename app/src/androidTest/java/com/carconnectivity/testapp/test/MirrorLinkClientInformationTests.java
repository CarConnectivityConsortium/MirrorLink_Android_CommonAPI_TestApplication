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

import android.os.Bundle;
import android.widget.Button;

import com.carconnectivity.testapp.MirrorLinkClientInformation;
import com.carconnectivity.testapp.test.base.BaseFunctionalTests;
import com.carconnectivity.testapp.views.LastExecutedView;
import com.carconnectivity.testapp.views.LastExecutedViewMultiline;
import com.carconnectivity.testapp.views.SuccesView;
import com.carconnectivity.testapp.views.TextViewString;

public class MirrorLinkClientInformationTests extends BaseFunctionalTests{
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
	
	public MirrorLinkClientInformationTests() {
		super();
		testedActivity =  MirrorLinkClientInformation.class;
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();

		clientIdentifier =  (TextViewString)  fields.get("clientIdentifier").get(activity);
		friendlyname =  (TextViewString)  fields.get("friendlyname").get(activity);
		manufacter =  (TextViewString)  fields.get("manufacter").get(activity);
		modelName =  (TextViewString)  fields.get("modelName").get(activity);
		modelNumber =  (TextViewString)  fields.get("modelNumber").get(activity);

		clientInformationSucceed =  (SuccesView)  fields.get("clientInformationSucceed").get(activity);
		clientInformationLastExecuted =  (LastExecutedView)  fields.get("clientInformationLastExecuted").get(activity);
		getClientInfo =  (Button)  fields.get("getClientInfo").get(activity);

		clientInformationCallbackFired =  (SuccesView)  fields.get("clientInformationCallbackFired").get(activity);
		clientInformationCallbackFiredList = (LastExecutedViewMultiline)  fields.get("clientInformationCallbackFiredList").get(activity);
		clearCallbackFiredIndicator = (Button)  fields.get("clearCallbackFiredIndicator").get(activity);
	}
	
	
	public void testPrecondition()
	{
		assertNotNull(clientIdentifier);
		assertNotNull(friendlyname);
		assertNotNull(manufacter);
		assertNotNull(modelName);
		assertNotNull(modelNumber);

		assertNotNull(clientInformationSucceed);
		assertNotNull(clientInformationLastExecuted );
		assertNotNull(getClientInfo );

		assertNotNull(clientInformationCallbackFired);
		assertNotNull(clientInformationCallbackFiredList);
		assertNotNull(clearCallbackFiredIndicator);
	}
	
	
	public void testValues() throws Exception
	{
		List<Object> args = new ArrayList<Object>();
	    Bundle b = new Bundle();
	    b.putString("CLIENT_IDENTIFIER","CLIENT_IDENTIFIER");
	    b.putString("CLIENT_FRIENDLY_NAME","CLIENT_FRIENDLY_NAME");
	    b.putString("CLIENT_MANUFACTURER","CLIENT_MANUFACTURER");
	    b.putString("CLIENT_MODEL_NAME", "CLIENT_MODEL_NAME");
	    b.putString("CLIENT_MODEL_NUMBER","CLIENT_MODEL_NUMBER");
	    args.add(b);
	    args.add(false);
	    rc.send("mDeviceInfoManager","setMirrorLinkClientInformation",args);
	    
	    Thread.sleep(500);
		getInstrumentation().waitForIdleSync();
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				getClientInfo.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		Thread.sleep(500);
		
		assertEquals("CLIENT_IDENTIFIER",clientIdentifier.getValue());
		assertEquals("CLIENT_FRIENDLY_NAME", friendlyname.getValue());
		assertEquals("CLIENT_MANUFACTURER", manufacter.getValue());
		assertEquals("CLIENT_MODEL_NAME", modelName.getValue());
		assertEquals("CLIENT_MODEL_NUMBER", modelNumber.getValue());

		assertTrue(clientInformationSucceed.getValue());
		assertNotSame(clientInformationLastExecuted.getValue(), "N/A");
	
		assertFalse(clientInformationCallbackFired.getValue());
		assertEquals(clientInformationCallbackFiredList.getValue(), "N/A");
		
		String clientInformationLastExecutedString = clientInformationLastExecuted.getValue();
		
		args = new ArrayList<Object>();
	    b = new Bundle();
	    b.putString("CLIENT_IDENTIFIER","CLIENT_IDENTIFIER2");
	    b.putString("CLIENT_FRIENDLY_NAME","CLIENT_FRIENDLY_NAME2");
	    b.putString("CLIENT_MANUFACTURER","CLIENT_MANUFACTURER2");
	    b.putString("CLIENT_MODEL_NAME", "CLIENT_MODEL_NAME2");
	    b.putString("CLIENT_MODEL_NUMBER","CLIENT_MODEL_NUMBER2");
	    args.add(b);
	    args.add(false);
	    rc.send("mDeviceInfoManager","setMirrorLinkClientInformation",args);
	    
	    Thread.sleep(500);
		getInstrumentation().waitForIdleSync();
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				getClientInfo.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		Thread.sleep(500);
		
		assertEquals("CLIENT_IDENTIFIER2",clientIdentifier.getValue());
		assertEquals("CLIENT_FRIENDLY_NAME2", friendlyname.getValue());
		assertEquals("CLIENT_MANUFACTURER2", manufacter.getValue());
		assertEquals("CLIENT_MODEL_NAME2", modelName.getValue());
		assertEquals("CLIENT_MODEL_NUMBER2", modelNumber.getValue());

		assertTrue(clientInformationSucceed.getValue());
		assertNotSame(clientInformationLastExecuted.getValue(), clientInformationLastExecutedString);
	
		assertFalse(clientInformationCallbackFired.getValue());
		assertEquals(clientInformationCallbackFiredList.getValue(), "N/A");
		
	}
	
	public void testCallback() throws Exception
	{
		Thread.sleep(1000);
			
		String clientInformationLastExecutedCallbackString = clientInformationCallbackFiredList.getValue();
		
		List<Object> args = new ArrayList<Object>();
	    Bundle b = new Bundle();
	    b.putString("CLIENT_IDENTIFIER","CLIENT_IDENTIFIER3");
	    b.putString("CLIENT_FRIENDLY_NAME","CLIENT_FRIENDLY_NAME3");
	    b.putString("CLIENT_MANUFACTURER","CLIENT_MANUFACTURER3");
	    b.putString("CLIENT_MODEL_NAME", "CLIENT_MODEL_NAME3");
	    b.putString("CLIENT_MODEL_NUMBER","CLIENT_MODEL_NUMBER3");
	    args.add(b);
	    args.add(true);
	    rc.send("mDeviceInfoManager","setMirrorLinkClientInformation",args);
	    
	    Thread.sleep(500);
		
		assertEquals("CLIENT_IDENTIFIER3",clientIdentifier.getValue());
		assertEquals("CLIENT_FRIENDLY_NAME3", friendlyname.getValue());
		assertEquals("CLIENT_MANUFACTURER3", manufacter.getValue());
		assertEquals("CLIENT_MODEL_NAME3", modelName.getValue());
		assertEquals("CLIENT_MODEL_NUMBER3", modelNumber.getValue());

		assertNotSame(clientInformationCallbackFiredList.getValue(), clientInformationLastExecutedCallbackString);
	
		assertTrue(clientInformationCallbackFired.getValue());
		assertNotSame(clientInformationCallbackFiredList.getValue(), "N/A");
		
	}
	
} 