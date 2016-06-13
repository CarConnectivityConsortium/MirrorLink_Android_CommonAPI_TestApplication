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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.carconnectivity.testapp.views.BooloeanInputView;
import com.carconnectivity.testapp.views.HeaderView;
import com.carconnectivity.testapp.views.IntInputView;
import com.carconnectivity.testapp.views.LastExecutedViewMultiline;
import com.carconnectivity.testapp.views.PaintView;
import com.carconnectivity.testapp.views.SuccesView;
import com.carconnectivity.testapp.views.TextViewString;
import com.mirrorlink.android.commonapi.Defs;
import com.mirrorlink.android.commonapi.IContextListener;
import com.mirrorlink.android.commonapi.IContextManager;

public class MirrorLinkContextInformation extends BaseActivity {
	PaintView pv;

	TextViewString applicationCategoryInputView = null;
	TextViewString contentCategoryInputView = null;

	IntInputView startXInputView = null;
	IntInputView startYInputView = null;

	IntInputView xInputView = null;
	IntInputView yInputView = null;

	Button setFramebufferStatus = null;

	BooloeanInputView handleBlocking = null;

	HashMap<String, Integer> contentCategories = new HashMap<String, Integer>();
	HashMap<String, Integer> applicationCategories = new HashMap<String, Integer>();
	
	int selectedItem = 0;
	
	PaintView blockingPaintView;

	private IContextManager mContextManager = null;
	
	TextViewString framebufferAreaString = null;
	TextViewString framebufferBlockingReasonString = null;
	SuccesView callbackFired= null;
	LastExecutedViewMultiline callbackFiredList = null;
	BooloeanInputView resultResponse = null;
	Button clearCallbackList = null;
	
	IContextListener mContextListener = new IContextListener.Stub() {
		@Override
		public void onFramebufferBlocked(final int reason, final Bundle fbArea) throws RemoteException {
			
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					String fbString = fbArea.getInt(Defs.Rect.X) + "," + fbArea.getInt(Defs.Rect.Y) + " " +fbArea.getInt(Defs.Rect.WIDTH) + "x" + fbArea.getInt(Defs.Rect.HEIGHT);
					
					blockingPaintView.setDimmension(fbArea.getInt(Defs.Rect.WIDTH) , fbArea.getInt(Defs.Rect.HEIGHT),fbArea.getInt(Defs.Rect.X) , fbArea.getInt(Defs.Rect.Y) );
					framebufferAreaString.setValue(fbString);

					
					framebufferBlockingReasonString.setValue(String.valueOf(reason));

					callbackFired.setValue(true);
					callbackFiredList.setNow();
				}
			});
		}
		
		@Override
		public void onAudioBlocked(int reason) throws RemoteException {
		}

		@Override
		public void onFramebufferUnblocked() throws RemoteException {
			String fbString = 0 + "," + 0 + " " +0 + "x" + 0;
			
			blockingPaintView.setDimmension(0,0,0,0);
			framebufferAreaString.setValue(fbString);

			
			framebufferBlockingReasonString.setValue("n/a");

			callbackFired.setValue(true);
			callbackFiredList.setNow();
			
		}

		@Override
		public void onAudioUnblocked() throws RemoteException {
			
		}
	};
	
	
	@Override
	protected void onResume()
	{
		mContextManager = getMirrorLinkApplicationContext().registerContextManager(this, mContextListener);
		if (mContextManager == null)
		{
			finish();
		}
		super.onResume();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		getMirrorLinkApplicationContext().unregisterContextManager(this, null);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v("BaseActivity", "onCreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);

		RelativeLayout layout = new RelativeLayout(this);
		pv = new PaintView(this);
		blockingPaintView = new PaintView(this, Color.RED);
		layout.addView(buildUI());
		layout.addView(pv);
		layout.addView(blockingPaintView);
		
		
		setContentView(layout);

		applicationCategories.put("UNKNOWN", 0x00000000);
		applicationCategories.put("PHONE", 0x00020000);
		applicationCategories.put("PHONE_CONTACT_LIST", 0x00020001);
		applicationCategories.put("PHONE_CALL_LOG", 0x00020002);
		applicationCategories.put("MEDIA", 0x00030000);
		applicationCategories.put("MEDIA_MUSIC", 0x00030001);
		applicationCategories.put("MEDIA_VIDEO", 0x00030002);
		applicationCategories.put("MEDIA_GAMING", 0x00030003);
		applicationCategories.put("MEDIA_IMAGE", 0x00030004);
		applicationCategories.put("MESSAGING", 0x00040000);
		applicationCategories.put("MESSAGING_SMS", 0x00040001);
		applicationCategories.put("MESSAGING_MMS", 0x00040002);
		applicationCategories.put("MESSAGING_EMAIL", 0x00040003);
		applicationCategories.put("NAVIGATION", 0x00050000);
		applicationCategories.put("BROWSER", 0x00060000);
		applicationCategories.put("BROWSER_APPLICATION_STORE", 0x00060001);
		applicationCategories.put("PRODUCTIVITY", 0x00070000);
		applicationCategories.put("PRODUCTIVITY_DOCUMENT_VIEWER", 0x00070001);
		applicationCategories.put("PRODUCTIVITY_DOCUMENT_EDITOR", 0x00070002);
		applicationCategories.put("INFORMATION", 0x00080000);
		applicationCategories.put("INFORMATION_NEWS", 0x00080001);
		applicationCategories.put("INFORMATION_WEATHER", 0x00080002);
		applicationCategories.put("INFORMATION_STOCKS", 0x00080003);
		applicationCategories.put("INFORMATION_TRAVEL", 0x00080004);
		applicationCategories.put("INFORMATION_SPORTS", 0x00080005);
		applicationCategories.put("INFORMATION_CLOCK", 0x00080006);
		applicationCategories.put("SOCIAL_NETWORKING", 0x00090000);
		applicationCategories.put("PIM", 0x000a0000);
		applicationCategories.put("PIM_CALENDAR", 0x000a0001);
		applicationCategories.put("PIM_NOTES", 0x000a0002);

		contentCategories.put("Text", 0);
		contentCategories.put("Video", 1);
		contentCategories.put("Image", 2);
		contentCategories.put("Vector Graphics", 3);
		contentCategories.put("3D Graphics", 4);
		contentCategories.put("User Interface", 5);
		contentCategories.put("Car Mode", 16);

	}

	void sendFrabufferStatus() {
		if (mContextManager != null) {
			List<Bundle> rectList = new ArrayList<Bundle>();

			Bundle rect = new Bundle();

			Bundle r = new Bundle();

			r.putInt(Defs.Rect.WIDTH, xInputView.getValue());
			r.putInt(Defs.Rect.HEIGHT, yInputView.getValue());
			r.putInt(Defs.Rect.X, startXInputView.getValue());
			r.putInt(Defs.Rect.Y, startYInputView.getValue());
			
			rect.putInt(Defs.FramebufferAreaContent.CONTENT_CATEGORY, Integer.decode(contentCategoryInputView.getValue()));
			rect.putInt(Defs.FramebufferAreaContent.APPLICATION_CATEGORY,applicationCategories.get(applicationCategoryInputView.getValue()));
			rect.putBundle(Defs.FramebufferAreaContent.RECT, r);

			rectList.add(rect);
			
			boolean handle = handleBlocking.getValue();

			try {
				mContextManager.setFramebufferContextInformation(rectList, handle);
				pv.setDimmension(xInputView.getValue(), yInputView.getValue(), startXInputView.getValue(), startYInputView.getValue());
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}


		}

	}

	ScrollView buildUI() {
		ScrollView scrollView = new ScrollView(this);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		layout.addView(new HeaderView(this, "framebuffer context information"));

		applicationCategoryInputView = new TextViewString(this, "Application category", "UNKNOWN");
		layout.addView(applicationCategoryInputView);
		applicationCategoryInputView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MirrorLinkContextInformation.this);
				
				final CharSequence[] items = new CharSequence[applicationCategories.size()];

				int i = 0;
				for (String key : applicationCategories.keySet()) {
					items[i++] = key;
				}

				builder.setTitle("Select content categories");
				builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						selectedItem = which;
						
					}
					
				});
				
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						applicationCategoryInputView.setValue(items[selectedItem].toString());
						dialog.cancel();
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();

			}
		});
		
		
		
		
		

		contentCategoryInputView = new TextViewString(this, "Content category", "0x0");
		layout.addView(contentCategoryInputView);

		contentCategoryInputView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MirrorLinkContextInformation.this);

				final CharSequence[] items = new CharSequence[contentCategories.size()];
				final boolean[] selectedItems = new boolean[contentCategories.size()];

				int i = 0;
				for (String key : contentCategories.keySet()) {
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
						int contentCategory = 0;
						
						for (int i = 0; i < selectedItems.length; i++) 
						{
							if (selectedItems[i] == true) {
								contentCategory += (1 << contentCategories.get(items[i]));
							}
						}
						contentCategoryInputView.setValue("0x" + String.format("%08x", contentCategory).toUpperCase());
						dialog.cancel();
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();

			}
		});

		startXInputView = new IntInputView(this, "Location of rectangle X", 0);
		layout.addView(startXInputView);

		startYInputView = new IntInputView(this, "Location of rectangle Y", 0);
		layout.addView(startYInputView);

		xInputView = new IntInputView(this, "Width of rectangle", 0);
		layout.addView(xInputView);

		yInputView = new IntInputView(this, "Height of rectangle", 0);
		layout.addView(yInputView);

		handleBlocking = new BooloeanInputView(this, "Handle blocking", false);
		layout.addView(handleBlocking);

		setFramebufferStatus = new Button(this);
		layout.addView(setFramebufferStatus);
		setFramebufferStatus.setText("Set framebuffer status");
		setFramebufferStatus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendFrabufferStatus();
			}
		});

		Button reset = new Button(this);
		reset.setText("Reset context information");
		reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					List<Bundle> content = new ArrayList<Bundle>();
					
					Bundle rect = new Bundle();
					Bundle r = new Bundle();
					r.putInt(Defs.Rect.WIDTH, getWindow().getDecorView().getWidth());
					r.putInt(Defs.Rect.HEIGHT, getWindow().getDecorView().getHeight());
					r.putInt(Defs.Rect.X, 0);
					r.putInt(Defs.Rect.Y, 0);
					rect.putBundle(Defs.FramebufferAreaContent.RECT, r);
					rect.putInt(Defs.FramebufferAreaContent.CONTENT_CATEGORY,0);
					rect.putInt(Defs.FramebufferAreaContent.APPLICATION_CATEGORY, 0);
					
					
					
					content.add(rect);
					
					if (mContextManager != null)
						mContextManager.setFramebufferContextInformation(content, false);
					
					
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		
		
		layout.addView(new HeaderView(this,"Blocking notification"));
		
		
		resultResponse = new BooloeanInputView(this, "Result response for framebuffer blocking notification", false);
		layout.addView(resultResponse);
		
		framebufferAreaString = new TextViewString(this, "Framebuffer area", "0,0 0x0");
		layout.addView(framebufferAreaString);

		framebufferBlockingReasonString = new TextViewString(this, "Framebuffer blocking reason","n/a");
		layout.addView(framebufferBlockingReasonString);
		

		
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
				callbackFiredList.clear();
				callbackFired.setValue(false);
			}
		});
		
		
		
		
		layout.addView(reset);
		scrollView.addView(layout);
		return scrollView;
	}

}
