package me.wcy.music.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import me.wcy.music.activity.AlarmAlert;

public class CallAlarm extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {

        String ring=intent.getStringExtra("RING");
       // Toast.makeText(context,"callalarm   "+ring,Toast.LENGTH_LONG).show();

        Intent i = new Intent(context, AlarmAlert.class);
        Bundle bundleRet = new Bundle();
        bundleRet.putString("STR_CALLER", "");
        i.putExtras(bundleRet);
        i.putExtra("RING",ring);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.d("testring",ring);
        context.startActivity(i);
    }
}