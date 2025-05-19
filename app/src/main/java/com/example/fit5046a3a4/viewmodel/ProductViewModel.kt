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

    // 当前餐厅信息
    private val _restaurant = MutableStateFlow<RestaurantEntity?>(null)
    val restaurant: StateFlow<RestaurantEntity?> = _restaurant.asStateFlow()

    // 分类对应的菜品列表
    private val _categoriesWithItems =
        MutableStateFlow<Map<FoodCategoryEntity, List<FoodItemEntity>>>(emptyMap())
    val categoriesWithItems: StateFlow<Map<FoodCategoryEntity, List<FoodItemEntity>>> =
        _categoriesWithItems.asStateFlow()

    /**
     * 加载某个餐厅的菜单：
     * 1. 先读取该餐厅信息
     * 2. 全局取出所有分类
     * 3. 对每个分类，再按餐厅过滤菜品，组合成 Map<分类, 菜品列表>
     */
    fun loadRestaurantMenu(restaurantId: Long) {
        viewModelScope.launch {
            // 1. 读餐厅本身
            _restaurant.value = restaurantDao.getRestaurantById(restaurantId)

            // 2+3. 读全局分类，并为每个分类加载当前餐厅的菜品
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
