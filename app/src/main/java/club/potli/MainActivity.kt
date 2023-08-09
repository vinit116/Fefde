package club.potli

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    companion object {
        private const val SMS_PERMISSIONS_CODE = 69
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_container)


        val readSmsPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS)
        val receiveSmsPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS)

        if (readSmsPermission == PackageManager.PERMISSION_GRANTED && receiveSmsPermission == PackageManager.PERMISSION_GRANTED){
            startHomeFrag()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_SMS, android.Manifest.permission.RECEIVE_SMS),
                SMS_PERMISSIONS_CODE
            )
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
                    startHomeFrag()
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
    private fun startHomeFrag() {
        val homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.layout_container, homeFragment).commit()
    }
}