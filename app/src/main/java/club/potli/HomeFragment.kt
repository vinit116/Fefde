package club.potli

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import club.potli.data.model.Potli
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
        val rootView = inflater.inflate(R.layout.fragment_home1, container, false)

        foodPotliImg = rootView.findViewById(R.id.food_potli_img)
        rentPotliImg = rootView.findViewById(R.id.rent_potli_img)
        travelPotliImg = rootView.findViewById(R.id.travel_potli_img)
        addPotliImg= rootView.findViewById(R.id.other_potli_img)

        Glide.with(this)
            .load(R.drawable.food_pot)
            .into(foodPotliImg)

        Glide.with(this)
            .load(R.drawable.rent_pot)
            .into(rentPotliImg)

        Glide.with(this)
            .load(R.drawable.travel_pot)
            .into(travelPotliImg)

        Glide.with(this)
            .load(R.drawable.others_pot)
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

            val potlis = user?.potlis

            var totalSpent = 0.0
            potlis?.forEach { (_, potli) ->
                potli.spent?.let { spent ->
                    totalSpent += spent
                }
            }

            val spentTillNowTextView: TextView = view.findViewById(R.id.spentrs)
            spentTillNowTextView.text = totalSpent.toString()

            potlis?.let {
                calculateAndUpdatePotliBalance("Food", it["Food"],  view.findViewById(R.id.food_bal_text))
                calculateAndUpdatePotliBalance("Rent", it["Rent"],  view.findViewById(R.id.rent_bal_text))
                calculateAndUpdatePotliBalance("Travel", it["Travel"],  view.findViewById(R.id.travel_bal_text))
            }

        }

        viewModel.fetchUserDataFromFirebase(uid)

        val updateBalanceButton : FrameLayout = view.findViewById(R.id.update_balance_button)


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

    private fun calculateAndUpdatePotliBalance(potliName: String, potliData: Potli?, potliBalanceTextView: TextView) {
        potliData?.let { potli ->
            val limit = potli.limit ?: 0.0
            val spent = potli.spent ?: 0.0
            val remainingBalance = limit - spent
            potliBalanceTextView.text = remainingBalance.toString()
            if (remainingBalance < 0) {
                potliBalanceTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.warning))
            }
        }
    }

    private fun openDialogWithName(potliName: String) {
        uid.let { DialogInsideFragment.newInstance(potliName, it) }
            .show(parentFragmentManager, "DialogInsideFragment")
    }
}