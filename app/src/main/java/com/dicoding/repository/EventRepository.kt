package com.dicoding.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.config.DicodingAPI
import com.dicoding.dto.Event
import com.dicoding.helper.ApiResponse
import com.dicoding.helper.NetworkBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Date

class EventRepository(database: EventDatabase, val client: DicodingAPI) {
    private val room: EventDao = database.eventDao()

    private suspend fun save(event: Event) = withContext(Dispatchers.IO) {
        room.save(event)
    }

    fun markFavorite(id: Int) = room.updateFavorite(id, true)
    fun unmarkFavorite(id: Int) = room.updateFavorite(id, false)

    fun getAllFavorite(): NetworkBoundResource<List<Event>, List<Event>> {
        return object : NetworkBoundResource<List<Event>, List<Event>>() {
            override fun loadFromDatabase(): LiveData<List<Event>> =
                room.getAllFavorite()

            override fun createCall(): LiveData<ApiResponse<List<Event>>> =
                liveData { emit(ApiResponse.Error("not Implemented")) }

            override fun saveCallResult(item: List<Event>) = Unit

            override fun shouldFetch(data: List<Event>?): Boolean = false
        }
    }

    fun getUpcoming(): NetworkBoundResource<List<Event>, List<Event>> {
        return object : NetworkBoundResource<List<Event>, List<Event>>() {
            override fun loadFromDatabase(): LiveData<List<Event>> =
                room.getAllUpComing(Date())

            override fun createCall(): LiveData<ApiResponse<List<Event>>> =
                EventApi().requestEvents(client.getActiveEvent())


            override fun saveCallResult(item: List<Event>) {
                item.forEach {
                    runBlocking {
                        save(it)
                    }
                }
            }

            override fun shouldFetch(data: List<Event>?): Boolean {
                var shouldI: Boolean
                runBlocking {
                    withContext(Dispatchers.IO) {
                        shouldI = !room.isUpcomingExits(Date())
                    }
                }
                return shouldI
            }

        }
    }

    fun getFinished(): NetworkBoundResource<List<Event>, List<Event>> {
        return object : NetworkBoundResource<List<Event>, List<Event>>() {
            override fun loadFromDatabase(): LiveData<List<Event>> =
                room.getAllFinished(Date())

            override fun createCall(): LiveData<ApiResponse<List<Event>>> =
                EventApi().requestEvents(client.getFinishEvent())


            override fun saveCallResult(item: List<Event>) {
                item.forEach {
                    runBlocking {
                        save(it)
                    }
                }
            }

            override fun shouldFetch(data: List<Event>?): Boolean {
                var shouldI: Boolean
                runBlocking {
                    withContext(Dispatchers.IO) {
                        shouldI = !room.isFinishedExist(Date())
                    }
                }
                return shouldI
            }
        }
    }

    fun getSearchFinished(query: String): NetworkBoundResource<List<Event>, List<Event>> {
        return object : NetworkBoundResource<List<Event>, List<Event>>() {
            override fun loadFromDatabase(): LiveData<List<Event>> =
                room.searchEventByTitle(query, Date())

            override fun createCall(): LiveData<ApiResponse<List<Event>>> =
                EventApi().requestEvents(client.getSearchEvent(query, 0))


            override fun saveCallResult(item: List<Event>) {
                item.forEach {
                    runBlocking {
                        save(it)
                    }
                }
            }

            override fun shouldFetch(data: List<Event>?): Boolean =
                data.isNullOrEmpty()

        }
    }

    fun getClosestEvent(): LiveData<Event> = room.getTheClosestEvent(Date())

    fun getDetailEvent(id: Int): NetworkBoundResource<Event, Event> {
        return object : NetworkBoundResource<Event, Event>() {
            override fun loadFromDatabase(): LiveData<Event> =
                room.getDetail(id)

            override fun createCall(): LiveData<ApiResponse<Event>> =
                EventApi().requestEventDetail(client.getDetail(id))

            override fun shouldFetch(data: Event?): Boolean =
                data?.endTime?.after(Date()) ?: false

            override fun saveCallResult(item: Event) {
                runBlocking {
                    save(item)
                }
            }
        }
    }

}