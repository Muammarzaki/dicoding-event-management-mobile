package com.dicoding.model

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.dicoding.config.DicodingAPI
import com.dicoding.repository.EventDatabase
import com.dicoding.repository.EventRepository
import com.dicoding.repository.SettingsPreference

@Suppress("UNCHECKED_CAST")
class ViewModelFactory {
    companion object {
        val Context.dataStore by preferencesDataStore(name = "settings")

        var EVENT_REPOSITORY: EventRepository? = null

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val myApplication =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                if (EVENT_REPOSITORY == null) {
                    EVENT_REPOSITORY = EventRepository(
                        EventDatabase.getInstance(myApplication),
                        DicodingAPI.client
                    )
                }

                if (modelClass.isAssignableFrom(DetailViewModel::class.java))
                    return DetailViewModel(EVENT_REPOSITORY!!) as T

                return MainViewModel(EVENT_REPOSITORY!!, SettingsPreference(myApplication.dataStore)) as T
            }
        }
    }

}
