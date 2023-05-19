package com.example.testjunior.main

import android.app.Application
import com.example.testjunior.BuildConfig
import com.example.testjunior.main.numbers.data.CloudModule



class NumberApp : Application() {

    override fun onCreate() {
        super.onCreate()

        //todo move out of here
        val cloudModule = if (BuildConfig.DEBUG)
            CloudModule.Debug()
        else
            CloudModule.Realise()

    }
}