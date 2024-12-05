package com.prescription.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class PrescriptionApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (com.prescription.app.BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }
}