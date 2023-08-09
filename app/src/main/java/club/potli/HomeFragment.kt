package club.potli

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =  inflater.inflate(R.layout.fragment_home, container, false)

        val foodPotliImg : ImageView = rootView.findViewById(R.id.food_potli_img)

        Glide.with(this)
            .load(R.drawable.food_potli)
            .into(foodPotliImg)

        return rootView
    }
}