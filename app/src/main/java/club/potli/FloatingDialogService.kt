package club.potli

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide

class FloatingDialogService : Service() {

    private var floatingView: RelativeLayout? = null
    private var windowManager: WindowManager? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showFloatingView()
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



        val foodPotliImg : ImageView? = floatingView?.findViewById<ImageView>(R.id.food_potli_img)

        if (foodPotliImg != null) {
            Glide.with(this)
                .load(R.drawable.food_potli)
                .into(foodPotliImg)
        }

        val rentPotliImg : ImageView? = floatingView?.findViewById<ImageView>(R.id.rent_potli_img)

        if (rentPotliImg != null) {
            Glide.with(this)
                .load(R.drawable.rent_potli)
                .into(rentPotliImg)
        }
        val travelPotliImg : ImageView? = floatingView?.findViewById<ImageView>(R.id.travel_potli_img)

        if (travelPotliImg != null) {
            Glide.with(this)
                .load(R.drawable.travel_potli)
                .into(travelPotliImg)
        }
        val addPotliImg : ImageView? = floatingView?.findViewById<ImageView>(R.id.add_potli_img)

        if (addPotliImg != null) {
            Glide.with(this)
                .load(R.drawable.baseline_add_24)
                .into(addPotliImg)
        }

        foodPotliImg?.setOnClickListener{
            Log.v("Click","Yes")
            if (floatingView != null) {
                windowManager?.removeView(floatingView)
                floatingView = null
            }
        }
        rentPotliImg?.setOnClickListener{
            Log.v("Click","Yes")
            if (floatingView != null) {
                windowManager?.removeView(floatingView)
                floatingView = null
            }
        }
        travelPotliImg?.setOnClickListener{
            Log.v("Click","Yes")
            if (floatingView != null) {
                windowManager?.removeView(floatingView)
                floatingView = null
            }
        }
        addPotliImg?.setOnClickListener{
            Log.v("Click","Yes")
            if (floatingView != null) {
                windowManager?.removeView(floatingView)
                floatingView = null
            }
        }
    }

    private fun stopFloatingDialogService() {
        val closeDialogIntent = Intent(this, FloatingDialogService::class.java)
        stopService(closeDialogIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (floatingView != null) {
            windowManager?.removeView(floatingView)
            floatingView = null
        }
        stopFloatingDialogService()
    }
}