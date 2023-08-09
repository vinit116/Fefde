package club.potli

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.provider.Telephony.SMS_RECEIVED") {
            val bundle = intent.extras
            if (bundle != null) {
                val pdus = bundle["pdus"] as Array<*>?
                val messages = arrayOfNulls<SmsMessage>(pdus?.size ?: 0)
                for (i in messages.indices) {
                    messages[i] = SmsMessage.createFromPdu(pdus?.get(i) as ByteArray?)
                }
            }
        }
    }
}