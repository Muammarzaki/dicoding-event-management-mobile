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
import com.dicoding.databinding.FragmentUpcomingBinding
import com.dicoding.helper.EventListAdapter
import com.dicoding.helper.Resource
import com.dicoding.model.MainViewModel
import com.dicoding.model.ViewModelFactory


class UpComingFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.Factory
    }
    private lateinit var binding: FragmentUpcomingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = EventListAdapter()
        binding.upcomingListEvent.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.listUpcoming.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    adapter.submitList(it.data)
                    binding.pUpcoming.visibility = GONE
                }
                is Resource.Error -> MainActivity.showErrorDialog(it.error, requireContext())
                Resource.Loading -> binding.pUpcoming.visibility = VISIBLE
            }
        }
    }
}