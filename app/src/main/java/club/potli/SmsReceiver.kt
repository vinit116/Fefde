package club.potli

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.provider.Telephony
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class SmsReceiver() : BroadcastReceiver() {
    private var amount: Double = 0.0

    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION){
            val bundle = intent.extras
            if (bundle != null) {
                val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
                for (message in messages) {
                    val timeStampMillis = message.timestampMillis
                    val timeStamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(timeStampMillis))
                    val messageBody = message.messageBody
                    val searchTerm1 = "debited by"
                    val searchTerm1Index = messageBody.indexOf(searchTerm1, ignoreCase = true)

                    val searchTerm2 = "transferred from"
                    val reversedBody = messageBody.reversed()
                    val searchTermIndex = reversedBody.indexOf(searchTerm2.reversed())

                    if(searchTerm1Index != -1){
                        val startIndex = searchTerm1Index + searchTerm1.length
                        val numberPattern = "\\d+(\\.\\d+)?".toRegex()
                        val matchResult = numberPattern.find(messageBody, startIndex)

                        if (matchResult != null) {
                            val number = matchResult.value
                            amount = try {
                                number.toDouble()
                            } catch (e: java.lang.NumberFormatException) {
                                -1.0
                            }
                        }
                    }

                    else if (searchTermIndex != -1) {
                        val amountIndex = searchTermIndex + searchTerm2.length
                        val reversedAmountString = reversedBody.substring(amountIndex).split(' ')[0]
                        val amountString = reversedAmountString.reversed()

                        val amount: Double = try {
                            amountString.toDoubleOrNull() ?: -1.0
                        } catch (e: java.lang.NumberFormatException) {
                            -1.0
                        }

                        if (amount != -1.0) {
                            Log.d("Extracted Amount", "$amount")
                        }
                    }
                    if (amount != -1.0) {
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
                        floatingDialogIntent.putExtra("amount", "$amount")
                        context?.startService(floatingDialogIntent)
                    }
                }
            }
        }
    }
}
