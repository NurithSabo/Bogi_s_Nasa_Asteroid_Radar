package com.udacity.asteroidradar

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: AsteroidDb)

    @Query("SELECT * FROM AsteroidDb ORDER BY closeApproachDate ASC")
    fun getAsteroids(): LiveData<List<AsteroidDb>>

    @Query("SELECT * FROM AsteroidDb WHERE closeApproachDate = :date")
    fun getTodayAsteroids(date: String): LiveData<List<AsteroidDb>>

    @Query("SELECT * FROM AsteroidDb WHERE closeApproachDate BETWEEN :startDate AND :endDate ORDER BY closeApproachDate ASC")
    fun getWeekAsteroids(startDate: String, endDate: String): LiveData<List<AsteroidDb>>
}