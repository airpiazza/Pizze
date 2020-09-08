package com.nicholaspiazza.pizze.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.nicholaspiazza.pizze.OurPosts
import com.nicholaspiazza.pizze.R
import com.nicholaspiazza.pizze.Users
import com.nicholaspiazza.pizze.ui.dashboard.DashboardFragment
import com.nicholaspiazza.pizze.ui.dashboard.DashboardViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_notifications.*

class NotificationsFragment() : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private val TAG = "NotificationsFragment"

//    private lateinit var recyclerView: RecyclerView

//    private lateinit var theViewAdapter: RecyclerView.Adapter<*>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        val searchButton: ImageButton = root.findViewById(R.id.search_button)
        searchButton.setOnClickListener {
            Log.d(TAG, "onCreateView: search button clicked")
            val searchInput = search_input.text.toString()
            Log.d(TAG, "onCreateView: $searchInput")
            val dbRefQuery: Query = FirebaseDatabase.getInstance().getReference("/users").orderByChild("fullName").equalTo(searchInput)
            val theOptions: FirebaseRecyclerOptions<Users> = FirebaseRecyclerOptions.Builder<Users>().setQuery(dbRefQuery, Users::class.java).build()
            val viewAdapter = object : FirebaseRecyclerAdapter<Users, ViewHolder>(theOptions){
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                    val theView = LayoutInflater.from(parent.context).inflate(R.layout.search_layout, parent, false)
                    return ViewHolder(theView)
                }

                override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Users) {
                    Log.d(TAG, "onBindViewHolder: ${model.fullName}")
                    holder.names.text = model.fullName
                }
            }
//            recyclerView.adapter = viewAdapter

            Log.d("DashboardFragment", "onCreateView: $recycler_view")
            Log.d(TAG, "onCreateView: $viewAdapter")
//            theViewAdapter = viewAdapter
            Log.d(TAG, "onCreateView: $search_recycle")
            search_recycle.adapter = viewAdapter

            viewAdapter.startListening()

        }
        return root
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var names: TextView = itemView.findViewById(R.id.names)


    }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        Log.d("DashboardFragment", "onViewCreated: $recycler_view")
//          search_recycle.adapter = theViewAdapter
//        Log.d(TAG, "onViewCreated: ${profile_recycler_view.top}")


//    }
}