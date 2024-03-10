package com.liid.dekanatkeys.views

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.liid.dekanatkeys.activities.ui.myApplication.MyApplicationFragment
import com.liid.dekanatkeys.models.Application

class ApplicationRecicleAdapter(private val myApplications: List<Application?>, private val parentFragment: MyApplicationFragment)
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
        item.setLessonNumber(position + 1)
        val myApplication = myApplications[position]!!
        //item.setApplicationInfo(myApplication.name, myApplication.description, myApplication.building, myApplication.classNumber, myApplication.date, myApplication.status)
    }
}