package club.potli

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_container)

        val homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.layout_container, homeFragment).commit()
    }
}