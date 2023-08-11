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
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.*

class CreateNewAccount : AppCompatActivity() {

    val app: App = App.create(APP_ID)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_account)


        val signUp = findViewById<ImageButton>(R.id.signUp)

        signUp.setOnClickListener {
            val userEmail = findViewById<EditText>(R.id.user_email).text.toString()
            val userPassword = findViewById<EditText>(R.id.user_password).text.toString()
            if (userEmail.isNotBlank() && userPassword.isNotBlank()){
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        app.emailPasswordAuth.registerUser(userEmail,userPassword)
                        Log.v("User", "User created Successfully")
                        startLoginActivity()
                    } catch (e: Exception) {
                        Log.e("User", "User creation failed: ${e.message}")
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                applicationContext,
                                "Email already used.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            else {
                Toast.makeText(
                    applicationContext,
                    "Please enter all the details",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun logInHereClick(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}