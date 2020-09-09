package id.hardianadi.githubusersearch.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.getSystemService
import id.hardianadi.githubusersearch.R
import id.hardianadi.githubusersearch.util.Util
import id.hardianadi.githubusersearch.util.Util.Companion.showAlarmNotification
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        private const val ID_REPEATING = 101
        private const val TIME_FORMAT = "HH:mm"
        const val EXTRA_MESSAGE = "message"
        const val TYPE_REPEATING = "RepeatingAlarm"
        const val NOTIFICATION_TIME = "09:00"
    }


    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra(EXTRA_MESSAGE)

        val title = TYPE_REPEATING
        val notifId = ID_REPEATING
        Log.d("AlarmReceiver", "Masuk Alarm")
        showAlarmNotification(context, title, message?:"", notifId)
    }

    fun setAlarm(context: Context, isActive: Boolean) {
        if(isActive) {
            if(!this.isAlarmSet(context)) {
                this.setRepeatingAlarm(context, NOTIFICATION_TIME, context.getString(R.string.notif_message))
            }
        }else{
            if(this.isAlarmSet(context)) {
                this.cancelAlarm(context)
            }
        }
    }

    private fun setRepeatingAlarm(context: Context, time: String, message: String) {
        if (isDateInvalid(time, TIME_FORMAT)) return
        val alarmManager = context.getSystemService<AlarmManager>()
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
            set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
            set(Calendar.SECOND, 0)
        }
        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        alarmManager?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        Util.showToastShort(context, "Switch", "Alarm Set at $time")
    }

    private fun isDateInvalid(date: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }
    }

    private fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService<AlarmManager>()
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = ID_REPEATING
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()

        alarmManager?.cancel(pendingIntent)
        Util.showToastShort(context, "Switch", "Alarm Cancelled")
    }

    private fun isAlarmSet(context: Context): Boolean {
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = ID_REPEATING

        return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_NO_CREATE) != null
    }
}
