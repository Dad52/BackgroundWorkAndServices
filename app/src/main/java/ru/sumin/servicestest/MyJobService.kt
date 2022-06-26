package ru.sumin.servicestest

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Build
import android.util.Log
import kotlinx.coroutines.*

/**
 * Created by Dad52(Sobolev) on 6/25/2022.
 */
class MyJobService : JobService() {

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

    override fun onStartJob(params: JobParameters?): Boolean {
        log("onStartCommand")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            coroutineScope.launch {
                var workItem = params?.dequeueWork()
                while (workItem != null) {
                    val page = workItem.intent.getIntExtra(PAGE, 0)

                    for (i in 0 until 5) {
                        delay(1000)
                        log("Timer $i, page $page")
                    }
                    params?.completeWork(workItem)//работа закончена, идем к след странице
                    workItem = params?.dequeueWork()//получение новой работы и выход из цикла
                }
                jobFinished(params, false)//завершение работы ВСЕГО СЕРВИСА
            }
        }
        return true//выполняется ли сервис? Если true - да, false - нет
    }// если выполняется асинхронный код, то дойдет до конца раньше
    // и нужно вернуть, что сервис ещё работает и завершать не нужно

    //если у нас там СИНХРОННЫЙ КОД, то указываем false

    override fun onStopJob(params: JobParameters?): Boolean {
        log("onStopJob")
        return true
    }//вызывается, если условия вызова пропало
    //если стоит условие - выполнять сервис во время зарядки, а она отключилась
    //то вызывается этто метод.
    //если сами завершили сервис - метод НЕ ВЫЗЫВАЕТСЯ

    private fun log(message: String) {
        Log.e("Service", "MyJobService $message")
    }

    companion object {
        const val JOB_ID = 111

        private const val PAGE = "page"

        fun newIntent(page: Int): Intent {
            return Intent().apply {
                putExtra(PAGE, page)
            }
        }
    }

}