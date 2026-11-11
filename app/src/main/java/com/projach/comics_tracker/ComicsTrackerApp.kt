package com.projach.comics_tracker

import android.app.Application
import com.projach.comics_tracker.domain.AWAKE
import com.projach.comics_tracker.domain.BASE_URL
import dagger.hilt.android.HiltAndroidApp
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

@HiltAndroidApp
class ComicsTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        //TODO(remove when we migrate to paid server)
        OkHttpClient().newCall(
            Request.Builder().url("$BASE_URL$AWAKE").build()
        ).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                response.close()
            }
        })
    }
}