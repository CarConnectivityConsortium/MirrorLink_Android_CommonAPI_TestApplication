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

import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.carconnectivity.testapp.R;
import com.carconnectivity.testapp.test.base.BaseFunctionalTests;
import com.carconnectivity.testapp.utils.KnobScreen;


public class KnobVisualiserTests extends BaseFunctionalTests {
	ImageView knobZ = null;
	ImageView knobY = null;
	ImageView knobX = null;
	
	Button knobLeft = null;
	Button knobRight = null;
	Button knobUp = null;
	Button knobDown = null;
	Button knobCenter = null;
	
	TextView knobLogs = null;
	
	@Override
	public void setUp() throws Exception {
		testedActivity = KnobScreen.class;
		super.setUp();
		
		knobX = (ImageView) activity.findViewById(R.id.knobX);
		knobY = (ImageView) activity.findViewById(R.id.knobY);
		knobZ = (ImageView) activity.findViewById(R.id.knobZ);

		knobLogs = (TextView) activity.findViewById(R.id.knobLogs);

		knobLeft = (Button) activity.findViewById(R.id.knobLeft);
		knobRight = (Button) activity.findViewById(R.id.knobRight);
		knobUp = (Button) activity.findViewById(R.id.knobUp);
		knobDown = (Button) activity.findViewById(R.id.knobDown);
		knobCenter = (Button) activity.findViewById(R.id.knobCenter);

	}

	public void testPrecondition()
	{
		assertNotNull(knobX);
		assertNotNull(knobY);
		assertNotNull(knobZ);

		assertNotNull(knobLogs );

		assertNotNull(knobLeft);
		assertNotNull(knobRight);
		assertNotNull(knobUp);
		assertNotNull(knobDown);
		assertNotNull(knobCenter);
	}
	

	public void testKnob() throws Exception 
	{
	
		
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT));
		assertTrue(knobRight.isPressed());
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_RIGHT));
		assertFalse(knobRight.isPressed());
		
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT));
		assertTrue(knobLeft.isPressed());
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_LEFT));
		assertFalse(knobLeft.isPressed());
		
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_DOWN));
		assertTrue(knobDown.isPressed());
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_DOWN));
		assertFalse(knobDown.isPressed());
		
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_UP));
		assertTrue(knobUp.isPressed());
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_UP));
		assertFalse(knobUp.isPressed());
		
		
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_CENTER));
		assertTrue(knobCenter.isPressed());
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_CENTER));
		assertFalse(knobCenter.isPressed());


			
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BUTTON_THUMBR));
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_THUMBR));
		assertEquals(10, (int) knobZ.getRotation());
		
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BUTTON_THUMBR));
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_THUMBR));
		assertEquals(20, (int) knobZ.getRotation());
		
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BUTTON_THUMBR));
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_THUMBR));
		assertEquals(30, (int) knobZ.getRotation());
		
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BUTTON_THUMBL));
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_THUMBL));
		assertEquals(20, (int) knobZ.getRotation());
		
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BUTTON_THUMBL));
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_THUMBL));
		assertEquals(10, (int) knobZ.getRotation());
		
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BUTTON_THUMBL));
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_THUMBL));
		assertEquals(0, (int) knobZ.getRotation());

		

		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BUTTON_R1));
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_R1));
		assertEquals(10, (int) knobX.getRotation());
		
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BUTTON_R1));
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_R1));
		assertEquals(20, (int) knobX.getRotation());
		
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BUTTON_R1));
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_R1));
		assertEquals(30, (int) knobX.getRotation());
		
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BUTTON_L1));
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_L1));
		assertEquals(20, (int) knobX.getRotation());
		
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BUTTON_L1));
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_L1));
		assertEquals(10, (int) knobX.getRotation());
		
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BUTTON_L1));
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_L1));
		assertEquals(0, (int) knobX.getRotation());


		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BUTTON_R2));
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_R2));
		assertEquals(10, (int) knobY.getRotation());
		
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BUTTON_R2));
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_R2));
		assertEquals(20, (int) knobY.getRotation());
		
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BUTTON_R2));
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_R2));
		assertEquals(30, (int) knobY.getRotation());
		
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BUTTON_L2));
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_L2));
		assertEquals(20, (int) knobY.getRotation());
		
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BUTTON_L2));
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_L2));
		assertEquals(10, (int) knobY.getRotation());
		
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BUTTON_L2));
		getInstrumentation().sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BUTTON_L2));
		assertEquals(0, (int) knobY.getRotation());
	
	}
} 