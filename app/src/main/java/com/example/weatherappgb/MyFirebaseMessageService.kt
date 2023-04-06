package com.example.weatherappgb

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.weatherappgb.view.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessageService : FirebaseMessagingService() {
    companion object{

       private const val TITLE_KEY = "myTitle"
       private const val MESSAGE_KEY = "myMessage"
       private const val NOTIFICATION_ID = 1
       private const val CHANNEL_ID = "chanel_id"
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if(!message.data.isNullOrEmpty()){
            val title = message.data[TITLE_KEY]
            val message = message.data[MESSAGE_KEY]
            if(!title.isNullOrEmpty()&&!message.isNullOrEmpty()){
                push(title,message)
            }
        }

    }
    private fun push(title:String,message:String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(applicationContext, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationBuilderHigh = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_navigation)
            setContentTitle(title)
            setContentText(message)
            setContentIntent(contentIntent)
            priority = NotificationManager.IMPORTANCE_HIGH
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelNameLow = "Name $CHANNEL_ID"
            val channelDescriptionLow = "Description $CHANNEL_ID"
            val channelPriorityLow = NotificationManager.IMPORTANCE_LOW
            val channelLow =
                NotificationChannel(CHANNEL_ID, channelNameLow, channelPriorityLow).apply {
                    description = channelDescriptionLow
                }
            notificationManager.createNotificationChannel(channelLow)
        }

        notificationManager.notify(NOTIFICATION_ID, notificationBuilderHigh.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}