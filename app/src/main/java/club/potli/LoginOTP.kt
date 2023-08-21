package club.potli

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import club.potli.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.ktx.Firebase
import io.realm.kotlin.mongodb.App
import java.util.concurrent.TimeUnit


class LoginOTP : AppCompatActivity() {
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
            Log.v("Phone No", phoneNo)

            val auth = FirebaseAuth.getInstance()

            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNo) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this) // Activity (for callback binding)
                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)

        }
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
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }
}


