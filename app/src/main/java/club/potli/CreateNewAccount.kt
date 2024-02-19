package club.potli

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import club.potli.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateNewAccount : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var userName : String

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_account)

        auth = Firebase.auth


        val signUp = findViewById<ImageButton>(R.id.signUp)

        val dobTextView = findViewById<TextView>(R.id.dob)

        dobTextView.setOnClickListener {

            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate = "$selectedDay-${selectedMonth + 1}-$selectedYear"
                    dobTextView.text = formattedDate
                },
                year,
                month,
                day
            )

            datePickerDialog.show()

        }

        signUp.setOnClickListener {

            userName = findViewById<EditText>(R.id.user_name).text.toString()

            val userEmail = findViewById<EditText>(R.id.user_email).text.toString()
            val userPassword = findViewById<EditText>(R.id.user_password).text.toString()


            if (userEmail.isNotBlank() && userPassword.isNotBlank() && userName.isNotBlank() && dobTextView.text.toString().isNotBlank()) {
                val viewModel = AppViewModel()

                viewModel.createUserWithEmailAndPassword(userEmail, userPassword)
                    .observe(this) { isSuccess ->
                        if (isSuccess) {
                            val firebaseUser = auth.currentUser
                            val uid = firebaseUser?.uid
                            val database = FirebaseDatabase.getInstance()
                            uid?.let {
                                val newUser = User().apply {
                                    username = userName
                                    email = userEmail
                                    dateOfBirth = dobTextView.text.toString()
                                }
                                viewModel.saveUserToDatabase(uid, newUser)
                            }
                            startLoginActivity()
                        } else {
                            Log.w("User", "createUserWithEmail:failure")
                            Toast.makeText(
                                applicationContext,
                                "User creation failed. Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
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

    fun logInHereClick(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}