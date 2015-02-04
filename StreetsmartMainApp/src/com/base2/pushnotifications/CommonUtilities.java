package com.base2.pushnotifications;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {	
	
	// give your server registration url here
   // static final String SERVER_URL = "http://streetsmartshop.in/streetsmartadmin4/shopping/registergcm";

    // Google project id
    public static final String SENDER_ID = "479615194378"; 

    /**
     * Tag used on log messages.
     */	
    
    
    static final String TAG = "AndroidHive GCM";

    public static final String DISPLAY_MESSAGE_ACTION ="com.base2.pushnotifications.DISPLAY_MESSAGE";

    public static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
  public static void displayMessage(Context context, String message,String msg,String msg1) 
    {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
