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
package com.carconnectivity.testapp.views;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carconnectivity.testapp.R;

public class IntInputView  extends LinearLayout{
	TextView tDescription = null;
	EditText tValue = null;
	
	public IntInputView(Context context)
	{
		super(context);
	}
	public IntInputView(Context context, String description, int defaultValue) {
		super(context);
		View.inflate(context, R.layout.view_input_int, this);
		
		tDescription = (TextView) findViewById(R.id.tDescription);
		tValue = (EditText) findViewById(R.id.intValue);
		
		tDescription.setText(description);
		tValue.setText(String.valueOf(defaultValue));
	}
	
	public int getValue()
	{
		int val = 0;
		
		
		String text = tValue.getText().toString();
		
		while(text.startsWith("0") && text.length() >1)
		{
			text = text.substring(1);
		}
		
		try
		{
			val = Integer.decode(text);
		}
		catch(Exception e)
		{
			Toast.makeText(getContext(), "Unable to convert " + tValue.getText().toString() + " to integer.", Toast.LENGTH_SHORT).show();
		}
		
		
		return val;
	}
	public void setValue(int value) {
		tValue.setText(String.valueOf(value));
	}
}



