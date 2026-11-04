package com.dicoding.repository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.dto.Event
import java.util.Date

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun save(event: Event)

    @Query("UPDATE event SET isFavorite = :isFavorite WHERE id = :id")
    fun updateFavorite(id: Int, isFavorite: Boolean)

    @Query("select * from event where id = :id")
    fun getDetail(id: Int): LiveData<Event>

    @Query("select * from event where endTime > :now order by beginTime desc")
    fun getAllUpComing(now: Date): LiveData<List<Event>>

    @Query("select * from event where beginTime < :now")
    fun getAllFinished(now: Date): LiveData<List<Event>>

    @Query("SELECT EXISTS(select * from event where beginTime > :now limit 1)")
    fun isUpcomingExits(now: Date): Boolean

    @Query("SELECT EXISTS(select * from event where beginTime < :now limit 1)")
    fun isFinishedExist(now: Date): Boolean

    @Query("select * from event where name like '%'||:titlePart||'%' and beginTime < :now")
    fun searchEventByTitle(titlePart: String, now: Date): LiveData<List<Event>>

    @Query("SELECT * FROM event WHERE id = :id")
    fun get(id: Int): LiveData<Event>

    @Query("SELECT * FROM event WHERE isFavorite = 1")
    fun getAllFavorite(): LiveData<List<Event>>

    @Query("select * from event where endTime > :now order by beginTime limit 1")
    fun getTheClosestEvent(now: Date): LiveData<Event>
}