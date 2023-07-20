package club.potli

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import club.potli.data.model.User
import com.google.android.material.textfield.TextInputEditText
import io.realm.kotlin.Realm
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.BaseRealm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {

    val app : App = App.create("potli-hlxas")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        val config = RealmConfiguration.Builder(schema = setOf(User :: class))
            .build()

        val createNewAccount = findViewById<TextView>(R.id.create_new_acc)
        createNewAccount.setOnClickListener{
            setContentView(R.layout.create_account)

            val logInHere = findViewById<TextView>(R.id.create_acc_log_in_here)
            logInHere.setOnClickListener{
                setContentView(R.layout.login_page)
            }

            val userEmail = findViewById<EditText>(R.id.user_email).text.toString()
            val userPassword = findViewById<EditText>(R.id.user_password).text.toString()
            val signUp = findViewById<ImageButton>(R.id.signUp)

            if (userEmail.isNotEmpty() && userPassword.isNotEmpty()) {
                signUp.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        app.emailPasswordAuth.registerUser(userEmail, userPassword)
                    }
                }
            }
        }

        val emailEditText = findViewById<EditText>(R.id.emailId)
//        val passwordTextInputEditText = findViewById<TextInputEditText>(R.id.password_text)


        emailEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if (emailEditText.text.isNotEmpty()) {
                    emailEditText.hint = ""
                }
            } else {
                if (emailEditText.text.isEmpty()) {
                    emailEditText.hint= "Enter your Email Address here"
                }
            }
        }

        val loginButton = findViewById<ImageButton>(R.id.login_button)
        loginButton.setOnClickListener{
//            val email = emailEditText.text.toString()
//            val password = passwordTextInputEditText.text.toString()


        }

    }
}