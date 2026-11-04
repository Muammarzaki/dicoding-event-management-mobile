package com.dicoding

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dicoding.databinding.ActivityMainBinding
import com.dicoding.dto.Event
import com.dicoding.model.MainViewModel
import com.dicoding.model.ViewModelFactory
import com.dicoding.scheduler.EventScheduler
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels { ViewModelFactory.Factory }
    private var isSchedulerCreated: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.darkMode.observe(this) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.apply {
            setContentView(root)

            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
            bottomNav.setupWithNavController(navHostFragment.navController)
        }

        val name = "Scheduler Channel"
        val descriptionText = "Channel for scheduled notifications"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(EventScheduler.SCHEDULE_CHANNEL, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (notificationManager.getNotificationChannel(EventScheduler.SCHEDULE_CHANNEL) == null) {
            notificationManager.createNotificationChannel(channel)
        }

        viewModel.getClosestEvent.observe(this) { resources ->
            if (resources == null) {
                return@observe
            }
            viewModel.enableNotification.observe(this) { isEnable ->
                if (isEnable) {
                    scheduleNotification(this, resources)
                } else {
                    if (isSchedulerCreated)
                        cancelAlarm(this, resources)
                }
            }
        }
    }

    companion object {
        fun showErrorDialog(message: String, context: Context) {
            AlertDialog.Builder(context)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

        @SuppressLint("SimpleDateFormat")
        val timeFormat = SimpleDateFormat("dd MMMM yyyy HH:mm")
    }

    private fun scheduleNotification(context: Context, event: Event) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, EventScheduler::class.java)
        intent.putExtra(EventScheduler.NOTIFY_CONTENT_ID, "1")
        intent.putExtra(EventScheduler.CONTENT_TITLE_KEY, event.name)
        intent.putExtra(EventScheduler.CONTENT_TEXT_KEY,
            event.beginTime?.let { timeFormat.format(it) })

        val pendingIntent = event.id?.let {
            PendingIntent.getBroadcast(
                context,
                it,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        pendingIntent?.let {
            val triggerTime = (event.beginTime?.time ?: 0) - (10 * 60000)
            Log.d(
                "MainActivity",
                "Scheduling notification for event: ${event.name} at $triggerTime"
            )
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                it
            )
            isSchedulerCreated = true
        } ?: Log.e("MainActivity", "PendingIntent is null!")
    }

    fun cancelAlarm(context: Context, event: Event) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, EventScheduler::class.java)
        val pendingIntent = event.id?.let {
            PendingIntent.getBroadcast(
                context,
                it,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
        }
        if (pendingIntent != null) {
            pendingIntent.cancel()
            alarmManager.cancel(pendingIntent)
        }
        Log.d(
            "MainActivity",
            "Scheduling notification for event: ${event.name} canceled"
        )
    }
}