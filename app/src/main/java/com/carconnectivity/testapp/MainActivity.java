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


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mirrorlink.android.commonapi.Defs;

import com.carconnectivity.testapp.dummy.DriverModeScrewdriver;

public class MainActivity extends Activity {
	private final static String LOG_TAG = MainActivity.class.getCanonicalName();
	
	private DriverModeScrewdriver driverMode;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.v(LOG_TAG, "onCreate");
		super.onCreate(savedInstanceState);

		final Intent intent = getIntent();
		if (intent != null) {
			final String action = intent.getAction();
			if (Defs.Intents.TERMINATE_MIRRORLINK_APP.equals(action)) {
				finishAffinity();
				return;
			}
		}

		final ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);

			final View homeBtn = findViewById(android.R.id.home);

			if (homeBtn != null) {
				final View.OnClickListener clickListener = new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// Go back to Home Screen
						final Intent startMain = new Intent(Intent.ACTION_MAIN);
						startMain.addCategory(Intent.CATEGORY_HOME);
						startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
						startActivity(startMain);
					}
				};

				MenuFragment.installOnClickListenerToHomeBtn(
						homeBtn,
						clickListener);
			}
		}

	    getFragmentManager().beginTransaction().replace(android.R.id.content, new MenuFragment()).commit();
	}

	@Override
	protected void onNewIntent(final Intent intent) {
		Log.v(LOG_TAG, "onNewIntent");

		if (intent != null) {
			final String action = intent.getAction();
			if (Defs.Intents.TERMINATE_MIRRORLINK_APP.equals(action)) {
				finishAffinity();
				return;
			}
		}
		super.onNewIntent(intent);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		driverMode = new DriverModeScrewdriver(this);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		driverMode.dispose();
		driverMode = null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (driverMode != null)
			driverMode.onResult(requestCode, resultCode);
	}
}
