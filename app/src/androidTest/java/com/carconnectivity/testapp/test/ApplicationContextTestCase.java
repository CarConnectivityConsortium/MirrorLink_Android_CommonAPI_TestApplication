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
 */package com.carconnectivity.testapp.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.test.ActivityUnitTestCase;

import com.carconnectivity.testapp.MainActivity;
import com.carconnectivity.testapp.MirrorLinkApplicationContext;
import com.mirrorlink.android.commonapi.ICertificationManager;
import com.mirrorlink.android.commonapi.IConnectionListener;
import com.mirrorlink.android.commonapi.IConnectionManager;
import com.mirrorlink.android.commonapi.IContextManager;
import com.mirrorlink.android.commonapi.IDataServicesManager;
import com.mirrorlink.android.commonapi.IDeviceInfoManager;
import com.mirrorlink.android.commonapi.IDeviceStatusManager;
import com.mirrorlink.android.commonapi.IDisplayManager;
import com.mirrorlink.android.commonapi.IEventMappingManager;
import com.mirrorlink.android.commonapi.INotificationManager;



public class ApplicationContextTestCase extends ActivityUnitTestCase<MainActivity> {
	MainActivity mActivity = null;
	
	public ApplicationContextTestCase() {
		super(MainActivity.class);
	}
	@Override
	public void setUp() throws Exception {
		super.setUp();
        startActivity(new Intent(getInstrumentation().getTargetContext(), MainActivity.class), null, null);
		mActivity = getActivity();
	
	}



	public void testNotificationManagerReference() {
		MirrorLinkApplicationContext context = (MirrorLinkApplicationContext) mActivity.getApplicationContext();
		
		INotificationManager  manager = context.getNotificationManager();
		assertNull(manager);
		
		
		context.registerNotificationManager(this, null);
		manager = context.getNotificationManager();
		assertNotNull(manager);
		
		context.unregisterNotificationManager(this, null);
		manager = context.getNotificationManager();
		assertNull(manager);
	}
	
	
	public void testDataServicesManagerManagerReference() {
		MirrorLinkApplicationContext context = (MirrorLinkApplicationContext) mActivity.getApplicationContext();
		
		IDataServicesManager  manager = context.getDataServicesManager();
		assertNull(manager);
		
	
		context.registerDataServicesManager(this, null);
		manager = context.getDataServicesManager();
		assertNotNull(manager);
		
		context.unregisterDataServicesManager(this, null);
		manager = context.getDataServicesManager();
		assertNull(manager);
	}
	
	
	public void testDeviceStatusManagerReference() {
		MirrorLinkApplicationContext context = (MirrorLinkApplicationContext) mActivity.getApplicationContext();
		
		IDeviceStatusManager  manager = context.getDeviceStatusManager();
		assertNull(manager);
		
		
		context.registerDeviceStatusManager(this, null);
		manager = context.getDeviceStatusManager();
		assertNotNull(manager);
		
		context.unregisterDeviceStatusManager(this, null);
		manager = context.getDeviceStatusManager();
		assertNull(manager);
	}
	
	
	public void testContextManagerReference() {
		MirrorLinkApplicationContext context = (MirrorLinkApplicationContext) mActivity.getApplicationContext();
		
		IContextManager  manager = context.getContextManager ();
		assertNull(manager);
		
		
		context.registerContextManager(this, null);
		manager = context.getContextManager ();
		assertNotNull(manager);
		
		context.unregisterContextManager(this, null);
		manager = context.getContextManager ();
		assertNull(manager);
	}
	
	
	
	public void testEventMappingManagerReference() {
		MirrorLinkApplicationContext context = (MirrorLinkApplicationContext) mActivity.getApplicationContext();
		
		IEventMappingManager manager = context.getEventMappingManager();
		assertNull(manager);
		
		
		context.registerEventMappingManager(this, null);
		manager = context.getEventMappingManager();
		assertNotNull(manager);
		
		context.unregisterEventMappingManager(this, null);
		manager = context.getEventMappingManager();
		assertNull(manager);
	}
	
	
	
	public void testDisplayManagerReference() {
		MirrorLinkApplicationContext context = (MirrorLinkApplicationContext) mActivity.getApplicationContext();
		
		IDisplayManager manager = context.getDisplayManager();
		assertNull(manager);
		
		
		context.registerDisplayManager(this, null);
		manager = context.getDisplayManager();
		assertNotNull(manager);
		
		context.unregisterDisplayManager(this, null);
		manager = context.getDisplayManager();
		assertNull(manager);
	}
	
	
	
	public void testCertificationManagerReference() {
		MirrorLinkApplicationContext context = (MirrorLinkApplicationContext) mActivity.getApplicationContext();
		
		ICertificationManager manager = context.getCertificationManager();
		assertNull(manager);
		
		
		context.registerCertificationManager(this, null);
		manager = context.getCertificationManager();
		assertNotNull(manager);
		
		context.unregisterCertificationManager(this, null);
		manager = context.getCertificationManager();
		assertNull(manager);
	}
	public void testDeviceInfoManagerReference() {
		MirrorLinkApplicationContext context = (MirrorLinkApplicationContext) mActivity.getApplicationContext();
		
		IDeviceInfoManager manager = context.getDeviceInfoManager();
		assertNull(manager);
		
		
		context.registerDeviceInfoManager(this, null);
		manager = context.getDeviceInfoManager();
		assertNotNull(manager);
		
		context.unregisterDeviceInfoManager(this, null);
		manager = context.getDeviceInfoManager();
		assertNull(manager);
	}
	
	
	
	
	public void testConnectionManagerReference() {
		MirrorLinkApplicationContext context = (MirrorLinkApplicationContext) mActivity.getApplicationContext();
		
		IConnectionManager manager = context.getConnectionManager();
		assertNull(manager);
		
		
		context.registerConnectionManager(this, null);
		manager = context.getConnectionManager();
		assertNotNull(manager);
		
		context.unregisterConnectionManager(this, null);
		manager = context.getConnectionManager();
		assertNull(manager);
	}

	public void testTwoReference() {
		Object o = new Object();
		
		MirrorLinkApplicationContext context = (MirrorLinkApplicationContext) mActivity.getApplicationContext();
		
		IConnectionManager manager = context.getConnectionManager();
		
		assertNull(manager);
		
		
		context.registerConnectionManager(this, null);
		manager = context.getConnectionManager();
		assertNotNull(manager);
		
		context.registerConnectionManager(o, null);
		manager = context.getConnectionManager();
		assertNotNull(manager);
		
		context.unregisterConnectionManager(this, null);
		manager = context.getConnectionManager();
		assertNotNull(manager);
		
		
		context.unregisterConnectionManager(o, null);
		manager = context.getConnectionManager();
		assertNull(manager);
		
		
		assertNotNull(mActivity);
	}
	
	public void testTwoReferenceWithListener() {
		Object o = new Object();
		IConnectionListener l = new IConnectionListener.Stub() {
			
			@Override
			public void onRemoteDisplayConnectionChanged(int remoteDisplayConnection) throws RemoteException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMirrorLinkSessionChanged(boolean mirrolinkSessionIsEstablished) throws RemoteException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAudioConnectionsChanged(Bundle audioConnections) throws RemoteException {
				// TODO Auto-generated method stub
				
			}
		};
		
		MirrorLinkApplicationContext context = (MirrorLinkApplicationContext) mActivity.getApplicationContext();
		
		IConnectionManager manager = context.getConnectionManager();
		
		assertNull(manager);
		
		
		context.registerConnectionManager(this, null);
		manager = context.getConnectionManager();
		assertNotNull(manager);
		
		context.registerConnectionManager(o, l);
		manager = context.getConnectionManager();
		assertNotNull(manager);
		
		context.unregisterConnectionManager(this, null);
		manager = context.getConnectionManager();
		assertNotNull(manager);
		

		
		
		context.unregisterConnectionManager(o, l);
		manager = context.getConnectionManager();
		assertNull(manager);
		
		
		assertNotNull(mActivity);
	}
} 