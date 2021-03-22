package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import androidx.navigation.findNavController
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.AsteroidRoom.Companion.getDatabase
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NasaApiForApod
import com.udacity.asteroidradar.api.isInternetConnection
import kotlinx.coroutines.launch
import retrofit2.await

enum class LoadingStatus { LOADING, ERROR, DONE }
enum class DayWeekAllFilter { SHOW_TODAY, SHOW_WEEK, SHOW_ALL}

@RequiresApi(Build.VERSION_CODES.M)
class MainViewModel(application: Application) : AndroidViewModel(application) {

//STATUS
    //MutableLiveData, for the status
        private val _status = MutableLiveData<LoadingStatus>()
        val status: LiveData<LoadingStatus>
            get() = _status

//PICTURE OF THE DAY
    //LiveData fo setting the pic of the day
    private val _picOfTheDay = MutableLiveData<PictureOfDay>()
        val picOfTheDay: LiveData<PictureOfDay>
            get() = _picOfTheDay
    //Pic of the day if there is internet
    @RequiresApi(Build.VERSION_CODES.M)
    fun getPicOfTheDay(application: Application) {
        if (isInternetConnection(application)) {
            viewModelScope.launch {
                var getPictureOfTheDayDeferred = NasaApiForApod.RETROFIT_SERVICE_FOR_APOD.connectToNasaPictureOTD()
                _picOfTheDay.value = getPictureOfTheDayDeferred.await()
            }
        } else {
            Toast.makeText(application, "No internet", Toast.LENGTH_SHORT).show()
        }
    }

//FILTER
    // set filter
    private val menuFilter = MutableLiveData<DayWeekAllFilter>(DayWeekAllFilter.SHOW_ALL)
    // update filter
    fun updateFilter(filter: DayWeekAllFilter){
        menuFilter.value = filter
    }

    // For setting the listData according to the menu settings
    val asteroids = Transformations.switchMap(menuFilter)
    {
        if (it == DayWeekAllFilter.SHOW_TODAY) {
            repo.asteroidsDay
        } else if (it == DayWeekAllFilter.SHOW_WEEK) {
            repo.asteroidsWeek
        } else {
            repo.asteroids
        }

    }


    private val database = getDatabase(application)
    private val repo = AsteroidRepo(database)

    //Live data for open selected asteroid's page
    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()
    val navigateToSelectedAsteroid: MutableLiveData<Asteroid?>
        get() = _navigateToSelectedAsteroid

    //Navigate  to selected asteroid
    fun asteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

    //Display selected asteroid
    fun displayAsteroidsDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    //
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun refreshAsteroids() {
        _status.value = LoadingStatus.LOADING
        try {
            repo.refreshAsteroidsRepo()
            _status.value = LoadingStatus.DONE
        } catch (e: Exception) {
            _status.value = LoadingStatus.DONE
        }
    }


//FOR FULL SCREEN PICTURE (NOT IN THE PROJECT DESCRIPTION)
    //Navigate to the full screen
    fun navigateToAllPageImage(view: View) {
        val action = MainFragmentDirections.actionMainFragmentToImage()
        view.findNavController().navigate(action)
    }

    //Navigate back from the full screen
    fun closePic(view: View) {
        view.findNavController().navigateUp()
    }

    init {
        viewModelScope.launch {
            getPicOfTheDay(application)
            refreshAsteroids()
        }
    }
}