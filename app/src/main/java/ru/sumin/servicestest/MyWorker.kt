package ru.sumin.servicestest

import android.content.Context
import android.util.Log
import androidx.work.*

/**
 * Created by Dad52(Sobolev) on 6/26/2022.
 */
class MyWorker(
    context: Context,
    private val workerParams: WorkerParameters
): Worker(context, workerParams) {

    override fun doWork(): Result {
        log("doWork")
        val page = workerParams.inputData.getInt(PAGE, 0)
        for (i in 0 until 10) {
            Thread.sleep(1000)
            log("Timer $i, page $page")
        }
        return Result.success()
    }//выполняется сразу в другом потоке

    private fun log(message: String) {
        Log.e("Service", "MyWorkManager $message")
    }

    companion object {
        private const val PAGE = "page"
        const val WORK_NAME = "work_name"

        fun makeRequest(page: Int): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<MyWorker>()
                .setInputData(workDataOf(PAGE to page))//либо можно Pair(PAGE, page)
                .setConstraints(makeConstrains())
                .build()
        }
        private fun makeConstrains() : Constraints {
            return Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build()
        }
    }

}