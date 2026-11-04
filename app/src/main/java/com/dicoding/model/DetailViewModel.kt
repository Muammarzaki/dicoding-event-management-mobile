package com.dicoding.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.dicoding.dto.Event
import com.dicoding.helper.Resource
import com.dicoding.repository.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: EventRepository) : ViewModel() {
    private val _searchDetailEvent = MutableLiveData<Int>()

    val getDetailEvent: LiveData<Resource<Event>> = _searchDetailEvent.switchMap { id ->
        repository.getDetailEvent(id).asLiveData()
    }

    fun searchDetailEvent(id: Int) {
        viewModelScope.launch {
            _searchDetailEvent.value = id
        }
    }

    fun saveEventFavorite(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.markFavorite(id)
        }
    }

    fun removeEventFavorite(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.unmarkFavorite(id)
        }
    }

}