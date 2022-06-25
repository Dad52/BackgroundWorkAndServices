package ru.sumin.servicestest

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

/**
 * Created by Dad52(Sobolev) on 6/25/2022.
 */
class MyForegroundService: Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
        }//канал нужно указывать начиная с android 8, до этой версии работает без этого

//        notificationManager.notify(1, notification)

        startForeground(NOTIFICATION_ID, createNotification())//если менять айди, то будут разные уведомления
        //как чаты в телеге - разные уведомления.
    }

    private fun createNotification() : Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Title")
            .setContentText("Text")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        log("onDestroy")
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        coroutineScope.launch {
            for (i in 0 until 10) {
                delay(1000)
                log("Timer $i")
            }
            stopSelf()//убивает сервис и убирает уведомление
        }
        return START_STICKY
    }
    //start_sticky - если система убила сервис(или мы его закрыли), сервис БУДЕТ пересоздан
    //start_not_sticky - если система убила сервис(или мы его закрыли), сервис пересоздан НЕ БУДЕТ
    //start_redeliver_intent - если система убила сервис, тот интент, который был - прилетит снова
    //иначе intent будет null и в нашем случае отсчет будет с 0, потому что intent == null
    //с android 8(26) сервисы ограничены. Теперь нужно уведомлять пользователя, что приложение что-то делает в фоне

    private fun log(message: String) {
        Log.e("Service", "MyForegroundService $message")
    }

    companion object {
        private const val CHANNEL_ID = "CHANNEL_ID"
        private const val CHANNEL_NAME = "channel_name"//категории каналов(долгий клик по уведомлению)
        //например, канал "Сообщения", "Рекомендации", "Акции" и тд
        private const val NOTIFICATION_ID = 1
        private const val EXTRA_START = "start"
        fun newIntent(context: Context) = Intent(context, MyForegroundService::class.java)
    }
}