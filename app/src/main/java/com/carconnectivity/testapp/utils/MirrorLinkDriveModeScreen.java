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
package com.carconnectivity.testapp.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.carconnectivity.testapp.ApplicationContext;
import com.carconnectivity.testapp.R;
import com.mirrorlink.android.commonapi.IDeviceStatusListener;

public class MirrorLinkDriveModeScreen extends Activity implements View.OnClickListener {

	private static String TAG = MirrorLinkDriveModeScreen.class.getCanonicalName();

	IDeviceStatusListener mDeviceStatusListener = new IDeviceStatusListener.Stub() {
		@Override
		public void onNightModeChanged(boolean nightMode) throws RemoteException {
		}

		@Override
		public void onMicrophoneStatusChanged(boolean micInput) throws RemoteException {
		}

		@Override
		public void onDriveModeChange(final boolean driveMode) throws RemoteException {
			if (!driveMode) {
				setResult(RESULT_OK);
				finish();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "onCreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.drive_mode_screen);

		final RelativeLayout layout = (RelativeLayout) findViewById(R.id.drive_mode_screen);
		layout.setOnClickListener(this);

		((ApplicationContext)getApplicationContext()).registerDeviceStatusManager(this, mDeviceStatusListener);
	}

	@Override
	protected void onDestroy() {

		((ApplicationContext)getApplicationContext()).unregisterDeviceStatusManager(this, mDeviceStatusListener);

		super.onDestroy();
	}

	@Override
	public void onClick(final View view) {
		// Go back to Home Screen
		final Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(startMain);
	}

	@Override
	public void onBackPressed() {

	}
}
