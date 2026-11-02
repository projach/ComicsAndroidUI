package com.projach.comics_tracker.core

import android.util.Log
import com.projach.comics_tracker.BuildConfig


object Logger {
    fun d(tag: String?, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun i(tag: String?, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg)
        }
    }

    fun e(tag: String?, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg)
        }
    }
}