package ru.sumin.servicestest

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Created by Dad52(Sobolev) on 6/25/2022.
 */
class MyIntentService2 : IntentService(NAME) {
    //если запустить несколько раз, то они будут в очереди и постепенно выполняться
    override fun onCreate() {
        super.onCreate()
        log("onCreate")
        setIntentRedelivery(true)
    }


    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    override fun onHandleIntent(intent: Intent?) {
        log("onHandleIntent")
        val page = intent?.getIntExtra(PAGE, 0) ?: 0
        for (i in 0 until 10) {
            Thread.sleep(1000)
            log("Timer $i $page")
        }//после выполнения этого метода сервис самостоятельно убьется
    }//метод выполняется в фоновом потоке

    private fun log(message: String) {
        Log.e("Service", "MyIntentService $message")
    }

    companion object {
        private const val NAME = "NAME_INTENT_SERVICE"
        private const val PAGE = "PAGE"

        fun newIntent(context: Context, page: Int) =
            Intent(context, MyIntentService2::class.java).apply {
                putExtra(PAGE, page)
            }
    }
}