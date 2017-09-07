package com.example.lorence.camerautils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by lorence on 07/09/2017.
 */

public class LogUtils {
    public static void log(int type, @NonNull String tag, @NonNull String message) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        switch (type) {
            case Log.DEBUG:
                Log.d(tag, message);
                break;

            case Log.ERROR:
                Log.e(tag, message);
                break;

            case Log.INFO:
                Log.i(tag, message);
                break;

            case Log.WARN:
                Log.w(tag, message);
                break;
            default:
                Log.v(tag, message);
                break;
        }
    }

    public static void log(int type, @NonNull String tag, @NonNull String message, @Nullable Throwable throwable) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        switch (type) {
            case Log.DEBUG:
                Log.d(tag, message, throwable);
                break;

            case Log.ERROR:
                Log.e(tag, message, throwable);
                break;

            case Log.INFO:
                Log.i(tag, message, throwable);
                break;

            case Log.WARN:
                Log.w(tag, message, throwable);
                break;
            default:
                Log.v(tag, message, throwable);
                break;
        }
    }
}
