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

import com.carconnectivity.testapp.views.HeaderView;
import com.carconnectivity.testapp.views.LastExecutedViewMultiline;
import com.carconnectivity.testapp.views.SuccesView;
import com.mirrorlink.android.commonapi.IConnectionListener;

public class MirrorLinkConnection extends BaseActivity {
	SuccesView connectionActive = null;
	Button getConncetionStatus = null;
	
	SuccesView callbackFired = null;
	LastExecutedViewMultiline callbackFiredList = null;
	Button clearCallbackList =null;
	

	@Override
	protected void onResume()
	{
		getMirrorLinkApplicationContext().registerConnectionManager(this, mConnectionListener);
		
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
	
	
	IConnectionListener mConnectionListener = new IConnectionListener.Stub()
	{
		@Override
		public void onMirrorLinkSessionChanged(final boolean mirrolinkSessionIsEstablished) throws RemoteException {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					connectionActive.setValue(mirrolinkSessionIsEstablished);
					callbackFired.setValue(true);
					callbackFiredList.setNow();
				}
			});
		}
		@Override
		public void onAudioConnectionsChanged(Bundle audioConnections) throws RemoteException {}
		@Override
		public void onRemoteDisplayConnectionChanged(int remoteDisplayConnection) throws RemoteException {}
	};
	
	@Override
	void updateValues()
	{
		boolean isConencted = false;
		if (getMirrorLinkApplicationContext().getConnectionManager() != null)
		{
			try {
				isConencted = getMirrorLinkApplicationContext().getConnectionManager().isMirrorLinkSessionEstablished();
			} catch (RemoteException e) {
				e.printStackTrace();
				return;
			}
		}
		connectionActive.setValue(isConencted);	
	}

	ScrollView buildUI()
	{
		ScrollView scrollView = new ScrollView(this);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		layout.addView(new HeaderView(this,"MirrorLink Connection Status"));
	
		connectionActive = new SuccesView(this, "Connection active", false);
		layout.addView(connectionActive);
		getConncetionStatus =new Button(this);
		layout.addView(getConncetionStatus);
		getConncetionStatus.setText("Get Connection Status");
		getConncetionStatus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateValues();
			}
		});;
		
		layout.addView(new HeaderView(this,"MirrorLink Connection Callbacks"));
		callbackFired = new SuccesView(this, "Callback fired", false);
		layout.addView(callbackFired);
		callbackFiredList = new LastExecutedViewMultiline(this);
		layout.addView(callbackFiredList);
		
		
		clearCallbackList = new Button(this);
		clearCallbackList.setText("Clear callback logs");
		clearCallbackList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callbackFiredList.clear();
				callbackFired.setValue(false);
			}
		});
		layout.addView(clearCallbackList);
		
		
		
		scrollView.addView(layout);
		return scrollView;
	}

}
