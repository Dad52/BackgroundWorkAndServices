package ru.sumin.servicestest

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService

/**
 * Created by Dad52(Sobolev) on 6/25/2022.
 */
class MyJobIntentService : JobIntentService() {

    //в этом сервисе есть минус - нельзя накладывать ограничения
    //когда делать работу - на зарядке, во время wifi и тд

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }


    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    override fun onHandleWork(intent: Intent) {
        log("onHandleIntent")
        val page = intent.getIntExtra(PAGE, 0)
        for (i in 0 until 10) {
            Thread.sleep(1000)
            log("Timer $i $page")
        }
    }

    private fun log(message: String) {
        Log.e("Service", "MyIntentService $message")
    }

    companion object {
        private const val PAGE = "PAGE"
        private const val JOB_ID = 111

        fun enqueue(context: Context, page: Int) {
            enqueueWork(
                context,
                MyJobIntentService::class.java,
                JOB_ID,
                newIntent(context, page)
            )
        }

        private fun newIntent(context: Context, page: Int) =
            Intent(context, MyJobIntentService::class.java).apply {
                putExtra(PAGE, page)
            }
    }
}