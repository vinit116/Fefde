package club.potli

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide

class HomeFragment : Fragment() {

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
}