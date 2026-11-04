package com.dicoding.dto

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity
data class Event(

    @field:SerializedName("summary")
    val summary: String? = null,

    @field:SerializedName("mediaCover")
    val mediaCover: String? = null,

    @field:SerializedName("registrants")
    val registrants: Int? = null,

    @field:SerializedName("imageLogo")
    val imageLogo: String? = null,

    @field:SerializedName("link")
    val link: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("ownerName")
    val ownerName: String? = null,

    @field:SerializedName("cityName")
    val cityName: String? = null,

    @field:SerializedName("quota")
    val quota: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    @PrimaryKey
    val id: Int? = null,

    @field:SerializedName("beginTime")
    val beginTime: Date? = null,

    @field:SerializedName("endTime")
    val endTime: Date? = null,

    @field:SerializedName("category")
    val category: String? = null,

    var isFavorite: Boolean = false,

    ) : Parcelable {
    companion object {
        val Diff: DiffUtil.ItemCallback<Event> = object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean =
                oldItem.id == newItem.id


            override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean =
                oldItem == newItem

        }
    }
}
