package ru.sumin.servicestest

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import ru.sumin.servicestest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.simpleService.setOnClickListener() {
            stopService(MyForegroundService.newIntent(this))//способ убить сервис
            startService(MyService.newIntent(this))
        }
        binding.foregroundService.setOnClickListener() {
            ContextCompat.startForegroundService(this, MyForegroundService.newIntent(this))
        }//ContextCompat сам проверяет версию и вызывает либо startForegroundService, либо startService < 26 API
    }
}
