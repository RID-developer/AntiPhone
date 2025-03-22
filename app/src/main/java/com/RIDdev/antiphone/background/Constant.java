package com.RIDdev.antiphone.background;

import static java.security.AccessController.getContext;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.RIDdev.antiphone.Database.DBOperation;
import com.RIDdev.antiphone.PackSet;

public class Constant extends Service {
    public static DBOperation Opr;
    public static PackSet Pack = new PackSet();
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
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Opr = new DBOperation(getApplicationContext());
        Opr.fill();
        Opr.Remake();
        Pack.PackName = "com.kde.konsole";
        Pack.ad = false;
        Opr.Insert();
        Initiate();
        return START_STICKY;
    }
    public void forever() {
        while (true) {
            long begining = System.currentTimeMillis();
            //something();
            Pack.time += 5;
            System.out.println("Hi");
            long end = System.currentTimeMillis() - begining;
            pause(safety((5000 - end)));
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
}
