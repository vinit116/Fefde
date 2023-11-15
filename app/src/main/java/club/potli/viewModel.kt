package club.potli

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AppViewModel : ViewModel() {
    var savedMonthlyBal : String = "0"
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    fun fetchUserName(uid: String?) {
        val usersRef = FirebaseDatabase.getInstance().getReference("users")
        uid?.let {
            usersRef.child(it).child("username").addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val username = dataSnapshot.value.toString()
                    _userName.postValue(username)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d(
                        "Error",
                        "Error getting username from Realtime Database",
                        databaseError.toException()
                    )
                }
            })
        }
    }
}