package com.liid.dekanatkeys.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.liid.dekanatkeys.R
import com.liid.dekanatkeys.databinding.FragmentNotificationBinding
import com.liid.dekanatkeys.helpers.Log
import com.liid.dekanatkeys.helpers.OKOApiSingleton
import com.liid.dekanatkeys.helpers.OKOCallback
import com.liid.dekanatkeys.models.ApplicationWithDateStatus
import com.liid.dekanatkeys.models.TransferKeySocketMessage
import com.liid.dekanatkeys.models.user.UserInfo
import com.liid.dekanatkeys.views.NotificationRecyclerAdapter
import com.liid.dekanatkeys.views.UsersRecyclerAdapter

class NotificationFragment : Fragment() {
    private lateinit var binding: FragmentNotificationBinding
    private lateinit var keyMessages: List<TransferKeySocketMessage>
    private lateinit var preferences: SharedPreferences
    private var applications = mutableListOf<ApplicationWithDateStatus>()
    private lateinit var notificationRecyclerView : RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        notificationRecyclerView = binding.notificationRecyclerView
        notificationRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        preferences = requireContext().getSharedPreferences(getString(R.string.SP_name),
            AppCompatActivity.MODE_PRIVATE
        )
        initKeyMessages()
        if(keyMessages.size > 0)
            getApplication(0)

        return binding.root
    }

    private fun getApplication(k: Int){
        val id = keyMessages[k].application_id
        OKOApiSingleton.api.getApplication(id).enqueue(OKOCallback<ApplicationWithDateStatus>(
            successCallback = {response ->
                if (response.body() != null) {
                    applications.add(response.body()!!)
                    if (k+1 < keyMessages.size)
                    {
                        getApplication(k+1)
                    }
                    else{
                        for (a in applications){
                            Log(a.application_id)
                        }
                        notificationRecyclerView.adapter = NotificationRecyclerAdapter(applications)
                    }
                }
            },
            errorCallback = {response ->
                Log("error code:" + response.code().toString())
            }
        ))
    }

    private fun initKeyMessages(){
        val keyMessagesMergedText = preferences.getString(getString(R.string.transfer_key_socket_message), null)
        if (keyMessagesMergedText == null){
            keyMessages = listOf()
        }
        else{
            val out = mutableListOf<TransferKeySocketMessage>()
            val keyMessagesSplitText = keyMessagesMergedText.split('#')
            for (k in keyMessagesSplitText){
                out.add(Gson().fromJson(k, TransferKeySocketMessage::class.java))
            }
            keyMessages = out.toList()
        }
        Log("keyMessages.size: " + keyMessages.size.toString())
    }

}