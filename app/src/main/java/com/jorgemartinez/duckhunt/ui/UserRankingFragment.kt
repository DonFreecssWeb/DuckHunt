package com.jorgemartinez.duckhunt.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.jorgemartinez.duckhunt.R
import com.jorgemartinez.duckhunt.models.User


class UserRankingFragment : Fragment() {

    private var columnCount = 1
    lateinit var userList: ArrayList<User>
    lateinit var adapter2:MyUsersRecyclerViewAdapter

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_ranking_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }

                userList = ArrayList()
                //obtener la lista de usuarios ordenado de manera descendente y limitados a 10
                db.collection("users")
                    .orderBy("ducks",Query.Direction.DESCENDING)
                    .limit(10)
                    .get().addOnCompleteListener {


                        for (document in it.result!!){
                            var userItem:User = document.toObject(User::class.java)
                             Log.e("ee",userItem.toString())
                            Log.e("ee",userItem.nick)
                                userList.add(userItem)

                        }
                        adapter2= MyUsersRecyclerViewAdapter(userList)
                        adapter = adapter2
                }


            }
        }
        return view
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            UserRankingFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}