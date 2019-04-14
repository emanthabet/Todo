package com.route.todo;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class TaskAlarmBroadcastReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title=intent.getStringExtra("title");
        String content=intent.getStringExtra("desc");
    ShowNotification(title,content,context);
    }
    public void ShowNotification(String title,String Desc,Context context){
        NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder= new NotificationCompat.Builder(context,"NoteApp")
                .setSmallIcon(R.drawable.ic_alert)
                .setContentText(Desc)
                .setContentTitle(title);
notificationManager.notify(1,builder.build());
}
}
