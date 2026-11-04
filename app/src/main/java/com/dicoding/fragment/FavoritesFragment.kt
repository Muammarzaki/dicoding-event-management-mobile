package com.dicoding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.MainActivity
import com.dicoding.databinding.FragmentFavoritesBinding
import com.dicoding.helper.EventListAdapter
import com.dicoding.helper.Resource
import com.dicoding.model.MainViewModel
import com.dicoding.model.ViewModelFactory

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private val favoriteEventViewModel: MainViewModel by viewModels { ViewModelFactory.Factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = EventListAdapter()
        with(binding) {
            favoriteListEvent.let {
                it.adapter = adapter
                it.layoutManager = LinearLayoutManager(context)
            }
        }
        with(favoriteEventViewModel) {
            listFavorite.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Error -> MainActivity.showErrorDialog(it.error, requireContext())
                    Resource.Loading -> binding.pFavoriteList.visibility = VISIBLE
                    is Resource.Success -> {
                        adapter.submitList(it.data)
                        binding.pFavoriteList.visibility = GONE
                    }
                }
            }
        }
    }
}