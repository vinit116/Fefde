package club.potli

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        if (user != null) {
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