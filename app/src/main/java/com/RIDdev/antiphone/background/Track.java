package com.RIDdev.antiphone.background;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;

public class Track {
    private static Context context;

    public static void init() {
        context = context.getApplicationContext();
    }

    public static String Check() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager manager = (UsageStatsManager)
                    android.app.AppGlobals.getApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE);

            long currentTime = System.currentTimeMillis();
            UsageStats stats = manager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, currentTime - 1000 * 60, currentTime);

            if (stats != null && stats.size() > 0) {
                return stats.get(stats.size() - 1).getPackageName();
            }
        }

        return null;
    }
}