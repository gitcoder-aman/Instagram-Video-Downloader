package com.tech.instasaver

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {
//    lateinit var applicationComponent: ApplicationComponent
//    override fun onCreate() {
//        super.onCreate()
//
//        applicationComponent = DaggerApplicationComponent.builder().build()
//    }
}