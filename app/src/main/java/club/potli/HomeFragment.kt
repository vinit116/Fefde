package club.potli

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class HomeFragment : Fragment(){
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: AppViewModel
    private lateinit var foodPotliImg : ImageView
    private lateinit var rentPotliImg : ImageView
    private lateinit var travelPotliImg : ImageView
    private lateinit var addPotliImg : ImageView
    private var isEditMode : Boolean = false
    private lateinit var userNameTextView : TextView
    private lateinit var uid : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        foodPotliImg = rootView.findViewById(R.id.food_potli_img)
        rentPotliImg = rootView.findViewById(R.id.rent_potli_img)
        travelPotliImg = rootView.findViewById(R.id.travel_potli_img)
        addPotliImg= rootView.findViewById(R.id.add_potli_img)

        Glide.with(this)
            .load(R.drawable.food_potli)
            .into(foodPotliImg)

        Glide.with(this)
            .load(R.drawable.rent_potli)
            .into(rentPotliImg)

        Glide.with(this)
            .load(R.drawable.travel_potli)
            .into(travelPotliImg)

        Glide.with(this)
            .load(R.drawable.baseline_add_24)
            .into(addPotliImg)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        val database = FirebaseDatabase.getInstance()


        viewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]
        userNameTextView = view.findViewById(R.id.userName)
        val monthlyBalanceText : TextView = view.findViewById(R.id.monthly_bal_amount)

        viewModel.user.observe(viewLifecycleOwner){ user ->
            val userName = user?.username ?: ""
            userNameTextView.text = userName
            val monthlyBalance = user?.monthlyBalance ?: "0"
            monthlyBalanceText.text = monthlyBalance
        }

        viewModel.fetchUserDataFromFirebase(uid)

        val updateBalanceButton : ImageButton = view.findViewById(R.id.update_balance_button)


        updateBalanceButton.setOnClickListener {
            isEditMode = !isEditMode

            if (isEditMode){
                monthlyBalanceText.isEnabled = true
                monthlyBalanceText.isFocusableInTouchMode = true
                monthlyBalanceText.requestFocus()
                Log.v("Focus", "Granted")

                val inputManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.showSoftInput(monthlyBalanceText,InputMethodManager.SHOW_IMPLICIT)
            } else {
                monthlyBalanceText.isEnabled = false
                monthlyBalanceText.isFocusableInTouchMode = false
                val monthlyBalance = monthlyBalanceText.text.toString()
                database.getReference("users").child(uid).child("monthlyBalance").setValue(monthlyBalance)
            }
        }

        foodPotliImg.setOnClickListener {
            openDialogWithName("Food")
        }

        rentPotliImg.setOnClickListener {
            openDialogWithName("Rent")
        }

        travelPotliImg.setOnClickListener {
            openDialogWithName("Travel")
        }

        addPotliImg.setOnClickListener {
            TODO("Implement adding a new potli and removing a previous one")
        }
    }

    private fun openDialogWithName(potliName: String) {
        uid.let { DialogInsideFragment.newInstance(potliName, it) }
            .show(parentFragmentManager, "DialogInsideFragment")
    }
}