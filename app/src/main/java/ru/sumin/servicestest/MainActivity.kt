package ru.sumin.servicestest

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobWorkItem
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import ru.sumin.servicestest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var page = 0

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
        binding.intentService.setOnClickListener() {
            startService(MyIntentService.newIntent(this))
        }
        binding.jobScheduler.setOnClickListener() {

            val componentName = ComponentName(this, MyJobService::class.java)

            val jobInfo = JobInfo.Builder(MyJobService.JOB_ID, componentName)
                .setRequiresCharging(true)//будет работать только во время зарядки
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .build()

            val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val intent = MyJobService.newIntent(page++)
                jobScheduler.enqueue(jobInfo, JobWorkItem(intent))//создание очереди работ
                jobScheduler.schedule(jobInfo)//запустить одну работу, если запустить несколько - прошлые отменятся
            } else {
                startService(MyIntentService2.newIntent(this, page++))
            }
        }
        binding.jobIntentService.setOnClickListener() {
            MyJobIntentService.enqueue(this, page++)
        }
        binding.workManager.setOnClickListener() {
            val workManager = WorkManager.getInstance(applicationContext)
            workManager.enqueueUniqueWork(
                MyWorker.WORK_NAME,
                ExistingWorkPolicy.APPEND,//новый воркер будет добавлен в очередь, а не удален или заменен
                MyWorker.makeRequest(page++)
            )
        }
    }
}
