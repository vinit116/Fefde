package club.potli

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import club.potli.util.Constants.APP_ID
import com.google.android.material.textfield.TextInputEditText
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import kotlinx.coroutines.*


class LoginActivity : AppCompatActivity() {

    private val app = App.create(APP_ID)
    private val user = app.currentUser

    private lateinit var userName : String
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page_email)

        actionBar?.hide()

        userName = intent.getStringExtra("USER_NAME").toString()

        val rememberMeCheckbox = findViewById<CheckBox>(R.id.checkbox_remember_me)

        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE)


        if (user != null && sharedPreferences.getBoolean("rememberUser", false)) {
            startMainActivity()
        } else {
            val loginButton = findViewById<ImageButton>(R.id.login_button)

            loginButton.setOnClickListener {
                val emailID = findViewById<EditText>(R.id.emailId).text.toString()
                val password = findViewById<TextInputEditText>(R.id.password_text).text.toString()
                if (emailID.isNotBlank() && password.isNotBlank()) {
                    val credentials = Credentials.emailPassword(emailID, password)

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val user = app.login(credentials)
                            Log.v("User", "Logged in Successfully")
                            withContext(Dispatchers.Main) {
                                if (rememberMeCheckbox.isChecked) {
                                    Log.v("Checkbox" , "Checked")
                                    saveRememberMePreference(true)
                                } else {
                                    saveRememberMePreference(false)
                                }
                                startMainActivity()
                            }
                        } catch (e: Exception) {
                            Log.e("User", "Login failed: ${e.message}")
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    applicationContext,
                                    "Email or password is incorrect. Please try again.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
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
        intent.putExtra("USER_NAME", userName)
        startActivity(intent)
        finish()
    }

    fun createNewAccountClick(view : View) {
        val intent = Intent(this, CreateNewAccount::class.java)
        startActivity(intent)
    }
}
