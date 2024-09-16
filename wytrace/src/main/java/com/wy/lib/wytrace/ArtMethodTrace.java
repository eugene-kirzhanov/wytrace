package com.wy.lib.wytrace;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;

import com.bytedance.shadowhook.ShadowHook;

/**
 * Description:
 *
 * @author zhou.junyou
 * Create by:Android Studio
 * Date:2022/9/22
 */
public class ArtMethodTrace {
    static {
        System.loadLibrary("wytrace");
        ShadowHook.init(new ShadowHook.ConfigBuilder()
                .setMode(ShadowHook.Mode.UNIQUE)
                .build());
    }

    public static void fix14debugApp(Context context) {
        if (Build.VERSION.SDK_INT == 34 && (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            bootImageNterp();
        }
    }


    /**
     * 修复Android 14 debug包卡顿
     */

    public static native void bootImageNterp();

}
