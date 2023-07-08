package com.fahmi.dicodingtodoaad.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.fahmi.dicodingtodoaad.R
import com.fahmi.dicodingtodoaad.data.Task
import com.fahmi.dicodingtodoaad.data.TaskRepository
import com.fahmi.dicodingtodoaad.ui.detail.DetailTaskActivity
import com.fahmi.dicodingtodoaad.utils.NOTIFICATION_CHANNEL_ID
import com.fahmi.dicodingtodoaad.utils.TASK_ID
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    private val applicationContext = ctx.applicationContext
    private val channelName = inputData.getString(NOTIFICATION_CHANNEL_ID)

    private fun getPendingIntent(task: Task): PendingIntent? {
        val intent = Intent(applicationContext, DetailTaskActivity::class.java).apply {
            putExtra(TASK_ID, task.id)
        }
        return TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            } else {
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }
    }

    override fun doWork(): Result {
        val repository = TaskRepository.getInstance(applicationContext)
        val nearestActiveTask = repository.getNearestActiveTask()

        if (nearestActiveTask != null && channelName != null) {
            val notification = buildNotification(nearestActiveTask)
            showNotification(notification)
        }

        return Result.success()
    }

    private fun buildNotification(task: Task): Notification {
        val currentDateTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val dueDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(task.dueDateMillis)

        val contentTitle = applicationContext.getString(R.string.app_name)
        val contentText = applicationContext.getString(
            R.string.notify_content,
            currentDateTime,
            task.title,
            dueDate
        )
        val contentIntent = getPendingIntent(task)

        val notificationBuilder = channelName?.let {
            NotificationCompat.Builder(applicationContext, it)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }

        return notificationBuilder?.build() ?: throw IllegalStateException("NotificationBuilder cannot be null")
    }

    private fun showNotification(notification: Notification) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notification)
    }

    companion object {
        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelId = "Task Reminder"
                val channelName = "Task Reminder Channel"
                val channelDescription = "Channel for task reminders"

                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(channelId, channelName, importance).apply {
                    description = channelDescription
                }

                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}
