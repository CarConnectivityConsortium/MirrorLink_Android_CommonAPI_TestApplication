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
import com.carconnectivity.testapp.views.TextViewString;
import com.mirrorlink.android.commonapi.Defs;
import com.mirrorlink.android.commonapi.IConnectionListener;

public class MirrorLinkRemoteDisplayConnection extends BaseActivity {
	TextViewString remoteDisplayType = null;
	Button getEstablishedREmoteDisplayConnections = null;
	
	SuccesView callbackFired= null;
	LastExecutedViewMultiline callbackFiredList = null;
	Button clearCallbackList = null;
	
	@Override
	protected void onResume()
	{
		getMirrorLinkApplicationContext().registerConnectionManager(this, mConnectionListener);

		if (getMirrorLinkApplicationContext().getConnectionManager()== null)
		{
			Toast.makeText(this, "Unable to get connection manager.", Toast.LENGTH_LONG).show();
			finish();
		}
		super.onResume();

	}
	@Override
	protected void onPause()
	{
		getMirrorLinkApplicationContext().registerConnectionManager(this, mConnectionListener);
		super.onPause();
	}
	
	
	
	IConnectionListener mConnectionListener = new IConnectionListener.Stub()
	{
		@Override
		public void onMirrorLinkSessionChanged(final boolean mirrolinkSessionIsEstablished) throws RemoteException {
		}
		@Override
		public void onAudioConnectionsChanged(Bundle audioConnections) throws RemoteException {}
		
		@Override
		public void onRemoteDisplayConnectionChanged(final int remoteDisplayConnection) throws RemoteException {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					fillData(remoteDisplayConnection);
					callbackFired.setValue(true);
					callbackFiredList.setNow();
				}
			});
		}
	};
	
	void updateValues()
	{
		int connections = 0;
		if (getMirrorLinkApplicationContext().getConnectionManager() != null)
		{
			try {
				connections = getMirrorLinkApplicationContext().getConnectionManager().getRemoteDisplayConnections();
			} catch (RemoteException e) {
				Toast.makeText(this, "Unable to get remote display type." , Toast.LENGTH_LONG).show();
				e.printStackTrace();
				return;
			}
		}
		
		fillData(connections);

	}
	
	void fillData(int connection)
	{
		if (connection == Defs.RemoteDisplayConnection.REMOTEDISPLAY_NONE)
			remoteDisplayType.setValue("None");
		else if (connection == Defs.RemoteDisplayConnection.REMOTEDISPLAY_VNC)
			remoteDisplayType.setValue("VNC");
		else if (connection == Defs.RemoteDisplayConnection.REMOTEDISPLAY_HSML)
			remoteDisplayType.setValue("HSML");
		else if (connection == Defs.RemoteDisplayConnection.REMOTEDISPLAY_WFD)
			remoteDisplayType.setValue("WFD");
		else if (connection == Defs.RemoteDisplayConnection.REMOTEDISPLAY_OTHER)
			remoteDisplayType.setValue("Other");
		
	}
	
	@Override
	ScrollView buildUI()
	{
		ScrollView scrollView = new ScrollView(this);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		remoteDisplayType = new TextViewString(this, "Remote display type", "");
		layout.addView(remoteDisplayType);
		
		getEstablishedREmoteDisplayConnections = new Button(this);
		layout.addView(getEstablishedREmoteDisplayConnections);
		getEstablishedREmoteDisplayConnections.setText("Get Established Remote Display Connections");
		getEstablishedREmoteDisplayConnections.setOnClickListener(new OnClickListener() {
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
