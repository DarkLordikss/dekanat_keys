package com.liid.dekanatkeys.activities.ui.myApplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.liid.dekanatkeys.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.liid.dekanatkeys.databinding.FragmentMyApplicationBinding
import com.liid.dekanatkeys.helpers.Log
import com.liid.dekanatkeys.helpers.OKOApiSingleton
import com.liid.dekanatkeys.helpers.OKOCallback
import com.liid.dekanatkeys.models.ApplicationWithDateStatus
import com.liid.dekanatkeys.models.ApplicationsRequest
import com.liid.dekanatkeys.models.ApplicationsResponse
import com.liid.dekanatkeys.models.TimetableWithList
import com.liid.dekanatkeys.views.ApplicationRecicleAdapter
import java.time.LocalDate

class MyApplicationFragment : Fragment() {

    private lateinit var binding: FragmentMyApplicationBinding
    private lateinit var myApplicationRecyclerView : RecyclerView
    val myApplicationViewModel: MyApplicationViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyApplicationBinding.inflate(inflater, container, false)

        myApplicationRecyclerView = binding.applicationRecycleView
        myApplicationRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.notificationButton.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_my_application_to_notificationFragment)

        }

        getApplications()

        return binding.root
    }

    private fun getApplications(){
        val preferences = requireContext().getSharedPreferences(getString(R.string.SP_name),
            AppCompatActivity.MODE_PRIVATE
        )
        val token = preferences.getString(getString(R.string.jwtTokenName), null)

        OKOApiSingleton.api.fetchMyApplications(LocalDate.parse("2024-01-01"), LocalDate.parse("2025-01-01"), listOf(1, 2, 4, 5, 6), "bearer $token")
            .enqueue(OKOCallback<ApplicationsResponse>(
                successCallback = {response ->
                    if (response.body() != null){
                        val applications = ApplicationWithDateStatus.getFromTimetableWithDates(response.body()!!.TimetableWithDates)
                        myApplicationRecyclerView.adapter = ApplicationRecicleAdapter(applications, this)
//
                    }
                },
                errorCallback = {response ->
                    Log("error code:" + response.code().toString())
                }
            ))
    }


}