package com.base2.streetsmart;

import static com.base2.pushnotifications.CommonUtilities.SENDER_ID;
import static com.base2.pushnotifications.CommonUtilities.displayMessage;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.base2.streetsmart.DetailedRegistrationActivity;
import com.base2.pushnotifications.ServerUtilities;
import com.base2.streetsmart.*;
import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";
	 
    public GCMIntentService()
    {
        super(SENDER_ID);
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
      //  Log.i(TAG, "Device registered: regId = " + registrationId);
        displayMessage(context, "Your device registred with GCM","Dhanish","kumar");
       // Log.d("NAME", GMainActivity.name);
        ServerUtilities.register(context,DetailedRegistrationActivity.name,DetailedRegistrationActivity.email, registrationId);
    }

    /**
     * Method called on device un registred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        displayMessage(context, getString(R.string.gcm_unregistered),getString(R.string.dhanish),getString(R.string.kumar));
        ServerUtilities.unregister(context,DetailedRegistrationActivity.name,DetailedRegistrationActivity.email, registrationId);
    }

    /**
     * Method called on Receiving a new message
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        String message = intent.getExtras().getString("price");
        String msg = intent.getExtras().getString("prabhu");
        String msg1 = intent.getExtras().getString("dealid");
        
        displayMessage(context, message,msg,msg1);
        // notifies user
        generateNotification(context, message,msg,msg1);
    }

    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        String msg = getString(R.string.dhanish,total);
        String msg1 = getString(R.string.kumar,total);
        displayMessage(context, message,msg,msg1);
        // notifies user
        generateNotification(context, message,msg,msg1);
    }

    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId),getString(R.string.gcm_error,errorId),getString(R.string.gcm_error,errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId),getString(R.string.gcm_recoverable_error,
                        errorId),getString(R.string.gcm_recoverable_error,
                                errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    
    
    
    
    
    
    
    
    
    private static void generateNotification(Context context, String message,String msg,String msg1) 
    {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);	
       
      
        Notification  notification = new Notification(icon, message+msg+msg1, when);
        String title1 = context.getString(R.string.app_name);
       if((notification.tickerText=message+msg) != null)
       {
    	   Intent notificationIntent = new Intent(context, SuperAdminNotification.class);
           // set intent so it does not start a new activity
           notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
           PendingIntent intent1 = PendingIntent.getActivity(context, 0, notificationIntent, 0);
           
           notification.setLatestEventInfo(context, "New Push", message+msg+msg1, intent1);
           
           notification.flags |= Notification.FLAG_AUTO_CANCEL;
           
           // Play default notification sound
           notification.defaults |= Notification.DEFAULT_SOUND;
           
           //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");
           
           // Vibrate if vibrate is enabled
           notification.defaults |= Notification.DEFAULT_VIBRATE;
           notificationManager.notify(0, notification);    
        
        
        }else
        {
        
        	String BigDealid = msg1;
            
           /* Intent notificationIntent1 = new Intent(context, DetailDealActivity.class);
            // set intent so it does not start a new activity
            notificationIntent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent1, 0);
            
            notification.setLatestEventInfo(context, "New Push", message+msg+msg1, intent);
            
            Bundle bundle = new Bundle();
    		 
    		bundle.putString("DealID", BigDealid);
    		notificationIntent1.putExtras(bundle);
            
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            
            // Play default notification sound
            notification.defaults |= Notification.DEFAULT_SOUND;
            
            //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");
            
            // Vibrate if vibrate is enabled
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notificationManager.notify(0, notification);*/

       
       
        
         
        }
    
    }
    
     

}
