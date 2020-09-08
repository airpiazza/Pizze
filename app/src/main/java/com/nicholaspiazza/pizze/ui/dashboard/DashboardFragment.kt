package com.nicholaspiazza.pizze.ui.dashboard

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.nicholaspiazza.pizze.OurPosts
import com.nicholaspiazza.pizze.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.posts_recycler.*


class DashboardFragment : Fragment() {



    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var recyclerView: RecyclerView

        private lateinit var theViewAdapter: RecyclerView.Adapter<*>
//    private lateinit var viewManager: RecyclerView.LayoutManager
    private val TAG ="DashboardFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

//        viewAdapter = MyAdapter()
//        recycler_view.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, true)


        val dbRefQuery: Query = FirebaseDatabase.getInstance().getReference("/posts").limitToLast(100)
        val theOptions: FirebaseRecyclerOptions<OurPosts> = FirebaseRecyclerOptions.Builder<OurPosts>().setQuery(dbRefQuery, OurPosts::class.java).build()
        val viewAdapter = object : FirebaseRecyclerAdapter<OurPosts, ViewHolder>(theOptions){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val theView = LayoutInflater.from(parent.context).inflate(R.layout.posts_recycler, parent, false)
                return ViewHolder(theView)
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int, model: OurPosts) {
//                if(model.theCaption.equals("un'altra")) {
                    holder.setUserName(model.theirFirstName, model.theirLastName)
                    holder.postTime.text = "Posted " + model.timeAndDate
                    holder.caption.text = model.theCaption
                    Picasso.get().load(model.thePhotoUrl).into(holder.postImage)
//                holder.postImage.visibility = View.GONE
//                }
//                else{
//                    holder.userName.visibility = View.GONE
//                    holder.postImage.visibility = View.GONE
//                    holder.postTime.visibility = View.GONE
//                    holder.caption.visibility = View.GONE
//                    holder.postImage.visibility = View.GONE
//                }

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
            userName.text = "$f $l"

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("DashboardFragment", "onViewCreated: $recycler_view")
        recycler_view.adapter = theViewAdapter
        Log.d(TAG, "onViewCreated: ${recycler_view.top}")

    }







}



