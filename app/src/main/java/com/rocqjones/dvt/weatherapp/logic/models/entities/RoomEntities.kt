package com.rocqjones.dvt.weatherapp.logic.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "t_current")
data class CurrentWeatherModel(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "locationName") val locationName: String? = null,
    @ColumnInfo(name = "latitude") val latitude: Double? = null,
    @ColumnInfo(name = "longitude") val longitude: Double? = null,
    @ColumnInfo(name = "temperature") val temperature: Double? = null,
    @ColumnInfo(name = "temp_min") val temp_min: Double? = null,
    @ColumnInfo(name = "temp_max") val temp_max: Double? = null,
    @ColumnInfo(name = "weatherMain") val weatherMain: String? = null,
    @ColumnInfo(name = "weatherDescription") val weatherDescription: String? = null,
)

@Entity(tableName = "t_forecast")
data class ForecastWeatherModel(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "locationName") val locationName: String? = null,
    @ColumnInfo(name = "latitude") val latitude: Double? = null,
    @ColumnInfo(name = "longitude") val longitude: Double? = null,
    @ColumnInfo(name = "temperature") val temperature: Double? = null,
    @ColumnInfo(name = "temp_min") val temp_min: Double? = null,
    @ColumnInfo(name = "temp_max") val temp_max: Double? = null,
    @ColumnInfo(name = "weatherMain") val weatherMain: String? = null,
    @ColumnInfo(name = "weatherDescription") val weatherDescription: String? = null,
    @ColumnInfo(name = "forecastDate") val forecastDate: String? = null,
)


@Entity(tableName = "t_favourite")
data class FavouriteWeatherModel(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "forecastId") val forecastId: String? = null,
    @ColumnInfo(name = "locationName") val locationName: String? = null,
    @ColumnInfo(name = "latitude") val latitude: Double? = null,
    @ColumnInfo(name = "longitude") val longitude: Double? = null,
    @ColumnInfo(name = "temperature") val temperature: Double? = null,
    @ColumnInfo(name = "temp_min") val temp_min: Double? = null,
    @ColumnInfo(name = "temp_max") val temp_max: Double? = null,
    @ColumnInfo(name = "weatherMain") val weatherMain: String? = null,
    @ColumnInfo(name = "weatherDescription") val weatherDescription: String? = null,
)