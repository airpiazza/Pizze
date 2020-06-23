package com.nicholaspiazza.pizze

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        signInLink()
        onStart()



        signing_up_button.setOnClickListener {
            Snackbar.make(constraint_layout_sign_up, "first name: ${first_name_input.text.toString()}", Snackbar.LENGTH_LONG).show()
        }
    }


    private fun signInLink() {
        sign_in_here_button.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
    }

    public override fun onStart() {
        super.onStart()
        val cUser = auth.currentUser
        updateUI(cUser)
    }

    private fun updateUI(cUser: FirebaseUser?) {
        if(cUser != null){
            val intent = Intent(this, LoggedIn::class.java)
            startActivity(intent)
        }
        else{
            Snackbar.make(constraint_layout_sign_up, R.string.you_have_not_signed_in, Snackbar.LENGTH_LONG).show()
        }
    }


}