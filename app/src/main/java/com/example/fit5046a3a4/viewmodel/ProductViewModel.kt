package com.example.fit5046a3a4.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit5046a3a4.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.get(application)
    private val restaurantDao = db.restaurantDao()
    private val foodDao = db.foodDao()

    private val _restaurant = MutableStateFlow<RestaurantEntity?>(null)
    val restaurant: StateFlow<RestaurantEntity?> = _restaurant.asStateFlow()

    private val _categoriesWithItems = MutableStateFlow<Map<FoodCategoryEntity, List<FoodItemEntity>>>(emptyMap())
    val categoriesWithItems: StateFlow<Map<FoodCategoryEntity, List<FoodItemEntity>>> = _categoriesWithItems.asStateFlow()

    fun loadRestaurantMenu(restaurantId: Long) {
        viewModelScope.launch {
            _restaurant.value = restaurantDao.getRestaurantById(restaurantId)

            foodDao.getCategoriesByRestaurant(restaurantId).collect { categories ->
                val result = mutableMapOf<FoodCategoryEntity, List<FoodItemEntity>>()
                for (cat in categories) {
                    val items = foodDao.getItemsByCategory(cat.id).firstOrNull() ?: emptyList()
                    result[cat] = items
                }
                _categoriesWithItems.value = result
            }
        }
    }
}
