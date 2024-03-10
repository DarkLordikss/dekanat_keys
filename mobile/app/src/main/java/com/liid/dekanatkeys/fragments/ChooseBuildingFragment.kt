package com.liid.dekanatkeys.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.liid.dekanatkeys.R
import com.liid.dekanatkeys.databinding.FragmentChooseBuildingBinding
import com.liid.dekanatkeys.helpers.Log
import com.liid.dekanatkeys.helpers.OKOApiSingleton
import com.liid.dekanatkeys.helpers.OKOCallback
import com.liid.dekanatkeys.models.BuildingsResponse
import androidx.navigation.fragment.findNavController
import com.liid.dekanatkeys.activities.ui.dashboard.DashboardViewModel


class ChooseBuildingFragment : Fragment() {
    private lateinit var binding: FragmentChooseBuildingBinding
    private lateinit var activityContext: Context
    private val dashboardViewModel: DashboardViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChooseBuildingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buildingListView.setOnItemClickListener { parent, view, position, id ->
            val textView = view as TextView
            dashboardViewModel.building = textView.text.toString().split(' ')[0]
            findNavController().navigate(R.id.action_chooseBuildingFragment_to_chooseClassroomFragment)
        }

        getBuildings()
    }

    private fun fillList(buildings: List<String> ){
        val b = buildings.map { "$it корпус" }
        val adapter = ArrayAdapter<String>(
            activityContext,
            R.layout.list_element,
            b
        )
        binding.buildingListView.adapter = adapter
    }

    private fun getBuildings(){
        OKOApiSingleton.api.fetchBuildings().enqueue(OKOCallback<BuildingsResponse>(
            successCallback = {response ->
                if (response.body() != null)
                {
                    fillList(response.body()!!.buildings)
                }
                else binding.root.removeView(binding.buildingListView)
            },
            errorCallback = {response ->
                Log("error code:" + response.code().toString())
                binding.root.removeView(binding.buildingListView)
            }
        ))
    }
}