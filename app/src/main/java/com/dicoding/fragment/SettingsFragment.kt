package com.dicoding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dicoding.databinding.FragmentSettingsBinding
import com.dicoding.model.MainViewModel
import com.dicoding.model.ViewModelFactory


class SettingsFragment : Fragment() {

    val viewmodel: MainViewModel by viewModels { ViewModelFactory.Factory }
    lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.run {
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            with(darkModeSwitch) {
                viewmodel.apply {
                    darkMode.observe(viewLifecycleOwner) {
                        isChecked = it
                    }
                    setOnCheckedChangeListener { _, isChecked ->
                        enableDarkMode(isChecked)
                    }
                }
            }

            with(scheduleSwitch) {
                viewmodel.apply {
                    enableNotification.observe(viewLifecycleOwner) {
                        isChecked = it
                    }
                    setOnCheckedChangeListener { _, isChecked ->
                        enableNotification(isChecked)
                    }
                }
            }
        }
    }

}
