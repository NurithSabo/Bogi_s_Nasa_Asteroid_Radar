package com.udacity.asteroidradar.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.*
import com.udacity.asteroidradar.Constants.API_Key
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDate

class AsteroidRepo(private val database : AsteroidRoom) {

    var asteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroids())
    {
        it.asDomainModel()
    }

    val asteroidsDay: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getTodayAsteroids(
        getNextSevenDaysFormattedDates().first())) { it.asDomainModel() }

    val asteroidsWeek = Transformations.map(database.asteroidDao.getWeekAsteroids(
        getNextSevenDaysFormattedDates().first(), getNextSevenDaysFormattedDates().last())) { it.asDomainModel() }


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun refreshAsteroidsRepo() {
        withContext(Dispatchers.IO) {
            val result = NasaApiForAsteroids.RETROFIT_SERVICE_FOR_ASTEROIDS.connectToNasaAsteroids(
                                LocalDate.now().toString(),
                                API_Key)
            val networkAsteroids = NetworkAsteroidContainer(parseAsteroidsJsonResult(JSONObject(result.body()!!)))
            database.asteroidDao.insertAll(*networkAsteroids.
            asDatabaseModel()
            )
        }
    }
}