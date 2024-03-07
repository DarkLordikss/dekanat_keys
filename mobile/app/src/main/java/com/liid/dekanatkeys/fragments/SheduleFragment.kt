package com.liid.dekanatkeys.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.liid.dekanatkeys.R
import com.liid.dekanatkeys.activities.MainActivity
import com.liid.dekanatkeys.databinding.FragmentSheduleBinding
import com.liid.dekanatkeys.helpers.Log
import com.liid.dekanatkeys.helpers.OKOApiSingleton
import com.liid.dekanatkeys.helpers.OKOCallback
import com.liid.dekanatkeys.models.ApplicationsRequest
import com.liid.dekanatkeys.models.user.LoginRequest
import com.liid.dekanatkeys.models.user.LoginResponse
import com.liid.dekanatkeys.views.OKODateBar
import com.liid.dekanatkeys.views.OKODateBarInteraction
import retrofit2.Call
import java.time.LocalDate

private const val BUILDING_PARAM = "building"
private const val CLASSROOM_PARAM = "classroom"

class SheduleFragment : Fragment(), OKODateBarInteraction {
    private lateinit var binding: FragmentSheduleBinding

    private var building: String? = null
    private var classroom: String? = null
    private lateinit var okoDateBar:OKODateBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            building = it.getString(BUILDING_PARAM)
            classroom = it.getString(CLASSROOM_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSheduleBinding.inflate(inflater, container, false)

        okoDateBar = OKODateBar(requireContext(), null, this).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 10
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                topToBottom = R.id.monthText
            }
        }

        binding.root.addView(okoDateBar)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getApplications()
    }

    private fun getApplications(){
        building?.let { Log(it) }
        val applicationRequest = ApplicationsRequest( building!!.toInt(), LocalDate.now(), LocalDate.now().plusDays(1), classroom!!.toInt())

        OKOApiSingleton.api.fetchApplications(applicationRequest.building,
                                            applicationRequest.start_date,
                                            applicationRequest.end_date,
                                            applicationRequest.statuses,
                                            applicationRequest.classrooms)
            .enqueue(OKOCallback<String>(
            successCallback = {response ->
                response.body()?.let { Log(it) }
            },
            errorCallback = {response ->
                Log("error code:" + response.code().toString())
            }
        ))
    }

    override fun setMonth(month: String) {
        binding.monthText.text = month
    }

    companion object {
        @JvmStatic
        fun newInstance(building: String, classroom: String) =
            SheduleFragment().apply {
                arguments = Bundle().apply {
                    putString(BUILDING_PARAM, building)
                    putString(CLASSROOM_PARAM, classroom)
                }
            }
    }
}