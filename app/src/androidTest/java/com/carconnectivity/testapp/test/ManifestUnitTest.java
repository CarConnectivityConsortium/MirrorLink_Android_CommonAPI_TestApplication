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
package com.carconnectivity.testapp.test;

import java.util.Arrays;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.test.suitebuilder.annotation.Suppress;

import com.carconnectivity.testapp.MainActivity;


@Suppress
public class ManifestUnitTest extends android.test.ActivityUnitTestCase<MainActivity> {

	private int buttonId;
	private MainActivity activity;

	public ManifestUnitTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Intent intent = new Intent(getInstrumentation().getTargetContext(),
				MainActivity.class);
		startActivity(intent, null, null);
		activity = getActivity();
				
	}
	
	public void testPermissions()  throws Exception 
	{
		PackageInfo pInfo = activity.getPackageManager().getPackageInfo("com.carconnectivity.testapp", PackageManager.GET_PERMISSIONS);
		assertTrue(Arrays.asList(pInfo.requestedPermissions).contains("com.mirrorlink.android.service.ACCESS_PERMISSION"));
	}
	
	public void testIntetns()  throws Exception 
	{
		
		
		//TODO:
		
//		<intent-filter>
//	        <action android:name="com.mirrorlink.android.app.TERMINATE" />
//	        <category android:name="android.intent.category.DEFAULT" />
//	    </intent-filter>
//	    <intent-filter>
//	        <action android:name="com.mirrorlink.android.app.LAUNCH" />
//	        <category android:name="android.intent.category.DEFAULT" />
//	    </intent-filter>
	}

}   