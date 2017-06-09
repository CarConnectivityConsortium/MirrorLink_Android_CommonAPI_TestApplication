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


import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.carconnectivity.testapp.views.BooloeanInputView;
import com.carconnectivity.testapp.views.HeaderView;
import com.carconnectivity.testapp.views.LastExecutedViewMultiline;
import com.carconnectivity.testapp.views.SuccesView;
import com.carconnectivity.testapp.views.TextViewString;
import com.mirrorlink.android.commonapi.IContextListener;
import com.mirrorlink.android.commonapi.IContextManager;

public class MirrorLinkAudioContextInformation extends BaseActivity {
	MediaPlayer mMediaPlayer = null;
	BooloeanInputView loopPlaying = null;
	BooloeanInputView applicationContentBoolView = null;
	TextViewString contentCategoryInputView = null;
	Button setAudioContextInformation = null;
	BooloeanInputView handleBlocking = null;
	BooloeanInputView audioContent = null;
	
	
	TextViewString audioBlockingReasonString = null;
	SuccesView audioBlockingNotification = null;
	SuccesView callbackFired= null;
	LastExecutedViewMultiline callbackFiredList = null;
	BooloeanInputView resultResponse = null;
	
	Button clearCallbackList = null;


	LinkedHashMap<String, Integer> contentCategories = new LinkedHashMap<String, Integer>();

	Button play = null;
	Button stop = null;
	Button reset = null;

	private IContextManager contextManager = null;

	@Override
	protected void onResume()
	{
		contextManager = getMirrorLinkApplicationContext().registerContextManager(this, mContextListener);

		if (contextManager == null)
		{
			Toast.makeText(this, "Unable to get context manager.", Toast.LENGTH_LONG).show();
			finish();
		}
		super.onResume();
	}
	@Override
	protected void onPause()
	{
		getMirrorLinkApplicationContext().unregisterContextManager(this,mContextListener);
		super.onPause();
	}
	
	IContextListener mContextListener = new IContextListener.Stub() {

		@Override
		public void onAudioBlocked(final int reason) throws RemoteException {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					audioBlockingNotification.setValue(true);
					callbackFiredList.setNow();
					audioBlockingReasonString.setValue(String.valueOf(reason));
					callbackFired.setValue(true);
				}
			});
		}

		@Override
		public void onFramebufferBlocked(int reason, Bundle framebufferArea) throws RemoteException {

		}

		@Override
		public void onFramebufferUnblocked() throws RemoteException {
		}

		@Override
		public void onAudioUnblocked() throws RemoteException {
			audioBlockingNotification.setValue(false);
			callbackFiredList.setNow();
			audioBlockingReasonString.setValue("");
			callbackFired.setValue(true);
		}
	};
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		

		contentCategories.put("GENERAL MEDIA", 0x00030000);
		contentCategories.put("NAVIGATION", 0x00050000);
		contentCategories.put("ANNOUNCEMENTS", 0x00080000);
		contentCategories.put("TELEPHONE", 0xF0000020);
		contentCategories.put("SPEECH", 0xF0000010);

		contentCategories.put("UNKNOWN", 0x00000000);
		contentCategories.put("PHONE", 0x00020000);
		contentCategories.put("PHONE_CONTACT_LIST", 0x00020001);
		contentCategories.put("PHONE_CALL_LOG", 0x00020002);
		contentCategories.put("MEDIA_MUSIC", 0x00030001);
		contentCategories.put("MEDIA_VIDEO", 0x00030002);
		contentCategories.put("MEDIA_GAMING", 0x00030003);
		contentCategories.put("MEDIA_IMAGE", 0x00030004);
		contentCategories.put("MESSAGING", 0x00040000);
		contentCategories.put("MESSAGING_SMS", 0x00040001);
		contentCategories.put("MESSAGING_MMS", 0x00040002);
		contentCategories.put("MESSAGING_EMAIL", 0x00040003);
		contentCategories.put("BROWSER", 0x00060000);
		contentCategories.put("BROWSER_APPLICATION_STORE", 0x00060001);
		contentCategories.put("PRODUCTIVITY", 0x00070000);
		contentCategories.put("PRODUCTIVITY_DOCUMENT_VIEWER", 0x00070001);
		contentCategories.put("PRODUCTIVITY_DOCUMENT_EDITOR", 0x00070002);
		contentCategories.put("INFORMATION_NEWS", 0x00080001);
		contentCategories.put("INFORMATION_WEATHER", 0x00080002);
		contentCategories.put("INFORMATION_STOCKS", 0x00080003);
		contentCategories.put("INFORMATION_TRAVEL", 0x00080004);
		contentCategories.put("INFORMATION_SPORTS", 0x00080005);
		contentCategories.put("INFORMATION_CLOCK", 0x00080006);
		contentCategories.put("SOCIAL_NETWORKING", 0x00090000);
		contentCategories.put("PIM", 0x000a0000);
		contentCategories.put("PIM_CALENDAR", 0x000a0001);
		contentCategories.put("PIM_NOTES", 0x000a0002);
		createContentView();

		mMediaPlayer = ((ApplicationContext)getMirrorLinkApplicationContext()).getMediaPlayer();
	}
	
	
	@Override 
	public void onDestroy()
	{
		super.onDestroy();
	}
	

	void setContextInformation()
	{
		if (contextManager != null)
		{
			boolean handle = handleBlocking.getValue();
			
			try {	
				String[] cat = contentCategoryInputView.getValue().split("\n");
				int[] categoriesArray = new int[cat.length];
				
				for (int i=0; i<cat.length; i++)
				{
					if (contentCategories.containsKey(cat[i]))
						categoriesArray[i] = contentCategories.get(cat[i]);
				}
				

				
				contextManager.setAudioContextInformation(audioContent.getValue(),categoriesArray, handle);
			}
			catch (Exception e) {
				Toast.makeText(this, "Unable to send Context information.", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
				return;
			}
		}
	}

	ScrollView buildUI()
	{
		ScrollView scrollView = new ScrollView(this);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		
		layout.addView(new HeaderView(this,"Audio test file"));
		layout.addView(new HeaderView(this,"The Elevator Bossa Nova- Bensound.com http://www.bensound.com/royalty-free-music/2"));

		
		play = new Button(this);
		play.setText("Play");
		play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (!mMediaPlayer.isPlaying())
				{
					AssetFileDescriptor audioFile;
					
					try {
						audioFile = getAssets().openFd("bensound-theelevatorbossanova.mp3");
						mMediaPlayer.setDataSource(audioFile.getFileDescriptor(), audioFile.getStartOffset(), audioFile.getLength());
						mMediaPlayer.prepare();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					mMediaPlayer.setLooping(loopPlaying.getValue());
					mMediaPlayer.start();	
				}
			}
		});
		layout.addView(play);
		
		stop = new Button(this);
		stop.setText("Stop");
		stop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mMediaPlayer.isPlaying())
				{
					mMediaPlayer.stop();
					mMediaPlayer.reset();
				}

			}
		});
		layout.addView(stop);
		
		loopPlaying = new BooloeanInputView(this, "Play in loop", false);
		layout.addView(loopPlaying);

		layout.addView(new HeaderView(this,"Audio context information"));
		
		applicationContentBoolView = new BooloeanInputView(this,"Application content",false);
		layout.addView(applicationContentBoolView);

		contentCategoryInputView = new TextViewString(this,"Content category","");
		layout.addView(contentCategoryInputView);
		
		contentCategoryInputView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder= new AlertDialog.Builder(MirrorLinkAudioContextInformation.this);
			
				final CharSequence[] items = new CharSequence[contentCategories.size()];
				final boolean[] selectedItems = new boolean[contentCategories.size()];
				
				int i = 0;
				for (String key : contentCategories.keySet()) 
				{
					items[i++] = key;
				}
				
			
				builder.setTitle("Select content categories");		
				builder.setMultiChoiceItems(items, selectedItems, new DialogInterface.OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
					}
				});
				
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String contentCategory = "";
						
						for(int i=0; i<selectedItems.length;i++)
						{
							if (selectedItems[i] == true)
							{
								contentCategory += items[i] + "\n";
							}						
						}
						contentCategoryInputView.setValue(contentCategory);
						dialog.cancel();
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});
		
		audioContent = new BooloeanInputView(this, "Audio Content", false);
		layout.addView(audioContent);
		
		handleBlocking = new BooloeanInputView(this, "Handle blocking", false);
		layout.addView(handleBlocking);
		
		setAudioContextInformation = new Button(this);
		layout.addView(setAudioContextInformation);
		setAudioContextInformation.setText("Set Audio Context Information");
		setAudioContextInformation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setContextInformation();
			}
		});
		
		reset = new Button(this);
		reset.setText("Reset audio context information");
		reset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					//Miscellaneous content
					int[] audioCategory = {0};
					
					if (contextManager  != null)
						contextManager.setAudioContextInformation(false, audioCategory,false);
					
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		layout.addView(reset);
		
		
		layout.addView(new HeaderView(this,"Audio Blocking Notification"));
		
		audioBlockingNotification = new SuccesView(this,"Is audio blocked ", false);
		layout.addView(audioBlockingNotification);
		
		resultResponse = new BooloeanInputView(this, "Result response for audio blocking notification", false);
		layout.addView(resultResponse);
		

		audioBlockingReasonString = new TextViewString(this, "Audio blocking reason","n/a");
		layout.addView(audioBlockingReasonString);
		

		
		layout.addView(new HeaderView(this,"callbacks"));
		callbackFired= new SuccesView(this,"Callback fired",false);
		layout.addView(callbackFired);
		callbackFiredList = new LastExecutedViewMultiline(this);
		layout.addView(callbackFiredList);
		clearCallbackList = new Button(this);
		layout.addView(clearCallbackList);
		clearCallbackList.setText("Clear callback logs");
		clearCallbackList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				audioBlockingReasonString.setValue("n/a");
				callbackFiredList.clear();
				callbackFired.setValue(false);
			}
		});

		
		scrollView.addView(layout);
		return scrollView;
	}


}
