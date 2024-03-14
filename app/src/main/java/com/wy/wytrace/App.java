package com.wy.wytrace;

import android.app.Application;
import android.os.Process;
import android.os.SystemClock;

import com.wy.lib.wytrace.ArtMethodTrace;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ArtMethodTrace.fix14debugApp(this);
        ArtMethodTrace.methodHookStart("", Process.myTid(), 10, true);
    }
}
