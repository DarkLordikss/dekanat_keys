package com.liid.dekanatkeys.activities.ui.myApplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.liid.dekanatkeys.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.liid.dekanatkeys.databinding.FragmentMyApplicationBinding
import com.liid.dekanatkeys.views.ApplicationRecicleAdapter

class MyApplicationFragment : Fragment() {

    private lateinit var binding: FragmentMyApplicationBinding
    private lateinit var myApplicationRecyclerView : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyApplicationBinding.inflate(inflater, container, false)

        myApplicationRecyclerView = binding.applicationRecycleView
        myApplicationRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        findNavController().navigate(R.id.action_navigation_my_application_to_usersFragment)

//        val adapter = ApplicationRecicleAdapter(applicationList, this)
//        myApplicationRecyclerView.adapter = adapter


        return binding.root
    }


}