package com.example.fit5046a3a4.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit5046a3a4.data.FoodDao
import com.example.fit5046a3a4.data.RestaurantDao
import com.example.fit5046a3a4.screens.MenuCategory
import com.example.fit5046a3a4.screens.MenuItem
import kotlinx.coroutines.flow.*

/**
 * ViewModel for loading restaurant menus.
 *
 * - loadMenuByRestaurant: retrieves global categories and filters items per restaurant.
 * - getRestaurantName: retrieves a restaurant's name by ID.
 */
class MenuViewModel(
    private val foodDao: FoodDao,
    private val restaurantDao: RestaurantDao
) : ViewModel() {

    /**
     * Load menu categories and items for a specific restaurant.
     *
     * 1. Observe all global categories.
     * 2. For each category, filter items by the given restaurantId.
     * 3. Combine into a list of MenuCategory.
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
                                        imageRes = foodItem.imageRes
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
     * Observe a restaurant's name by its ID.
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
}
