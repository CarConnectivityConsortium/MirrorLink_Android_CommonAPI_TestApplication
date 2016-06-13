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
 */package com.carconnectivity.testapp.test.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import android.os.Bundle;
import android.util.Log;

public class RemoteController{
	String LOG_TAG = RemoteController.class.getCanonicalName();
	Socket socket = null;
	OutputStream writer = null; 
	public boolean open()
	{
		Log.d(LOG_TAG,"open()");
		try {
			socket = new Socket("127.0.0.1", 65000);
			if (socket.isConnected())
			{				
				writer = socket.getOutputStream();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}
		return true;
	}

	
	public boolean send(String memberName, String methodName, String args)
	{
		if (writer != null)
		{
			String response ="{\"memberName\":\""+memberName+"\",\"methodName\": \""+methodName+"\", \"arguments\": "+args+"}\n";
			
			Log.v(LOG_TAG, "Send: " + response);
			try {
				
     			writer.write(response.getBytes());
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean send(String memberName, String methodName, List<Object> args)
	{
		String argsString = serializeData(args);
		return send(memberName, methodName, argsString);
	}
	
	String serializeBundle(Bundle b, String bKey)
	{
	    String argsString = "{";
	    argsString += "\"key\":\"" + bKey + "\",";
	    argsString += "\"type\": \"Bundle\",";
	    argsString += "\"value\": [";
	    
	    
	    String[] keys = b.keySet().toArray(new String[b.keySet().size()]);

	    for (String key: keys )
	    {
	        String type = b.get(key).getClass().getSimpleName();
	        if (type.contains("Bundle"))
	        {
	            argsString += serializeBundle(b.getBundle(key), key);
	        }
	        else
	        {
	            argsString += "{";
	            argsString += "\"key\":\"" + key + "\",";
	            argsString += "\"type\":\"" + type + "\",";
	            argsString += "\"value\":\"" + b.get(key).toString() + "\"";
	            argsString += "}";
	        }

	        if (key != keys[keys.length-1])
	             argsString += ",";
	    }
	    argsString += "]}";

	    return argsString;
	}
	String serializeData(List<Object> args)
	{
		String s = "[";
		
	    for(int i=0; i<args.size();i++)
	    {
	        String type = args.get(i).getClass().getSimpleName();

	        if (type.contains("Bundle"))
	        {
	            s += serializeBundle((Bundle) args.get(i), "");
	        }
	        else
	        {
	            s += "{";
	            s += "\"type\":\"" + type + "\",";
	            s += "\"value\":\"" + args.get(i).toString() + "\"";
	            s += "}";
	        }

	        if (i < args.size()-1)
	            s +=",";
	    }
	    s += "]";
		return s;		
	}
	
	

	public void close()
	{
		Log.d(LOG_TAG,"close()");
		try {
			if (writer !=null)
				writer.close();
			if (socket !=null)
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		writer = null;
	}
}
