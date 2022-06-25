package ru.sumin.servicestest

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

/**
 * Created by Dad52(Sobolev) on 6/25/2022.
 */
class MyService: Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
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
        val startNum = intent?.getIntExtra(EXTRA_START, 0) ?: 0
        coroutineScope.launch {
            for (i in startNum until 100) {
                delay(1000)
                log("Timer $i")
            }
        }
        return START_STICKY
    }
    //start_sticky - если система убила сервис(или мы его закрыли), сервис БУДЕТ пересоздан
    //start_not_sticky - если система убила сервис(или мы его закрыли), сервис пересоздан НЕ БУДЕТ
    //start_redeliver_intent - если система убила сервис, тот интент, который был - прилетит снова
    //иначе intent будет null и в нашем случае отсчет будет с 0, потому что intent == null
    //с android 8(26) сервисы ограничены. Теперь нужно уведомлять пользователя, что приложение что-то делает в фоне

    private fun log(message: String) {
        Log.e("Service", "MyService $message")
    }

    companion object {
        private const val EXTRA_START = "start"
        fun newIntent(context: Context) = Intent(context, MyService::class.java).apply {
            putExtra(EXTRA_START, 10)
        }
    }
}