package com.jorgemartinez.duckhunt.ui

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.jorgemartinez.duckhunt.databinding.FragmentUserRankingBinding
import com.jorgemartinez.duckhunt.models.User


class MyUsersRecyclerViewAdapter(
    private val values: List<User>
) : RecyclerView.Adapter<MyUsersRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentUserRankingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.nickname.text = item.nick
        holder.ducks.text = item.ducks.toString()
        holder.position.text = (position + 1).toString()
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentUserRankingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val ducks: TextView = binding.textViewDucks
        val position: TextView = binding.textViewPosition
        val  nickname: TextView = binding.textViewNick

        override fun toString(): String {
            return super.toString() + " '" + nickname.text + "'"
        }
    }

}