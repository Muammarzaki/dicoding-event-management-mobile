package com.dicoding

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.databinding.ActivityDetailBinding
import com.dicoding.helper.Resource
import com.dicoding.model.DetailViewModel
import com.dicoding.model.ViewModelFactory

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var eventId: Int = 0
    private val viewModel: DetailViewModel by viewModels { ViewModelFactory.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        eventId = intent.getIntExtra(DETAIL_KEY, eventId)

        viewModel.searchDetailEvent(eventId)
        viewModel.getDetailEvent.observe(this) { detail ->
            when (detail) {
                is Resource.Error -> MainActivity.showErrorDialog(detail.error, this)
                Resource.Loading -> binding.pDetail.visibility = VISIBLE
                is Resource.Success -> {
                    binding.pDetail.visibility = GONE
                    with(detail.data) {
                        with(binding) {
                            setContentView(binding.root)
                            eventName.text = name
                            eventSummary.text = summary
                            @Suppress("DEPRECATION")
                            eventDescription.text = Html.fromHtml(description)
                            eventCategoryV.text = category
                            eventRegistrantsV.text = category
                            eventOwnerV.text = ownerName
                            eventQuotaV.text = quota.toString()
                            eventCityV.text = cityName
                            eventBeginTimeV.text =
                                beginTime?.let { MainActivity.timeFormat.format(it) }
                            eventEndTimeV.text = endTime?.let { MainActivity.timeFormat.format(it) }
                            eventRegistrantsV.text = registrants.toString()
                            eventAvailableV.text = (quota!! - registrants!!).toString()
                            Glide.with(root.context)
                                .load(mediaCover)
                                .into(imageLogo)
                            eventLink.setOnClickListener {
                                val toBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                                startActivity(toBrowser)
                            }
                            if (isFavorite) favoriteMark.setImageResource(R.drawable.favorite_mark_on)

                            favoriteMark.setOnClickListener {
                                isFavorite = !isFavorite
                                if (isFavorite) {
                                    favoriteMark.setImageResource(R.drawable.favorite_mark_on)
                                    id?.let { it1 -> viewModel.saveEventFavorite(it1) }
                                } else {
                                    favoriteMark.setImageResource(R.drawable.favorite_mark_off)
                                    id?.let { it1 -> viewModel.removeEventFavorite(it1) }
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    companion object {
        const val DETAIL_KEY = "DETAIL_EXTRA"
    }
}
