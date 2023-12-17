package club.potli

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import club.potli.data.model.Potli
import club.potli.data.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DialogInsideFragment : DialogFragment() {
    companion object {
        private const val ARG_POTLI_NAME = "arg_potli_name"
        private const val ARG_USER_ID = "arg_user_id"

        fun newInstance(potliName: String, userId: String): DialogInsideFragment {
            val fragment = DialogInsideFragment()
            val args = Bundle()
            args.putString(ARG_POTLI_NAME, potliName)
            args.putSerializable(ARG_USER_ID, userId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.dialog_box_inside, container, false)
        val potliNameTextView: TextView = rootView.findViewById(R.id.active_potli_name)
        val limitEditText: EditText = rootView.findViewById(R.id.limit_amt)
        val spentTextView: TextView = rootView.findViewById(R.id.spent_amt)

        val userId = arguments?.getString(ARG_USER_ID)
        val potliName = arguments?.getString(ARG_POTLI_NAME)


        userId?.let { uid ->
            FirebaseDatabase.getInstance().reference.child("users").child(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val user = dataSnapshot.getValue(User::class.java)

                            potliNameTextView.text = potliName

                            val potliRef = dataSnapshot.child("potlis").child(potliName!!)
                            val limit = potliRef.child("limit").getValue(Double::class.java)
                            val spent = potliRef.child("spent").getValue(Double::class.java)

                            limitEditText.setText(limit?.toString())
                            spentTextView.text = spent?.toString()

                            limitEditText.addTextChangedListener(object : TextWatcher {
                                override fun beforeTextChanged(
                                    s: CharSequence?,
                                    start: Int,
                                    count: Int,
                                    after: Int
                                ) {
                                }

                                override fun onTextChanged(
                                    s: CharSequence?,
                                    start: Int,
                                    before: Int,
                                    count: Int
                                ) {
                                }

                                override fun afterTextChanged(s: Editable?) {
                                    val enteredLimit = s.toString().toDoubleOrNull()
                                    enteredLimit?.let { limit ->
                                        potliName.let { name ->
                                            val potli = Potli()
                                            potli.limit = limit
                                            potli.spent = 0.0

                                            userId.let { uid ->
                                                FirebaseDatabase.getInstance().reference.child("users")
                                                    .child(uid)
                                                    .addListenerForSingleValueEvent(object :
                                                        ValueEventListener {
                                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                            if (dataSnapshot.exists()) {

                                                                user?.let {
                                                                    it.potlis[name] = potli
                                                                    FirebaseDatabase.getInstance().reference.child(
                                                                        "users"
                                                                    )
                                                                        .child(uid).setValue(user)
                                                                }
                                                            }
                                                        }

                                                        override fun onCancelled(databaseError: DatabaseError) {
                                                            // Handle onCancelled
                                                        }
                                                    })
                                            }
                                        }
                                    }
                                }
                            })
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle onCancelled
                    }
                })
        }
        return rootView
    }
}