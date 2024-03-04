package com.liid.dekanatkeys.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.liid.dekanatkeys.R
import com.liid.dekanatkeys.databinding.FragmentSheduleBinding

private const val BUILDING_PARAM = "building"
private const val CLASSROOM_PARAM = "classroom"

class SheduleFragment : Fragment() {
    private lateinit var binding: FragmentSheduleBinding

    // TODO: Rename and change types of parameters
    private var building: String? = null
    private var classroom: String? = null

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.building.text = "Корпус: " + building
        binding.classroom.text = "Аудитория: " + classroom
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