package com.liid.dekanatkeys.views

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.liid.dekanatkeys.helpers.Log
import com.liid.dekanatkeys.models.Application
import com.liid.dekanatkeys.views.sheduleItem.OKOSheduleItem
import com.liid.dekanatkeys.views.sheduleItem.OKOSheduleItemBooked
import com.liid.dekanatkeys.views.sheduleItem.OKOSheduleItemFree

class TimetableRecycleAdapter(private val applications: List<Application?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_FREE = 0
    private val VIEW_TYPE_BOOKED = 1

    class TimetableViewHolderFree(var itemView: OKOSheduleItemFree) : RecyclerView.ViewHolder(itemView){
//        val largeTextView: TextView = itemView.largeTextView
//        val smallTextView: TextView = itemView.smallTextView
//        init {
//            itemView.setOnClickListener{
//                Log(largeTextView.text.toString())
//            }
//        }
    }

    class TimetableViewHolderBooked(var itemView: OKOSheduleItemBooked) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_FREE){

            TimetableViewHolderFree(OKOSheduleItemFree(parent.context, null))
        }
        else
        {
            TimetableViewHolderBooked(OKOSheduleItemBooked(parent.context, null))
        }
    }

    override fun getItemCount(): Int {
        return applications.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is TimetableViewHolderFree -> {
                val item = (holder as TimetableViewHolderFree).itemView as OKOSheduleItemFree
                item.setLessonNumber(position + 1)
            }
            is TimetableViewHolderBooked -> {
                val item = (holder as TimetableViewHolderBooked).itemView as OKOSheduleItemBooked
                item.setLessonNumber(position + 1)
                val application = applications[position]!!
                item.setApplicationInfo(application.name, application.description, application.building, application.classNumber)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (applications[position] != null){
            VIEW_TYPE_BOOKED
        } else{
            VIEW_TYPE_FREE
        }
    }
}