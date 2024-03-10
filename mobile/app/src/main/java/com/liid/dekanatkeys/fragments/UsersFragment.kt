package com.liid.dekanatkeys.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.liid.dekanatkeys.R
import com.liid.dekanatkeys.databinding.FragmentUsersBinding
import com.liid.dekanatkeys.helpers.Log
import com.liid.dekanatkeys.helpers.OKOApiSingleton
import com.liid.dekanatkeys.helpers.OKOCallback
import com.liid.dekanatkeys.models.user.UserInfo
import com.liid.dekanatkeys.views.UsersRecyclerAdapter


class UsersFragment : Fragment() {
    private lateinit var binding: FragmentUsersBinding
    private lateinit var usersRecyclerView : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsersBinding.inflate(inflater, container, false)
        usersRecyclerView = binding.usersRecyclerView
        usersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        getUsers()
        return binding.root
    }

    private fun getUsers(){
        val preferences = requireContext().getSharedPreferences(getString(R.string.SP_name),
            AppCompatActivity.MODE_PRIVATE
        )
        val token = preferences.getString(getString(R.string.jwtTokenName), null)

        OKOApiSingleton.api.fetchUsers(listOf(1, 2), "bearer $token").enqueue(OKOCallback<List<UserInfo>>(
            successCallback = {response ->
                if (response.body() != null) {
                    usersRecyclerView.adapter = UsersRecyclerAdapter(response.body()!!, this)
                }
            },
            errorCallback = {response ->
                Log("error code:" + response.code().toString())
            }
        ))
    }
}