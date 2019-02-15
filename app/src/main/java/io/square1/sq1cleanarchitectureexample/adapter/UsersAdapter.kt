package io.square1.sq1cleanarchitectureexample.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.square1.sq1cleanarchitectureexample.R
import io.square1.sq1cleanarchitectureexample.model.UIUserModel

class UsersAdapter(private val context: Context, private val userList: ArrayList<UIUserModel>) :
    RecyclerView.Adapter<UsersAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUserName = itemView.findViewById<TextView>(R.id.tvUserName)
        val ivUserAvatar = itemView.findViewById<ImageView>(R.id.ivUserAvatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UsersAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.user_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UsersAdapter.ViewHolder, position: Int) {
        holder.tvUserName.text = userList[position].email.trim()
        Glide.with(context)
            .load(userList[position].picture?.medium)
            .apply(RequestOptions.circleCropTransform())
            .into(holder.ivUserAvatar)
    }

}