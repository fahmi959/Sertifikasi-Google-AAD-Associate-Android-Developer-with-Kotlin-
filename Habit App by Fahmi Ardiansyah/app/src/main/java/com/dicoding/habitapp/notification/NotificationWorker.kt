package com.dicoding.habitapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dicoding.habitapp.R
import com.dicoding.habitapp.ui.detail.DetailHabitActivity
import com.dicoding.habitapp.utils.HABIT_ID
import com.dicoding.habitapp.utils.HABIT_TITLE

class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    private val habitId = inputData.getInt(HABIT_ID, 0)
    private val habitTitle = inputData.getString(HABIT_TITLE)

    override fun doWork(): Result {
        val prefManager = androidx.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val shouldNotify = prefManager.getBoolean(applicationContext.getString(R.string.pref_key_notify), false)

        // TODO 12 : If notification preference on, show notification with pending intent
        if (shouldNotify) {
            showNotification()
        }

        return Result.success()
    }

    private fun showNotification() {
        val contentText = applicationContext.getString(R.string.notify_content)

        // Build the notification
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle("$habitTitle")
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        // Add pending intent to open detail habit activity
        val intent = Intent(applicationContext, DetailHabitActivity::class.java)
        intent.putExtra(HABIT_ID, habitId)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            habitId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        builder.setContentIntent(pendingIntent)

        // Show the notification if permission is granted
        if (checkNotificationPermission()) {
            with(NotificationManagerCompat.from(applicationContext)) {
                notify(habitId, builder.build())
            }
        }
    }

    private fun checkNotificationPermission(): Boolean {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.areNotificationsEnabled()
        } else {
            NotificationManagerCompat.from(applicationContext).areNotificationsEnabled()
        }
    }



    companion object {
        private const val CHANNEL_ID = "habit_channel"
    }
}