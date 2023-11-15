package club.potli

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page_email)
        actionBar?.hide()

        auth = Firebase.auth
        val currentUser = auth.currentUser

        val rememberMeCheckbox = findViewById<CheckBox>(R.id.checkbox_remember_me)

        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE)


        if (currentUser != null && sharedPreferences.getBoolean("rememberUser", false)) {
            startMainActivity()
        } else {
            val loginButton = findViewById<ImageButton>(R.id.login_button)

            loginButton.setOnClickListener {
                val emailID = findViewById<EditText>(R.id.emailId).text.toString()
                val password = findViewById<TextInputEditText>(R.id.password_text).text.toString()
                if (emailID.isNotBlank() && password.isNotBlank()) {
                    auth.signInWithEmailAndPassword(emailID, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign-in success
                                Log.d("User", "Logged in Successfully")
                                val firebaseUser = auth.currentUser
                                if (rememberMeCheckbox.isChecked) {
                                    saveRememberMePreference(true)
                                } else {
                                    saveRememberMePreference(false)
                                }
                                startMainActivity()
                            } else {
                                // Sign-in failed
                                Log.e("User", "Login failed")
                                Toast.makeText(
                                    applicationContext,
                                    "Email or password is incorrect. Please try again.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Please enter Email/Password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    private fun saveRememberMePreference(remember: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("rememberUser", remember)
        editor.apply()
    }
    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun createNewAccountClick(view : View) {
        val intent = Intent(this, CreateNewAccount::class.java)
        startActivity(intent)
    }
}
