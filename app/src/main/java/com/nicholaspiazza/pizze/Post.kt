package com.nicholaspiazza.pizze

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.ImageDecoder.Source
import android.media.Image
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_post.*

class Post : AppCompatActivity() {
    private val TAG = "Post"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        val goBackButton = findViewById<Button>(R.id.goback)
        goBackButton.setOnClickListener {
            finish()
        }
        add_image.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, 0)
            Log.d(TAG, "onCreate: clicked select image button")
        }
    }

//    val photoUri: Uri? = null
//    @RequiresApi(Build.VERSION_CODES.P)
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
//            val photoUri = data.data
//            if (photoUri != null) {
//                ImageDecoder.createSource(contentResolver, photoUri)
//            }
//            Log.d(TAG, "onActivityResult: got data ${photoUri}")
//
//
//        }
//    }
}