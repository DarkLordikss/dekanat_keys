package com.liid.dekanatkeys.views

import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.liid.dekanatkeys.R
import com.liid.dekanatkeys.helpers.Log
import com.liid.dekanatkeys.models.ApplicationWithDateStatus
import java.time.LocalDate

class NotificationRecyclerAdapter(private val notifications : List<ApplicationWithDateStatus>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class NotificationRecyclerHolder(var itemView: OKOApplicationItemMessage) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NotificationRecyclerHolder(OKOApplicationItemMessage(parent.context, null))
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = (holder as NotificationRecyclerAdapter.NotificationRecyclerHolder).itemView as OKOApplicationItemMessage
        val notification = notifications[position]
        item.applyButton.setOnClickListener{
            
        }
        item.setLessonNumber(position + 1)
        item.setApplicationInfo(notification.name, notification.description, notification.building, notification.classNumber, LocalDate.parse(notification.date), notification.status)
        item.setLessonNumber(notification.lessonNumber)
    }
}