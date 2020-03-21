package com.example.workmanagersample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var workManager: WorkManager
    private lateinit var oneTimeReq: OneTimeWorkRequest
    private lateinit var periodicReq: PeriodicWorkRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        workManager = WorkManager.getInstance(this)

        btn_one_time.tag = "stopped"
        btn_periodic.tag = "stopped"

        btn_one_time.setOnClickListener {
            if(it.tag == "stopped"){
                btn_one_time.tag = "started"
                btn_one_time.text = "Cancel One Time Request"
                startOneTimeRequest()
            } else{
                btn_one_time.tag = "stopped"
                btn_one_time.text = "Start One Time Request"
                workManager.cancelWorkById(oneTimeReq.id)
            }
        }

        btn_periodic.setOnClickListener {
            if(it.tag == "stopped"){
                btn_periodic.tag = "started"
                btn_periodic.text = "Cancel Periodic Request"
                startPeriodicRequest()
            } else{
                btn_periodic.tag = "stopped"
                btn_periodic.text = "Start Periodic Request"
                workManager.cancelWorkById(periodicReq.id)
            }
        }

    }

    private fun startOneTimeRequest(){
        val constraints = Constraints.Builder()
            .build()

        val data = Data.Builder()
            .putString("channel_id","one_time_work")
            .putString("title", "One Time Request")
            .putString("message", "This notification is from One Time Request")
            .build()

        oneTimeReq = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        workManager.enqueue(oneTimeReq)
    }

    private fun startPeriodicRequest(){
        val constraints = Constraints.Builder()
            .build()

        val data = Data.Builder()
            .putString("channel_id","periodic_work")
            .putString("title", "Periodic Request")
            .putString("message", "This notification is from Periodic Request")
            .build()

        periodicReq = PeriodicWorkRequest.Builder(MyWorker::class.java,15,TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        workManager.enqueueUniquePeriodicWork("start_periodic_work",ExistingPeriodicWorkPolicy.REPLACE,periodicReq)
    }

}
