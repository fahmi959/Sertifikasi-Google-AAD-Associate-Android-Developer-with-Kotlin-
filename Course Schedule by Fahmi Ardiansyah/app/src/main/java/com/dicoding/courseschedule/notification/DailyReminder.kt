package com.dicoding.courseschedule.notification




import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.data.CourseDatabase
import com.dicoding.courseschedule.data.DataRepository
import com.dicoding.courseschedule.ui.home.HomeActivity
import com.dicoding.courseschedule.util.executeThread
import java.util.Calendar

class DailyReminder : BroadcastReceiver() {

    companion object {
        private const val NOTIFICATION_ID = 12345
        private const val CHANNEL_ID = "course_schedule_channel"
        private const val CHANNEL_NAME = "Course Schedule"
        private const val GROUP_KEY = "course_schedule_group"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val handler = Handler(Looper.getMainLooper())

        executeThread {
            val dao = CourseDatabase.getInstance(context).courseDao()
            val repository = DataRepository.getInstance(context, dao)
            val courses = repository.getTodaySchedule()

            handler.post {
                courses.observeForever { courseList ->
                    if (courseList != null && courseList.isNotEmpty()) {
                        showNotification(context, courseList)
                    }
                }
            }
        }
    }


    // TODO 12 : Implement daily reminder for every 06.00 a.m using AlarmManager
    @SuppressLint("InlinedApi")
    fun setDailyReminder(context: Context) {
        Toast.makeText(context.applicationContext, "Alarm Daily Reminder diaktifkan", Toast.LENGTH_SHORT).show()

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyReminder::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        // Buat objek Calendar untuk mengatur waktu alarm pada pukul 6 pagi
        val calendar = Calendar.getInstance()
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, 21)
            set(Calendar.MINUTE, 28)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Atur waktu alarm untuk setiap hari pada pukul 6 pagi
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }



    @SuppressLint("InlinedApi")
    fun cancelAlarm(context: Context) {
        Toast.makeText(context.applicationContext, "Alarm Daily Reminder dimatikan", Toast.LENGTH_SHORT).show()

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyReminder::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT) // Add the mutability flag


        // Batalkan alarm yang sudah diatur sebelumnya
        alarmManager.cancel(pendingIntent)
    }

    private fun showNotification(context: Context, content: List<Course>) {
        // TODO 13 : Show today schedules in inbox style notification & open HomeActivity when notification tapped
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(context.getString(R.string.today_schedule))
            .setContentText(context.getString(R.string.notification_message_format))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(getPendingIntent(context))
            .setStyle(createInboxStyle(context, content))
            .setGroupSummary(true)
            .setGroup(GROUP_KEY)

        val notificationManager = NotificationManagerCompat.from(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        try {
            if (notificationManager.areNotificationsEnabled()) {
                notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
            } else {
                // Handle the case where the permission is not granted
                // You can show a message to the user or redirect them to the app settings
            }
        } catch (e: SecurityException) {
            // Handle the security exception here
        }
    }

    private fun getPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun createInboxStyle(context: Context, content: List<Course>): NotificationCompat.Style {
        val notificationStyle = NotificationCompat.InboxStyle()
        val timeString = context.getString(R.string.notification_message_format)
        content.forEach {
            val courseData = String.format(timeString, it.startTime, it.endTime, it.courseName)
            notificationStyle.addLine(courseData)
        }
        return notificationStyle
    }
}
