package com.dicoding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import androidx.recyclerview.widget.RecyclerView.VISIBLE
import com.dicoding.MainActivity
import com.dicoding.databinding.FragmentHomeBinding
import com.dicoding.helper.EventListAdapter
import com.dicoding.helper.Resource
import com.dicoding.model.MainViewModel
import com.dicoding.model.ViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.run {
            root
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter0 = EventListAdapter()
        val adapter1 = EventListAdapter()
        with(binding) {
            upcomingListHorizontal.apply {
                adapter = adapter0
                layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
            }
            finishedListVertical.apply {
                adapter = adapter1
                layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)
            }
        }
        with(viewModel) {
            listUpcoming.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Error -> MainActivity.showErrorDialog(it.error, requireContext())
                    Resource.Loading -> binding.pUpcoming.visibility = VISIBLE
                    is Resource.Success -> {
                        adapter0.submitList(it.data.take(5))
                        binding.pUpcoming.visibility = GONE
                    }
                }
            }
            listFinished.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Error -> MainActivity.showErrorDialog(it.error, requireContext())
                    Resource.Loading -> binding.pFinished.visibility = VISIBLE
                    is Resource.Success -> {
                        adapter1.submitList(it.data.take(5))
                        binding.pFinished.visibility = GONE
                    }
                }
            }
        }
    }


}