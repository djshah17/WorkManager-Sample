# WorkManager-Sample
This is a demo app of how to use work manager in kotlin.


## Add the below dependency in your app level build.gradle file
```
implementation 'androidx.work:work-runtime-ktx:2.3.4'
```

## Create a Worker class
```
class MyWorker(private val context: Context, private val workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        val getData = workerParams.inputData
        val channelId = getData.getString("channel_id")
        val title = getData.getString("title")
        val message = getData.getString("message")
        context.sendNotification(channelId,title,message)
        return Result.success()
    }

}
```

## Create a WorkManager instance to create work requests
```
val workManager = WorkManager.getInstance(this)
```

## Start and Cancel WorkRequests
```
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

    private fun cancelOneTimeRequest(){
        workManager.cancelWorkById(oneTimeReq.id)
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

    private fun cancelPeriodicRequest(){
        workManager.cancelWorkById(periodicReq.id)
    }
```
