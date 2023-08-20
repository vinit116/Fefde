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
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi

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
    }
    override fun onDestroy() {
        super.onDestroy()
        floatingView?.let {
            windowManager?.removeView(it)
            floatingView = null
        }
    }
}