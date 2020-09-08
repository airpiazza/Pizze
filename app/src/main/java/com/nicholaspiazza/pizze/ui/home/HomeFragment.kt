package com.nicholaspiazza.pizze.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.nicholaspiazza.pizze.OurPosts
import com.nicholaspiazza.pizze.R
import com.nicholaspiazza.pizze.SignUp
import com.nicholaspiazza.pizze.ui.dashboard.DashboardFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var fN: String
    private lateinit var lN: String

    private lateinit var theViewAdapter: RecyclerView.Adapter<*>

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

        val uidOfUser = FirebaseAuth.getInstance().currentUser?.uid.toString()

        val dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(uidOfUser)

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                fN = snapshot.child("userFirstName").getValue().toString() ?: ""
                lN = snapshot.child("lastName").getValue().toString() ?: ""
                user_name_profile.setText("$fN $lN")

            }
            override fun onCancelled(error: DatabaseError) {
            }

        })

        val dbRefQuery: Query = FirebaseDatabase.getInstance().getReference("/posts").limitToLast(100)
        val theOptions: FirebaseRecyclerOptions<OurPosts> = FirebaseRecyclerOptions.Builder<OurPosts>().setQuery(dbRefQuery, OurPosts::class.java).build()
        val viewAdapter = object : FirebaseRecyclerAdapter<OurPosts, HomeFragment.ViewHolder>(theOptions){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFragment.ViewHolder {
                val theView = LayoutInflater.from(parent.context).inflate(R.layout.posts_recycler, parent, false)
                return HomeFragment.ViewHolder(theView)
            }

            override fun onBindViewHolder(holder: HomeFragment.ViewHolder, position: Int, model: OurPosts) {
                if(model.theUserUID.equals(uidOfUser)) {
                    holder.setUserName(model.theirFirstName, model.theirLastName)
                    holder.postTime.setText("Posted " + model.timeAndDate)
                    holder.caption.setText(model.theCaption)
                    Picasso.get().load(model.thePhotoUrl).into(holder.postImage)
                }
                else{
                    holder.userName.visibility = View.GONE
                    holder.postImage.visibility = View.GONE
                    holder.postTime.visibility = View.GONE
                    holder.caption.visibility = View.GONE
                    holder.postImage.visibility = View.GONE
                }
            }
        }
//        recyclerView.adapter = viewAdapter

        Log.d("DashboardFragment", "onCreateView: $recycler_view")
        theViewAdapter = viewAdapter

        viewAdapter.startListening()


        return root
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var userName: TextView = itemView.findViewById(R.id.username_post)
        var postTime: TextView = itemView.findViewById(R.id.post_time)
        var caption: TextView = itemView.findViewById(R.id.caption)
        var postImage: ImageView = itemView.findViewById(R.id.postImage)

        fun setUserName(f: String, l: String){
            userName.setText("$f $l")

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("DashboardFragment", "onViewCreated: $recycler_view")
        profile_recycler_view.adapter = theViewAdapter
        Log.d(TAG, "onViewCreated: ${profile_recycler_view.top}")


    }
}