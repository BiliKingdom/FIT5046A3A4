package com.example.fit5046a3a4.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fit5046a3a4.data.FoodDao
import com.example.fit5046a3a4.data.RestaurantDao

class MenuViewModelFactory(
    private val foodDao: FoodDao,
    private val restaurantDao: RestaurantDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MenuViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MenuViewModel(foodDao, restaurantDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



