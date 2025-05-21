package com.example.fit5046a3a4.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit5046a3a4.data.FoodDao
import com.example.fit5046a3a4.data.RestaurantDao
import com.example.fit5046a3a4.data.RestaurantEntity
import com.example.fit5046a3a4.screens.MenuCategory
import com.example.fit5046a3a4.screens.MenuItem
import kotlinx.coroutines.flow.*

class MenuViewModel(
    private val foodDao: FoodDao,
    private val restaurantDao: RestaurantDao
) : ViewModel() {

    /**
     * Load menu categories and items for a specific restaurant.
     */
    fun loadMenuByRestaurant(restaurantId: Long): StateFlow<List<MenuCategory>> {
        return foodDao.getAllCategories()
            .flatMapLatest { categories ->
                combine(
                    categories.map { category ->
                        foodDao.getItemsByCategoryAndRestaurant(
                            category.id,
                            restaurantId
                        ).map { items ->
                            MenuCategory(
                                name = category.name,
                                items = items.map { foodItem ->
                                    MenuItem(
                                        name = foodItem.name,
                                        price = "$${"%.2f".format(foodItem.price)}",
                                        imageRes = foodItem.imageRes,
                                        description = foodItem.description
                                    )
                                }
                            )
                        }
                    }
                ) { it.toList() }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    /**
     * Observe restaurant name only.
     */
    fun getRestaurantName(restaurantId: Long): StateFlow<String> {
        return restaurantDao.getRestaurantByIdFlow(restaurantId)
            .map { it?.name ?: "Unknown Restaurant" }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ""
            )
    }

    /**
     * Observe full RestaurantEntity (e.g. for latitude/longitude/map)
     */
    fun getRestaurantByIdFlow(restaurantId: Long): StateFlow<RestaurantEntity?> {
        return restaurantDao.getRestaurantByIdFlow(restaurantId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )
    }
}
