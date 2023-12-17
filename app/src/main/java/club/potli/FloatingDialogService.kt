package club.potli

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide

class FloatingDialogService : Service(){
    private var floatingView: RelativeLayout? = null
    private var windowManager: WindowManager? = null

    private var amount: Double = 0.0

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val amountStr = intent?.getStringExtra("amount")
        amount = amountStr?.toDoubleOrNull() ?: 0.0
        Log.v("onStartCommand", "$amount")
        try {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivity(intent)
            } else {
                showFloatingView()
            }
        } catch (e: WindowManager.BadTokenException){
            e.printStackTrace()
        }

        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showFloatingView() {
        val inflater = LayoutInflater.from(this)
        floatingView = inflater.inflate(R.layout.dialog_box_outside, null) as RelativeLayout

        Log.v("Floating Window","Opened")

        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            android.graphics.PixelFormat.TRANSLUCENT
        )

        layoutParams.gravity = Gravity.CENTER
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager?.addView(floatingView, layoutParams)

        val foodPotliImg : ImageView? = floatingView?.findViewById(R.id.food_potli_img)
        val rentPotliImg : ImageView? = floatingView?.findViewById(R.id.rent_potli_img)
        val travelPotliImg : ImageView? = floatingView?.findViewById(R.id.travel_potli_img)
        val addPotliImg : ImageView? = floatingView?.findViewById(R.id.add_potli_img)

        if (foodPotliImg != null) {
            Glide.with(this)
                .load(R.drawable.food_potli)
                .into(foodPotliImg)
        }

        if (rentPotliImg != null) {
            Glide.with(this)
                .load(R.drawable.rent_potli)
                .into(rentPotliImg)
        }

        if (travelPotliImg != null) {
            Glide.with(this)
                .load(R.drawable.travel_potli)
                .into(travelPotliImg)
        }

        if (addPotliImg != null) {
            Glide.with(this)
                .load(R.drawable.baseline_add_24)
                .into(addPotliImg)
        }

        foodPotliImg?.setOnClickListener{
            Log.v("Click","Yes")
            onImageClick(amount, R.id.food_potli_img)
        }
        rentPotliImg?.setOnClickListener{
            Log.v("Click","Yes")
            onImageClick(amount, R.id.rent_potli_img)
        }
        travelPotliImg?.setOnClickListener{
            Log.v("Click","Yes")
            onImageClick(amount, R.id.travel_potli_img)
        }
        addPotliImg?.setOnClickListener{
            Log.v("Click","Yes")
            onImageClick(amount, R.id.add_potli_img)
        }
    }



    private fun onImageClick(amount: Double, imageId: Int) {
        if (floatingView != null) {
            windowManager?.removeView(floatingView)
            floatingView = null
        }
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v("Destroyed","Yes")
        if (floatingView != null) {
            windowManager?.removeView(floatingView)
            floatingView = null
        }
        stopSelf()
    }
}