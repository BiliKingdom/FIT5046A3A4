package com.example.fit5046a3a4.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit5046a3a4.data.FoodDao
import com.example.fit5046a3a4.screens.MenuCategory
import com.example.fit5046a3a4.screens.MenuItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MenuViewModel(private val foodDao: FoodDao) : ViewModel() {

    fun loadMenuByRestaurant(restaurantId: Long): StateFlow<List<MenuCategory>> {
        return foodDao.getCategoriesByRestaurant(restaurantId)
            .flatMapLatest { categories ->
                combine(
                    categories.map { category ->
                        foodDao.getItemsByCategory(category.id).map { items ->
                            MenuCategory(
                                name = category.name,
                                items = items.map {
                                    MenuItem(
                                        name = it.name,
                                        price = "$${"%.2f".format(it.price)}",
                                        imageRes = it.imageRes
                                    )
                                }
                            )
                        }
                    }
                ) { it.toList() }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }
}


