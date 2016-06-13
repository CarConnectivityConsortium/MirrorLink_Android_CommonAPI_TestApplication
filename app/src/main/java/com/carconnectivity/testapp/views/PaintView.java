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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;


public class PaintView extends View{
	Paint paint = new Paint();
	
	int startX;
	int x;
	int startY;
	int y;
	
	
	public PaintView(Context context, int col)
	{
		super(context);
		paint.setColor(col);
	}
	public PaintView(Context context)
	{
		super(context);
		paint.setColor(Color.CYAN);
	}
	
	@Override
	public void onDraw(Canvas canvas) {
	    paint.setAlpha(80);
	    canvas.drawRect(startX, startY, x, y, paint);
	}
	
	
	public void setDimmension(int x, int y, int startX, int startY)
	{
		this.x = x + startX;
		this.y = y + startY;
		this.startX = startX;
		this.startY = startY;
		
		invalidate();
		
	}
}



