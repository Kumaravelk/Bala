package com.base2.pushnotifications;

//import static com.base2.pushnotifications.CommonUtilities.SERVER_URL;
import static com.base2.pushnotifications.CommonUtilities.TAG;
import static com.base2.pushnotifications.CommonUtilities.displayMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.base2.streetsmart.*;
import com.google.android.gcm.GCMRegistrar;


public final class ServerUtilities {
	public static final int MAX_ATTEMPTS = 5;
    public static final int BACKOFF_MILLI_SECONDS = 2000;
    public static final Random random = new Random();
    
    
    public static final String TAG1 = "GCM register";
    /**
     * Register this account/device pair within the server.
     *
     */
    public static void register(final Context context, String name, String email, final String gcmid) {
       // Log.i(TAG, "registering device (regId = " + regId + ")");
      //  String serverUrl = SERVER_URL;
       // Map<String, String> params = new HashMap<String, String>();
       // params.put("regId", regId);
        //params.put("name", name);
       // params.put("email", email);
        
        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        // Once GCM returns a registration id, we need to register on our server
        // As the server might be down, we will retry it a couple
        // times.
        for (int i = 1; i <= MAX_ATTEMPTS; i++) 
        {
           // Log.d(TAG, "Attempt #" + i + " to register");
            try {
                displayMessage(context, context.getString(
                        R.string.server_registering, i, MAX_ATTEMPTS),context.getString(
                                R.string.server_registering, i, MAX_ATTEMPTS),context.getString(
                                        R.string.server_registering, i, MAX_ATTEMPTS));
                postData(name,email,gcmid);
                GCMRegistrar.setRegisteredOnServer(context, true);
                String message = context.getString(R.string.server_registered);
                String msg = context.getString(R.string.server_registered);
                String msg1 = context.getString(R.string.server_registered);
                CommonUtilities.displayMessage(context, message,msg,msg1);
                return;
            } catch (JSONException e) {
                // Here we are simplifying and retrying on any error; in a real
                // application, it should retry only on unrecoverable errors
                // (like HTTP error code 503).
             //   Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                  //  Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    // Activity finished before we complete - exit.
                  //  Log.d(TAG, "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();
                    return;
                }
                // increase backoff exponentially
                backoff *= 2;
            }
        }
        String message = context.getString(R.string.server_register_error,
                MAX_ATTEMPTS);
        String msg = context.getString(R.string.server_register_error,
                MAX_ATTEMPTS);
        String msg1 = context.getString(R.string.server_register_error,
                MAX_ATTEMPTS);
        CommonUtilities.displayMessage(context, message,msg,msg1);
    }

    /**
     * Unregister this account/device pair within the server.
     */
    public static void unregister(final Context context,String name,String email,final String gcmid) 
    {
      //  Log.i(TAG, "unregistering device (regId = " + regId + ")");
      //  String serverUrl = SERVER_URL + "/unregister";
       // Map<String, String> params = new HashMap<String, String>();
       // params.put("regId", gcmid);
        try {
            postData(name,email, gcmid);
            GCMRegistrar.setRegisteredOnServer(context, false);
            String message = context.getString(R.string.server_unregistered);
            String msg = context.getString(R.string.server_unregistered);
            String msg1 = context.getString(R.string.server_unregistered);
            CommonUtilities.displayMessage(context, message,msg,msg1);
        } catch (JSONException e) {
            // At this point the device is unregistered from GCM, but still
            // registered in the server.
            // We could try to unregister again, but it is not necessary:
            // if the server tries to send a message to the device, it will get
            // a "NotRegistered" error message and should unregister the device.
            String message = context.getString(R.string.server_unregister_error,
                    e.getMessage());
            String msg = context.getString(R.string.server_unregister_error,
                    e.getMessage());
            String msg1 = context.getString(R.string.server_unregister_error,
                    e.getMessage());
            CommonUtilities.displayMessage(context, message,msg,msg1);
        }
    }

    /**
     * Issue a POST request to the server.
     *
     * @param endpoint POST address.
     * @param params request parameters.
     *
     * @throws IOException propagated from POST.
     */
   /* private static void post(String endpoint, Map<String, String> params)
            throws IOException {   	
        
        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        Log.v(TAG, "Posting '" + body + "' to " + url);
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        try {
        	//Log.e("URL", "> " + url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();
            if (status != 200) {
              throw new IOException("Post failed with error code " + status);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
      }*/
    
    public static void postData(String name,String email,String gcmid) throws JSONException {

		 

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://54.169.81.215/streetsmartadmin4/shopping/registergcm");
         
		try {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

			nameValuePairs.add(new BasicNameValuePair("email",email));
			nameValuePairs.add(new BasicNameValuePair("name", name));
			nameValuePairs.add(new BasicNameValuePair("gcmid",gcmid));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			String str = EntityUtils.toString(response.getEntity());

			JSONObject jsonobj = new JSONObject(str);

			JSONObject objjson = jsonobj.getJSONObject("result");

			String error1 = objjson.getString("error");
			String message1 = objjson.getString("message");
			
			
			 
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

	}
}
