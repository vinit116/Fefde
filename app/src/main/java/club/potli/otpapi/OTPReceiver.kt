package club.potli.otpapi

import android.content.Context
import android.content.Intent
import android.widget.Toast
import club.potli.MainActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import club.potli.util.Constants.SHOUTOUT_API_KEY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class OTPReceiver {
    private val client = OkHttpClient()

    fun verifyOtp(code: String, referenceId: String, context: Context) {
        val mediaType = "application/json".toMediaTypeOrNull()
        val requestBody = """
            [{
                "code": "$code",
                "referenceId": "$referenceId"
            }]
        """.trimIndent().toRequestBody(mediaType)

        val request = Request.Builder()
            .url("https://api.getshoutout.com/otpservice/verify")
            .post(requestBody)
            .addHeader("content-type", "application/json")
            .addHeader("Authorization", "Apikey $SHOUTOUT_API_KEY")
            .build()


        CoroutineScope(Dispatchers.IO).launch {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // Handle network or request failure
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        startMainActivity(context)
                    } else {
                        GlobalScope.launch(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                "OTP Verification Failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            })
        }
    }
    private fun startMainActivity(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }

}