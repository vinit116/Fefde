package club.potli

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import club.potli.otpapi.OTPReceiver
import club.potli.otpapi.OTPSender
import club.potli.otpapi.OTPSenderCallback
import club.potli.util.Constants
import io.realm.kotlin.mongodb.App


class LoginOTP : AppCompatActivity(), OTPSenderCallback {
    private val app = App.create(Constants.APP_ID)
    private val user = app.currentUser

    private lateinit var referenceId: String
    private lateinit var digit1: EditText
    private lateinit var digit2: EditText
    private lateinit var digit3: EditText
    private lateinit var digit4: EditText
    private lateinit var digit5: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page_phoneno)

        val getOTP = findViewById<ImageButton>(R.id.get_otp_button)

        getOTP.setOnClickListener {
            val phoneNo = findViewById<EditText>(R.id.tenDigit).text.toString()
            Log.v("Phone No", "$phoneNo")
            OTPSender().sendOtp(this, phoneNo, this)
            startOTPInput()
        }
    }

    override fun onOtpSent(referenceId: String) {
        this.referenceId = referenceId
    }


    private fun startOTPInput() {
        setContentView(R.layout.otp_input)
        digit1 = findViewById(R.id.digit1)
        digit2 = findViewById(R.id.digit2)
        digit3 = findViewById(R.id.digit3)
        digit4 = findViewById(R.id.digit4)
        digit5 = findViewById(R.id.digit5)

        setupTextWatchers()
    }

    private fun setupTextWatchers() {
        digit1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 1) {
                    digit2.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        digit2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 1) {
                    digit3.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        digit3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 1) {
                    digit4.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        digit4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 1) {
                    digit5.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        digit5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val otp = "${digit1.text}${digit2.text}${digit3.text}${digit4.text}${digit5.text}"
                checkOTP(otp, referenceId, this@LoginOTP)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }

    fun checkOTP(otp : String, referenceId: String, context: Context){
        OTPReceiver().verifyOtp(otp, referenceId, context)
    }
}

