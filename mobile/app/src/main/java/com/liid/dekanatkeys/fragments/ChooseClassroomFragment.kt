package com.liid.dekanatkeys.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.liid.dekanatkeys.R
import com.liid.dekanatkeys.activities.ui.dashboard.DashboardViewModel
import com.liid.dekanatkeys.databinding.FragmentChooseClassroomBinding
import com.liid.dekanatkeys.helpers.Log
import com.liid.dekanatkeys.helpers.OKOApiSingleton
import com.liid.dekanatkeys.helpers.OKOCallback
import com.liid.dekanatkeys.models.BuildingsResponse
import com.liid.dekanatkeys.models.ClassroomResponse

private const val BUILDING_PARAM = "building"

class ChooseClassroomFragment : Fragment() {
    private var building: String? = null
    private lateinit var binding: FragmentChooseClassroomBinding
    private lateinit var activityContext: Context
    private val dashboardViewModel: DashboardViewModel by activityViewModels()
    private val classroomId = mutableMapOf<String, String>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityContext = context
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChooseClassroomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.classroomListView.setOnItemClickListener { parent, view, position, id ->
            val textView = view as TextView
            dashboardViewModel.classroom = textView.text.toString()
            dashboardViewModel.classroomId = classroomId[dashboardViewModel.classroom]
            findNavController().navigate(R.id.action_chooseClassroomFragment_to_navigation_dashboard)
        }
        building = dashboardViewModel.building
        getClassrooms()
    }

    private fun fillList(classrooms: List<String> ){
        val adapter = ArrayAdapter<String>(
            activityContext,
            R.layout.list_element,
            classrooms
        )
        binding.classroomListView.adapter = adapter
    }

    private fun getClassrooms(){
        building?.let {
            OKOApiSingleton.api.fetchClassrooms(it.toInt()).enqueue(OKOCallback<ClassroomResponse>(
                successCallback = {response ->
                    if (response.body() != null) {
                        for (c in response.body()!!.classrooms){
                            classroomId[c.number] = c.id
                        }
                        val classroomNumbers = response.body()!!.classrooms.map { it.number }
                        fillList(classroomNumbers)
                    } else binding.root.removeView(binding.classroomListView)
                },
                errorCallback = {response ->
                    Log("error code:" + response.code().toString())
                    binding.root.removeView(binding.classroomListView)
                }
            ))
        }
    }

}