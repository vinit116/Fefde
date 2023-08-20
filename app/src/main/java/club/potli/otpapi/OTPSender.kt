package club.potli.otpapi

import android.content.Context
import android.util.Log
import android.widget.Toast
import club.potli.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class OTPSender {
    private val client = OkHttpClient()

    fun sendOtp(context: Context, phoneNo : String, callback: OTPSenderCallback) {
        Log.v("SendOTP", "Working")
        val mediaType = "application/json".toMediaTypeOrNull()
        val requestBody = """
            {
                "source": "ShoutDEMO",
                "transport": "sms",
                "content": {
                    "sms": "Your code is {{code}}"
                },
                "destination": "+91$phoneNo"
            }
        """.trimIndent().toRequestBody(mediaType)

        val request = Request.Builder()
            .url("https://api.getshoutout.com/otpservice/send")
            .post(requestBody)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Apikey ${Constants.SHOUTOUT_API_KEY}")
            .build()


        CoroutineScope(Dispatchers.IO).launch {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // Handle network or request failure
                }
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        Log.v("Response", "Successful")
                        GlobalScope.launch(Dispatchers.Main){
                            Toast.makeText(
                                context,
                                "OTP has been successfully sent!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        val responseBody = response.body?.string()
                        try {
                            val jsonObject = responseBody?.let { JSONObject(it) }
                            val referenceId = jsonObject?.getString("referenceId")

                            if (referenceId != null) {
                                callback.onOtpSent(referenceId)
                            }

                        } catch (e: JSONException) {
                            // Handle JSON parsing error
                        }
                    } else {
                        GlobalScope.launch(Dispatchers.Main){
                            Toast.makeText(
                                context,
                                "Failed to send OTP. Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            })
        }
    }
}