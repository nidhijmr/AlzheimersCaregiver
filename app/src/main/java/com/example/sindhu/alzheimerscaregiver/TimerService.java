package com.example.sindhu.alzheimerscaregiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;


public class TimerService extends Service {
    // Stores list of timers and names for the timers
    private ArrayList<CountDownTimer> timers = new ArrayList<>();
    private ArrayList<TaskModal> tasks = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            // Fetch length and name from intent
            final TaskModal task = intent.getParcelableExtra("task");

            if (intent.getBooleanExtra("isCreate", true)) {
                // Add to the ArrayLists
                tasks.add(task);
                timers.add(new CountDownTimer(task.getTimeInMilliseconds(), 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        Intent intent = new Intent(TimerService.this, DailyActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent pendingIntent = PendingIntent.getActivity(TimerService.this, (int) task.getId(), intent,
                                PendingIntent.FLAG_ONE_SHOT);

                        // Send notification on finish
                        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(TimerService.this)
                                .setSmallIcon(R.mipmap.goodshows_logo_white)
                                .setAutoCancel(true)
                                .setContentTitle(tasks.get(timers.indexOf(this)).getName())
                                .setContentText(tasks.get(timers.indexOf(this)).getName()
                                        + " should be done. Don't forget to check on it.")
                                .setLights(Color.BLUE, 2000, 2000)
                                .setVibrate(new long[]{250, 250, 250, 250})
                                .setSound(defaultSoundUri)
                                .setContentIntent(pendingIntent);

                        NotificationManager notificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        notificationManager.notify((int) task.getId(), notificationBuilder.build());
                    }
                }.start());
            } else {
                for (int i = 0; i < tasks.size(); i++) {
                    if (task.getId() == tasks.get(i).getId()) {
                        timers.get(i).cancel();
                        timers.remove(i);
                        tasks.remove(i);
                        break;
                    }
                }
            }
        } else {
            Log.d("service", "intent was null lol");
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
