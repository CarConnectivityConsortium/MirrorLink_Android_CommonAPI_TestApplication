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


import android.os.Bundle;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.carconnectivity.testapp.BaseActivity;
import com.carconnectivity.testapp.R;

public class KnobScreen extends BaseActivity {
	private final static String LOG_TAG = KnobScreen.class.getCanonicalName();
	ImageView knobZ = null;
	ImageView knobY = null;
	ImageView knobX = null;
	
	Button knobLeft = null;
	Button knobRight = null;
	Button knobUp = null;
	Button knobDown = null;
	Button knobCenter = null;
	

	TextView knobLogs = null;
	 
	boolean isShiftPressed = false;;


	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.knob_screen);
		knobX = (ImageView) findViewById(R.id.knobX);
		knobY = (ImageView) findViewById(R.id.knobY);
		knobZ = (ImageView) findViewById(R.id.knobZ);

		knobLogs = (TextView) findViewById(R.id.knobLogs);
		knobLogs.setMovementMethod(new ScrollingMovementMethod());
		
		knobLeft = (Button) findViewById(R.id.knobLeft);
		knobRight = (Button) findViewById(R.id.knobRight);
		knobUp = (Button) findViewById(R.id.knobUp);
		knobDown = (Button) findViewById(R.id.knobDown);
		knobCenter = (Button) findViewById(R.id.knobCenter);
	}
	


	void rotateKnobZLeft()
	{
		float rotation = knobZ.getRotation();
		knobZ.setRotation((float) (rotation - 10.0));
	}
	
	void rotateKnobZRight()
	{
		float rotation = knobZ.getRotation();
		knobZ.setRotation((float) (rotation + 10.0));
	}
	
	void rotateKnobYLeft()
	{
		float rotation = knobY.getRotation();
		knobY.setRotation((float) (rotation - 10.0));
	}
	
	void rotateKnobYRight()
	{
		float rotation = knobY.getRotation();
		knobY.setRotation((float) (rotation + 10.0));
	}
	
	void rotateKnobXLeft()
	{
		float rotation = knobX.getRotation();
		knobX.setRotation((float) (rotation - 10.0));
	}
	
	void rotateKnobXRight()
	{
		float rotation = knobX.getRotation();
		knobX.setRotation((float) (rotation + 10.0));
	}

	
	void pressKnobRight(boolean pressed)
	{
		knobRight.setPressed(pressed);
	}
	void pressKnobLeft(boolean pressed)
	{
		knobLeft.setPressed(pressed);
	}
	
	void pressKnobUp(boolean pressed)
	{
		knobUp.setPressed(pressed);
	}
	void pressKnobDown(boolean pressed)
	{
		knobDown.setPressed(pressed);
	}
	
	void pressKnobCenter(boolean pressed)
	{
		knobCenter.setPressed(pressed);
	}
	
	
	boolean parseKeyEvent(int keyCode, KeyEvent event, boolean pressed)
	{ 
		switch (keyCode) {
        case 188:  // up-right diagonal
			log("Knob_2D_n_shift_up_right " + pressed);
			pressKnobUp(pressed);
			pressKnobRight(pressed);
			return true;
        case 189: // up-left diagonal
			log("Knob_2D_n_shift_up_left " + pressed);
			pressKnobUp(pressed);
			pressKnobLeft(pressed);
			return true;
        case 190: // down right diagonal
			log("Knob_2D_n_shift_down_right " + pressed);
			pressKnobRight(pressed);
			pressKnobDown(pressed);
			return true;
        case 191: // down left diagonal
			log("Knob_2D_n_shift_down_left " + pressed);
			pressKnobLeft(pressed);
			pressKnobDown(pressed);
			return true;
        case KeyEvent.KEYCODE_DPAD_RIGHT:
        	log("Knob_2D_n_shift_right " + pressed);
        	pressKnobRight(pressed);
        	return true;
        case KeyEvent.KEYCODE_DPAD_LEFT:
        	log("Knob_2D_n_shift_left " + pressed);
        	pressKnobLeft(pressed);	
        	return true;
        case KeyEvent.KEYCODE_DPAD_UP:
        	log("Knob_2D_n_shift_up " + pressed);
        	pressKnobUp(pressed);
        	return true;
        case KeyEvent.KEYCODE_DPAD_DOWN:
        	log("Knob_2D_n_shift_down " + pressed);
        	pressKnobDown(pressed);
        	return true;
        case KeyEvent.KEYCODE_DPAD_CENTER:
        	log("Knob_2D_n_shift_push " + pressed);
        	pressKnobCenter(pressed);
        	return true;
        case KeyEvent.KEYCODE_TAB:
        	if (!isShiftPressed)
        	{
	        	log("Knob_2D_n_rotate_z " + pressed);
	        	if (pressed)
	        		rotateKnobZRight();	
        	}
        	else
        	{
	        	log("Knob_2D_n_rotate_Z " + pressed);
	        	if (pressed)
	        		rotateKnobZLeft();	
        	}
        	return true;
        case KeyEvent.KEYCODE_BUTTON_L2:
        	log("Knob_2D_n_rotate_y " + pressed);
        	if (pressed)
        		rotateKnobYLeft();	
        	return true;
        case KeyEvent.KEYCODE_BUTTON_R2:
        	log("Knob_2D_n_rotate_Y " + pressed);
        	if (pressed)
        		rotateKnobYRight();	
        	return true;
        	
        case KeyEvent.KEYCODE_BUTTON_L1:
        	log("Knob_2D_n_rotate_x " + pressed);
        	if (pressed)
        		rotateKnobXLeft();	
        	return true;
        case KeyEvent.KEYCODE_BUTTON_R1:
        	log("Knob_2D_n_rotate_X " + pressed);
        	if (pressed)
        		rotateKnobXRight();	
        	return true;
        case KeyEvent.KEYCODE_SHIFT_LEFT:
        case KeyEvent.KEYCODE_SHIFT_RIGHT:
        	isShiftPressed = pressed;
        	
        default:
            return super.onKeyUp(keyCode, event);
		}
	}
	void log(String logMessage)
	{
		Log.d(LOG_TAG, logMessage);
		
		knobLogs.append(logMessage+"\n");
		
		Layout layout = knobLogs.getLayout();
        if(layout != null){
            int s = layout.getLineBottom(knobLogs.getLineCount() - 1) - knobLogs.getScrollY() - knobLogs.getHeight();
            if(s > 0)
            	knobLogs.scrollBy(0, s);
        }
	}
	
	
	@Override 
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return parseKeyEvent(keyCode, event, false);
	}
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	   return parseKeyEvent(keyCode, event, true);
	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		
	  if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) 
	  {
		  return parseKeyEvent(KeyEvent.KEYCODE_DPAD_CENTER, event, event.getAction()==KeyEvent.ACTION_DOWN);
	  }
	  else if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
	  {
		  finish();
	  }
          
	  return super.dispatchKeyEvent(event);
	}
	

}
