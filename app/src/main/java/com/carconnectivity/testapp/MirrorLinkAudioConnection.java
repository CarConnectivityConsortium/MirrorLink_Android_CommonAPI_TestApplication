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
import com.carconnectivity.testapp.views.LastExecutedView;
import com.carconnectivity.testapp.views.LastExecutedViewMultiline;
import com.carconnectivity.testapp.views.SuccesView;
import com.carconnectivity.testapp.views.TextViewString;
import com.mirrorlink.android.commonapi.Defs;
import com.mirrorlink.android.commonapi.IConnectionListener;

public class MirrorLinkAudioConnection extends BaseActivity {
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

	
	@Override
	protected void onResume()
	{
		getMirrorLinkApplicationContext().registerConnectionManager(this,mConnectionListener);
		
		if (getMirrorLinkApplicationContext().getConnectionManager()== null)
		{
			finish();
		}
		super.onResume();

	}
	@Override
	protected void onPause()
	{
		getMirrorLinkApplicationContext().unregisterConnectionManager(this, mConnectionListener);
		super.onPause();
	}

	
	IConnectionListener mConnectionListener = new IConnectionListener.Stub() {
		@Override
		public void onMirrorLinkSessionChanged(final boolean mirrolinkSessionIsEstablished) throws RemoteException {

		}

		@Override
		public void onAudioConnectionsChanged(final Bundle audioConnections) throws RemoteException {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					fillConnectionData(audioConnections);
					timestampLastUpdate.setNow();
					callbackFired.setValue(true);
					callbackFiredList.setNow();
				}
			});
		}

		@Override
		public void onRemoteDisplayConnectionChanged(int remoteDisplayConnection) throws RemoteException {
		}
	};

	@Override
	void updateValues() {
		Bundle connections = null;
		if (getMirrorLinkApplicationContext().getConnectionManager() != null) {
			try {
				connections = getMirrorLinkApplicationContext().getConnectionManager().getAudioConnections();
			} catch (RemoteException e) {
				e.printStackTrace();
				return;
			}
		}
		if (connections == null) {
			Toast.makeText(this, "Unable to get audio connections.", Toast.LENGTH_LONG).show();
			return;
		}

		fillConnectionData(connections);

		
		timestampLastUpdate.setNow();

	}
	
	private void fillConnectionData(Bundle audioConnections)
	{
		int mediaOut = audioConnections.getInt(Defs.AudioConnections.MEDIA_AUDIO_OUT);
		if (mediaOut == Defs.AudioConnections.MEDIA_OUT_NONE)
			mediaOutString.setValue("Not Established");
		else if (mediaOut == Defs.AudioConnections.MEDIA_OUT_BT_AD2DP)
			mediaOutString.setValue("BT A2DP");
		else if (mediaOut == Defs.AudioConnections.MEDIA_OUT_RTP)
			mediaOutString.setValue("RTP");

		int mediaIn = audioConnections.getInt(Defs.AudioConnections.MEDIA_AUDIO_IN);
		if (mediaIn == Defs.AudioConnections.MEDIA_IN_NONE)
			mediaInString.setValue("Not Established");
		else if (mediaIn == Defs.AudioConnections.MEDIA_IN_RTP)
			mediaInString.setValue("RTP");
		

		int voiceControl = audioConnections.getInt(Defs.AudioConnections.VOICE_CONTROL);
		if (voiceControl == Defs.AudioConnections.VOICE_CONTROL_NONE)
			voiceControllString.setValue("Not Established");
		else if (voiceControl == Defs.AudioConnections.VOICE_CONTROL_BT_HFP)
			voiceControllString.setValue("BT HFP + BVRA");
		else if (voiceControl == Defs.AudioConnections.VOICE_CONTROL_RTP)
			voiceControllString.setValue("RTP");
		
		
		int phoneAudio = audioConnections.getInt(Defs.AudioConnections.PHONE_AUDIO);
		if (phoneAudio == Defs.AudioConnections.INCALL_AUDIO_NONE)
			phoneAudioString.setValue("Not Established");
		if (phoneAudio == Defs.AudioConnections.INCALL_AUDIO_BT_HFP)
			phoneAudioString.setValue("BT HFP");
		else if (phoneAudio == Defs.AudioConnections.INCALL_AUDIO_RTP)
			phoneAudioString.setValue("RTP");
		
		rtpPayloadTypes.setValue(audioConnections.getString(Defs.AudioConnections.PAYLOAD_TYPES));
	}
	
	

	@Override
	ScrollView buildUI() {
		ScrollView scrollView = new ScrollView(this);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		mediaOutString = new TextViewString(this, "Media out", "");
		layout.addView(mediaOutString);

		mediaInString = new TextViewString(this,"Media In", "");
		layout.addView(mediaInString);

		voiceControllString = new TextViewString(this, "Voice control", "");
		layout.addView(voiceControllString);

		phoneAudioString = new TextViewString(this, "Phone audio", "");
		layout.addView(phoneAudioString);


		rtpPayloadTypes = new TextViewString(this, "Supported RTP Payload Types", "");
		layout.addView(rtpPayloadTypes);
		timestampLastUpdate = new LastExecutedView(this);
		layout.addView(timestampLastUpdate);

		getEstablishedAudioConnection = new Button(this);
		layout.addView(getEstablishedAudioConnection);
		getEstablishedAudioConnection.setText("Get established Audio Connections");
		getEstablishedAudioConnection.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateValues();
			}
		});

		layout.addView(new HeaderView(this, "callbacks"));
		callbackFired = new SuccesView(this, "Callback fired", false);
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
