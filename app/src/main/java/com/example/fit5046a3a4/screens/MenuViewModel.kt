package com.example.fit5046a3a4.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit5046a3a4.data.FoodDao
import com.example.fit5046a3a4.screens.MenuCategory
import com.example.fit5046a3a4.screens.MenuItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MenuViewModel(private val foodDao: FoodDao) : ViewModel() {

    private val _menuData = MutableStateFlow<List<MenuCategory>>(emptyList())
    val menuData: StateFlow<List<MenuCategory>> = _menuData

    fun loadMenuByRestaurant(restaurantId: Long) {
        viewModelScope.launch {
            foodDao.getCategoriesByRestaurant(restaurantId)
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
                .collect { result ->
                    _menuData.value = result
                }
        }
    }
}
