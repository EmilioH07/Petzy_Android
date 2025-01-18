package com.erha.dogsrf.Actividades

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.erha.dogsrf.R

class ActivityAdapter(
    private val activities: List<Activity>,
    private val onClick: (Activity) -> Unit
) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.activityTitle)
        private val image: ImageView = itemView.findViewById(R.id.activityImage)

        fun bind(activity: Activity) {
            title.text = activity.title
            Glide.with(itemView.context)
                .load(activity.image)
                .into(image)

            itemView.setOnClickListener { onClick(activity) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false)
        return ActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        holder.bind(activities[position])
    }

    override fun getItemCount(): Int = activities.size
}
