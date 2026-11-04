package com.dicoding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.MainActivity
import com.dicoding.databinding.FragmentFinishedBinding
import com.dicoding.helper.EventListAdapter
import com.dicoding.helper.Resource
import com.dicoding.model.MainViewModel
import com.dicoding.model.ViewModelFactory


class FinishedFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.Factory
    }
    private lateinit var binding: FragmentFinishedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = EventListAdapter()
        with(binding) {
            finishedListEvent.let {
                it.adapter = adapter
                it.layoutManager = LinearLayoutManager(requireContext())
            }

            searchView
                .setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        viewModel.searchFinishedEvents(query ?: "")
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        viewModel.searchFinishedEvents(newText ?: "")
                        return true
                    }
                })
        }

        with(viewModel)
        {
            listSearchEvent.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Error -> MainActivity.showErrorDialog(it.error, requireContext())
                    Resource.Loading -> binding.pFinishedListEvent.visibility = VISIBLE
                    is Resource.Success -> {
                        adapter.submitList(it.data)
                        binding.pFinishedListEvent.visibility = GONE
                    }
                }
            }
            listFinished.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Error -> MainActivity.showErrorDialog(it.error, requireContext())
                    Resource.Loading -> binding.pFinishedListEvent.visibility = VISIBLE
                    is Resource.Success -> {
                        adapter.submitList(it.data)
                        binding.pFinishedListEvent.visibility = GONE
                    }
                }
            }

        }

    }


}