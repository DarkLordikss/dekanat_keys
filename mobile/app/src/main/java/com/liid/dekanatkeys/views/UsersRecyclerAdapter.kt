package com.liid.dekanatkeys.views

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.liid.dekanatkeys.fragments.UsersFragment
import com.liid.dekanatkeys.helpers.Log
import com.liid.dekanatkeys.models.user.UserInfo


class UsersRecyclerAdapter(private val users: List<UserInfo>, parentFragment: UsersFragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class UsersRecyclerHolder(var itemView: UserInfoItem) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UsersRecyclerHolder(UserInfoItem(parent.context, null))
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = (holder as UsersRecyclerHolder).itemView as UserInfoItem
        val user = users[position]
        item.setUserInfo(user.email, user.name, user.id)
        item.setOnClickListener {
            Log((it as UserInfoItem).userId)
        }
    }
}