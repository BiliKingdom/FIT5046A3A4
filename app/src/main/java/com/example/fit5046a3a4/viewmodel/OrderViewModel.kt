package com.example.fit5046a3a4.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit5046a3a4.data.AppDatabase
import com.example.fit5046a3a4.data.RestaurantEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class OrderViewModel(application: Application) : AndroidViewModel(application) {

    private val restaurantDao = AppDatabase.get(application).restaurantDao()
    private val campusDao = AppDatabase.get(application).campusDao()

    private val _restaurantList = MutableStateFlow<List<RestaurantEntity>>(emptyList())
    val restaurantList: StateFlow<List<RestaurantEntity>> = _restaurantList.asStateFlow()

    fun loadRestaurantsForCampus(campusName: String) {
        viewModelScope.launch {
            val campus = campusDao.getCampusByName(campusName)
            if (campus != null) {
                restaurantDao.getRestaurantsByCampus(campus.id)
                    .collect { list -> _restaurantList.value = list }
            }
        }
    }
}
