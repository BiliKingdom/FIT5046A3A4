package com.example.fit5046a3a4.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit5046a3a4.data.AppDatabase
import com.example.fit5046a3a4.data.FoodCategoryEntity
import com.example.fit5046a3a4.data.FoodItemEntity
import com.example.fit5046a3a4.data.RestaurantEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.get(application)
    private val restaurantDao = db.restaurantDao()
    private val foodDao = db.foodDao()

    private val _restaurant = MutableStateFlow<RestaurantEntity?>(null)
    val restaurant: StateFlow<RestaurantEntity?> = _restaurant.asStateFlow()

    private val _categoriesWithItems =
        MutableStateFlow<Map<FoodCategoryEntity, List<FoodItemEntity>>>(emptyMap())
    val categoriesWithItems: StateFlow<Map<FoodCategoryEntity, List<FoodItemEntity>>> =
        _categoriesWithItems.asStateFlow()


    fun loadRestaurantMenu(restaurantId: Long) {
        viewModelScope.launch {
            _restaurant.value = restaurantDao.getRestaurantById(restaurantId)

            foodDao.getAllCategories()
                .flatMapLatest { categories ->
                    combine(
                        categories.map { category ->
                            foodDao.getItemsByCategoryAndRestaurant(
                                category.id,
                                restaurantId
                            ).map { items ->
                                category to items
                            }
                        }
                    ) { pairs ->
                        @Suppress("UNCHECKED_CAST")
                        (pairs as Array<Pair<FoodCategoryEntity, List<FoodItemEntity>>>).toMap()
                    }
                }
                .collect { map ->
                    _categoriesWithItems.value = map
                }
        }
    }
}
