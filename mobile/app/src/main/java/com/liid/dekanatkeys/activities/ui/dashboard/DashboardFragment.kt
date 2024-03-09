package com.liid.dekanatkeys.activities.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.liid.dekanatkeys.R
import com.liid.dekanatkeys.databinding.FragmentDashboardBinding
import com.liid.dekanatkeys.helpers.Log
import com.liid.dekanatkeys.helpers.OKOApiSingleton
import com.liid.dekanatkeys.helpers.OKOCallback
import com.liid.dekanatkeys.models.ApplicationsRequest
import com.liid.dekanatkeys.models.ApplicationsResponse
import com.liid.dekanatkeys.models.TimetableWithList
import com.liid.dekanatkeys.views.OKODateBar
import com.liid.dekanatkeys.views.OKODateBarInteraction
import com.liid.dekanatkeys.views.TimetableRecycleAdapter
import java.time.LocalDate

class DashboardFragment : Fragment(), OKODateBarInteraction {

    private var _binding: FragmentDashboardBinding? = null
    private var building: String? = null
    private var classroom: String? = null
    private lateinit var okoDateBar: OKODateBar
    private lateinit var timetableRecycleView : RecyclerView
    private val dashboardViewModel: DashboardViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log("onCreateView")

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

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

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        building = dashboardViewModel.building
        classroom = dashboardViewModel.classroom

        getApplications()
    }
    private fun getApplications(){
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun setMonth(month: String) {
        binding.monthText.text = month
    }
}