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
import android.widget.TextView;

import com.carconnectivity.testapp.BaseActivity;
import com.carconnectivity.testapp.R;

public class MultimediaKeysScreen extends BaseActivity {
	private final static String LOG_TAG = MultimediaKeysScreen.class.getCanonicalName();

	
	Button playpauseButton = null;
	Button stopButton = null;
	Button prevButton = null;
	Button nextButton = null;
	
	Button rewindButton = null;
	Button forwardButton = null;
	
	Button muteButton = null;
	Button photoButton = null;
	
	Button zoomInButton = null;
	Button zoomOutButton = null;

	TextView multimediaKeysLogs = null;
	
	Button[] fbuttons = new Button[12];


	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.multimedia_screen);

		multimediaKeysLogs = (TextView) findViewById(R.id.multimediaLogs);
		multimediaKeysLogs.setMovementMethod(new ScrollingMovementMethod());
		
		playpauseButton = (Button) findViewById(R.id.playpauseButton);
		stopButton = (Button) findViewById(R.id.stopButton);
		prevButton = (Button) findViewById(R.id.previousButton);
		nextButton = (Button) findViewById(R.id.nextButton);
		
		rewindButton = (Button) findViewById(R.id.rewindButton);
		forwardButton = (Button) findViewById(R.id.fwdButton);
		photoButton  = (Button) findViewById(R.id.photoButton);
		
		zoomInButton = (Button) findViewById(R.id.zoomInButton);
		zoomOutButton = (Button) findViewById(R.id.zoomOutButton);
		
		
		fbuttons[0] = (Button) findViewById(R.id.f1Button);
		fbuttons[1] = (Button) findViewById(R.id.f2Button);
		fbuttons[2] = (Button) findViewById(R.id.f3Button);
		fbuttons[3] = (Button) findViewById(R.id.f4Button);
		fbuttons[4] = (Button) findViewById(R.id.f5Button);
		fbuttons[5] = (Button) findViewById(R.id.f6Button);
		fbuttons[6] = (Button) findViewById(R.id.f7Button);
		fbuttons[7] = (Button) findViewById(R.id.f8Button);
		fbuttons[8] = (Button) findViewById(R.id.f9Button);
		fbuttons[9] = (Button) findViewById(R.id.f10Button);
		fbuttons[10] = (Button) findViewById(R.id.f11Button);
		fbuttons[11] = (Button) findViewById(R.id.f12Button);
		
		muteButton = (Button) findViewById(R.id.muteButton);
	}
	

	void pressPlayPause(boolean pressed)
	{
		playpauseButton.setPressed(pressed);
		
		if (pressed)
		{
			if (playpauseButton.getText() == "Play")
				playpauseButton.setText("Pause");
			else
				playpauseButton.setText("Play");
		}

	}
	void pressMute(boolean pressed)
	{
		muteButton.setPressed(pressed);
		if (pressed)
		{
			if (muteButton.getText() == "Mute")
				muteButton.setText("Unmute");
			else
				muteButton.setText("Mute");
		}
	}
	void pressStop(boolean pressed)
	{
		stopButton.setPressed(pressed);
	}
	void pressPrev(boolean pressed)
	{
		prevButton.setPressed(pressed);
	}
	void pressNext(boolean pressed)
	{
		nextButton.setPressed(pressed);
	}
	
	
	void pressRewind(boolean pressed)
	{
		rewindButton.setPressed(pressed);
	}
	void pressForward(boolean pressed)
	{
		forwardButton.setPressed(pressed);
	}
	void pressPhoto(boolean pressed)
	{
		photoButton.setPressed(pressed);
	}
	
	void pressZoomIn(boolean pressed)
	{
		zoomInButton.setPressed(pressed);
	}
	void pressZoomOut(boolean pressed)
	{
		zoomOutButton.setPressed(pressed);
	}
	
	void pressF(int num, boolean pressed) {
		fbuttons[num- 1].setPressed(pressed);
	}
	
	boolean parseKeyEvent(int keyCode, KeyEvent event, boolean pressed)
	{
		switch (keyCode) {
        case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
        	log("KEYCODE_MEDIA_PLAY_PAUSE " + pressed);
        	pressPlayPause(pressed);
        	return true;
        case KeyEvent.KEYCODE_MEDIA_STOP:
        	log("KEYCODE_MEDIA_STOP " + pressed);
        	pressStop(pressed);	
        	return true;
        case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
        	log("KEYCODE_MEDIA_FAST_FORWARD " + pressed);
        	pressForward(pressed);
        	return true;
        case KeyEvent.KEYCODE_MEDIA_REWIND:
        	log("KEYCODE_MEDIA_REWIND " + pressed);
        	pressRewind(pressed);
        	return true;
        case KeyEvent.KEYCODE_MEDIA_NEXT:
        	log("KEYCODE_MEDIA_NEXT " + pressed);
        	pressNext(pressed);
        	return true;
        case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
        	log("KEYCODE_MEDIA_PREVIOUS " + pressed);
        	pressPrev(pressed);
        	return true;
        case KeyEvent.KEYCODE_VOLUME_MUTE:
        	log("KEYCODE_VOLUME_MUTE " + pressed);
        	pressMute(pressed);
        	return true;
        case KeyEvent.KEYCODE_CAMERA:
        	log("KEYCODE_CAMERA " + pressed);
        	pressPhoto(pressed);
        	return true;
        case KeyEvent.KEYCODE_ZOOM_IN:
	        log("KEYCODE_ZOOM_IN " + pressed);
	        pressZoomIn(pressed);
	        return true;
        case KeyEvent.KEYCODE_ZOOM_OUT:
        	log("KEYCODE_ZOOM_OUT " + pressed);
        	pressZoomOut(pressed);
        	return true;
        	
        case KeyEvent.KEYCODE_F1:
        	log("XK_FUNCTION_KEY_0 " + pressed);
        	pressF(1,pressed);
        	return true;
        case KeyEvent.KEYCODE_F2:
        	log("XK_FUNCTION_KEY_1 " + pressed);
        	pressF(2,pressed);
        	return true;
        case KeyEvent.KEYCODE_F3:
        	log("XK_FUNCTION_KEY_2 " + pressed);
        	pressF(3,pressed);
        	return true;
        case KeyEvent.KEYCODE_F4:
        	log("XK_FUNCTION_KEY_3 " + pressed);
        	pressF(4,pressed);
        	return true;
        case KeyEvent.KEYCODE_F5:
        	log("XK_FUNCTION_KEY_4 " + pressed);
        	pressF(5,pressed);
        	return true;
        case KeyEvent.KEYCODE_F6:
        	log("XK_FUNCTION_KEY_5 " + pressed);
        	pressF(6,pressed);
        	return true;
        case KeyEvent.KEYCODE_F7:
        	log("XK_FUNCTION_KEY_6 " + pressed);
        	pressF(7,pressed);
        	return true;
        case KeyEvent.KEYCODE_F8:
        	log("XK_FUNCTION_KEY_7 " + pressed);
        	pressF(8,pressed);
        	return true;
        case KeyEvent.KEYCODE_F9:
        	log("XK_FUNCTION_KEY_8 " + pressed);
        	pressF(9,pressed);
        	return true;
        case KeyEvent.KEYCODE_F10:
        	log("XK_FUNCTION_KEY_9 " + pressed);
        	pressF(10,pressed);
        	return true;
        case KeyEvent.KEYCODE_F11:
        	log("XK_FUNCTION_KEY_10 " + pressed);
        	pressF(11,pressed);
        	return true;
        case KeyEvent.KEYCODE_F12:
        	log("XK_FUNCTION_KEY_11 " + pressed);
        	pressF(12,pressed);
        	return true;
       	
        default:
            return super.onKeyUp(keyCode, event);
		}
	}
	void log(String logMessage)
	{
		Log.d(LOG_TAG, logMessage);
		
		multimediaKeysLogs.append(logMessage+"\n");
		
		Layout layout = multimediaKeysLogs.getLayout();
        if(layout != null){
            int s = layout.getLineBottom(multimediaKeysLogs.getLineCount() - 1) - multimediaKeysLogs.getScrollY() - multimediaKeysLogs.getHeight();
            if(s > 0)
            	multimediaKeysLogs.scrollBy(0, s);
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
	  if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
	  {
		  finish();
	  }
	  return super.dispatchKeyEvent(event);
	}
	

}
