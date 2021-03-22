package com.udacity.asteroidradar

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AsteroidDb::class], version = 1, exportSchema = false)
abstract class AsteroidRoom : RoomDatabase() {
    abstract val asteroidDao: DatabaseDao

    companion object {

        @Volatile
        private var INSTANCE : AsteroidRoom? = null

        fun getDatabase(context: Context): AsteroidRoom {
            synchronized(this) {

                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            AsteroidRoom::class.java,
                            "ASTEROIDS"
                    )

                            .fallbackToDestructiveMigration()
                            .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }}
