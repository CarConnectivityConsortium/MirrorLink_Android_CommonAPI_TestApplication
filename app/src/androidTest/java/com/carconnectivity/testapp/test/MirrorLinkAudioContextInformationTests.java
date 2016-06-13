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

import android.media.MediaPlayer;
import android.widget.Button;

import com.carconnectivity.testapp.MirrorLinkAudioContextInformation;
import com.carconnectivity.testapp.test.base.BaseFunctionalTests;
import com.carconnectivity.testapp.views.BooloeanInputView;
import com.carconnectivity.testapp.views.TextViewString;

public class MirrorLinkAudioContextInformationTests extends BaseFunctionalTests {
	
	MediaPlayer mMediaPlayer = null;
	BooloeanInputView applicationContentBoolView = null;
	TextViewString contentCategoryInputView = null;
	Button setAudioContextInformation = null;
	BooloeanInputView handleBlocking = null;
	Button play = null;
	Button stop = null;
	Button reset = null;
	
	public MirrorLinkAudioContextInformationTests() {
		super();
		testedActivity =  MirrorLinkAudioContextInformation.class;
	}

	@Override 
	public void setUp() throws Exception
	{
		super.setUp();
	
		mMediaPlayer = (MediaPlayer) fields.get("mMediaPlayer").get(activity);
		applicationContentBoolView = (BooloeanInputView) fields.get("applicationContentBoolView").get(activity);
		contentCategoryInputView = (TextViewString) fields.get("contentCategoryInputView").get(activity);
		setAudioContextInformation = (Button) fields.get("setAudioContextInformation").get(activity);
		handleBlocking = (BooloeanInputView) fields.get("handleBlocking").get(activity);
		
		play = (Button) fields.get("play").get(activity);
		stop = (Button) fields.get("stop").get(activity);
		reset = (Button) fields.get("reset").get(activity);
		
	}
	
	
	public void testAudioPlayback() throws Exception
	{
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				play.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		Thread.sleep(3000);
		
		assertTrue(mMediaPlayer.isPlaying());
		
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				stop.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		Thread.sleep(500);
		
		assertFalse(mMediaPlayer.isPlaying());
		
		
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				play.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		Thread.sleep(30000);
		
		assertTrue(mMediaPlayer.isPlaying());
	}
	
	
	
	public void testAudioContextInformation() throws Exception
	{
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				contentCategoryInputView.setValue("0x00000001");
			}
		});
		
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				setAudioContextInformation.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		Thread.sleep(1000);
		
		
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				contentCategoryInputView.setValue("0x00000002");
			}
		});
		
		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				setAudioContextInformation.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		Thread.sleep(1000);
		

		getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				reset.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		Thread.sleep(1000);
		

	}
} 