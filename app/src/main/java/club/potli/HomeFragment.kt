package club.potli

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import androidx.core.content.getSystemService
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide

class HomeFragment : Fragment() {
    private lateinit var viewModel: AppViewModel
    private var isEditMode : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =  inflater.inflate(R.layout.fragment_home, container, false)

        val foodPotliImg : ImageView = rootView.findViewById(R.id.food_potli_img)
        val rentPotliImg : ImageView = rootView.findViewById(R.id.rent_potli_img)
        val travelPotliImg : ImageView = rootView.findViewById(R.id.travel_potli_img)
        val addPotliImg : ImageView = rootView.findViewById(R.id.add_potli_img)

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

        viewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]


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
                Log.v("OnClick", "${viewModel.savedMonthlyBal}")
            }
        }
    }
}