package com.dicoding.helper

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.DetailEventActivity
import com.dicoding.DetailEventActivity.Companion.DETAIL_KEY
import com.dicoding.databinding.EventCardBinding
import com.dicoding.dto.Event

class EventListAdapter :
    ListAdapter<Event, EventListAdapter.EventCardHolder>(Event.Diff) {

    open class EventCardHolder(val binding: EventCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            Glide.with(binding.root.context)
                .load(event.mediaCover)
                .centerCrop()
                .into(binding.upCardImage)
            binding.upcomingCardTitle.text = event.name
            binding.root.setOnClickListener {
                val toDetail = Intent(binding.root.context, DetailEventActivity::class.java)
                toDetail.putExtra(DETAIL_KEY, event.id)
                binding.root.context.startActivity(toDetail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventCardHolder {
        val view = EventCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return EventCardHolder(view)
    }

    override fun onBindViewHolder(holder: EventCardHolder, position: Int) {
        holder.bind(getItem(position))
    }
}