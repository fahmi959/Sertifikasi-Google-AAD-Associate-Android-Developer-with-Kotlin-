package com.dicoding.habitapp.ui.list


import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dicoding.habitapp.R
import com.dicoding.habitapp.ui.add.AddHabitActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


// TODO 16 : Write UI test to validate when user tap Add Habit (+), the AddHabitActivity displayed
@RunWith(AndroidJUnit4::class)
class HabitActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(HabitListActivity::class.java)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        Intents.init()
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun whenClickAddHabit_ShouldDisplayAddHabitActivity() {
        // Lakukan tindakan klik pada tombol "Add Habit".
        onView(withId(R.id.fab)).perform(click())

        // Use intended() with hasComponent() matcher to verify the expected AddHabitActivity is launched
        intended(hasComponent(AddHabitActivity::class.java.name))
    }



}