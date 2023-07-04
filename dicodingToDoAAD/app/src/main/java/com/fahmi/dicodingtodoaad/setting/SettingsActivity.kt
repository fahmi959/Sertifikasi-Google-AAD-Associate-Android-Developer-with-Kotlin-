package com.fahmi.dicodingtodoaad.setting

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.work.WorkManager
import com.fahmi.dicodingtodoaad.R

import androidx.work.*
import com.fahmi.dicodingtodoaad.notification.NotificationWorker
import com.fahmi.dicodingtodoaad.utils.NOTIFICATION_CHANNEL_ID
import java.util.concurrent.TimeUnit


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val prefNotification =
                findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
            prefNotification?.setOnPreferenceChangeListener { preference, newValue ->
                val channelName = getString(R.string.notify_channel_name)
                val notificationEnabled = newValue as Boolean

                if (notificationEnabled) {
                    scheduleDailyReminder(channelName)
                } else {
                    cancelDailyReminder()
                }

                true
            }
        }

        private fun scheduleDailyReminder(channelName: String) {
            val workManager = WorkManager.getInstance(requireContext())

            // Create an input data object with the channel name
            val inputData = Data.Builder()
                .putString(NOTIFICATION_CHANNEL_ID, channelName)
                .build()

            // Define the constraints for the work request
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build()

            // Create a work request to schedule the daily reminder
            val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .setInputData(inputData)
                .build()

            // Enqueue the work request
            workManager.enqueueUniquePeriodicWork(
                "dailyReminder",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )
        }

        private fun cancelDailyReminder() {
            val workManager = WorkManager.getInstance(requireContext())

            // Cancel the work request with the given unique name
            workManager.cancelUniqueWork("dailyReminder")
        }
    }
}