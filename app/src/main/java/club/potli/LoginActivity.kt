package club.potli

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.internal.CheckableImageButton
import com.google.android.material.textfield.TextInputLayout


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        val emailEditText = findViewById<EditText>(R.id.emailId)
        emailEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if (emailEditText.text.isNotEmpty()) {
                    emailEditText.hint = ""
                }
            } else {
                if (emailEditText.text.isEmpty()) {
                    emailEditText.hint = "Enter your Email Address here"
                }
            }
        }

    }
}