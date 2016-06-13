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

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.carconnectivity.testapp.BaseActivity;
import com.carconnectivity.testapp.R;

public class AudioChannels extends BaseActivity {

	private final static String LOG_TAG = AudioChannels.class
			.getCanonicalName();

	private MediaPlayer media;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.audio_channels);

		((Button) findViewById(R.id.buttonLeftChannel))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						playMedia("lchan.mp3");
						Log.d(LOG_TAG, "Playing / left audio channel.");
					}
				});

		((Button) findViewById(R.id.buttonRightChannel))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						playMedia("rchan.mp3");
						Log.d(LOG_TAG, "Playing / right audio channel.");
					}
				});

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (media != null) {
			media.release();
			media = null;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void playMedia(String descriptorName) {
		if (media == null) {
			media = new MediaPlayer();
		} else {
			media.release();
			media = new MediaPlayer();
		}

		AssetFileDescriptor descriptor;
		try {
			descriptor = getAssets().openFd(descriptorName);
			media.setDataSource(descriptor.getFileDescriptor(),
					descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();
			media.prepare();
			media.setLooping(false);
			media.start();
		} catch (IOException e) {
			Log.e(LOG_TAG, "Error occured while trying to play a media.", e);
		}
	}

}
