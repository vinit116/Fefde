package club.potli

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.content.IntentFilter

class SmsBackgroundService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val smsReceiver = SmsReceiver()
        val intentFilter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        registerReceiver(smsReceiver, intentFilter)

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}