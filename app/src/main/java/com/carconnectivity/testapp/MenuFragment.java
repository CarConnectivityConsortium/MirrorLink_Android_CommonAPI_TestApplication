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

import android.app.Dialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MenuFragment extends PreferenceFragment {
	private final static String LOG_TAG = MenuFragment.class.getCanonicalName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.v(LOG_TAG, "onCreate");
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.menu);
	}

	@Override
	public boolean onPreferenceTreeClick(
			final PreferenceScreen preferenceScreen,
			final Preference preference) {
		super.onPreferenceTreeClick(preferenceScreen, preference);

		if (preference instanceof PreferenceScreen) {
			MenuFragment.initializeActionBarHomeBtn((PreferenceScreen) preference);
		}

		return false;
	}

	public static void installOnClickListenerToHomeBtn(
			final View homeBtn,
			final View.OnClickListener clickListener) {

		if (homeBtn == null) {
			Log.v(LOG_TAG, "Failed to install onClickListener to home btn");
			return;
		}

		final ViewParent homeBtnContainer = homeBtn.getParent();

		// The home button is an ImageView inside a FrameLayout
		if (homeBtnContainer instanceof FrameLayout) {
			final ViewGroup containerParent = (ViewGroup)homeBtnContainer.getParent();

			if (containerParent instanceof LinearLayout) {
				containerParent.setOnClickListener(
						clickListener);
			} else {
				// Just set it on the home button
				((FrameLayout) homeBtnContainer).setOnClickListener(
						clickListener);
			}
		} else {
			// The 'If all else fails' default case
			homeBtn.setOnClickListener(clickListener);
		}
	}

	/** Sets up the action bar for an {@link PreferenceScreen} */
	private static void initializeActionBarHomeBtn(
			final PreferenceScreen preferenceScreen) {
		final Dialog dialog = preferenceScreen.getDialog();

		if (dialog != null) {
			// Initialize the action bar
			dialog.getActionBar().setDisplayHomeAsUpEnabled(true);

			// Initialize the action bar home btn
			final View homeBtn = dialog.findViewById(android.R.id.home);
			final View.OnClickListener dismissDialogClickListener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			};

			MenuFragment.installOnClickListenerToHomeBtn(
					homeBtn,
					dismissDialogClickListener);
		}
	}
}
