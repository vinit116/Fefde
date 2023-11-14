package club.potli

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import club.potli.util.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val app = App.create(Constants.APP_ID)
    private val user = app.currentUser

    private lateinit var viewModel: AppViewModel

    companion object {
        private const val SMS_PERMISSIONS_CODE = 69
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_container)


        if (!Settings.canDrawOverlays(this)){
            requestOverlayPermission()
        }


        viewModel = ViewModelProvider(this)[AppViewModel::class.java]


        val readSmsPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS)
        val receiveSmsPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS)
        val profileBtn = findViewById<FrameLayout>(R.id.profile_photo_container)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        if (readSmsPermission == PackageManager.PERMISSION_GRANTED && receiveSmsPermission == PackageManager.PERMISSION_GRANTED){
            loadFragment(HomeFragment())
            SmsBackgroundService()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_SMS, android.Manifest.permission.RECEIVE_SMS),
                SMS_PERMISSIONS_CODE
            )
        }

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_rewards -> {
                    loadFragment(RewardsFragment())
                    true
                }
                R.id.navigation_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.navigation_add_potli -> {
                    loadFragment(AddPotliFragment())
                    true
                }
                R.id.navigation_contact_us -> {
                    loadFragment(ContactUsFragment())
                    true
                }
                else -> {
                    false
                }
            }
        }

        profileBtn.setOnClickListener {
            logOut()
            startLoginActivity()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            SMS_PERMISSIONS_CODE -> {
                if (grantResults.size == 2 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    loadFragment(HomeFragment())
                    SmsBackgroundService()
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.READ_SMS, android.Manifest.permission.RECEIVE_SMS),
                        SMS_PERMISSIONS_CODE
                    )
                }
            }
        }
    }

    private fun requestOverlayPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )

        val activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                loadFragment(HomeFragment())
            }
        }
        activityResultLauncher.launch(intent)
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.layout_container, fragment).commit()
    }

    private fun logOut() {
        CoroutineScope(Dispatchers.IO).launch {
            user?.logOut()
        }
        Firebase.auth.signOut()
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}