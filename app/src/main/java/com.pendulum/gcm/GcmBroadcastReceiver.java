/*
 *
 *  Proprietary and confidential. Property of Kellton Tech Solutions Ltd. Do not disclose or distribute.
 *  You must have written permission from Kellton Tech Solutions Ltd. to use this code.
 *
 */

package com.pendulum.gcm;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public abstract class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
    }

    /**
     * param context
     *
     * @return the component name. Example //ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
     */

    public abstract ComponentName getComponentName(Context context);

}

	
