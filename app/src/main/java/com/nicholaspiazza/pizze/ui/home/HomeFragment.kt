package com.nicholaspiazza.pizze.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.nicholaspiazza.pizze.R
import com.nicholaspiazza.pizze.SignUp
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private val TAG = "HomeFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        val button: Button = root.findViewById(R.id.temporary_log_out)
        button.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Log.d(TAG, "onCreateView: user signed out")
            val intent = Intent(this.context, SignUp::class.java)
            startActivity(intent)
        }
        return root
    }
}