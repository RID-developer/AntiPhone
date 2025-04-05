package com.RIDdev.antiphone;

import android.adservices.ondevicepersonalization.AppInfo;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.RIDdev.antiphone.background.Constant;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppBlock extends AppCompatActivity {
    private List<ApplicationInfo> getInstalledApps() {
        PackageManager pm = getPackageManager();
        return pm.getInstalledApplications(PackageManager.GET_META_DATA);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_app_block);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        LinearLayout list = findViewById(R.id.list);
        PackageManager pm = getPackageManager();
        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_META_DATA);
        System.out.println(apps.size());
        Collections.sort(apps, new Comparator<PackageInfo>() {
            @Override
            public int compare(PackageInfo app1, PackageInfo app2) {
                String appName1 = pm.getApplicationLabel(app1.applicationInfo).toString();
                String appName2 = pm.getApplicationLabel(app2.applicationInfo).toString();
                return appName1.compareToIgnoreCase(appName2);
            }
        });

        for (PackageInfo app : apps) {

                Button btn = new Button(this);
                btn.setText(pm.getApplicationLabel(app.applicationInfo));
            Log.d("AppDetails", "PackName: " + app.packageName);
               // btn.setTag(app.packageName);
                btn.setOnClickListener(v -> {
                    String packageName = app.packageName;
                    Intent intent = new Intent(this, AppDetails.class);
                    Log.d("AppDetails", "PackName: " + packageName);
                    intent.putExtra("packageName", packageName);
                    startActivity(intent);
                });
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, 16);
                btn.setLayoutParams(params);
                btn.setVisibility(View.VISIBLE);
                list.addView(btn);

        }
    }
    public void back(View v)
    {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}