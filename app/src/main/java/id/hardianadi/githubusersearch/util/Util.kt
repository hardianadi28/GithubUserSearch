package id.hardianadi.githubusersearch.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import id.hardianadi.githubusersearch.R
import id.hardianadi.githubusersearch.ui.mainlist.MainActivity

/**
 * @author hardiansyah (hardiansyah.adi@gmail.com)
 * @since 08/09/2020
 */
class Util {
    companion object {

        private const val ALARM_CHANNEL_ID = "Channel_1"
        private const val ALARM_CHANNEL_NAME = "AlarmManager channel"
        private const val NOTIFICATION_REQUEST_CODE = 200

        private fun showToast(context: Context, title: String, message: String?, duration: Int) {
            Toast.makeText(context, "$title : $message", duration).show()
        }

        fun showToastShort(context: Context, title: String, message: String?) {
            showToast(context, title, message, Toast.LENGTH_SHORT)
        }

        private fun showNotification(
            context: Context,
            title: String,
            message: String,
            notifId: Int,
            icon: Int,
            channelId: String,
            channelName: String
        ) {
            val notificationManagerCompat = context.getSystemService<NotificationManager>()
            val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            val pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val builder = NotificationCompat.Builder(context, channelId)
                .setContentIntent(pendingIntent)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setSound(alarmSound)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                channel.enableVibration(true)
                channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
                builder.setChannelId(channelId)
                notificationManagerCompat?.createNotificationChannel(channel)
            }
            val notification = builder.build()
            notificationManagerCompat?.notify(notifId, notification)
        }

        fun showAlarmNotification(context: Context, title: String, message: String, notifId: Int) =
            showNotification(
                context,
                title,
                message,
                notifId,
                R.drawable.ic_access_time_black,
                ALARM_CHANNEL_ID,
                ALARM_CHANNEL_NAME
            )
    }
}