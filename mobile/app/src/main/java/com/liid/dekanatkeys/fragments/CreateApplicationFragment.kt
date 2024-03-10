package com.liid.dekanatkeys.fragments

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.liid.dekanatkeys.R
import com.liid.dekanatkeys.activities.LoginActivity
import com.liid.dekanatkeys.activities.SuccessRegistrationActivity
import com.liid.dekanatkeys.activities.ui.dashboard.DashboardViewModel
import com.liid.dekanatkeys.databinding.FragmentChooseBuildingBinding
import com.liid.dekanatkeys.databinding.FragmentCreateApplicationBinding
import com.liid.dekanatkeys.helpers.Log
import com.liid.dekanatkeys.helpers.OKOApiSingleton
import com.liid.dekanatkeys.helpers.OKOCallback
import com.liid.dekanatkeys.models.BuildingsResponse
import com.liid.dekanatkeys.models.CreateApplicationRequest
import com.liid.dekanatkeys.models.CreateApplicationResponse
import com.liid.dekanatkeys.models.user.UserProfile
import retrofit2.Call


class CreateApplicationFragment : Fragment() {
    private lateinit var binding: FragmentCreateApplicationBinding
    private val dashboardViewModel: DashboardViewModel by activityViewModels()

    private var nameError = true
    private var descriptionError = true
    private var dublicatesError = true
    private lateinit var nameErrorTextView : TextView
    private lateinit var descriptionErrorTextView : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nameErrorTextView = TextView(requireContext()).apply {
            setTextColor(requireContext().getColor(R.color.red))
            text = "Не может быть пустым"
            textSize = 15F
        }
        descriptionErrorTextView = TextView(requireContext()).apply {
            setTextColor(requireContext().getColor(R.color.red))
            text = "Не может быть пустым"
            textSize = 15F
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateApplicationBinding.inflate(inflater, container, false)
        binding.name.addTextChangedListener{text ->
            if(text.isNullOrEmpty()){
                nameError = true
                binding.name.setErrorColor()
                addChild(binding.nameLayout, nameErrorTextView)
            }
            else{
                nameError = false
                binding.name.setColor()
                removeChild(binding.nameLayout, nameErrorTextView)
                dashboardViewModel.name = text.toString()
            }
            checkCreateButtonStatus()
        }
        binding.description.addTextChangedListener{text ->
            if(text.isNullOrEmpty()){
                descriptionError = true
                binding.description.setErrorColor()
                addChild(binding.descriptionLayout, descriptionErrorTextView)
            }
            else{
                descriptionError = false
                binding.description.setColor()
                removeChild(binding.descriptionLayout, descriptionErrorTextView)
                dashboardViewModel.description = text.toString()
            }
            checkCreateButtonStatus()
        }
        binding.dublicates.addTextChangedListener{text ->
            if(text.isNullOrEmpty() || text.toString() == "0"){
                dublicatesError = true
                binding.dublicates.setErrorColor()
            }
            else{
                dublicatesError = false
                binding.dublicates.setColor()
                dashboardViewModel.dublicates = text.toString().toInt()
            }
            checkCreateButtonStatus()
        }
        binding.createButton.setOnClickListener {
            createApplication()
        }

        val preferences = requireContext().getSharedPreferences(getString(R.string.SP_name), MODE_PRIVATE)
        val role = preferences.getString(getString(R.string.roleName), null)
        if (role != null){
            if (role != "Преподаватель"){
                dublicatesError = false
                removeChild(binding.root, binding.dublicates)
                removeChild(binding.root, binding.dublicatesLayout)
                binding.root.removeView(binding.dublicates)
            }
        }

        return binding.root
    }

    private fun addChild(layout: LinearLayout, view: View){
        if (layout.indexOfChild(view) == -1) layout.addView(view)
    }
    private fun removeChild(layout: LinearLayout, view: View){
        if (layout.indexOfChild(view) != -1) layout.removeView(view)
    }
    private fun checkCreateButtonStatus(){
        binding.createButton.isEnabled = !nameError && !descriptionError && !dublicatesError
    }

    private fun createApplication(){
        val response = CreateApplicationRequest(
            dashboardViewModel.classroomId!!,
            dashboardViewModel.name!!,
            dashboardViewModel.description!!,
            dashboardViewModel.currentDate.toString(),
            dashboardViewModel.lessonNumber,
            dashboardViewModel.dublicates)

        val preferences = requireContext().getSharedPreferences(getString(R.string.SP_name), MODE_PRIVATE)
        val token = preferences.getString(getString(R.string.jwtTokenName), null)
        Log(token.toString())

        OKOApiSingleton.api.createApplication(response, "bearer $token").enqueue(OKOCallback<CreateApplicationResponse>(
            successCallback = {response ->
                Log(response.body()!!.message)
                findNavController().navigate(R.id.action_createApplicationFragment_to_navigation_my_application)
            },
            errorCallback = {response ->
                Log("error code:" + response.code().toString())
                if (response.errorBody()!!.string() == "{\"detail\":\"you has already occupied this classroom\"}"){
                    Toast.makeText(requireContext(), "Вы уже подавали эту заявку", Toast.LENGTH_LONG).show()
                }
                else{
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        ))
    }
}
