package com.nicholaspiazza.pizze

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.ImageDecoder.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.ScaleDrawable
import android.media.Image
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.core.graphics.decodeBitmap
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.nicholaspiazza.pizze.R.drawable.other_add_image
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_post.*
import java.net.URI
import java.security.acl.LastOwnerException
import java.text.SimpleDateFormat
import java.util.*

class Post : AppCompatActivity() {
    private val TAG = "Post"
    val photoUri: Uri? = null
    private lateinit var stringURI: String
    private lateinit var theAuth: FirebaseAuth
    private lateinit var lN : String
    private lateinit var fN: String
    private val postStorage = FirebaseStorage.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        val imageCheck = add_image.drawable.toString()
        val goBackButton = findViewById<Button>(R.id.goback)


        goBackButton.setOnClickListener {
            finish()
        }

        theAuth = FirebaseAuth.getInstance()

        Log.d(TAG, "onCreate: uid is ${theAuth.currentUser?.uid ?: ""}")

        add_image.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, 0)
            Log.d(TAG, "onCreate: clicked select image button")
        }

        post_post.setOnClickListener {
            if(add_image.drawable.toString() == imageCheck)
            {
                Snackbar.make(post,
                    "sorry, you have to select a picture first",
                    Snackbar.LENGTH_LONG
                ).show()
                Log.d(TAG, "onCreate: no picture uploaded when selected post")
            }
            else{
                Log.d(TAG, "onCreate: ${stringURI}")
                val postUID = UUID.randomUUID().toString()
                val storageReference = postStorage.getReference("posted_images/$postUID")
                storageReference.putFile(Uri.parse(stringURI)).addOnSuccessListener {
                    Log.d(TAG, "onCreate: success $it")
                    storageReference.downloadUrl.addOnSuccessListener {
                        Log.d(TAG, "onCreate: the url is $it")
                        val postURL = it.toString()
                        val whatTheCaptionIs: String = post_text_input.text.toString()
                        val uidOfUser = theAuth.currentUser?.uid.toString()
                        Log.d(TAG, "onCreate: ${whatTheCaptionIs + uidOfUser}")
                        val dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(uidOfUser)
                        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                lN = snapshot.child("lastName").getValue().toString()
                                fN = snapshot.child("userFirstName").getValue().toString()
                                puttingPostInfoInDatabase(firstName = fN, lastName = lN, caption = whatTheCaptionIs, aPostUrl = postURL, aUserUid = uidOfUser)

                            }
                            override fun onCancelled(error: DatabaseError) {
                            }

                        })
                    }
                }

            }
        }
    }



    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            val photoUri = data.data
            if (photoUri != null) {
                val theImageSource = createSource(contentResolver, photoUri)
                val drawable = decodeDrawable(theImageSource)
                add_image.setImageDrawable(drawable)
                stringURI = photoUri.toString()
            }
        }


        Log.d(TAG, "onActivityResult: got data $stringURI")
    }

    private fun puttingPostInfoInDatabase(caption: String, lastName: String, firstName: String, aPostUrl: String, aUserUid: String){
        val databaseReferenceObject= FirebaseDatabase.getInstance().reference
        val dateFormat = SimpleDateFormat("MM'-'dd'-'yyyy' 'HH':'mm':'ss", Locale.US)
        val date = Date()
        val currentDate: String = dateFormat.format(date)
        val theNewestPost = PostInfo(caption, lastName, firstName, aPostUrl, aUserUid, currentDate)
        databaseReferenceObject.child("posts").child("$currentDate $fN $lN").setValue(theNewestPost).addOnSuccessListener {
            Log.d(TAG, "post is now in database")
//            databaseReferenceObject.child("users").child(theAuth.currentUser?.uid.toString()).child("posts")
//                .child("$fN $lN $currentDate").setValue(theNewestPost)
            finish()
        }

    }
}

data class PostInfo(val theCaption: String, val theirLastName: String, val theirFirstName: String, val thePhotoUrl: String, val theUserUID: String, val timeAndDate: String)