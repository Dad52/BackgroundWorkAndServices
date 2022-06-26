package ru.sumin.servicestest

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
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
class MyIntentService : IntentService(NAME) {
//если запустить несколько раз, то они будут в очереди и постепенно выполняться
    override fun onCreate() {
        super.onCreate()
        log("onCreate")

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        startForeground(NOTIFICATION_ID,
            createNotification())//если менять айди, то будут разные уведомления
        //как чаты в телеге - разные уведомления.
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Title")
            .setContentText("Text")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    override fun onHandleIntent(intent: Intent?) {
        log("onHandleIntent")
        for (i in 0 until 10) {
            Thread.sleep(1000)
            log("Timer $i")
        }//после выполнения этого метода сервис самостоятельно убьется
    }//метод выполняется в фоновом потоке

    private fun log(message: String) {
        Log.e("Service", "MyIntentService $message")
    }

    companion object {
        private const val NAME = "NAME_INTENT_SERVICE"
        private const val CHANNEL_ID = "CHANNEL_ID"
        private const val CHANNEL_NAME =
            "channel_name"//категории каналов(долгий клик по уведомлению)

        //например, канал "Сообщения", "Рекомендации", "Акции" и тд
        private const val NOTIFICATION_ID = 1
        private const val EXTRA_START = "start"
        fun newIntent(context: Context) = Intent(context, MyIntentService::class.java)
    }
}