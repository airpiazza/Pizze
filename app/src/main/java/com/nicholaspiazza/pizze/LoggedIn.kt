package com.nicholaspiazza.pizze

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import kotlinx.android.synthetic.main.activity_logged_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_home.*

class LoggedIn : AppCompatActivity() {

    private val TAG = "LoggedIn"
    private lateinit var theViewAdapter: RecyclerView.Adapter<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged_in)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.profile_nav, R.id.pizzas_nav, R.id.notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        supportActionBar?.hide()
    }

    public override fun onStart() {
        super.onStart()
        val cUser = FirebaseAuth.getInstance().currentUser
//        if (cUser != null) {
//            Log.d(TAG, "onStart: firebase user object ${cUser.uid} created from firebase auth object, now being passed to updateUI function ")
//        }
        updateUI(cUser)
        val postButton = post_button_id
        postButton.setOnClickListener {
            val intent = Intent(this, Post::class.java)
            startActivity(intent)
            Log.d(TAG, "onStart: clicked post button")
        }
    }

    private fun updateUI(cUser: FirebaseUser?) {
        if(cUser == null){
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            Log.d(TAG, "updateUI: user sent back to log in page")
        }
        Log.d(TAG, "updateUI: update UI function ran")
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var userName: TextView = itemView.findViewById(R.id.username_post)
        var postTime: TextView = itemView.findViewById(R.id.post_time)
        var caption: TextView = itemView.findViewById(R.id.caption)

        fun setUserName(f: String, l: String){
            userName.setText("$f $l")

        }
    }

}