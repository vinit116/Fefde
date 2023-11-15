package club.potli

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


class LoginOTP : AppCompatActivity() {

    private var isVerificationInProgress = false
    private val auth = FirebaseAuth.getInstance()
    private lateinit var storedVerificationId : String
    private lateinit var resendToken : PhoneAuthProvider.ForceResendingToken
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var digit1: EditText
    private lateinit var digit2: EditText
    private lateinit var digit3: EditText
    private lateinit var digit4: EditText
    private lateinit var digit5: EditText
    private lateinit var digit6: EditText

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d("TAG", "onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w("TAG", "onVerificationFailed", e)

            when (e) {
                is FirebaseAuthInvalidCredentialsException -> {
                    // Invalid request
                }

                is FirebaseTooManyRequestsException -> {
                    // The SMS quota for the project has been exceeded
                }

                is FirebaseAuthMissingActivityForRecaptchaException -> {
                    // reCAPTCHA verification attempted with null Activity
                }
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d("TAG", "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            resendToken = token

            startOTPInput()
        }
    }

    override fun onStart() {
        super.onStart()

        if (FirebaseAuth.getInstance().currentUser != null) {
            startMainActivity()
        } else if (isVerificationInProgress) {
            val phoneNumber = findViewById<EditText>(R.id.tenDigit).text.toString()
            startPhoneNumberVerification(phoneNumber)
        }
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")

                    startMainActivity()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Sign in Failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(
                            applicationContext,
                            "Code invalid, Please try again."
                            ,Toast.LENGTH_SHORT
                        ).show()
                    }
                    // Update UI
                }
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("verification_in_progress", isVerificationInProgress)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        isVerificationInProgress = savedInstanceState.getBoolean("verification_in_progress", false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page_phoneno)

        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        

        val getOTP = findViewById<ImageButton>(R.id.get_otp_button)


        getOTP.setOnClickListener {
            val phoneNo = findViewById<EditText>(R.id.tenDigit).text.toString()
            Log.v("Phone No", phoneNo)
            isVerificationInProgress = true
            startPhoneNumberVerification(phoneNo)
        }
    }



    private fun startOTPInput() {
        setContentView(R.layout.otp_input)
        digit1 = findViewById(R.id.digit1)
        digit2 = findViewById(R.id.digit2)
        digit3 = findViewById(R.id.digit3)
        digit4 = findViewById(R.id.digit4)
        digit5 = findViewById(R.id.digit5)
        digit6 = findViewById(R.id.digit6)

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
                if (s?.length == 1) {
                    digit6.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        digit6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val otp = "${digit1.text}${digit2.text}${digit3.text}${digit4.text}${digit5.text}${digit6.text}"
                if (otp.length == 6) {
                    val credential = PhoneAuthProvider.getCredential(storedVerificationId, otp)
                    signInWithPhoneAuthCredential(credential)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun useEmailInsteadClick(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}


