package com.liid.dekanatkeys.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.liid.dekanatkeys.R
import com.liid.dekanatkeys.activities.MainActivity
import com.liid.dekanatkeys.databinding.FragmentSheduleBinding
import com.liid.dekanatkeys.helpers.Log
import com.liid.dekanatkeys.helpers.OKOApiSingleton
import com.liid.dekanatkeys.helpers.OKOCallback
import com.liid.dekanatkeys.models.ApplicationsRequest
import com.liid.dekanatkeys.models.ApplicationsResponse
import com.liid.dekanatkeys.models.TimetableWithList
import com.liid.dekanatkeys.models.user.LoginRequest
import com.liid.dekanatkeys.models.user.LoginResponse
import com.liid.dekanatkeys.views.OKODateBar
import com.liid.dekanatkeys.views.OKODateBarInteraction
import com.liid.dekanatkeys.views.TimetableRecycleAdapter
import retrofit2.Call
import java.time.LocalDate

private const val BUILDING_PARAM = "building"
private const val CLASSROOM_PARAM = "classroom"

class SheduleFragment : Fragment(), OKODateBarInteraction {
    private lateinit var binding: FragmentSheduleBinding

    private var building: String? = null
    private var classroom: String? = null
    private lateinit var okoDateBar: OKODateBar
    private lateinit var timetableRecycleView : RecyclerView
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
            layoutParams = LinearLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 10
            }
        }
        binding.bodyLayout.addView(okoDateBar, 1)

        timetableRecycleView = binding.timetableRecycleView
        timetableRecycleView.layoutManager = LinearLayoutManager(requireContext())
//        timetableRecycleView.adapter = TimetableRecycleAdapter(fillList())

        return binding.root
    }

    private fun fillList(): List<String> {
        val data = mutableListOf<String>()
        (0..30).forEach { i -> data.add("$i element") }
        return data
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getApplications()
    }

    private fun getApplications(){
        building?.let { Log(it) }
        val applicationRequest = ApplicationsRequest( building!!.toInt(), LocalDate.parse("2024-02-13"), LocalDate.parse("2024-02-16"), classroom!!.toInt())

        OKOApiSingleton.api.fetchApplications(applicationRequest.building,
                                            applicationRequest.start_date,
                                            applicationRequest.end_date,
                                            applicationRequest.statuses,
                                            applicationRequest.classrooms)
            .enqueue(OKOCallback<ApplicationsResponse>(
            successCallback = {response ->
                if (response.body() != null){
                    val timetable = TimetableWithList(response.body()!!.TimetableWithDates[1].timetable)
                    timetableRecycleView.adapter = TimetableRecycleAdapter(timetable.applications)

//                    for (i in 0 until timetable.applications.size){
//                            if (timetable.applications[i] == null){
//                                Log("$i: null")
//                            }
//                            else{
//                                Log("$i: ${timetable.applications[i]!!.id}")
//                            }
//                        }

//                    for(td in response.body()!!.TimetableWithDates){
//
//                        val timetable = TimetableWithList(td.timetable)
//                        timetableRecycleView.adapter = TimetableRecycleAdapter(timetable.applications)
//                        for (i in 0 until timetable.applications.size){
//                            if (timetable.applications[i] == null){
//                                Log("$i: null")
//                            }
//                            else{
//                                Log("$i: ${timetable.applications[i]!!.id}")
//                            }
//                        }
//
//                        Log(td.date)
//                    }
                }

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