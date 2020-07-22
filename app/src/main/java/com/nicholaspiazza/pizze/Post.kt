package com.nicholaspiazza.pizze

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Post : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        val goBackButton = findViewById<Button>(R.id.goback)
        goBackButton.setOnClickListener {
            finish()
        }
    }
}