package club.potli

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment

interface DialogClickListener {
    fun onDialogButtonClick(amount: Double)
}

class DialogOutsideFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_box_inside, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val smsReceiver = SmsReceiver()

        val foodPotliLayout = view.findViewById<FrameLayout>(R.id.food_potli_layout)

    }

}