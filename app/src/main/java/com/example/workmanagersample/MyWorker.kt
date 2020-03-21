package com.example.workmanagersample

import android.app.*
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

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

    private fun Context.sendNotification(
        channelId: String?,
        contentTitle: String?,
        contentText: String?
    ) {
        try {
            // Get an instance of the Notification manager
            val mNotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Android O requires a Notification Channel.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Create the channel for the notification
                val mChannel =
                    NotificationChannel(channelId, getString(R.string.app_name) , NotificationManager.IMPORTANCE_HIGH)

                // Set the Notification Channel for the Notification Manager.
                mNotificationManager.createNotificationChannel(mChannel)
            }

            // Get a notification builder that's compatible with platform versions >= 4
            val builder = NotificationCompat.Builder(this)

            // Define the notification settings.
            builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(contentTitle)
                .setContentText(contentText)


            // Set the Channel ID for Android O.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                channelId?.let { builder.setChannelId(it) } // Channel ID
            } else {
                builder.priority = Notification.PRIORITY_HIGH
            }

            // Dismiss notification once the user touches it.
            builder.setAutoCancel(true)

            // Issue the notification
            mNotificationManager.notify(0, builder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}