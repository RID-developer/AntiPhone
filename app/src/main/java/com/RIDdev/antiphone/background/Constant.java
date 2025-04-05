package com.RIDdev.antiphone.background;

import static java.security.AccessController.getContext;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.RIDdev.antiphone.Database.DBOperation;
import com.RIDdev.antiphone.PackSet;

import java.util.List;

public class Constant extends Service {
    public static DBOperation Opr;
    public static PackSet Pack = new PackSet();
    public static int total = 0;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
public void Initiate() {
        Thread Everlasting = new Thread(new Runnable() {
            @Override
            public void run() {
                forever();
            }
        });
        Everlasting.start();
    }
    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Opr = new DBOperation(getApplicationContext());
        Opr.fill();
        Opr.Remake();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "AntiPhone Channel";
            String description = "Channel for AntiPhone foreground service";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("default", name, importance);
            channel.setDescription(description);


            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        Notification notification = new NotificationCompat.Builder(this, "default")
                .setContentTitle("AntiPhone: Active")
                .setSmallIcon(android.R.drawable.stat_notify_sync)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC);
        } else {
            startForeground(1, notification);
        }

        Initiate();
        return START_STICKY;
    }
    public void forever() {
        while (true) {
            long begining = System.currentTimeMillis();
            String check = Check();
            Restart();
            if ((check != null) && !check.equals(Pack.PackName)) {
                Opr.Insert(Pack);
                Pack.Reset();
                Pack.PackName = Check();
                if (Pack.PackName.equals("com.RIDdev.antiphone"))
                    Pack.blacklist = false;
                Opr.Retrieve(Pack);
            }
            Pack.time += 5;
            serve(Pack.time);
            total += 5;
            if(Pack.time > Pack.lim*60)
                Send();
            Opr.Insert(Pack);
            long end = System.currentTimeMillis() - begining;
            pause(safety((5000 - end)));
        }
    }

    public void serve(int x) {
        OkHttpClient client = new OkHttpClient();
        String number = String.valueOf(x);
        RequestBody body = RequestBody.create(number, MediaType.parse("text/plain"));
        Request request = new Request.Builder()
                .url("http://192.168.0.237/24:3000/receive")
                .post(body)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        System.out.println("great :D");
                    } else {
                        System.out.println("AAAA " + response.message());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void Send() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "default";
            CharSequence channelName = "Default Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Important Notification")
                .setContentText(Pack.Message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
    private void Restart() {
        Calendar calendar = Calendar.getInstance();
        int Day = calendar.get(Calendar.DAY_OF_YEAR);

        if (Pack.day != Day) {
            Pack.time = 0;
            Pack.day = Day;
            Opr.Insert(Pack);
        }
    }
        public long safety(long x)
        {
            if(x<1)
                return 1;
            else
                return x;
        }
        public void pause(long time)
        {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        public String Check()
        {
            UsageStatsManager usm = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> stats = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 60 * 5, time);
            UsageStats target = null;
            time = Long.MIN_VALUE;
            for (UsageStats usageStats : stats) {
                if (usageStats.getLastTimeUsed() > time) {
                    time = usageStats.getLastTimeUsed();
                    target = usageStats;
                }
            }
            if (target != null) {
                return target.getPackageName();
            }
            else
                return null;
        }
}
