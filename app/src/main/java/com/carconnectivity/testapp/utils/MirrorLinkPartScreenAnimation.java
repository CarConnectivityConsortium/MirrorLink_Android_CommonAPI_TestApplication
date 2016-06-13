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


import java.util.Random;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carconnectivity.testapp.R;

public class MirrorLinkPartScreenAnimation extends Activity implements OnClickListener{
	RelativeLayout mLayout = null; 
	ImageView mPartialBackgroundImage = null;
	
	int mCurrentMode = -1;
	
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.part_screen_mode);
		
		mLayout = (RelativeLayout) findViewById(R.id.partScreenTextLayout);
		mLayout.setOnClickListener(this);
		
		
		mPartialBackgroundImage = (ImageView) findViewById(R.id.partialBackgroundImage);
		
		registerForContextMenu(mLayout);
		
		
		if( savedInstanceState != null)
		{
			int previousBackgroundId = savedInstanceState.getInt("PREVIOUS_BACKGROUND");
			setBackground(previousBackgroundId);
		}
			
		
	}
	
	@Override        
    protected void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putInt("PREVIOUS_BACKGROUND", mCurrentMode);

		super.onSaveInstanceState(savedInstanceState);      
    }
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,  ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.partial_screen_update_menu, menu);
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		mPartialBackgroundImage.setImageDrawable(null);
		setBackground( item.getItemId());
		return false;
 
	}
	
	void setBackground(int id)
	{
		mCurrentMode = id;
		switch (id) {
		case R.id.black_red:
			mLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_black_red));
			break;	
		case R.id.white_red:
			mLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_white_red));
			break;	
		case R.id.black_blue:
			mLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_black_blue));
			break;	
		case R.id.white_blue:
			mLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_white_blue));
			break;		
		case R.id.black_green:
			mLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_black_green));
			break;	
		case R.id.white_green:
			mLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_white_green));
			break;	
		case R.id.test_screen:
			mLayout.setBackgroundColor(Color.WHITE);
			mPartialBackgroundImage.setImageDrawable(getResources().getDrawable(R.drawable.scaling_test_16_9));
			break;
		default:
			break;
		}
	}
	
	
	@Override
	protected void onStart()
	{
		super.onStart();
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true)
				{
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							TextView number = (TextView) findViewById(R.id.fullScreenText);
							int val = Integer.parseInt(number.getText().toString());
							val++;
							number.setText(String.valueOf(val));
							
							Random rnd = new Random(); 
							int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));   
							number.setBackgroundColor(color);
						}
					});
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
	}
	@Override
	public void onClick(View v) {
		openContextMenu(v);
		
	}
	
}
