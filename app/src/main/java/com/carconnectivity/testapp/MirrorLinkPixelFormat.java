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


import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.carconnectivity.testapp.views.HeaderView;
import com.carconnectivity.testapp.views.LastExecutedViewMultiline;
import com.carconnectivity.testapp.views.SuccesView;
import com.carconnectivity.testapp.views.TextViewInt;
import com.mirrorlink.android.commonapi.Defs;
import com.mirrorlink.android.commonapi.IDisplayListener;
import com.mirrorlink.android.commonapi.IDisplayManager;

public class MirrorLinkPixelFormat extends BaseActivity {
	TextViewInt pixelFormatBitsPerPixel = null;
	TextViewInt pixelFormatDepth = null;
	TextViewInt pixelFormatRed = null;
	TextViewInt pixelFormatGreen = null;
	TextViewInt pixelFormatBlue = null;
	SuccesView success = null;
	Button getPixelFormat = null;
	SuccesView callbackFired= null;
	LastExecutedViewMultiline callbackFiredList = null;
	Button clearCallbackList = null;
	private IDisplayManager mDisplayManager = null;
	
	@Override
	protected void onResume()
	{
		mDisplayManager = getMirrorLinkApplicationContext().registerDisplayManager(this, mDisplayListener);
		if (mDisplayManager== null)
		{
			Toast.makeText(this, "Unable to get display manager.", Toast.LENGTH_LONG).show();
			finish();
		}
		super.onResume();
	}
	@Override
	protected void onPause()
	{
		getMirrorLinkApplicationContext().unregisterDisplayManager(this, mDisplayListener);
		super.onPause();
	}
	

	IDisplayListener mDisplayListener = new IDisplayListener.Stub() {
		@Override
		public void onPixelFormatChanged(final Bundle pixelFormat) throws RemoteException {
			runOnUiThread(new Runnable() {
				public void run() {
					fillData(pixelFormat);

					callbackFired.setValue(true);
					callbackFiredList.setNow();
				}
			});
		}
		@Override
		public void onDisplayConfigurationChanged(final Bundle displayConfiguration) throws RemoteException {
	
		}
	};

	void updateValues()
	{
		Bundle pixelFormats= null;
		if (mDisplayManager != null)
		{
			try {
				pixelFormats = mDisplayManager.getClientPixelFormat();
			} catch (RemoteException e) {
				e.printStackTrace();
				success.setValue(false);
				return;
			}
		}
		if (pixelFormats != null)
		{
			fillData(pixelFormats);
			success.setValue(true);
		}

		
	}

	void fillData(Bundle clientPixelFormat)
	{
		pixelFormatBitsPerPixel.setValue(clientPixelFormat.getInt(Defs.ClientPixelFormat.BITS_PER_PIXEL));
		pixelFormatDepth.setValue(clientPixelFormat.getInt(Defs.ClientPixelFormat.DEPTH));
		pixelFormatRed.setValue(clientPixelFormat.getInt(Defs.ClientPixelFormat.RED_MAX));
		pixelFormatGreen.setValue(clientPixelFormat.getInt(Defs.ClientPixelFormat.GREEN_MAX));
		pixelFormatBlue.setValue(clientPixelFormat.getInt(Defs.ClientPixelFormat.BLUE_MAX));
		
	}

	
	ScrollView buildUI()
	{
		ScrollView scrollView = new ScrollView(this);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		pixelFormatBitsPerPixel = new TextViewInt(this, "Bits per pixel", 0);
		layout.addView(pixelFormatBitsPerPixel);
		
		pixelFormatDepth = new TextViewInt(this, "Depth", 0);
		layout.addView(pixelFormatDepth);
		
		pixelFormatRed = new TextViewInt(this, "Red", 0);
		layout.addView(pixelFormatRed);
		
		pixelFormatGreen = new TextViewInt(this, "Green", 0);
		layout.addView(pixelFormatGreen);
		
		pixelFormatBlue = new TextViewInt(this, "Blue", 0);
		layout.addView(pixelFormatBlue);
		
	

		success = new SuccesView(this, "Get Supported Pixel Format succeed", false);
		layout.addView(success);
		
		getPixelFormat = new Button(this);
		layout.addView(getPixelFormat);
		getPixelFormat.setText("Get supported pixel formats");
		getPixelFormat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateValues();
			}
		});
		
		
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
		
		scrollView.addView(layout);
		return scrollView;
	}

}
