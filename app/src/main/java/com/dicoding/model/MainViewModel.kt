package com.dicoding.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.dicoding.dto.Event
import com.dicoding.helper.Resource
import com.dicoding.repository.EventRepository
import com.dicoding.repository.SettingsPreference
import kotlinx.coroutines.launch

class MainViewModel(private val repository: EventRepository, private val pref: SettingsPreference) :
    ViewModel() {

    val listUpcoming: LiveData<Resource<List<Event>>> = repository.getUpcoming().asLiveData()

    val listFinished: LiveData<Resource<List<Event>>> = repository.getFinished().asLiveData()

    val listFavorite: LiveData<Resource<List<Event>>> = repository.getAllFavorite().asLiveData()

    private val _searchQuery = MutableLiveData<String>()
    val listSearchEvent: LiveData<Resource<List<Event>>> = _searchQuery.switchMap { query ->
        repository.getSearchFinished(query).asLiveData()
    }

    fun searchFinishedEvents(query: String) {
        _searchQuery.value = query
    }

    val darkMode = pref.getDarkModePreference().asLiveData()

    fun enableDarkMode(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.enableDarkModePreference(isDarkModeActive)
        }
    }

    val enableNotification = pref.getNotificationPreference().asLiveData()

    fun enableNotification(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.enableNotificationPreference(isDarkModeActive)
        }
    }

    val getClosestEvent: LiveData<Event> = repository.getClosestEvent()
}