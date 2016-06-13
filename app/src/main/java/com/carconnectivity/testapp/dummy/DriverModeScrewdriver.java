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
package com.carconnectivity.testapp.dummy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.RemoteException;

import com.carconnectivity.testapp.ApplicationContext;
import com.carconnectivity.testapp.utils.MirrorLinkDriveModeScreen;
import com.mirrorlink.android.commonapi.IDeviceStatusListener;
import com.mirrorlink.lib.MirrorLinkApplicationContext;
import com.mirrorlink.lib.ServiceReadyCallback;

public class DriverModeScrewdriver {

	private Activity mContext;

	private int mStoredRingerMode = AudioManager.RINGER_MODE_NORMAL;

	static final int DRIVE_MODE_REQUEST = 1;
	private MirrorLinkApplicationContext mMirrorLinkApplicationContext = null;

	public DriverModeScrewdriver(Activity activity) {
		this.mContext = activity;

		ApplicationContext.getContext(new ServiceReadyCallback() {
			@Override
			public void connected(MirrorLinkApplicationContext mirrorLinkApplicationContext) {
				mMirrorLinkApplicationContext = mirrorLinkApplicationContext;
				mirrorLinkApplicationContext.registerDeviceStatusManager(this, mDeviceStatusListener);
			}
		});
	}

	public void dispose(){
		if (mMirrorLinkApplicationContext!= null){
			mMirrorLinkApplicationContext.unregisterDeviceStatusManager(this, mDeviceStatusListener);
		}
	}
	
	IDeviceStatusListener mDeviceStatusListener = new IDeviceStatusListener.Stub() {

		@Override
		public void onNightModeChanged(boolean nightMode) throws RemoteException {
		}
		
		@Override
		public void onMicrophoneStatusChanged(boolean micInput) throws RemoteException {
		}
		
		@Override
		public void onDriveModeChange(final boolean driveMode) throws RemoteException {
			if (driveMode) {
				Intent intent = new Intent(mContext.getApplicationContext(), MirrorLinkDriveModeScreen.class);
				mStoredRingerMode = getRingerMode();
				setRingMode(AudioManager.RINGER_MODE_SILENT);
			    mContext.startActivityForResult(intent, DRIVE_MODE_REQUEST);
			}
			
		}
	};
	
	public void onResult(int requestCode, int resultCode) {
	    if (requestCode == DRIVE_MODE_REQUEST && resultCode==Activity.RESULT_OK) {
			setRingMode(mStoredRingerMode);
	    }
	}

	private int getRingerMode() {
		AudioManager am = (AudioManager)(mContext.getSystemService(Context.AUDIO_SERVICE));
		return am.getRingerMode();
	}
	
	private void setRingMode(int ringer) {
		AudioManager am = (AudioManager)(mContext.getSystemService(Context.AUDIO_SERVICE));
		am.setRingerMode(ringer);
	}	
	
}
