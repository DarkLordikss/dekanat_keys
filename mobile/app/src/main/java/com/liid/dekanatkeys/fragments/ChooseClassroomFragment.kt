package com.liid.dekanatkeys.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.liid.dekanatkeys.R
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
    private lateinit var onDataPassListener :ChooseClassroomFragmentDataPass
    private lateinit var activityContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityContext = context
        onDataPassListener = context as ChooseClassroomFragmentDataPass
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            building = it.getString(BUILDING_PARAM)
        }
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
            onDataPassListener.onChooseClassroomFragmentDataPass(textView.text.toString())
        }
        getClassrooms()
    }

    private fun fillList(classrooms: List<String> ){
        val adapter = ArrayAdapter<String>(
            activityContext,
            android.R.layout.simple_list_item_1,
            classrooms
        )
        binding.classroomListView.adapter = adapter
    }

    private fun getClassrooms(){
        building?.let {
            OKOApiSingleton.api.fetchClassrooms(it.toInt()).enqueue(OKOCallback<ClassroomResponse>(
                successCallback = {response ->
                    if (response.body() != null) {
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

    companion object {
        @JvmStatic
        fun newInstance(building: String) =
            ChooseClassroomFragment().apply {
                arguments = Bundle().apply {
                    putString(BUILDING_PARAM, building)
                }
            }
    }
}