package com.liid.dekanatkeys.views

import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.liid.dekanatkeys.R
import com.liid.dekanatkeys.activities.ui.myApplication.MyApplicationFragment
import com.liid.dekanatkeys.helpers.Log
import com.liid.dekanatkeys.models.Application
import com.liid.dekanatkeys.models.ApplicationWithDateStatus
import java.time.LocalDate

class ApplicationRecicleAdapter(private val myApplications: List<ApplicationWithDateStatus>, private val parentFragment: MyApplicationFragment)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    class MyApplicationViewHolder(var itemView: OKOApplicationItem) : RecyclerView.ViewHolder(itemView){

    }

    override fun getItemCount(): Int {
        return myApplications.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyApplicationViewHolder(OKOApplicationItem(parent.context, null))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = (holder as MyApplicationViewHolder).itemView as OKOApplicationItem
        val myApplication = myApplications[position]
        item.changeButton.setOnClickListener{
            parentFragment.myApplicationViewModel.applicationId = myApplication.application_id
            Log(parentFragment.myApplicationViewModel.applicationId.toString())
            parentFragment.findNavController().navigate(R.id.action_navigation_my_application_to_usersFragment)
        }
        item.setLessonNumber(position + 1)
        item.setApplicationInfo(myApplication.name, myApplication.description, myApplication.building, myApplication.classNumber, LocalDate.parse(myApplication.date), myApplication.status)
        item.setLessonNumber(myApplication.lessonNumber)
    }

}