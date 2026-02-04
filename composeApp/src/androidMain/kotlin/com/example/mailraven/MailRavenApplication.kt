package com.example.mailraven

import android.app.Application
import com.example.mailraven.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MailRavenApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MailRavenApplication)
            androidLogger()
            modules(appModule)
        }
    }
}