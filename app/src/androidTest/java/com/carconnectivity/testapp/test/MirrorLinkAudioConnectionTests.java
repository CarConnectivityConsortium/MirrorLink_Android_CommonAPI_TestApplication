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

import com.carconnectivity.testapp.MirrorLinkAudioConnection;
import com.carconnectivity.testapp.test.base.BaseFunctionalTests;
import com.carconnectivity.testapp.views.LastExecutedView;
import com.carconnectivity.testapp.views.LastExecutedViewMultiline;
import com.carconnectivity.testapp.views.SuccesView;
import com.carconnectivity.testapp.views.TextViewString;
import com.mirrorlink.android.commonapi.Defs;

public class MirrorLinkAudioConnectionTests extends BaseFunctionalTests {
	TextViewString mediaOutString = null;
	TextViewString mediaInString = null;
	TextViewString voiceControllString = null;
	TextViewString phoneAudioString = null;

	TextViewString rtpPayloadTypes = null;
	LastExecutedView timestampLastUpdate = null;

	Button getEstablishedAudioConnection = null;

	SuccesView callbackFired = null;
	LastExecutedViewMultiline callbackFiredList = null;
	Button clearCallbackList = null;
	
	
	public MirrorLinkAudioConnectionTests() {
		super();
		testedActivity =  MirrorLinkAudioConnection.class;
	}
	
	@Override 
	public void setUp() throws Exception
	{
		super.setUp();
		mediaOutString = (TextViewString) fields.get("mediaOutString").get(activity);
		mediaInString = (TextViewString) fields.get("mediaInString").get(activity);
		voiceControllString = (TextViewString) fields.get("voiceControllString").get(activity);
		phoneAudioString = (TextViewString) fields.get("phoneAudioString").get(activity);

		rtpPayloadTypes = (TextViewString) fields.get("rtpPayloadTypes").get(activity);
		timestampLastUpdate = (LastExecutedView) fields.get("timestampLastUpdate").get(activity);

		getEstablishedAudioConnection = (Button) fields.get("getEstablishedAudioConnection").get(activity);

		callbackFired = (SuccesView) fields.get("callbackFired").get(activity);
		callbackFiredList = (LastExecutedViewMultiline) fields.get("callbackFiredList").get(activity);
		clearCallbackList = (Button) fields.get("clearCallbackList").get(activity);

	}
	public void testPrecondition()
	{
		assertNotNull(mediaOutString);
		assertNotNull(mediaInString);
		assertNotNull(voiceControllString);
		assertNotNull(phoneAudioString);

		assertNotNull(rtpPayloadTypes);
		assertNotNull(timestampLastUpdate);

		assertNotNull(getEstablishedAudioConnection);

		assertNotNull(callbackFired);
		assertNotNull(callbackFiredList);
		assertNotNull(clearCallbackList);	
	}
	
	public void testAudioButton() throws Exception
	{
		List<Object> args = new ArrayList<Object>();

	    Bundle b = new Bundle();
	    b.putInt("MEDIA_AUDIO_OUT",Defs.AudioConnections.MEDIA_OUT_NONE);
	    b.putInt("MEDIA_AUDIO_IN",Defs.AudioConnections.MEDIA_IN_NONE);
	    b.putInt("VOICE_CONTROL",Defs.AudioConnections.VOICE_CONTROL_NONE);
	    b.putInt("PHONE_AUDIO",Defs.AudioConnections.INCALL_AUDIO_NONE);
	    b.putString("PAYLOAD_TYPES", "0,1");
	    args.add(b);
	    args.add(false);

	    rc.send("mConnectionManager","setAudioConnections",args);
	    
	    String timestampLastUpdateOld = timestampLastUpdate.getValue();
	    
	    Thread.sleep(1500);
		getInstrumentation().waitForIdleSync();
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				getEstablishedAudioConnection.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		Thread.sleep(500);
	    
		assertEquals(mediaOutString.getValue(), "Not Established");
		assertEquals(mediaInString.getValue(), "Not Established");
		assertEquals(voiceControllString.getValue() , "Not Established");
		assertEquals(phoneAudioString.getValue() , "Not Established");
		
		assertEquals(rtpPayloadTypes.getValue() , "0,1");
		assertNotSame(timestampLastUpdateOld, timestampLastUpdate.getValue());
		
		
		
		///#######################################################################################
		
		args = new ArrayList<Object>();
	    b = new Bundle();
	    b.putInt("MEDIA_AUDIO_OUT",Defs.AudioConnections.MEDIA_OUT_BT_AD2DP);
	    b.putInt("MEDIA_AUDIO_IN",Defs.AudioConnections.MEDIA_IN_RTP);
	    b.putInt("VOICE_CONTROL",Defs.AudioConnections.VOICE_CONTROL_RTP);
	    b.putInt("PHONE_AUDIO",Defs.AudioConnections.INCALL_AUDIO_RTP);
	    b.putString("PAYLOAD_TYPES", "0,1,2");
	    args.add(b);
	    args.add(false);

	    rc.send("mConnectionManager","setAudioConnections",args);
	    
	    timestampLastUpdateOld = timestampLastUpdate.getValue();
	    
	    Thread.sleep(500);
		getInstrumentation().waitForIdleSync();
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				getEstablishedAudioConnection.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		Thread.sleep(500);
	    
		assertEquals(mediaOutString.getValue(), "BT A2DP");
		assertEquals(mediaInString.getValue(), "RTP");
		assertEquals(voiceControllString.getValue() , "RTP");
		assertEquals(phoneAudioString.getValue() ,"RTP");
		assertEquals(rtpPayloadTypes.getValue() , "0,1,2");
		assertNotSame(timestampLastUpdateOld, timestampLastUpdate.getValue());
		
		
	///#######################################################################################
		
		args = new ArrayList<Object>();
	    b = new Bundle();
	    b.putInt("MEDIA_AUDIO_OUT",Defs.AudioConnections.MEDIA_OUT_RTP);
	    b.putInt("MEDIA_AUDIO_IN",Defs.AudioConnections.MEDIA_IN_NONE);
	    b.putInt("VOICE_CONTROL",Defs.AudioConnections.VOICE_CONTROL_BT_HFP);
	    b.putInt("PHONE_AUDIO",Defs.AudioConnections.INCALL_AUDIO_BT_HFP);
	    b.putString("PAYLOAD_TYPES", "0,1,2,3");
	    args.add(b);
	    args.add(false);

	    rc.send("mConnectionManager","setAudioConnections",args);
	    
	    timestampLastUpdateOld = timestampLastUpdate.getValue();
	    
	    Thread.sleep(500);
		getInstrumentation().waitForIdleSync();
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				getEstablishedAudioConnection.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		Thread.sleep(500);
	    
		assertEquals(mediaOutString.getValue(), "RTP");
		assertEquals(mediaInString.getValue(), "Not Established");
		assertEquals(voiceControllString.getValue() , "BT HFP + BVRA");
		assertEquals(phoneAudioString.getValue() ,"BT HFP");
		assertEquals(rtpPayloadTypes.getValue() , "0,1,2,3");
		assertNotSame(timestampLastUpdateOld, timestampLastUpdate.getValue());
	    
	    
		
	}
	public void testAudioCallback() throws InterruptedException
	{
		List<Object> args = new ArrayList<Object>();

	    Bundle b = new Bundle();
	    b.putInt("MEDIA_AUDIO_OUT",Defs.AudioConnections.MEDIA_OUT_NONE);
	    b.putInt("MEDIA_AUDIO_IN",Defs.AudioConnections.MEDIA_IN_NONE);
	    b.putInt("VOICE_CONTROL",Defs.AudioConnections.VOICE_CONTROL_NONE);
	    b.putInt("PHONE_AUDIO",Defs.AudioConnections.INCALL_AUDIO_NONE);
	    b.putString("PAYLOAD_TYPES", "0,1,4,5");
	    args.add(b);
	    args.add(true);

	    rc.send("mConnectionManager","setAudioConnections",args);
	    
	    String timestampLastUpdateOld = callbackFiredList.getValue();
	    
	    Thread.sleep(1500);
		getInstrumentation().waitForIdleSync();
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				getEstablishedAudioConnection.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		Thread.sleep(500);
	    
		assertEquals(mediaOutString.getValue(), "Not Established");
		assertEquals(mediaInString.getValue(), "Not Established");
		assertEquals(voiceControllString.getValue() , "Not Established");
		assertEquals(phoneAudioString.getValue() , "Not Established");
		
		assertEquals(rtpPayloadTypes.getValue() , "0,1,4,5");
		assertNotSame(timestampLastUpdateOld, callbackFiredList.getValue());
		
		assertTrue(callbackFired.getValue());
	}
} 