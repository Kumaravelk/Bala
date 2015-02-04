package com.base2.streetsmart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartUpIntentReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent startServiceIntent = new Intent(context, TestingService.class);
		context.startService(startServiceIntent);
	}

}
