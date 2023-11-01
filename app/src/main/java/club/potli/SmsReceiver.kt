package club.potli

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.provider.Telephony
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class SmsReceiver() : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION){
            val bundle = intent.extras
            if (bundle != null) {
                val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
                for (message in messages) {
                    val timeStampMillis = message.timestampMillis
                    val timeStamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(timeStampMillis))
                    val messageBody = message.messageBody
                    val searchTerm = "debited by"
                    val searchTermIndex = messageBody.indexOf(searchTerm, ignoreCase = true)

                    if(searchTermIndex != -1){
                        val startIndex = searchTermIndex + searchTerm.length
                        val numberPattern = "\\d+(\\.\\d+)?".toRegex()
                        val matchResult = numberPattern.find(messageBody, startIndex)

                        if (matchResult != null){
                            val number = matchResult.value
                            val amount : Double = try{
                                number.toDouble()
                            } catch (e: java.lang.NumberFormatException) {
                                -1.0
                            }

                            if (amount != -1.0) {
                                Log.v("Amount", "Debited by $amount")
                                Log.v("Timestamp", "Received at $timeStamp")
                                Toast.makeText(
                                    context,
                                    "Debited Amount : $amount",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Toast.makeText(
                                    context,
                                    "At $timeStamp",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val floatingDialogIntent = Intent(context, FloatingDialogService::class.java)
                                context?.startService(floatingDialogIntent)
                            }
                        }
                    }
                }
            }
        }
    }
}
