package com.liid.dekanatkeys.activities.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.liid.dekanatkeys.R
import com.liid.dekanatkeys.activities.LoginActivity
import com.liid.dekanatkeys.databinding.FragmentProfileBinding
import com.liid.dekanatkeys.helpers.Log
import com.liid.dekanatkeys.helpers.OKOApiSingleton
import com.liid.dekanatkeys.helpers.OKOCallback
import com.liid.dekanatkeys.models.user.LogoutResponse
import com.liid.dekanatkeys.models.user.UserProfile

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        val preferences = requireContext().getSharedPreferences(getString(R.string.SP_name), AppCompatActivity.MODE_PRIVATE)
        val email = preferences.getString("email", null)
        val name = preferences.getString("name", null)
        val role = preferences.getString("role", null)

        binding.emailView.text = email
        binding.nameView.text = name
        binding.roleView.text = role

        binding.logoutButton.setOnClickListener{
            val token = preferences.getString(getString(R.string.jwtTokenName), null)
            OKOApiSingleton.api.logoutUser("bearer $token").enqueue(OKOCallback<LogoutResponse>(
                successCallback = {response ->
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                },
                errorCallback = {response ->
                    Log("error code:" + response.code().toString())
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                }
            ))
        }

        return binding.root
    }


}