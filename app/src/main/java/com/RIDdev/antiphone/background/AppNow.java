package com.RIDdev.antiphone.background;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

public class AppNow extends AccessibilityService {
    private String AppOld = "";
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String Name = event.getPackageName().toString();

        if (Name != null) {
            Constant.Pack.PackName = Name;
        } else {
            Constant.Pack.PackName = "Home";
            Constant.Pack.blacklist = false;
        }
    }

    @Override
    public void onInterrupt() {
        return;
    }
}