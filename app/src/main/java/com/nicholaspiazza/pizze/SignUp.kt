package com.nicholaspiazza.pizze

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlin.math.sign


class SignUp : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val TAG = "SignUp"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()
        onStart()

        signInLink()

        sign_in_button.setOnClickListener {
            var email = sign_in_email.text.toString()
            var password = sign_in_password.text.toString()
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if(it.isSuccessful) {
                    Log.d(TAG, "onCreate: sign in worked")
                    val theUser = auth.currentUser
                    updateUI(theUser)
                }else{
                    Log.w(TAG, "onCreate: signInWithEmail: failed", it.exception)
                    Snackbar.make(signing_in_sign_up_activity_constraint_layout, "sorry bro the signing in didn't work", Snackbar.LENGTH_LONG).show()
                    updateUI(null)
                }
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        val cUser = auth.currentUser
        if (cUser != null) {
            Log.d(TAG, "onStart: firebase user object ${cUser.uid} created from firebase auth object, now being passed to updateUI function ")
        }
        updateUI(cUser)
    }

    private fun updateUI(cUser: FirebaseUser?) {
        if(cUser != null){
            val intent = Intent(this, LoggedIn::class.java)
            startActivity(intent)
            Log.d(TAG, "updateUI: current user object existed, logged in")
        }
        else{
            Snackbar.make(signing_in_sign_up_activity_constraint_layout, R.string.you_have_not_signed_in, Snackbar.LENGTH_LONG).show()
            Log.d(TAG, "updateUI: current user was not logged in")
        }
        Log.d(TAG, "updateUI: update UI function ran")
    }

    private fun signInLink() {
        go_to_register.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            Log.d(TAG, "signInLink: user clicked register link")
        }
    }

}