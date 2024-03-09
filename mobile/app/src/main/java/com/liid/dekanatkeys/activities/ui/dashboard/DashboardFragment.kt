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
    private var startDate: LocalDate? = null
    private var endDate: LocalDate? = null

    private var currentDay = 2

    private lateinit var okoDateBar: OKODateBar
    private lateinit var timetableRecycleView : RecyclerView
    val dashboardViewModel: DashboardViewModel by activityViewModels()

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        building = dashboardViewModel.building
        classroom = dashboardViewModel.classroom
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        startDate = okoDateBar.startDate
        endDate = okoDateBar.endDate
        dashboardViewModel.currentDay = okoDateBar.activeButtonPos

        timetableRecycleView = binding.timetableRecycleView
        timetableRecycleView.layoutManager = LinearLayoutManager(requireContext())

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getApplications()
    }
    private fun getApplications(){
        Log(building.toString())
        Log(startDate.toString())
        Log(endDate.toString())
        Log(classroom.toString())
        val applicationRequest = ApplicationsRequest( building!!.toInt(), startDate!!, endDate!!, classroom!!.toInt())

        OKOApiSingleton.api.fetchApplications(applicationRequest.building,
            applicationRequest.start_date,
            applicationRequest.end_date,
            applicationRequest.statuses,
            applicationRequest.classrooms)
            .enqueue(OKOCallback<ApplicationsResponse>(
                successCallback = {response ->
                    if (response.body() != null){
                        dashboardViewModel.timetables.clear()
                        for (timetable in response.body()!!.TimetableWithDates){
                            dashboardViewModel.timetables.add(TimetableWithList(timetable.timetable))
                        }
                        setDay()
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

    private fun setDay(){
        timetableRecycleView.adapter = TimetableRecycleAdapter(dashboardViewModel.timetables[dashboardViewModel.currentDay].applications)
    }

    override fun setMonth(month: String) {
        binding.monthText.text = month
    }

    override fun setStartEndDates(start: LocalDate, end: LocalDate, pos: Int) {
        startDate = start
        endDate = end
        dashboardViewModel.currentDay = pos
        getApplications()
    }

    override fun dateButtonClicked(pos: Int) {
        dashboardViewModel.currentDay = pos
        setDay()
    }
}