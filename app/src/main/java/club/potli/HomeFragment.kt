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

class HomeFragment : Fragment(), TransactionCallback {
    private lateinit var viewModel: AppViewModel
    private var isEditMode : Boolean = false
    private lateinit var auth: FirebaseAuth
    private lateinit var userNameTextView : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)


        viewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]



            val foodPotliImg: ImageView = rootView.findViewById(R.id.food_potli_img)
            val rentPotliImg: ImageView = rootView.findViewById(R.id.rent_potli_img)
            val travelPotliImg: ImageView = rootView.findViewById(R.id.travel_potli_img)
            val addPotliImg: ImageView = rootView.findViewById(R.id.add_potli_img)

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
        userNameTextView = view.findViewById(R.id.userName)

        viewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        viewModel.fetchUserName(uid)

        // Observe changes in username LiveData
        viewModel.userName.observe(viewLifecycleOwner) { username ->
            userNameTextView.text = username // Update UI here
        }


        val updateBalanceButton : ImageButton = view.findViewById(R.id.update_balance_button)
        val monthlyBalanceText : TextView = view.findViewById(R.id.monthly_bal_amount)

        monthlyBalanceText.text = viewModel.savedMonthlyBal

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
                viewModel.savedMonthlyBal = monthlyBalanceText.text.toString()
                Log.v("OnClick", viewModel.savedMonthlyBal)
            }
        }
    }

    private fun updateBalance(textViewId: Int, amount: Double) {
        val textView = view?.findViewById<TextView>(textViewId)
        textView?.let {
            val currentBalance = it.text.toString().toDouble()
            val newBalance = currentBalance - amount
            Log.v("Deduction Check", "$newBalance")
            it.text = newBalance.toString()
        }
    }

    override fun onTransactionAmountUpdated(amount: Double, imageId: Int) {
        when (imageId) {
            R.id.food_potli_img -> updateBalance(R.id.food_bal_text, amount)
            R.id.rent_potli_img -> updateBalance(R.id.rent_bal_text, amount)
            R.id.travel_potli_img -> updateBalance(R.id.travel_bal_text, amount)
            R.id.add_potli_img -> updateBalance(R.id.add_bal_text, amount)
        }
    }
}