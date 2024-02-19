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
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import club.potli.data.model.Potli
import club.potli.data.model.User
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FloatingDialogService : Service(){
    private var floatingView: RelativeLayout? = null
    private var windowManager: WindowManager? = null

    private var amount: Double = 0.0

    private val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

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
            onImageClick(amount, R.id.food_potli_layout)
            Log.v("Food","Clicked")
            Log.v("Amount","$amount")
        }
        rentPotliImg?.setOnClickListener{
            onImageClick(amount, R.id.rent_potli_layout)
        }
        travelPotliImg?.setOnClickListener{
            onImageClick(amount, R.id.travel_potli_layout)
        }
        addPotliImg?.setOnClickListener{
            Toast.makeText(this,"Coming Soon!!",Toast.LENGTH_SHORT).show()
            stopSelf()
        }
    }

    private fun onImageClick(amount: Double, potliLayoutId: Int) {
        val potliName = getPotliNameForLayoutId(potliLayoutId)

        val databaseReference = FirebaseDatabase.getInstance().reference.child("users").child(uid)

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue(User::class.java)

                    val potli = user?.potlis?.get(potliName)
                    potli?.let {
                        it.spent = it.spent?.plus(amount)
                        user.potlis[potliName] = it

                        // Save updated user data to the database
                        dataSnapshot.ref.setValue(user)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(applicationContext,"You spent Rs. $amount on $potliName ",Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(applicationContext,"Error!!",Toast.LENGTH_SHORT).show()
                                }
                            }
                    } ?: run {
                        val newPotli = Potli()
                        newPotli.limit = 0.0
                        newPotli.spent = amount
                        user?.potlis?.put(potliName, newPotli)

                        // Save updated user data to the database
                        dataSnapshot.ref.setValue(user)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(applicationContext,"Initialized and spent Rs. $amount on $potliName ",Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(applicationContext,"Error!!",Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle onCancelled
            }
        })

        stopFloatingView()
    }

    private fun stopFloatingView() {
        if (floatingView != null) {
            windowManager?.removeView(floatingView)
            floatingView = null
        }
        stopSelf()
    }
    private fun getPotliNameForLayoutId(layoutId: Int): String {
        return when (layoutId) {
            R.id.food_potli_layout -> "Food"
            R.id.rent_potli_layout -> "Rent"
            R.id.travel_potli_layout -> "Travel"
            R.id.add_potli_layout -> "Add"
            else -> ""
        }
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