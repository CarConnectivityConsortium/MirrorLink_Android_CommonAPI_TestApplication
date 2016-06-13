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
 */package com.carconnectivity.testapp.test.base;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.Suppress;
import android.util.Log;

import com.carconnectivity.testapp.MainActivity;
import com.carconnectivity.testapp.test.utils.RemoteController;

@Suppress
public class BaseFunctionalTests extends ActivityInstrumentationTestCase2<MainActivity> {
	protected RemoteController rc = new RemoteController();
	protected MainActivity mActivity = null;
	protected Class testedActivity = null;
	
	protected Activity activity = null;
	protected Map<String,Field> fields = null;
	public BaseFunctionalTests() {
		super(MainActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		getInstrumentation().waitForIdleSync();
		activity = startActivity();
		getInstrumentation().waitForIdleSync();
		rc.open();
		
		Log.v("BaseFunctionalTests", "setUp");
		getInstrumentation().waitForIdleSync();
		
		fields = new HashMap<String, Field>();
		for(Field f: testedActivity.getDeclaredFields())
		{
			f.setAccessible(true);
			fields.put(f.getName(), f);
		}
	}

	public void testActivity() 
	{
		getInstrumentation().waitForIdleSync();
		assertNotNull(activity);
		assertFalse(activity.isFinishing());
		getInstrumentation().waitForIdleSync();
	}
	
	@Override
	public void tearDown() throws Exception {
		if (activity != null)
		{
			activity.finish();
		}
		if (mActivity != null)
		{
			mActivity.finish();
		}
		
		rc.close();
		Log.v("BaseFunctionalTests", "tearDown");
		getInstrumentation().waitForIdleSync();
		super.tearDown();

	}
	
	protected Activity startActivity(){
		ActivityMonitor monitor = getInstrumentation().addMonitor(testedActivity.getName(), null, false);
		
		Intent i = new Intent(mActivity.getApplicationContext(), testedActivity);

		mActivity.startActivity(i);
		Activity a = monitor.waitForActivityWithTimeout(5000);
		getInstrumentation().removeMonitor(monitor);
		return a;	
	}

} 