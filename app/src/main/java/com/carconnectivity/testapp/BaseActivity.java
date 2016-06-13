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
import android.os.RemoteException;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.Toast;

import com.carconnectivity.testapp.dummy.DriverModeScrewdriver;
import com.mirrorlink.android.commonapi.IConnectionListener;
import com.mirrorlink.android.commonapi.IConnectionManager;
import com.mirrorlink.lib.MirrorLinkApplicationContext;
import com.mirrorlink.lib.ServiceReadyCallback;

public class BaseActivity extends Activity {
	private final static String LOG_TAG = BaseActivity.class.getCanonicalName();
	
	private DriverModeScrewdriver driveMode;
	private IConnectionManager mConnectionManager = null;
	private IConnectionListener mConnectionListener = new IConnectionListener.Stub() {

		@Override
		public void onRemoteDisplayConnectionChanged(int remoteDisplayConnection) throws RemoteException {}
		
		@Override
		public void onMirrorLinkSessionChanged(final boolean mirrolinkSessionIsEstablished) throws RemoteException {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					isMirrorLinkConnected(mirrolinkSessionIsEstablished);
				}
			});
		}
		
		@Override
		public void onAudioConnectionsChanged(Bundle audioConnections) throws RemoteException {}
	};
	
	
	void isMirrorLinkConnected(boolean connected)
	{
		ActionBar actionBar = getActionBar();
		
		if (connected)
		{
			if (actionBar != null)
			{
				actionBar.setIcon(R.drawable.ic_action_connected);
			}
		}
		else
		{
			if (actionBar != null)
			{
				actionBar.setIcon(R.drawable.ic_action_disconnected);
			}
		}
	}
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		Log.v("BaseActivity", "onCreate");
		super.onCreate(savedInstanceState);
		
		createContentView();
		
		ActionBar actionBar = getActionBar();
		if (actionBar!=null)
			actionBar.setIcon(R.drawable.ic_action_disconnected);
		
	}
	@Override
	protected void onDestroy() {
		Log.v(LOG_TAG, "onDestroy");
	    super.onDestroy();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (driveMode != null)
			driveMode.onResult(requestCode, resultCode);
	}	
		
	public MirrorLinkApplicationContext getMirrorLinkApplicationContext()
	{
		return (MirrorLinkApplicationContext) getApplicationContext();
	}

	protected void createContentView()
	{
		setContentView(buildUI());
	}

	ScrollView buildUI()
	{
		ScrollView scrollView = new ScrollView(this);
		return scrollView;
	}

	void updateValues()
	{
	
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		driveMode = new DriverModeScrewdriver(this);
        mConnectionManager = getMirrorLinkApplicationContext().registerConnectionManager(this, mConnectionListener);

        if (mConnectionManager != null)
        {
			try{
				isMirrorLinkConnected(mConnectionManager.isMirrorLinkSessionEstablished());
			}catch (Exception e){
				e.printStackTrace();
			}
        }
        updateValues();
	}
	protected void onPause()
	{
		driveMode.dispose();
		driveMode = null;
		getMirrorLinkApplicationContext().unregisterConnectionManager(this, mConnectionListener);
		super.onPause();
	}


}
