package com.nicholaspiazza.pizze

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReferenceObject: DatabaseReference
    private var userFirstNameInput = false
    private var userLastNameInput = false
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        FirebaseAuth.getInstance().signOut()
        auth = FirebaseAuth.getInstance()
        signInLink()
        onStart()
        registeringUserWithSignUpButton()
    }

    private fun registeringUserWithSignUpButton() {
        signing_up_button.setOnClickListener {
            var firstName: String = gettingFirstNameInput()
            var lastName: String = gettingLastNameInput()
            var email: String = gettingEmailInput()
            var password: String = gettingPasswordInput()
            Log.d(TAG, "onCreate: firstname and lastname:${firstName}&${lastName}.")
            Log.d(TAG, "onCreate: email and password${email}&${password}")
            if ((email != "") && (password != "")) {
                if (email.contains("@") && email.contains(".")) {
                    if ((firstName != "") && (lastName != "")) {
                        Log.d(TAG, "onCreate: first name and last name are put in")
                        Log.d(TAG, "onCreate: user will now be created")
                        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                            if (it.isSuccessful) {
                                Log.d(
                                    TAG,
                                    "onCreate: user created and their uid is ${auth.uid.toString()}"
                                )
                                puttingUserInfoInDatabase(firstName, lastName, auth.uid.toString())
                                val theRegisteredUser = auth.currentUser
                                updateUI(theRegisteredUser)
                            } else {
                                Log.w(TAG, "onCreate: user on created", it.exception)
                                Snackbar.make(
                                    constraint_layout_sign_up,
                                    "my bad, the sign up didn't work",
                                    Snackbar.LENGTH_LONG
                                ).show()
                                updateUI(null)
                            }
                        }
                    } else {
                        Log.d(TAG, "onCreate: first and/or last name are not put in")
                        Snackbar.make(
                            constraint_layout_sign_up,
                            "Hold up, make sure you put in your first and last name",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Log.d(TAG, "onCreate: email did not contain @ or .")
                    Snackbar.make(
                        constraint_layout_sign_up,
                        "not a valid email",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            } else {
                Log.d(TAG, "onCreate: email and password not put in")
                Snackbar.make(
                    constraint_layout_sign_up,
                    "sorry bro, you have to put an email and password",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun gettingPasswordInput(): String {
        var password: String = password_input.text.toString()
        return password
    }

    private fun gettingEmailInput(): String {
        var email: String = email_input.text.toString().trim()
        return email
    }

    private fun gettingLastNameInput(): String {
        var lastName: String = last_name_input.text.toString().trim()
        return lastName
    }

    private fun gettingFirstNameInput(): String {
        var firstName: String = first_name_input.text.toString().trim()
        return firstName
    }

    private fun signInLink() {
        sign_in_here_button.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            Log.d(TAG, "signInLink: user clicked sign in link")
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
            Snackbar.make(constraint_layout_sign_up, R.string.you_have_not_signed_in, Snackbar.LENGTH_LONG).show()
            Log.d(TAG, "updateUI: current user was not logged in")
        }
        Log.d(TAG, "updateUI: update UI function ran")
    }

    private fun puttingUserInfoInDatabase(f: String, l: String, u: String){
        databaseReferenceObject = FirebaseDatabase.getInstance().reference
        val theNewestUser = RegisteredUser(u, f, l)
        databaseReferenceObject.child("users").child(u).setValue(theNewestUser).addOnSuccessListener {
            Log.d(TAG, "puttingUserInfoInDatabase: user is now in database")
        }
    }
}

data class RegisteredUser(var theUID: String, var userFirstName: String, var lastName: String)