package club.potli

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import club.potli.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AppViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?>
        get() = _user

    fun fetchUserDataFromFirebase(userId: String) {

        val userRef = database.getReference("users").child(userId)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                _user.postValue(user)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Fetching","Error")
            }
        })
    }
    fun createUserWithEmailAndPassword(email: String, password: String): LiveData<Boolean> {
        val resultLiveData = MutableLiveData<Boolean>()
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    resultLiveData.postValue(true)
                } else {
                    resultLiveData.postValue(false)
                }
            }
        return resultLiveData
    }

    fun saveUserToDatabase(userId: String, user: User) {
        val userRef = database.getReference("users").child(userId)
        userRef.setValue(user)
    }
}