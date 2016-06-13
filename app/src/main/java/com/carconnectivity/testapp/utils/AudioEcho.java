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


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.carconnectivity.testapp.BaseActivity;
import com.carconnectivity.testapp.R;

public class AudioEcho extends BaseActivity {
	private final static String LOG_TAG = AudioEcho.class.getCanonicalName();

	Button stop = null;
	Button start = null;
	
	MediaRecorder recorder = new MediaRecorder();
	
	File bufferA = null;
	FileOutputStream fosA = null;
	FileInputStream fisA = null;
	
	File bufferB = null;
	FileOutputStream fosB = null;
	FileInputStream fisB = null;
	
	
	boolean usingA = true;
	boolean running = true;
	
	MediaPlayer mMediaPlayer = null;

	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.echo_screen);
		
		
		File outputDir = this.getCacheDir(); 
		
		try {
			bufferA = File.createTempFile("bufferA", "aac", outputDir);
			bufferB = File.createTempFile("bufferB", "aac", outputDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		mMediaPlayer = new MediaPlayer();

		
		start = (Button) findViewById(R.id.startButton);
		start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				running =true;
				try {
					fosA = new FileOutputStream(bufferA);
					fisA = new FileInputStream(bufferA);
					
					fosB = new FileOutputStream(bufferB);
					fisB = new FileInputStream(bufferB);
					
					
					
					prepareRecorder(true);
	
					Thread startThread = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								Log.v(LOG_TAG, "Starting echo");
								
								runOnUiThread(new Runnable() {
									public void run() {
										start.setEnabled(false);
										stop.setEnabled(true);
									}
								});
								
								
								Thread.sleep(5000);
								while(running)
								{
									changeBuffers();
									for (int i=0;i<10;i++)
									{
										if (!running)
										{
											break;
										}
										Thread.sleep(500);
									}
									
									
								}
								Log.v(LOG_TAG, "Echo stopped. Cleaning resources");
					
								
								runOnUiThread(new Runnable() {
									public void run() {
										start.setEnabled(true);
										stop.setEnabled(false);
									}
								});
		
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					startThread.start();
					
					 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
		stop = (Button) findViewById(R.id.stopButton);
		stop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				stop.setEnabled(false);
				running = false;
				
				recorder.stop();
				recorder.reset();  
				mMediaPlayer.stop();
				mMediaPlayer.reset();
			}
		});
		stop.setEnabled(false);
	}
	
	void prepareRecorder(boolean useA) throws IllegalStateException, IOException
	{
		Log.v(LOG_TAG, "prepareRecorders" + useA);
		File outputDir = this.getCacheDir(); 
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
		
		if (useA)
		{	
			bufferA = File.createTempFile("bufferA", "aac", outputDir);
			fosA = new FileOutputStream(bufferA);
			fisA = new FileInputStream(bufferA);
			
		
			recorder.setOutputFile(fosA.getFD());
		}	
		else
		{
			bufferB = File.createTempFile("bufferB", "aac", outputDir);
			fosB = new FileOutputStream(bufferB);
			fisB = new FileInputStream(bufferB);
		
			recorder.setOutputFile(fosB.getFD());
		}
		recorder.prepare();
		recorder.start();  
		
		usingA =useA;
	}
	
	
	
	void changeBuffers() throws IllegalArgumentException, IllegalStateException, IOException
	{
		recorder.stop();
		recorder.reset();
		
		mMediaPlayer.stop();
		mMediaPlayer.reset();
		if (usingA)
			mMediaPlayer.setDataSource(fisA.getFD());
		else
			mMediaPlayer.setDataSource(fisB.getFD());
		prepareRecorder(!usingA);
		
		mMediaPlayer.prepare();
		mMediaPlayer.start();
	}
	
	
	@Override
	public void onDestroy()
	{
		recorder.release();
		mMediaPlayer.release();
		super.onDestroy();
	}
		

}
