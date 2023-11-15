package club.potli

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

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

        signUp.setOnClickListener {

            userName = findViewById<EditText>(R.id.user_name).text.toString()

            val userEmail = findViewById<EditText>(R.id.user_email).text.toString()
            val userPassword = findViewById<EditText>(R.id.user_password).text.toString()
            if (userEmail.isNotBlank() && userPassword.isNotBlank() && userName.isNotBlank()) {
                auth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // User creation success
                            Log.d("User", "Created Successfully")
                            val firebaseUser = auth.currentUser
                            val uid = firebaseUser?.uid
                            Log.v("uid","$uid")
                            val database = FirebaseDatabase.getInstance()
                            val usersRef = database.getReference("users")
                            uid?.let {
                                usersRef.child(it).child("username").setValue(userName)
                                    .addOnSuccessListener {
                                        Log.d("Username", "Saved in Realtime Database!")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w("Username", "Error writing to Realtime Database", e)
                                    }
                            }
                            startLoginActivity()
                        } else {
                            // User creation failed
                            Log.w("User", "createUserWithEmail:failure", task.exception)
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