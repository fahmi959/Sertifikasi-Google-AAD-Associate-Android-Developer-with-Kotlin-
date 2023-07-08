package com.dicoding.courseschedule.ui.setting



import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.notification.DailyReminder

import java.util.concurrent.TimeUnit

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    private lateinit var dailyReminderSwitchPreference: SwitchPreference
    private lateinit var themeListPreference: ListPreference
    private lateinit var dailyReminder: DailyReminder

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        // TODO 10 : Update theme based on value in ListPreference
        // TODO 11 : Schedule and cancel notification in DailyReminder based on SwitchPreference

        dailyReminderSwitchPreference = findPreference(getString(R.string.pref_key_notify))!!
        themeListPreference = findPreference(getString(R.string.pref_key_dark))!!

        dailyReminderSwitchPreference.onPreferenceChangeListener = this
        themeListPreference.onPreferenceChangeListener = this

        dailyReminder = DailyReminder()
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        dailyReminder = DailyReminder()
        return when (preference) {
            dailyReminderSwitchPreference -> {
                val isReminderEnabled = newValue as Boolean
                if (isReminderEnabled) {
                    // Mengaktifkan Notif
                    context?.let { dailyReminder.setDailyReminder(it) }
                } else {
                    // Menonaktifkan Notif
                    context?.let { dailyReminder.cancelAlarm(it) }
                }
                true
            }
            themeListPreference -> {
                val themeValue = newValue as String
                updateTheme(themeValue)
                true
            }
            else -> false
        }
    }


    private fun updateTheme(themeValue: String) {
        val nightMode = when (themeValue) {
            getString(R.string.pref_dark_off) -> AppCompatDelegate.MODE_NIGHT_NO
            getString(R.string.pref_dark_on) -> AppCompatDelegate.MODE_NIGHT_YES
            getString(R.string.pref_dark_auto) -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            else -> AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
    }
}
